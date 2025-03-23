package com.tub.ise.energyprofilersimulator.v2;

import com.tub.ise.energyprofilersimulator.StaticsMetrics;
import com.tub.ise.energyprofilersimulator.model.*;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.channels.Pipe;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimulationEnvV2 {
    private static final Logger logger = LoggerFactory.getLogger(SimulationEnvV2.class);

    // ### 1
    // sample path : /operations-definition.csv
    public List<Operation> createSimulationBasicEvn(String fileLocation) {
        List<Operation> operationList = new ArrayList<>();
        Workbook workbook = null;

        try {
//            workbook = WorkbookFactory.create(new File(fileLocation));
            workbook = WorkbookFactory.create(new File((this.getClass().getResource(fileLocation)).toURI()));
            logger.info("Number of sheets: " + workbook.getNumberOfSheets());
            workbook.forEach(sheet -> {
                logger.info("Title of sheet => " + sheet.getSheetName());

                DataFormatter dataFormatter = new DataFormatter();
                int index = 0;
                for (Row row : sheet) {
                    if (index++ == 0) continue;
                    Operation operation = new Operation();

                    if (row.getCell(0) != null && (row.getCell(0).getCellType() == CellType.NUMERIC || row.getCell(0).getCellType() == CellType.FORMULA )) {
                        operation.setOperationId((int) row.getCell(0).getNumericCellValue());
                    }

                    if (row.getCell(1) != null && (row.getCell(1).getCellType() == CellType.NUMERIC || row.getCell(1).getCellType() == CellType.FORMULA )) {
                        operation.setCpuUsageUnit((int) row.getCell(1).getNumericCellValue());
                    }

                    if (row.getCell(2) != null && (row.getCell(2).getCellType() == CellType.NUMERIC || row.getCell(2).getCellType() == CellType.FORMULA )) {
                        operation.setMemUsageUnit((int) row.getCell(2).getNumericCellValue());
                    }

                    if (row.getCell(3) != null && (row.getCell(3).getCellType() == CellType.NUMERIC || row.getCell(3).getCellType() == CellType.FORMULA )) {
                        operation.setOutputDataVolumeUnit((int) row.getCell(3).getNumericCellValue());
                    }

                    if (row.getCell(4) != null && (row.getCell(4).getCellType() == CellType.NUMERIC || row.getCell(4).getCellType() == CellType.FORMULA )) {
                        operation.setInputDataVolumeUnit((int) row.getCell(4).getNumericCellValue());
                    }

                    if (row.getCell(5) != null && (row.getCell(5).getCellType() == CellType.NUMERIC || row.getCell(5).getCellType() == CellType.FORMULA )) {
                        operation.setObservationUnit((int) row.getCell(5).getNumericCellValue());
                    }
                    if (row.getCell(8) != null && (row.getCell(8).getCellType() == CellType.STRING )) {
                        operation.setOperationType(row.getCell(8).getStringCellValue());
                    }
                    if (row.getCell(9) != null && (row.getCell(9).getCellType() == CellType.STRING )) {
                        operation.setOperationCode(row.getCell(9).getStringCellValue());
                    }

                    operationList.add(operation);
                }
            });
        } catch (EncryptedDocumentException | IOException e) {
            logger.error(e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (workbook != null) workbook.close();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return operationList;
    }


    // ### 2
    // maxNumOfOpPerPip  = pipeline length
    // maxNumOfOpPerPip = number of operation in the Excel file
    // maxNumOfOpPerPip is exclusive
    public ArrayList<Pipeline> createPipelines(int numberOfPopulation, int maxNumOfOpPerPip, List<Operation> operationList) {

        ArrayList<Pipeline> pipelineList = new ArrayList<>();
        //creating pipelines
        for (int i = 0; i < numberOfPopulation; i++) {
            List<Operation> randomOperationList = new ArrayList<Operation>();
            Pipeline pipeline = new Pipeline();
            pipeline.setId(i);
            pipeline.setSubPipelineList(new ArrayList<>());
            pipeline.setPipelineOperations(new ArrayList<>());
            //random length of pipleine
            int pipelineLength = getRandomNumber(2, maxNumOfOpPerPip);
            pipeline.setLength(pipelineLength);
            //randomly assign operations to the pipeline
            for (int j = 0; j < pipelineLength; j++) {
                Operation selectedOperation = operationList.get(getRandomNumber(0, operationList.size()));
                randomOperationList.add(selectedOperation);
            }
            List<Operation> distinctOperations = randomOperationList.stream()
                    .distinct()
                    .collect(Collectors.toList());
            pipelineList.add(pipeline);
            Collections.sort(distinctOperations, Comparator.comparingInt(Operation::getOperationId));
            pipeline.setPipelineOperations(distinctOperations);
        }
return pipelineList;
    }

    ///// #3
    //find all common subsets in the pipelines
    public static  List<CommonPipelines> findCommonInAllPairs(List<Pipeline> pipelinesList, Integer minLength) {
        List<CommonPipelines> commonLists = new ArrayList<>();

        // Compare every pair of lists
        for (int i = 0; i < pipelinesList.size(); i++) {
            for (int j = i+1; j < pipelinesList.size(); j++) {
                List<Operation> common = findCommonElements(pipelinesList.get(i).getPipelineOperations(),pipelinesList.get(j).getPipelineOperations());

                if (!common.isEmpty() && common.size() >= minLength ) {
                    commonLists.add( CommonPipelines.builder().
                            pipelineIdList(Arrays.asList(pipelinesList.get(i).getId(),pipelinesList.get(j).getId()))
                            .commonOperationList(common)
                            .build());
                }
            }
        }
        return commonLists.stream().distinct().collect(Collectors.toList());
       // return commonLists;
    }

    ///// #3.1
    public static List<Operation> findCommonElements(List<Operation> operationList1, List<Operation> operationList2) {
        List<Operation> commonElements = new ArrayList<>();

        int len1 = operationList1.size();
        int len2 = operationList2.size();

        for (int i = 0; i < len1; i++) {
            for (int j = 0; j < len2; j++) {
                if (operationList1.get(i).getOperationId().equals(operationList2.get(j).getOperationId()) &&
                    (operationList1.get(i).getOperationType().equals("f")|| operationList1.get(i).getOperationType().equals("a"))) {
                    List<Operation> tempCommon = new ArrayList<>();
                    int k = i, l = j;
                    while (k < len1 && l < len2 && operationList1.get(k).getOperationId().equals(operationList2.get(l).getOperationId())
                            && (operationList1.get(i).getOperationType().equals("f")|| operationList1.get(i).getOperationType().equals("a"))) {
                        tempCommon.add(operationList1.get(k));
                        k++;
                        l++;
                    }
                    if (tempCommon.size() > commonElements.size()) {
                        commonElements = tempCommon;
                    }
                }
            }
        }

        return commonElements;
    }


    // ### 4
    //create pipeline energy consumption object (new pipeline distribution)

public ArrayList<PipelineEnergyConsumption> createNewPipelinesConfigAndEnergy(List<Pipeline> pipelineList , List<CommonPipelines> commonPipelinesList ){
ArrayList<PipelineEnergyConsumption> pipelineEnergyConsumptions = new ArrayList<>();
        for (CommonPipelines commonPipeline: commonPipelinesList){
            for (Integer pipelineId : commonPipeline.getPipelineIdList()){
                Optional<Pipeline> optionalPipeline = pipelineList.stream().filter(pipeline -> pipeline.getId().equals(pipelineId)).findFirst();

                if(!optionalPipeline.isPresent()){
                    logger.error("mismatch pipeline id on the common operations");
                }else {
                    Integer pipelineSize= optionalPipeline.get().getPipelineOperations().size();
                    Integer commonPipelineSize= commonPipeline.getCommonOperationList().size();
                    //only if the common operation is at the very beginning or end !
                    if(optionalPipeline.get().getPipelineOperations().get(0).getOperationId().equals(commonPipeline.getCommonOperationList().get(0).getOperationId())){
                        List<Operation> commonSplit = optionalPipeline.get().getPipelineOperations().subList(0, commonPipelineSize);
                        List<Operation> uniqueSplit = optionalPipeline.get().getPipelineOperations().subList(commonPipelineSize, pipelineSize);
                       Long energyCommon= operationEnergyConsumption(commonSplit);
                       Long energyUnique= operationEnergyConsumption(uniqueSplit);
                        pipelineEnergyConsumptions.add(PipelineEnergyConsumption.builder()
                                .commonOperationList(commonSplit)
                                .uniqueOperationList(uniqueSplit)
                                .commonOperationsEnergyConsumption(energyCommon)
                                .wholePipelineEnergyConsumption(energyUnique+energyCommon)
                                .originalPipeline(optionalPipeline.get().getId())
                                 .commonPipeline(commonPipeline.getPipelineIdList().stream().filter(p->!p.equals(optionalPipeline.get().getId())).toList())
                                 .originalPipelineOperation(optionalPipeline.get().getPipelineOperations())
                                .build());
                    } else if (optionalPipeline.get().getPipelineOperations().get(pipelineSize-1).getOperationId().equals(commonPipeline.getCommonOperationList().get(commonPipelineSize-1).getOperationId())) {
                    //1,2,3,4,5   s= 5   ---  4,5
                        List<Operation> commonSplit = optionalPipeline.get().getPipelineOperations().subList(pipelineSize-commonPipelineSize, pipelineSize);
                        List<Operation> uniqueSplit = optionalPipeline.get().getPipelineOperations().subList(0, pipelineSize-commonPipelineSize);
                        Long energyCommon= operationEnergyConsumption(commonSplit);
                        Long energyUnique= operationEnergyConsumption(uniqueSplit);

                        pipelineEnergyConsumptions.add(PipelineEnergyConsumption.builder()
                                .commonOperationList(commonSplit)
                                .uniqueOperationList(uniqueSplit)
                                .commonOperationsEnergyConsumption(energyCommon)
                                .wholePipelineEnergyConsumption(energyUnique+energyCommon)
                                .originalPipeline(optionalPipeline.get().getId())
                                .originalPipelineOperation(optionalPipeline.get().getPipelineOperations())
                                .commonPipeline(commonPipeline.getPipelineIdList().stream().filter(p->!p.equals(optionalPipeline.get().getId())).toList())
                                .build());
                    }
                }
            }
        }
        return pipelineEnergyConsumptions;
}

///4.1
    public Long operationEnergyConsumption(List<Operation> operationList){

        Long totalEnergyConsumption = 0L;

        for (Operation op : operationList){

             totalEnergyConsumption = totalEnergyConsumption +
                    ((long) op.getCpuUsageUnit() * StaticsMetrics.PROVIDER_CPU_ENERGY_CONSUMPTION) +
                    ((long) op.getMemUsageUnit() * StaticsMetrics.PROVIDER_MEMORY_ENERGY_CONSUMPTION) +
                    ((long) op.getObservationUnit() * StaticsMetrics.PROVIDER_OBSERVATION_ENERGY_CONSUMPTION) +
                    ((long) op.getInputDataVolumeUnit() * StaticsMetrics.PROVIDER_INSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION);
        }
        return totalEnergyConsumption;
        }

    //### 5
    public void publishEnergyConsumptionReport(ArrayList<PipelineEnergyConsumption> pipelineEnergyConsumptions){

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("pipeline_execution_plans_info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("originalPipeline");
        row.createCell(1).setCellValue("commonPipeline");
        row.createCell(2).setCellValue("commonOperationList");
        row.createCell(3).setCellValue("uniqueOperationList");
        row.createCell(4).setCellValue("commonOperationsEnergyConsumption");
        row.createCell(5).setCellValue("wholePipelineEnergyConsumption");
        row.createCell(6).setCellValue("percentageOfCommonEnergy");
        row.createCell(7).setCellValue("originalPipelineOperations");


        //HSSFCellStyle dateCellStyle = workbook.createCellStyle();

        int dataRowIndex = 1;
    for (PipelineEnergyConsumption pipeline :pipelineEnergyConsumptions ) {
        HSSFRow dataRow = sheet.createRow(dataRowIndex);
        if(pipeline.getOriginalPipeline() != null) {
            dataRow.createCell(0).setCellValue(pipeline.getOriginalPipeline());
        }

        if (pipeline.getOriginalPipelineOperation() != null) {
            dataRow.createCell(1).setCellValue(pipeline.getCommonPipeline().stream()
                            .map(Object::toString)
                    .collect(Collectors.joining(",")));
        } else {
            dataRow.createCell(1).setCellValue("EMPTY");
        }

        if (pipeline.getCommonOperationList() != null) {
            dataRow.createCell(2).setCellValue(pipeline.getCommonOperationList().stream()
                    .map(operation -> operation.getOperationId().toString())
                    .collect(Collectors.joining(",")));
        } else {
            dataRow.createCell(2).setCellValue("EMPTY");
        }

        if (pipeline.getUniqueOperationList() != null) {
            dataRow.createCell(3).setCellValue(pipeline.getUniqueOperationList() .stream()
                    .map(operation -> operation.getOperationId().toString())
                    .collect(Collectors.joining(",")));
        } else {
            dataRow.createCell(3).setCellValue("EMPTY");
        }
        dataRow.createCell(4).setCellValue(pipeline.getCommonOperationsEnergyConsumption());

        dataRow.createCell(5).setCellValue(pipeline.getWholePipelineEnergyConsumption());
        dataRow.createCell(6).setCellValue((double) (pipeline.getCommonOperationsEnergyConsumption() * 100) /pipeline.getWholePipelineEnergyConsumption());

        if (pipeline.getOriginalPipelineOperation() != null) {
            dataRow.createCell(7).setCellValue(pipeline.getOriginalPipelineOperation() .stream()
                    .map(operation -> operation.getOperationId().toString())
                    .collect(Collectors.joining(",")));
        } else {
            dataRow.createCell(7).setCellValue("EMPTY");
        }

        dataRowIndex++;
    }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream ops = null;
        try {
            String filePath = "./model_output-"+ getRandomNumber(0, 100) +".xlsx" ;
            ops = new FileOutputStream(new File(filePath));
            workbook.write(ops);
            workbook.close();
            ops.close();

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException:",e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException:",e.getMessage());
            throw new RuntimeException(e);
        }
    }
    //###6
    public void publishGeneratedPipelinesReport(ArrayList<Pipeline> pipelineList){

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("pipeline_info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("PipelineId");

        row.createCell(1).setCellValue("filter_1");
        row.createCell(2).setCellValue("filter_2");
        row.createCell(3).setCellValue("filter_3");

        row.createCell(4).setCellValue("anonym_1");
        row.createCell(5).setCellValue("anonym_2");
        row.createCell(6).setCellValue("anonym_3");

        row.createCell(7).setCellValue("aggrigate_1");
        row.createCell(8).setCellValue("aggrigate_2");
        row.createCell(9).setCellValue("aggrigate_3");

        row.createCell(10).setCellValue("convert_1");
        row.createCell(11).setCellValue("convert_2");




        //HSSFCellStyle dateCellStyle = workbook.createCellStyle();

        int dataRowIndex = 1;
        for (Pipeline pipeline :pipelineList ) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            if(pipeline.getId() != null) {
                ArrayList<Integer> createdCellIndex = new ArrayList<>();
                dataRow.createCell(0).setCellValue(pipeline.getId());
                for (Operation operation: pipeline.getPipelineOperations()){
                    logger.info(operation.getOperationCode());
                    Integer map = mapOperationTypesToCells(operation.getOperationCode());
                      dataRow.createCell(map).setCellValue(1);
                    createdCellIndex.add(map);
                }
                for (int i = 1; i <12 ; i++) {
                    if(!createdCellIndex.contains(i)){
                        dataRow.createCell(i).setCellValue(0);

                    }
                }
            }


            dataRowIndex++;
        }

/*        //set 0 to all empty cells
        for (int i = 0; i < dataRowIndex; i++) {
            for (Cell cell : sheet.getRow(i)) {
                if (cell == null || cell.getCellType() == CellType.BLANK) {
                    cell.setCellValue(0);
                }
            }
        }*/

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream ops = null;
        try {
            String filePath = "./pipeline_model_output-"+ getRandomNumber(0, 100) +".xlsx" ;
            ops = new FileOutputStream(new File(filePath));
            workbook.write(ops);
            workbook.close();
            ops.close();

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException:",e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException:",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void publishEnergyConsumptionReportDistinct(ArrayList<PipelineEnergyConsumption> pipelineEnergyConsumptions){

     List<PipelineEnergyConsumption> disticntPipelineEnergyConsumption = pipelineEnergyConsumptions.stream().distinct().collect(Collectors.toList());


        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("pipeline_execution_plans_info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("originalPipeline");
        row.createCell(1).setCellValue("commonPipeline");
        row.createCell(2).setCellValue("commonOperationList");
        row.createCell(3).setCellValue("uniqueOperationList");
        row.createCell(4).setCellValue("commonOperationsEnergyConsumption");
        row.createCell(5).setCellValue("wholePipelineEnergyConsumption");
        row.createCell(6).setCellValue("percentageOfCommonEnergy");
        row.createCell(7).setCellValue("originalPipelineOperations");


        //HSSFCellStyle dateCellStyle = workbook.createCellStyle();

        int dataRowIndex = 1;
        for (PipelineEnergyConsumption pipeline :disticntPipelineEnergyConsumption ) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);
            if(pipeline.getOriginalPipeline() != null) {
                dataRow.createCell(0).setCellValue(pipeline.getOriginalPipeline());
            }

            if (pipeline.getOriginalPipelineOperation() != null) {
                dataRow.createCell(1).setCellValue(pipeline.getCommonPipeline().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(",")));
            } else {
                dataRow.createCell(1).setCellValue("EMPTY");
            }

            if (pipeline.getCommonOperationList() != null) {
                dataRow.createCell(2).setCellValue(pipeline.getCommonOperationList().stream()
                        .map(operation -> operation.getOperationId().toString())
                        .collect(Collectors.joining(",")));
            } else {
                dataRow.createCell(2).setCellValue("EMPTY");
            }

            if (pipeline.getUniqueOperationList() != null) {
                dataRow.createCell(3).setCellValue(pipeline.getUniqueOperationList() .stream()
                        .map(operation -> operation.getOperationId().toString())
                        .collect(Collectors.joining(",")));
            } else {
                dataRow.createCell(3).setCellValue("EMPTY");
            }
            dataRow.createCell(4).setCellValue(pipeline.getCommonOperationsEnergyConsumption());

            dataRow.createCell(5).setCellValue(pipeline.getWholePipelineEnergyConsumption());
            dataRow.createCell(6).setCellValue((double) (pipeline.getCommonOperationsEnergyConsumption() * 100) /pipeline.getWholePipelineEnergyConsumption());

            if (pipeline.getOriginalPipelineOperation() != null) {
                dataRow.createCell(7).setCellValue(pipeline.getOriginalPipelineOperation() .stream()
                        .map(operation -> operation.getOperationId().toString())
                        .collect(Collectors.joining(",")));
            } else {
                dataRow.createCell(7).setCellValue("EMPTY");
            }

            dataRowIndex++;
        }

        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream ops = null;
        try {
            String filePath = "./model_output-"+ getRandomNumber(0, 100) +".xlsx" ;
            ops = new FileOutputStream(new File(filePath));
            workbook.write(ops);
            workbook.close();
            ops.close();

        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException:",e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            logger.error("IOException:",e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public int mapOperationTypesToCells(String operationType){
        List cellDefaultValues = List.of(0,0,0,0,0,0,0,0,0,0,0);
        int cellCode =-1;
        switch(operationType) {
            case "filter_1":
                cellCode=1;
                break;
            case "filter_2":
                cellCode=2;
                break;
            case "filter_3":
                cellCode=3;
                break;
            case "anonym_1":
                cellCode=4;
                break;
            case "anonym_2":
                cellCode=5;
                break;
            case "anonym_3":
                cellCode=6;
                break;
            case "aggrigate_1":
                cellCode=7;
                break;
            case "aggrigate_2":
                cellCode=8;
                break;
            case "aggrigate_3":
                cellCode=9;
                break;
            case "convert_1":
                cellCode=10;
                break;
            case "convert_2":
                cellCode=11;
                break;

        }
        return cellCode;
    }

  /////////////////////////////////////////////////////////////////////////////////////////////


}