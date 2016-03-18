package com.ucredit.hermes.model.tongdun;

import javax.persistence.Embeddable;

/**
 * 传入的条件
 *
 * @author zhouwuyuan
 */
@Embeddable
public class Condition {
    /**
     * 合作方 网站(包括WAP、HTML5)、移动端(IOS、安卓)
     */
    private String partner_code;
    /**
     * 合作方秘钥 网站(包括WAP、HTML5)、移动端(IOS、安卓)
     */
    private String secret_key;
    /**
     * 事件标识 接口调用时用该字段区分不同场景
     */
    private String event_id;
    /**
     * IP 网站(包括WAP、HTML5)、移动端(IOS、安卓)
     */
    private String ip_address;
    /**
     * 浏览器UA 仅限网站(包括WAP、HTML5)
     */
    private String user_agent_cust;
    /**
     * 请求来源 仅限网站(包括WAP、HTML5)
     */
    private String refer_cust;
    /**
     * TokenId 仅限网站(包括WAP、HTML5)
     */
    private String token_id;
    /**
     * 设备信息 仅限移动端(IOS、安卓)
     */
    private String black_box;
    /**
     * 账户
     */
    private String account_login;
    /**
     * 手机
     */
    private String account_mobile;
    /**
     * 邮箱
     */
    private String account_email;
    /**
     * 身份证
     */
    private String id_number;
    /**
     * 状态
     */
    private String state;
    /**
     * 姓名
     */
    private String account_name;
    /**
     * 座机
     */
    private String account_phone;
    /**
     * 工作单位
     */
    private String organization;
    /**
     * 地址
     */
    private String account_address;
    /**
     * 联系人电话
     */
    private String contacts_phone;
    /**
     * 修改银行卡号
     */
    private String card_number;

    private String resp_detail_type;

    private String device_type;

    private String contact1_name;

    private String contact1_mobile;

    private String contact2_name;

    private String contact2_mobile;

    private String contact3_name;

    private String contact3_mobile;

    private String ext_contact4_name;

    private String ext_contact4_mobile;

    private String apply_id;

    public String getApply_id() {
        return this.apply_id;
    }

    public void setApply_id(String apply_id) {
        this.apply_id = apply_id;
    }

    public String getContact1_name() {
        return this.contact1_name;
    }

    public void setContact1_name(String contact1_name) {
        this.contact1_name = contact1_name;
    }

    public String getContact1_mobile() {
        return this.contact1_mobile;
    }

    public void setContact1_mobile(String contact1_mobile) {
        this.contact1_mobile = contact1_mobile;
    }

    public String getContact2_name() {
        return this.contact2_name;
    }

    public void setContact2_name(String contact2_name) {
        this.contact2_name = contact2_name;
    }

    public String getContact2_mobile() {
        return this.contact2_mobile;
    }

    public void setContact2_mobile(String contact2_mobile) {
        this.contact2_mobile = contact2_mobile;
    }

    public String getContact3_name() {
        return this.contact3_name;
    }

    public void setContact3_name(String contact3_name) {
        this.contact3_name = contact3_name;
    }

    public String getContact3_mobile() {
        return this.contact3_mobile;
    }

    public void setContact3_mobile(String contact3_mobile) {
        this.contact3_mobile = contact3_mobile;
    }

    public String getExt_contact4_name() {
        return this.ext_contact4_name;
    }

    public void setExt_contact4_name(String ext_contact4_name) {
        this.ext_contact4_name = ext_contact4_name;
    }

    public String getExt_contact4_mobile() {
        return this.ext_contact4_mobile;
    }

    public void setExt_contact4_mobile(String ext_contact4_mobile) {
        this.ext_contact4_mobile = ext_contact4_mobile;
    }

    public String getPartner_code() {
        return this.partner_code;
    }

    public void setPartner_code(String partner_code) {
        this.partner_code = partner_code;
    }

