package com.tub.ise.energyprofilersimulator.model;

import java.util.List;

public class Pipeline {

    private Integer id;
    private Integer length;
    private List<Operation> pipelineOperations;

    private List<SubPipeline> subPipelineList;

    private Long totalEnergyConsumption;



    public Long getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }

    public void setTotalEnergyConsumption(Long totalEnergyConsumption) {
        this.totalEnergyConsumption = totalEnergyConsumption;
    }

    public List<SubPipeline> getSubPipelineList() {
        return subPipelineList;
    }

    public void setSubPipelineList(List<SubPipeline> subPipelineList) {
        this.subPipelineList = subPipelineList;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public List<Operation> getPipelineOperations() {
        return pipelineOperations;
    }

    public void setPipelineOperations(List<Operation> pipelineOperations) {
        this.pipelineOperations = pipelineOperations;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static PipelineBuilder builder() {
        return new PipelineBuilder();
    }


    public static final class PipelineBuilder {
        private Pipeline pipeline;

        private PipelineBuilder() {
            pipeline = new Pipeline();
        }

        public static PipelineBuilder aPipeline() {
            return new PipelineBuilder();
        }

        public PipelineBuilder id(Integer id) {
            pipeline.setId(id);
            return this;
        }

        public PipelineBuilder length(Integer length) {
            pipeline.setLength(length);
            return this;
        }

        public PipelineBuilder pipelineOperations(List<Operation> pipelineOperations) {
            pipeline.setPipelineOperations(pipelineOperations);
            return this;
        }

        public PipelineBuilder subPipelineList(List<SubPipeline> subPipelineList) {
            pipeline.setSubPipelineList(subPipelineList);
            return this;
        }

        public PipelineBuilder totalEnergyConsumption(Long totalEnergyConsumption) {
            pipeline.setTotalEnergyConsumption(totalEnergyConsumption);
            return this;
        }


        public Pipeline build() {
            return pipeline;
        }
    }
}
