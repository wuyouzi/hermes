package com.ucredit.hermes.webservice.gzt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * anonymous complex type的 Java 类。
 * <p>
 * 以下模式片段指定包含在此类中的预期内容。
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="queryBatchReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "queryBatchReturn" })
@XmlRootElement(name = "queryBatchResponse")
public class QueryBatchResponse {

    @XmlElement(required = true)
    protected String queryBatchReturn;

    /**
     * 获取queryBatchReturn属性的值。
     *
     * @return
     *         possible object is {@link String }
     */
    public String getQueryBatchReturn() {
        return this.queryBatchReturn;
    }

    /**
     * 设置queryBatchReturn属性的值。
     *
     * @param value
     *        allowed object is {@link String }
     */
    public void setQueryBatchReturn(String value) {
        this.queryBatchReturn = value;
    }

}