    public String getSecret_key() {
        return this.secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getEvent_id() {
        return this.event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getIp_address() {
        return this.ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getUser_agent_cust() {
        return this.user_agent_cust;
    }

    public void setUser_agent_cust(String user_agent_cust) {
        this.user_agent_cust = user_agent_cust;
    }

    public String getRefer_cust() {
        return this.refer_cust;
    }

    public void setRefer_cust(String refer_cust) {
        this.refer_cust = refer_cust;
    }

    public String getToken_id() {
        return this.token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public String getBlack_box() {
        return this.black_box;
    }

    public void setBlack_box(String black_box) {
        this.black_box = black_box;
    }

    public String getAccount_login() {
        return this.account_login;
    }

    public void setAccount_login(String account_login) {
        this.account_login = account_login;
    }

    public String getAccount_mobile() {
        return this.account_mobile;
    }

    public void setAccount_mobile(String account_mobile) {
        this.account_mobile = account_mobile;
    }

    public String getAccount_email() {
        return this.account_email;
    }

    public void setAccount_email(String account_email) {
        this.account_email = account_email;
    }

    public String getId_number() {
        return this.id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAccount_name() {
        return this.account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_phone() {
        return this.account_phone;
    }

    public void setAccount_phone(String account_phone) {
        this.account_phone = account_phone;
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getAccount_address() {
        return this.account_address;
    }

    public void setAccount_address(String account_address) {
        this.account_address = account_address;
    }

    public String getContacts_phone() {
        return this.contacts_phone;
    }

    public void setContacts_phone(String contacts_phone) {
        this.contacts_phone = contacts_phone;
    }

    public String getCard_number() {
        return this.card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getResp_detail_type() {
        return this.resp_detail_type;
    }

    public void setResp_detail_type(String resp_detail_type) {
        this.resp_detail_type = resp_detail_type;
    }

    public String getDevice_type() {
        return this.device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime
            * result
                + (this.account_address == null ? 0 : this.account_address
                .hashCode());
        result = prime * result
                + (this.account_email == null ? 0 : this.account_email.hashCode());
        result = prime * result
                + (this.account_login == null ? 0 : this.account_login.hashCode());
        result = prime
            * result
                + (this.account_mobile == null ? 0 : this.account_mobile.hashCode());
        result = prime * result
                + (this.account_name == null ? 0 : this.account_name.hashCode());
        result = prime * result
                + (this.account_phone == null ? 0 : this.account_phone.hashCode());
        result = prime * result
                + (this.black_box == null ? 0 : this.black_box.hashCode());
        result = prime * result
                + (this.card_number == null ? 0 : this.card_number.hashCode());
        result = prime
            * result
                + (this.contacts_phone == null ? 0 : this.contacts_phone.hashCode());
        result = prime * result
                + (this.event_id == null ? 0 : this.event_id.hashCode());
        result = prime * result
                + (this.id_number == null ? 0 : this.id_number.hashCode());
        result = prime * result
                + (this.ip_address == null ? 0 : this.ip_address.hashCode());
        result = prime * result
                + (this.organization == null ? 0 : this.organization.hashCode());
        result = prime * result
                + (this.partner_code == null ? 0 : this.partner_code.hashCode());
        result = prime * result
                + (this.refer_cust == null ? 0 : this.refer_cust.hashCode());
        result = prime
            * result
                + (this.resp_detail_type == null ? 0 : this.resp_detail_type
                .hashCode());
        result = prime * result
                + (this.secret_key == null ? 0 : this.secret_key.hashCode());
        result = prime * result
            + (this.state == null ? 0 : this.state.hashCode());
        result = prime * result
                + (this.token_id == null ? 0 : this.token_id.hashCode());
        result = prime
            * result
                + (this.user_agent_cust == null ? 0 : this.user_agent_cust
                .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Condition other = (Condition) obj;
        if (this.account_address == null) {
            if (other.account_address != null) {
                return false;
            }
        } else if (!this.account_address.equals(other.account_address)) {
            return false;
        }
        if (this.account_email == null) {
            if (other.account_email != null) {
                return false;
            }
        } else if (!this.account_email.equals(other.account_email)) {
            return false;
        }
        if (this.account_login == null) {
            if (other.account_login != null) {
                return false;
            }
        } else if (!this.account_login.equals(other.account_login)) {
            return false;
        }
        if (this.account_mobile == null) {
            if (other.account_mobile != null) {
                return false;
            }
        } else if (!this.account_mobile.equals(other.account_mobile)) {
            return false;
        }
        if (this.account_name == null) {
            if (other.account_name != null) {
                return false;
            }
        } else if (!this.account_name.equals(other.account_name)) {
            return false;
        }
        if (this.account_phone == null) {
            if (other.account_phone != null) {
                return false;
            }
        } else if (!this.account_phone.equals(other.account_phone)) {
            return false;
        }
        if (this.black_box == null) {
            if (other.black_box != null) {
                return false;
            }
        } else if (!this.black_box.equals(other.black_box)) {
            return false;
        }
        if (this.card_number == null) {
            if (other.card_number != null) {
                return false;
            }
        } else if (!this.card_number.equals(other.card_number)) {
            return false;
        }
        if (this.contacts_phone == null) {
            if (other.contacts_phone != null) {
                return false;
            }
        } else if (!this.contacts_phone.equals(other.contacts_phone)) {
            return false;
        }
        if (this.event_id == null) {
            if (other.event_id != null) {
                return false;
            }
        } else if (!this.event_id.equals(other.event_id)) {
            return false;
        }
        if (this.id_number == null) {
            if (other.id_number != null) {
                return false;
            }
        } else if (!this.id_number.equals(other.id_number)) {
            return false;
        }
        if (this.ip_address == null) {
            if (other.ip_address != null) {
                return false;
            }
        } else if (!this.ip_address.equals(other.ip_address)) {
            return false;
        }
        if (this.organization == null) {
            if (other.organization != null) {
                return false;
            }
        } else if (!this.organization.equals(other.organization)) {
            return false;
        }
        if (this.partner_code == null) {
            if (other.partner_code != null) {
                return false;
            }
        } else if (!this.partner_code.equals(other.partner_code)) {
            return false;
        }
        if (this.refer_cust == null) {
            if (other.refer_cust != null) {
                return false;
            }
        } else if (!this.refer_cust.equals(other.refer_cust)) {
            return false;
        }
        if (this.resp_detail_type == null) {
            if (other.resp_detail_type != null) {
                return false;
            }
        } else if (!this.resp_detail_type.equals(other.resp_detail_type)) {
            return false;
        }
        if (this.secret_key == null) {
            if (other.secret_key != null) {
                return false;
            }
        } else if (!this.secret_key.equals(other.secret_key)) {
            return false;
        }
        if (this.state == null) {
            if (other.state != null) {
                return false;
            }
        } else if (!this.state.equals(other.state)) {
            return false;
        }
        if (this.token_id == null) {
            if (other.token_id != null) {
                return false;
            }
        } else if (!this.token_id.equals(other.token_id)) {
            return false;
        }
        if (this.user_agent_cust == null) {
            if (other.user_agent_cust != null) {
                return false;
            }
        } else if (!this.user_agent_cust.equals(other.user_agent_cust)) {
            return false;
        }
        return true;
    }

}
