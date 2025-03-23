package com.tub.ise.energyprofilersimulator.model;

import java.util.List;

public class SubPipeline {

    private String id;
    private Integer pipelineId;

    // 0 = data provider   ,   1= data consumer
    //private Integer runningEnv;

    private List<Operation> consumerSideOperationList;
    private List<Operation> providerSideOperationList;

    private Long totalEnergyConsumption;

    public Long getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }

    public void setTotalEnergyConsumption(Long totalEnergyConsumption) {
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(Integer pipelineId) {
        this.pipelineId = pipelineId;
    }

    public List<Operation> getConsumerSideOperationList() {
        return consumerSideOperationList;
    }

    public void setConsumerSideOperationList(List<Operation> consumerSideOperationList) {
        this.consumerSideOperationList = consumerSideOperationList;
    }

    public List<Operation> getProviderSideOperationList() {
        return providerSideOperationList;
    }

    public void setProviderSideOperationList(List<Operation> providerSideOperationList) {
        this.providerSideOperationList = providerSideOperationList;
    }

    //    public Integer getRunningEnv() {
//        return runningEnv;
//    }
//
//    public void setRunningEnv(Integer runningEnv) {
//        this.runningEnv = runningEnv;
//    }


    public static SubPipelineBuilder builder() {
        return new SubPipelineBuilder();
    }


    public static final class SubPipelineBuilder {
        private SubPipeline subPipeline;

        private SubPipelineBuilder() {
            subPipeline = new SubPipeline();
        }


        public SubPipelineBuilder id(String id) {
            subPipeline.setId(id);
            return this;
        }

        public SubPipelineBuilder pipelineId(Integer pipelineId) {
            subPipeline.setPipelineId(pipelineId);
            return this;
        }

        public SubPipelineBuilder consumerSideOperationList(List<Operation> consumerSideOperationList) {
            subPipeline.setConsumerSideOperationList(consumerSideOperationList);
            return this;
        }

        public SubPipelineBuilder providerSideOperationList(List<Operation> providerSideOperationList) {
            subPipeline.setProviderSideOperationList(providerSideOperationList);
            return this;
        }

        public SubPipelineBuilder totalEnergyConsumption(Long totalEnergyConsumption) {
            subPipeline.setTotalEnergyConsumption(totalEnergyConsumption);
            return this;
        }

        public SubPipeline build() {
            return subPipeline;
        }
    }
}
