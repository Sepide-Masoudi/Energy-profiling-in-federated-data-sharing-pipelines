package com.tub.ise.energyprofilersimulator;

import com.tub.ise.energyprofilersimulator.model.Operation;
import com.tub.ise.energyprofilersimulator.model.Pipeline;
import com.tub.ise.energyprofilersimulator.model.SubPipeline;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class SimulationEnv {
    private static final Logger logger = LoggerFactory.getLogger(SimulationEnv.class);

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
                        operation.setConsumerDeployUsageUnit((int) row.getCell(3).getNumericCellValue());
                    }
                    if (row.getCell(4) != null && (row.getCell(4).getCellType() == CellType.NUMERIC || row.getCell(4).getCellType() == CellType.FORMULA )) {
                        operation.setProviderDeployUsageUnit((int) row.getCell(4).getNumericCellValue());
                    }

                    if (row.getCell(5) != null && (row.getCell(5).getCellType() == CellType.NUMERIC || row.getCell(5).getCellType() == CellType.FORMULA )) {
                        operation.setOutputDataVolumeUnit((int) row.getCell(5).getNumericCellValue());
                    }

                    if (row.getCell(6) != null && (row.getCell(6).getCellType() == CellType.NUMERIC || row.getCell(6).getCellType() == CellType.FORMULA )) {
                        operation.setInputDataVolumeUnit((int) row.getCell(6).getNumericCellValue());
                    }

                    if (row.getCell(7) != null && (row.getCell(7).getCellType() == CellType.NUMERIC || row.getCell(7).getCellType() == CellType.FORMULA )) {
                        operation.setObservationUnit((int) row.getCell(7).getNumericCellValue());
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
                pipeline.getPipelineOperations().add(selectedOperation);
            }
            pipelineList.add(pipeline);
        }
return pipelineList;
    }


    // ### 3
    //create all possible  different sub-pipelines for all pipelines
public void createDifferentPipelineConfigurations(ArrayList<Pipeline> pipelineArrayList){

    for (int i = 0; i < pipelineArrayList.size(); i++) {

        Pipeline pipeline = pipelineArrayList.get(i);
        List<SubPipeline> subPipelineList = new ArrayList<>();
        //running the whole pipeline in consumer side
        SubPipeline cSubPipe = SubPipeline.builder().pipelineId(pipeline.getId())
                .id(UUID.randomUUID().toString())
                .consumerSideOperationList(pipeline.getPipelineOperations())
                .pipelineId(pipeline.getId())
                .build();
        subPipelineList.add(cSubPipe);

        //running the whole pipeline in provider side
        SubPipeline pSubPipe = SubPipeline.builder().pipelineId(pipeline.getId())
                .id(UUID.randomUUID().toString())
                .providerSideOperationList(pipeline.getPipelineOperations())
                .pipelineId(pipeline.getId())
                .build();
        subPipelineList.add(pSubPipe);

        List<Operation> pipelineOperations = pipeline.getPipelineOperations();

        for (int j = 0; j < pipelineOperations.size(); j++) {
            List<Operation> pOperationSubList = pipeline.getPipelineOperations().subList(0, j + 1);
            List<Operation> cOperationSubList = pipeline.getPipelineOperations().subList(j + 1, pipelineOperations.size());
            SubPipeline subPipe = SubPipeline.builder().pipelineId(pipeline.getId())
                    .id(UUID.randomUUID().toString())
                    .providerSideOperationList(pOperationSubList)
                    .consumerSideOperationList(cOperationSubList)
                    .pipelineId(pipeline.getId())
                    .build();
            subPipelineList.add(subPipe);
        }

        pipeline.setSubPipelineList(subPipelineList);
    }

}

    //### 4.1

