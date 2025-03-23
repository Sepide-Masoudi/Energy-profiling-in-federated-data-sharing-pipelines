package com.tub.ise.energyprofilersimulator.model;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

public class CommonPipelines {

   List<Integer> pipelineIdList;
   List<Operation>  commonOperationList;

    public List<Integer> getPipelineIdList() {
        return pipelineIdList;
    }

    public void setPipelineIdList(List<Integer> pipelineIdList) {
        this.pipelineIdList = pipelineIdList;
    }

    public List<Operation> getCommonOperationList() {
        return commonOperationList;
    }

    public void setCommonOperationList(List<Operation> commonOperationList) {
        this.commonOperationList = commonOperationList;
    }


    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CommonPipelines commonPipelines;

        private Builder() {
            commonPipelines = new CommonPipelines();
        }


        public Builder pipelineIdList(List<Integer> pipelineIdList) {
            commonPipelines.setPipelineIdList(pipelineIdList);
            return this;
        }

        public Builder commonOperationList(List<Operation> commonOperationList) {
            commonPipelines.setCommonOperationList(commonOperationList);
            return this;
        }

        public CommonPipelines build() {
            return commonPipelines;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonPipelines that)) return false;
        return CollectionUtils.isEqualCollection(getPipelineIdList(), that.getPipelineIdList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPipelineIdList(), getCommonOperationList());
    }
}
