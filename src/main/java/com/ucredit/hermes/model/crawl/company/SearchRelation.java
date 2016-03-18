package com.ucredit.hermes.model.crawl.company;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 记录中间关联信息
 * 
 * @author zhouwuyuan
 */
@Entity
@Table(name = "crawl_search_relation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SearchRelation extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -5537380312099228168L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 外键
     */
    private int fk_id;
    /**
     * 登陆名
     */
    private String systemId;
    /**
     * 操作人
     */
    private String operationName;
    /**
     * ip
     */
    private String ip;
    private String lendRequestId;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public int getFk_id() {
        return this.fk_id;
    }

    public void setFk_id(int fk_id) {
        this.fk_id = fk_id;
    }

    public String getSystemId() {
        return this.systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getOperationName() {
        return this.operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLendRequestId() {
        return this.lendRequestId;
    }

    public void setLendRequestId(String lendRequestId) {
        this.lendRequestId = lendRequestId;
    }

}
