package com.tub.ise.energyprofilersimulator.model;

import java.util.List;
import java.util.Objects;

public class PipelineEnergyConsumption {

    private Long commonOperationsEnergyConsumption;
    private Long wholePipelineEnergyConsumption;
    private List<Operation> commonOperationList;
    private List<Integer> commonPipeline;

    private Integer   originalPipeline;

    private List<Operation> uniqueOperationList;

    private List<Operation> originalPipelineOperation;

    public Integer getOriginalPipeline() {
        return originalPipeline;
    }

    public void setOriginalPipeline(Integer originalPipeline) {
        this.originalPipeline = originalPipeline;
    }

    public Long getCommonOperationsEnergyConsumption() {
        return commonOperationsEnergyConsumption;
    }

    public void setCommonOperationsEnergyConsumption(Long commonOperationsEnergyConsumption) {
        this.commonOperationsEnergyConsumption = commonOperationsEnergyConsumption;
    }

    public Long getWholePipelineEnergyConsumption() {
        return wholePipelineEnergyConsumption;
    }

    public void setWholePipelineEnergyConsumption(Long wholePipelineEnergyConsumption) {
        this.wholePipelineEnergyConsumption = wholePipelineEnergyConsumption;
    }

    public List<Operation> getCommonOperationList() {
        return commonOperationList;
    }

    public void setCommonOperationList(List<Operation> commonOperationList) {
        this.commonOperationList = commonOperationList;
    }

    public List<Integer> getCommonPipeline() {
        return commonPipeline;
    }

    public void setCommonPipeline(List<Integer> commonPipeline) {
        this.commonPipeline = commonPipeline;
    }

    public List<Operation> getUniqueOperationList() {
        return uniqueOperationList;
    }

    public void setUniqueOperationList(List<Operation> uniqueOperationList) {
        this.uniqueOperationList = uniqueOperationList;
    }

    public List<Operation> getOriginalPipelineOperation() {
        return originalPipelineOperation;
    }

    public void setOriginalPipelineOperation(List<Operation> originalPipelineOperation) {
        this.originalPipelineOperation = originalPipelineOperation;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private PipelineEnergyConsumption pipelineEnergyConsumption;

        private Builder() {
            pipelineEnergyConsumption = new PipelineEnergyConsumption();
        }

        public Builder commonOperationsEnergyConsumption(Long commonOperationsEnergyConsumption) {
            pipelineEnergyConsumption.setCommonOperationsEnergyConsumption(commonOperationsEnergyConsumption);
            return this;
        }

        public Builder wholePipelineEnergyConsumption(Long wholePipelineEnergyConsumption) {
            pipelineEnergyConsumption.setWholePipelineEnergyConsumption(wholePipelineEnergyConsumption);
            return this;
        }

        public Builder commonOperationList(List<Operation> commonOperationList) {
            pipelineEnergyConsumption.setCommonOperationList(commonOperationList);
            return this;
        }

        public Builder commonPipeline(List<Integer> commonPipeline) {
            pipelineEnergyConsumption.setCommonPipeline(commonPipeline);
            return this;
        }

        public Builder originalPipeline(Integer originalPipeline) {
            pipelineEnergyConsumption.setOriginalPipeline(originalPipeline);
            return this;
        }

        public Builder uniqueOperationList(List<Operation> uniqueOperationList) {
            pipelineEnergyConsumption.setUniqueOperationList(uniqueOperationList);
            return this;
        }

        public Builder originalPipelineOperation(List<Operation> originalPipelineOperation) {
            pipelineEnergyConsumption.setOriginalPipelineOperation(originalPipelineOperation);
            return this;
        }

        public PipelineEnergyConsumption build() {
            return pipelineEnergyConsumption;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PipelineEnergyConsumption that)) return false;
      return Objects.equals(getCommonPipeline().get(0), that.getOriginalPipeline()) && Objects.equals(getOriginalPipeline(), that.getCommonPipeline().get(0));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommonPipeline(), getOriginalPipeline());
    }
}