public void calculateDifferentConfigEnergy(ArrayList<Pipeline> pipelineArrayList) {

        ArrayList<Long> differentConfigurationEnergy = new ArrayList<>();

        for (int i = 0; i < pipelineArrayList.size(); i++) {
        Pipeline pipeline =pipelineArrayList.get(i);
        for (SubPipeline subPipeline: pipeline.getSubPipelineList()){
            subPipeline.setTotalEnergyConsumption(subPipelineTotalEnergyConsumption(subPipeline));
        }
    }
}

    // ### 4.2
    public Long subPipelineTotalEnergyConsumption(SubPipeline subPipeline) {

        Long consumerEnergyConsumption = 0L;
        Long providerEnergyConsumption = 0L;
        Long totalEnergyConsumption = 0L;
        List<Operation> providerSideOperationList = subPipeline.getProviderSideOperationList();
        List<Operation> consumerSideOperationList = subPipeline.getConsumerSideOperationList();

        //provider
        if (providerSideOperationList != null && !providerSideOperationList.isEmpty()) {
            for (int i = 0; i < providerSideOperationList.size(); i++) {
                Operation providerSideOperation = providerSideOperationList.get(i);

                providerEnergyConsumption = providerEnergyConsumption +
                        (providerSideOperation.getCpuUsageUnit() * StaticsMetrics.PROVIDER_CPU_ENERGY_CONSUMPTION) +
                        (providerSideOperation.getMemUsageUnit() * StaticsMetrics.PROVIDER_MEMORY_ENERGY_CONSUMPTION) +
                        (providerSideOperation.getProviderDeployUsageUnit() * StaticsMetrics.PROVIDER_DEPLOY_ENERGY_CONSUMPTION) +
                        (providerSideOperation.getObservationUnit() * StaticsMetrics.PROVIDER_OBSERVATION_ENERGY_CONSUMPTION) +
                        (providerSideOperation.getInputDataVolumeUnit() * StaticsMetrics.PROVIDER_INSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION);

                if (/*subPipeline.getConsumerSideOperationList() != null &&
                subPipeline.getConsumerSideOperationList().size() > 0 && */
                        i == (providerSideOperationList.size() - 1)) {
                    providerEnergyConsumption += (providerSideOperation.getOutputDataVolumeUnit() * StaticsMetrics.PROVIDER_OUTSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION); // **

                } else {
                    providerEnergyConsumption += (providerSideOperation.getOutputDataVolumeUnit() * StaticsMetrics.PROVIDER_INSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION); // **

                }
            }
        }

        //consumer
        if (consumerSideOperationList != null && !consumerSideOperationList.isEmpty()) {
        for (int i = 0; i < consumerSideOperationList.size(); i++) {
            Operation consumerSideOperation = consumerSideOperationList.get(i);

            consumerEnergyConsumption = consumerEnergyConsumption +
                    (consumerSideOperation.getCpuUsageUnit() * StaticsMetrics.CONSUMER_CPU_ENERGY_CONSUMPTION) +
                    (consumerSideOperation.getMemUsageUnit() * StaticsMetrics.CONSUMER_MEMORY_ENERGY_CONSUMPTION) +
                    (consumerSideOperation.getConsumerDeployUsageUnit() * StaticsMetrics.CONSUMER_DEPLOY_ENERGY_CONSUMPTION) +
                    (consumerSideOperation.getObservationUnit() * StaticsMetrics.CONSUMER_OBSERVATION_ENERGY_CONSUMPTION) +
                    (consumerSideOperation.getInputDataVolumeUnit() * StaticsMetrics.CONSUMER_INSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION);

            if (/*subPipeline.getConsumerSideOperationList() != null &&
                    subPipeline.getConsumerSideOperationList().size() > 0 &&*/
                    i == (consumerSideOperationList.size() - 1)) {
                consumerEnergyConsumption += (consumerSideOperation.getOutputDataVolumeUnit() * StaticsMetrics.CONSUMER_OUTSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION); // **

            } else {
                consumerEnergyConsumption += (consumerSideOperation.getOutputDataVolumeUnit() * StaticsMetrics.CONSUMER_INSIDE_DATA_TRANSMISSION_ENERGY_CONSUMPTION); // **

            }
        }
    }

        totalEnergyConsumption = providerEnergyConsumption+consumerEnergyConsumption; // + monitoring once ?!

        return totalEnergyConsumption;
    }

    //### 5
    public void publishEnergyConsumptionReport(ArrayList<Pipeline> pipelines){

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("pipeline_execution_plans_info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("sub_pipeline_id");
        row.createCell(1).setCellValue("pipeline_id");
        row.createCell(2).setCellValue("provider_side_operation_list");
        row.createCell(3).setCellValue("consumer_side_operation_list");
        row.createCell(4).setCellValue("total_energy_consumption");
        row.createCell(5).setCellValue("pipeline_length");
        row.createCell(6).setCellValue("pipeline_operation_list");


        //HSSFCellStyle dateCellStyle = workbook.createCellStyle();

        int dataRowIndex = 1;
    for (Pipeline pipeline :pipelines ) {
    for (SubPipeline subPipeline : pipeline.getSubPipelineList()) {
        HSSFRow dataRow = sheet.createRow(dataRowIndex);
        dataRow.createCell(0).setCellValue(subPipeline.getId());
        dataRow.createCell(1).setCellValue(subPipeline.getPipelineId());

        if (subPipeline.getProviderSideOperationList() != null) {
            dataRow.createCell(2).setCellValue(subPipeline.getProviderSideOperationList().stream()
                    .map(operation -> operation.getOperationId().toString())
                    .collect(Collectors.joining(",")));
        } else {
            dataRow.createCell(2).setCellValue("EMPTY");
        }

        if (subPipeline.getConsumerSideOperationList() != null) {
            dataRow.createCell(3).setCellValue(subPipeline.getConsumerSideOperationList().stream()
                    .map(operation -> operation.getOperationId().toString())
                    .collect(Collectors.joining(",")));
        } else {
            dataRow.createCell(3).setCellValue("EMPTY");
        }
        dataRow.createCell(4).setCellValue(subPipeline.getTotalEnergyConsumption());

        dataRow.createCell(5).setCellValue(pipeline.getLength());
        dataRow.createCell(6).setCellValue(pipeline.getPipelineOperations().stream()
                .map(operation -> operation.getOperationId().toString())
                .collect(Collectors.joining(",")));
        dataRowIndex++;
    }
}
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
        }

        FileOutputStream ops = null;
        try {
            ops = new FileOutputStream(new File("./model_output.xlsx"));
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
}