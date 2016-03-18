package com.ucredit.hermes.model.tongdun;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.ucredit.hermes.model.pengyuan.BaseModel;

/**
 * 设备指纹信息，若用户传入resp_detail_type字段包含device，则返回结果包含该信息，否则无
 *
 * @author zhouwuyuan
 */
@Entity
@Table(name = "tongdun_device_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DeviceInfo extends BaseModel<Integer> {
    /**
     *
     */
    private static final long serialVersionUID = -5859367539116647570L;
    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 浏览器
     */
    @Column(length = 32)
    private String browser;
    /**
     * 浏览器版本
     */
    private String browserVersion;
    /**
     * 设备精准ID
     */
    private String deviceId;
    /**
     * 设备类型
     */
    private String deviceType;
    /**
     * 浏览器是否启用Cookie，true表示开启
     */
    private boolean enabledCk;
    /**
     * 浏览器是否加载Flash
     */
    private boolean enabledFl;
    /**
     * 浏览器是否启用Javascript
     */
    private boolean enabledJs;
    /**
     * 局域网IP
     */
    private String ethIp;
    /**
     * flash请求到达的时间
     */
    private Date flashTimestamp;
    /**
     * 字体md5
     */
    private String fontId;
    /**
     * profile请求到达时间
     */
    private Date gmtCreate;
    /**
     * 侦测脚本中Image是否加载
     */
    private boolean imageLoaded;
    /**
     * 系统语言等信息
     */
    private String langRes;
    /**
     * 操作系统
     */
    private String os;
    /**
     * 信息采集的方式：1代表JS请求 2代表图片请求
     */
    private int profileType;
    /**
     * 代理ip
     */
    private String proxyIp;
    /**
     * 会话请求来源URL
     */
    private String refer;
    /**
     * 屏幕分辨率
     */
    private String screenRes;
    /**
     * 设备智能ID
     */
    private String smartId;
    /**
     * 是否成功，成功则为true
     */
    private boolean success;
    /**
     * 用户所在时区
     */
    private int timeZone;
    /**
     * 时间戳
     */
    private long timestamp;
    /**
     * 标识符
     */
    private String tokenId;
    /**
     * 客户环境真实IP
     */
    private String trueIp;
    /**
     * 浏览器UserAgent信息
     */
    private String userAgent;
    /**
     * 真实IP的地理位置信息
     */
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "geoip_info_id")
    private GeoipInfo geoip_info;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getBrowser() {
        return this.browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserVersion() {
        return this.browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isEnabledCk() {
        return this.enabledCk;
    }

    public void setEnabledCk(boolean enabledCk) {
        this.enabledCk = enabledCk;
    }

    public boolean isEnabledFl() {
        return this.enabledFl;
    }

    public void setEnabledFl(boolean enabledFl) {
        this.enabledFl = enabledFl;
    }

    public boolean isEnabledJs() {
        return this.enabledJs;
    }

    public void setEnabledJs(boolean enabledJs) {
        this.enabledJs = enabledJs;
    }

    public String getEthIp() {
        return this.ethIp;
    }

    public void setEthIp(String ethIp) {
        this.ethIp = ethIp;
    }

    public Date getFlashTimestamp() {
        return this.flashTimestamp;
    }

    public void setFlashTimestamp(Date flashTimestamp) {
        this.flashTimestamp = flashTimestamp;
    }

    public String getFontId() {
        return this.fontId;
    }

    public void setFontId(String fontId) {
        this.fontId = fontId;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public boolean isImageLoaded() {
        return this.imageLoaded;
    }

    public void setImageLoaded(boolean imageLoaded) {
        this.imageLoaded = imageLoaded;
    }

    public String getLangRes() {
        return this.langRes;
    }

    public void setLangRes(String langRes) {
        this.langRes = langRes;
    }

    public String getOs() {
        return this.os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getProxyIp() {
        return this.proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public String getRefer() {
        return this.refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getScreenRes() {
        return this.screenRes;
    }

    public void setScreenRes(String screenRes) {
        this.screenRes = screenRes;
    }

    public String getSmartId() {
        return this.smartId;
    }

    public void setSmartId(String smartId) {
        this.smartId = smartId;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(int timeZone) {
        this.timeZone = timeZone;
    }

    public int getProfileType() {
        return this.profileType;
    }

    public void setProfileType(int profileType) {
        this.profileType = profileType;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTokenId() {
        return this.tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTrueIp() {
        return this.trueIp;
    }

    public void setTrueIp(String trueIp) {
        this.trueIp = trueIp;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public GeoipInfo getGeoip_info() {
        return this.geoip_info;
    }

    public void setGeoip_info(GeoipInfo geoip_info) {
        this.geoip_info = geoip_info;
    }

}
