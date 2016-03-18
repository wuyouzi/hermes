package com.ucredit.hermes.dto;

import java.io.Serializable;
import java.util.List;

import com.ucredit.hermes.enums.BaiRongResultCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.BaiRongAuthentication;
import com.ucredit.hermes.model.BaiRongConsumption;
import com.ucredit.hermes.model.BaiRongLocation;
import com.ucredit.hermes.model.BaiRongMedia;
import com.ucredit.hermes.model.BaiRongStability;

public class BaiRongResult implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6857617035968085059L;

    /**
     * 查询结果
     */
    private ResultType resultType;

    /**
     * 查询结果成功或失败对应编码
     */
    private BaiRongResultCode resultCode;
    /**
     * 查询结果成功或失败对应编码描述
     */
    private String resultCodeDesc;

    /**
     * 查询结果对应的查询条件的ID
     */
    private Integer searchId;

    /**
     * 身份信息核查
     */
    private BaiRongAuthentication authentication;

    /**
     * 稳定性评估
     */
    private BaiRongStability stability;

    /**
     * 消费评估报告
     */
    private List<BaiRongConsumption> consumptions;

    /**
     * 位置信息核查
     */
    private List<BaiRongLocation> locations;

    /**
     * 兴趣爱好
     */
    private List<BaiRongMedia> medias;

    public ResultType getResultType() {
        return this.resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public BaiRongResultCode getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(BaiRongResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCodeDesc() {
        return this.resultCodeDesc;
    }

    public void setResultCodeDesc(String resultCodeDesc) {
        this.resultCodeDesc = resultCodeDesc;
    }

    public Integer getSearchId() {
        return this.searchId;
    }

    public void setSearchId(Integer searchId) {
        this.searchId = searchId;
    }

    public BaiRongAuthentication getAuthentication() {
        return this.authentication;
    }

    public void setAuthentication(BaiRongAuthentication authentication) {
        this.authentication = authentication;
    }

    public BaiRongStability getStability() {
        return this.stability;
    }

    public void setStability(BaiRongStability stability) {
        this.stability = stability;
    }

    public List<BaiRongConsumption> getConsumptions() {
        return this.consumptions;
    }

    public void setConsumptions(List<BaiRongConsumption> consumptions) {
        this.consumptions = consumptions;
    }

    public List<BaiRongMedia> getMedias() {
        return this.medias;
    }

    public void setMedias(List<BaiRongMedia> medias) {
        this.medias = medias;
    }

    public List<BaiRongLocation> getLocations() {
        return this.locations;
    }

    public void setLocations(List<BaiRongLocation> locations) {
        this.locations = locations;
    }

}
