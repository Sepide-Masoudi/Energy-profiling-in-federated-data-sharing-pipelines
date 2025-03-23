package com.tub.ise.energyprofilersimulator.model;

public class Operation {
    private Integer operationId;
    private Integer pipelineId = 0 ;
    private Integer  subPipelineId = 0;
    private Integer  cpuUsageUnit;
    private Integer  MemUsageUnit;
    private Integer providerDeployUsageUnit;
    private Integer consumerDeployUsageUnit;
    private Integer  outputDataVolumeUnit;
    private Integer  inputDataVolumeUnit;
    private Integer  observationUnit;

    private String operationCode;
    private String operationType;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Integer getConsumerDeployUsageUnit() {
        return consumerDeployUsageUnit;
    }

    public void setConsumerDeployUsageUnit(Integer consumerDeployUsageUnit) {
        this.consumerDeployUsageUnit = consumerDeployUsageUnit;
    }

    public Integer getOperationId() {
        return operationId;
    }

    public void setOperationId(Integer operationId) {
        this.operationId = operationId;
    }

    public Integer getPipelineId() {
        return pipelineId;
    }

    public void setPipelineId(Integer pipelineId) {
        this.pipelineId = pipelineId;
    }

    public Integer getSubPipelineId() {
        return subPipelineId;
    }

    public void setSubPipelineId(Integer subPipelineId) {
        this.subPipelineId = subPipelineId;
    }

    public Integer getCpuUsageUnit() {
        return cpuUsageUnit;
    }

    public void setCpuUsageUnit(Integer cpuUsageUnit) {
        this.cpuUsageUnit = cpuUsageUnit;
    }

    public Integer getMemUsageUnit() {
        return MemUsageUnit;
    }

    public void setMemUsageUnit(Integer memUsageUnit) {
        MemUsageUnit = memUsageUnit;
    }

    public Integer getProviderDeployUsageUnit() {
        return providerDeployUsageUnit;
    }

    public void setProviderDeployUsageUnit(Integer providerDeployUsageUnit) {
        this.providerDeployUsageUnit = providerDeployUsageUnit;
    }

    public Integer getOutputDataVolumeUnit() {
        return outputDataVolumeUnit;
    }

    public void setOutputDataVolumeUnit(Integer outputDataVolumeUnit) {
        this.outputDataVolumeUnit = outputDataVolumeUnit;
    }

    public Integer getInputDataVolumeUnit() {
        return inputDataVolumeUnit;
    }

    public void setInputDataVolumeUnit(Integer inputDataVolumeUnit) {
        this.inputDataVolumeUnit = inputDataVolumeUnit;
    }

    public Integer getObservationUnit() {
        return observationUnit;
    }

    public void setObservationUnit(Integer observationUnit) {
        this.observationUnit = observationUnit;
    }


    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Operation operation;

        private Builder() {
            operation = new Operation();
        }

        public static Builder anOperation() {
            return new Builder();
        }

        public Builder operationId(Integer operationId) {
            operation.setOperationId(operationId);
            return this;
        }

        public Builder pipelineId(Integer pipelineId) {
            operation.setPipelineId(pipelineId);
            return this;
        }

        public Builder subPipelineId(Integer subPipelineId) {
            operation.setSubPipelineId(subPipelineId);
            return this;
        }

        public Builder cpuUsageUnit(Integer cpuUsageUnit) {
            operation.setCpuUsageUnit(cpuUsageUnit);
            return this;
        }

        public Builder MemUsageUnit(Integer MemUsageUnit) {
            operation.setMemUsageUnit(MemUsageUnit);
            return this;
        }

        public Builder providerDeployUsageUnit(Integer providerDeployUsageUnit) {
            operation.setProviderDeployUsageUnit(providerDeployUsageUnit);
            return this;
        }

        public Builder consumerDeployUsageUnit(Integer consumerDeployUsageUnit) {
            operation.setConsumerDeployUsageUnit(consumerDeployUsageUnit);
            return this;
        }

        public Builder outputDataVolumeUnit(Integer outputDataVolumeUnit) {
            operation.setOutputDataVolumeUnit(outputDataVolumeUnit);
            return this;
        }

        public Builder inputDataVolumeUnit(Integer inputDataVolumeUnit) {
            operation.setInputDataVolumeUnit(inputDataVolumeUnit);
            return this;
        }

        public Builder observationUnit(Integer observationUnit) {
            operation.setObservationUnit(observationUnit);
            return this;
        }

        public Builder operationCode(String operationCode) {
            operation.setOperationCode(operationCode);
            return this;
        }

        public Builder operationType(String operationType) {
            operation.setOperationType(operationType);
            return this;
        }

        public Operation build() {
            return operation;
        }
    }
}
