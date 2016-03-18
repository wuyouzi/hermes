/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 日志表
 *
 * @author liuqianqian
 */
@Entity
@Table(name = "log_infos")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LogInfo extends BaseModel<Integer> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -2830662309328951152L;

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 记录的主表名
     */

    @Column(length = 64, nullable = false)
    private String tableName;

    /**
     * 主表记录的id
     */
    private int recordId;

    /**
     * 查询人
     */
    @Column(length = 128, nullable = false)
    private String operationName;

    /**
     * 当前查询人ip
     */

    @Column(length = 64, nullable = false)
    private String ip;

    /**
     * 查询人机构
     */
    @ManyToOne
    private UserGroup group;

    /**
     * 数据源渠道
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private DataChannel dataChannel;

    /**
     * 记录信息
     */
    @Lob
    private String data;

    /**
     * 查询信息
     */
    @Lob
    private String queryString;

    /**
     * 子报告结果(都为0是FAILED，一个为1，是SUCCESS)
     */
    private boolean status;

    /**
     * 报告编号
     */
    @Column(length = 32)
    private String reportID;

    /**
     * 为查询申请条件中的引用ID
     */
    @Column(length = 32)
    private String refID;

    /**
     * 鹏元返回的数据是否有错误(子报告其中一个错误 就返回true)
     */
    private boolean systemError;

    /**
     * 该客户是否被冻结
     */
    private boolean frozen;

    /**
     * 第三方报告生成结束时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "buildEndTime")
    private Date buildEndTime;

    /**
     * 请求时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", nullable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date lastUpdateTime;
    /**
     * 处理完成时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "endTime", nullable = false)
    private Date endTime;

    /**
     * 返回结果的状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 32)
    private ResultType resultType;

    /**
     * 错误代码
     */
    @Column(length = 128)
    private String errorCode;

    /**
     * 错误信息
     */
    @Lob
    private String errorMessage;

    /**
     * 进件号
     */
    @Column(length = 64)
    private String lendRequestId;

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

    public LogInfo() {

    }

    public LogInfo(String operationName, UserGroup group, String ip, Date date,
            String tableName) {
        this.operationName = operationName;
        this.group = group;
        this.ip = ip;
        this.createTime = date;
        this.tableName = tableName;
        this.lastUpdateTime = date;
    }

    /**
     * @return the lastUpdateTime
     */
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    /**
     * @param lastUpdateTime
     *        the lastUpdateTime to set
     */
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getQueryString() {
        return this.queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the resultType
     */
    public ResultType getResultType() {
        return this.resultType;
    }

    /**
     * @param resultType
     *        the resultType to set
     */
    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the id
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * @param id
     *        the id to set
     */
    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the createTime
     */
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * @param createTime
     *        the createTime to set
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return the dataChannel
     */
    public DataChannel getDataChannel() {
        return this.dataChannel;
    }

    /**
     * @param dataChannel
     *        the dataChannel to set
     */
    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    /**
     * @return the recordId
     */
    public int getRecordId() {
        return this.recordId;
    }

    /**
     * @param recordId
     *        the recordId to set
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * @param tableName
     *        the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return this.ip;
    }

    /**
     * @param ip
     *        the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public UserGroup getGroup() {
        return this.group;
    }

    public void setGroup(UserGroup group) {
        this.group = group;
    }

    /**
     * @return the status
     */
    public boolean isStatus() {
        return this.status;
    }

    /**
     * @param status
     *        the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return the reportID
     */
    public String getReportID() {
        return this.reportID;
    }

    /**
     * @param reportID
     *        the reportID to set
     */
    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    /**
     * @return the refID
     */
    public String getRefID() {
        return this.refID;
    }

    /**
     * @param refID
     *        the refID to set
     */
    public void setRefID(String refID) {
        this.refID = refID;
    }

    /**
     * @return the systemError
     */
    public boolean isSystemError() {
        return this.systemError;
    }

    /**
     * @param systemError
     *        the systemError to set
     */
    public void setSystemError(boolean systemError) {
        this.systemError = systemError;
    }

    /**
     * @return the frozen
     */
    public boolean isFrozen() {
        return this.frozen;
    }

    /**
     * @param frozen
     *        the frozen to set
     */
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    /**
     * @return the buildEndTime
     */
    public Date getBuildEndTime() {
        return this.buildEndTime;
    }

    /**
     * @param buildEndTime
     *        the buildEndTime to set
     */
    public void setBuildEndTime(Date buildEndTime) {
        this.buildEndTime = buildEndTime;
    }

}
