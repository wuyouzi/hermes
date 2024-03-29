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
     * @return
     *         possible object is {@link String }
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * @param value
     *        allowed object is {@link String }
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * @return
     *         possible object is {@link String }
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * @param value
     *        allowed object is {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * @return
     *         possible object is {@link String }
     */
    public String getType() {
        return this.type;
    }

    /**
     * @param value
     *        allowed object is {@link String }
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * @return
     *         possible object is {@link String }
     */
    public String getParam() {
        return this.param;
    }

    /**
     * @param value
     *        allowed object is {@link String }
     */
    public void setParam(String value) {
        this.param = value;
    }

}
