package com.tub.ise.energyprofilersimulator;

import com.tub.ise.energyprofilersimulator.model.CommonPipelines;
import com.tub.ise.energyprofilersimulator.model.Operation;
import com.tub.ise.energyprofilersimulator.model.Pipeline;
import com.tub.ise.energyprofilersimulator.model.PipelineEnergyConsumption;
import com.tub.ise.energyprofilersimulator.v2.SimulationEnvV2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class EnergyProfilerSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnergyProfilerSimulatorApplication.class, args);
        //firstEvaluationScenario();
        secondEvaluationScenario();
    }

//    public  static void firstEvaluationScenario(){
//        SimulationEnv senv = new SimulationEnv();
//        List<Operation> simulatedOperations = senv.createSimulationBasicEvn("/operations-definition.xlsx");
//        ArrayList<Pipeline> pipelines = senv.createPipelines(StaticsMetrics.NUMBER_OF_PIPELINES, StaticsMetrics.RANDOM_SEED_OF_OPR_PER_PIP, simulatedOperations);
//        senv.createDifferentPipelineConfigurations(pipelines);
//        senv.calculateDifferentConfigEnergy(pipelines);
//        senv.publishEnergyConsumptionReport(pipelines);
//    }

    public static void secondEvaluationScenario(){
        SimulationEnvV2 senv = new SimulationEnvV2();
        List<Operation> simulatedOperations = senv.createSimulationBasicEvn("/operations-definition-2.xlsx");
        ArrayList<Pipeline> pipelines = senv.createPipelines(StaticsMetrics.NUMBER_OF_PIPELINES, StaticsMetrics.RANDOM_SEED_OF_OPR_PER_PIP, simulatedOperations);
        List<CommonPipelines> commonInAllPairs = senv.findCommonInAllPairs(pipelines,StaticsMetrics.THRESHOLD_FOR_MIN_COMMON_LIST_LENGTH);
        ArrayList<PipelineEnergyConsumption> newPipelinesConfigAndEnergy = senv.createNewPipelinesConfigAndEnergy(pipelines, commonInAllPairs);
        senv.publishGeneratedPipelinesReport(pipelines);
       senv.publishEnergyConsumptionReport(newPipelinesConfigAndEnergy);
       // senv.publishEnergyConsumptionReportDistinct(newPipelinesConfigAndEnergy);
    }
}
