package com.ucredit.service.web.idcard.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="querySingleReturn" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "querySingleReturn" })
@XmlRootElement(name = "querySingleResponse")
public class QuerySingleResponse {

    @XmlElement(required = true)
    protected String querySingleReturn;

    /**
     * @return
     *         possible object is {@link String }
     */
    public String getQuerySingleReturn() {
        return this.querySingleReturn;
    }

    /**
     * @param value
     *        allowed object is {@link String }
     */
    public void setQuerySingleReturn(String value) {
        this.querySingleReturn = value;
    }

}
