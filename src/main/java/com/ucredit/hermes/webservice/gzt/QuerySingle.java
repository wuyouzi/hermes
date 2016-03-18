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
 *         &lt;element name="userName_" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="password_" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="type_" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="param_" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "userName", "password", "type", "param" })
@XmlRootElement(name = "querySingle")
public class QuerySingle {

    @XmlElement(name = "userName_", required = true)
    protected String userName;
    @XmlElement(name = "password_", required = true)
    protected String password;
    @XmlElement(name = "type_", required = true)
    protected String type;
    @XmlElement(name = "param_", required = true)
    protected String param;

    /**
     * 获取userName属性的值。
     *
     * @return
     *         possible object is {@link String }
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * 设置userName属性的值。
     *
     * @param value
     *        allowed object is {@link String }
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * 获取password属性的值。
     *
     * @return
     *         possible object is {@link String }
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * 设置password属性的值。
     *
     * @param value
     *        allowed object is {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * 获取type属性的值。
     *
     * @return
     *         possible object is {@link String }
     */
    public String getType() {
        return this.type;
    }

    /**
     * 设置type属性的值。
     *
     * @param value
     *        allowed object is {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * 获取param属性的值。
     *
     * @return
     *         possible object is {@link String }
     */
    public String getParam() {
        return this.param;
    }

    /**
     * 设置param属性的值。
     *
     * @param value
     *        allowed object is {@link String }
     */
    public void setParam(String value) {
        this.param = value;
    }

}
