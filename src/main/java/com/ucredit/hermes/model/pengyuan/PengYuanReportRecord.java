package com.ucredit.hermes.model.pengyuan;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.validator.constraints.Length;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.QueryType;

@Entity
@Table(name = "PengYuanReportRecord")
public class PengYuanReportRecord extends BaseModel<Integer> {
    private static final long serialVersionUID = 2824817464722197903L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 关联表的ID
     */
    private String tableId;
    /**
     * 外键id，根据queryType关联不同表的refId
     */
    @Index(name = "idx_PengYuanReportRecord_main_id")
    private String mainId;
    /**
     * 鹏元公司返回的报告ID
     */
    private String pengYuanRePortId;
    /**
     * 鹏元公司返回的流水号ID
     */
    private String pengYuanReturnRefId;
    @Index(name = "idx_PengYuanReportRecord_sub_report_code")
    @Column(length = 64)
    @Length(max = 64)
    private String subReportCode;
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    @Index(name = "idx_PengYuanReportRecord_query_type")
    private QueryType queryType;
    /**
     * 数据来源
     */
    @Index(name = "idx_PengYuanReportRecord_data_channel")
    @Enumerated(EnumType.STRING)
    @Column(length = 32, nullable = false)
    private DataChannel dataChannel;
    /**
     * 子报告类型下对应查到的条数
     */
    private int count;
    /**
     * 结果返回时间
     */
    @Index(name = "idx_PengYuanReportRecord_result_time")
    private Date resultTime;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubReportCode() {
        return this.subReportCode;
    }

    public void setSubReportCode(String subReportCode) {
        this.subReportCode = subReportCode;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMainId() {
        return this.mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public QueryType getQueryType() {
        return this.queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    public DataChannel getDataChannel() {
        return this.dataChannel;
    }

    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
    }

    public Date getResultTime() {
        return this.resultTime;
    }

    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }

    public String getPengYuanRePortId() {
        return this.pengYuanRePortId;
    }

    public void setPengYuanRePortId(String pengYuanRePortId) {
        this.pengYuanRePortId = pengYuanRePortId;
    }

    public String getPengYuanReturnRefId() {
        return this.pengYuanReturnRefId;
    }

    public void setPengYuanReturnRefId(String pengYuanReturnRefId) {
        this.pengYuanReturnRefId = pengYuanReturnRefId;
    }

    public String getTableId() {
        return this.tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

}
