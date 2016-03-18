package com.ucredit.hermes.model.qianhai;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * record信息
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "qianhai_records")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QianhaiRecords extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -140877635606560072L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 业务发生时间
     */
    private String dataBuildTime;
    /**
     * 数据状态
     * 99 - 权限不足
     * 1 - 已验证
     * 2 - 未验证
     */
    private String dataStatus;
    /**
     * erCode
     */
    private String erCode;
    /**
     * erMsg
     */
    private String erMsg;
    /**
     * 查询严重等级
     */
    private String gradeQuery;
    /**
     * 证件号码
     */
    private String idNo;
    /**
     * 证件类型
     */
    private String idType;
    /**
     * 金额范围
     */
    private String moneyBound;
    /**
     * 主体名称
     */
    private String name;
    /**
     * 预留字段1
     */
    private String reservedFiled1;
    /**
     * 预留字段2
     */
    private String reservedFiled2;
    /**
     * 预留字段3
     */
    private String reservedFiled3;
    /**
     * 预留字段4
     */
    private String reservedFiled4;
    /**
     * 预留字段5
     */
    private String reservedFiled5;
    /**
     * 序列号
     */
    private String seqNo;
    /**
     * 来源代码
     */
    private String sourceId;
    /**
     * 查询状态
     */
    private String state;

    @Column(name = "search_record_id")
    private int search_record_id;

    public String getDataBuildTime() {
        return this.dataBuildTime;
    }

    public void setDataBuildTime(String dataBuildTime) {
        this.dataBuildTime = dataBuildTime;
    }

    public String getDataStatus() {
        return this.dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getErCode() {
        return this.erCode;
    }

    public void setErCode(String erCode) {
        this.erCode = erCode;
    }

    public String getErMsg() {
        return this.erMsg;
    }

    public void setErMsg(String erMsg) {
        this.erMsg = erMsg;
    }

    public String getGradeQuery() {
        return this.gradeQuery;
    }

    public void setGradeQuery(String gradeQuery) {
        this.gradeQuery = gradeQuery;
    }

    public String getIdNo() {
        return this.idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getIdType() {
        return this.idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getMoneyBound() {
        return this.moneyBound;
    }

    public void setMoneyBound(String moneyBound) {
        this.moneyBound = moneyBound;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReservedFiled1() {
        return this.reservedFiled1;
    }

    public void setReservedFiled1(String reservedFiled1) {
        this.reservedFiled1 = reservedFiled1;
    }

    public String getReservedFiled2() {
        return this.reservedFiled2;
    }

    public void setReservedFiled2(String reservedFiled2) {
        this.reservedFiled2 = reservedFiled2;
    }

    public String getReservedFiled3() {
        return this.reservedFiled3;
    }

    public void setReservedFiled3(String reservedFiled3) {
        this.reservedFiled3 = reservedFiled3;
    }

    public String getReservedFiled4() {
        return this.reservedFiled4;
    }

    public void setReservedFiled4(String reservedFiled4) {
        this.reservedFiled4 = reservedFiled4;
    }

    public String getReservedFiled5() {
        return this.reservedFiled5;
    }

    public void setReservedFiled5(String reservedFiled5) {
        this.reservedFiled5 = reservedFiled5;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getSourceId() {
        return this.sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getSearch_record_id() {
        return this.search_record_id;
    }

    public void setSearch_record_id(int search_record_id) {
        this.search_record_id = search_record_id;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
