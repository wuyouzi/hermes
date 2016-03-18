//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB)
// Reference Implementation, v2.2.5-b10
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source
// schema.
// Generated on: 2013.10.29 at 05:50:20 PM CST
//

package com.ucredit.service.web.idcard.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}status"/>
 *         &lt;element ref="{}value"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "status", "value" })
@XmlRootElement(name = "message")
public class Message {

    /**
     * 比对结果码 3:一致
     */
    public static final String AUTH_CODE_VALID = "3";
    public static final String AUTH_CODE_VALID_DESC = "一致";

    /**
     * 比对结果码 2:不一致
     */
    public static final String AUTH_CODE_INCONSISTENT = "2";
    public static final String AUTH_CODE_INCONSISTENT_DESC = "姓名和身份证号不一致";

    /**
     * 比对结果码 1:库中无此号
     */
    public static final String AUTH_CODE_NOT_FOUND = "1";
    public static final String AUTH_CODE_NOT_FOUND_DESC = "身份证号未找到";

    /**
     * 接口成功调用码(只有当网络故障时才不为0)
     */
    public static final String PROCESS_CODE = "0";

    /**
     * 查询成功执行码(只有当国政通服务器宕掉时才不为0)
     */
    public static final String QUERY_CODE = "0";

    protected String status;
    @XmlElement(required = true)
    protected String value;

    /**
     * Gets the value of the status property.
     * 
     * @return
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *         possible object is {@link String }
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *        allowed object is {@link String }
     */
    public void setValue(String value) {
        this.value = value;
    }

}