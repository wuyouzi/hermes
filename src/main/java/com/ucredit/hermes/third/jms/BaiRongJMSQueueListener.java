package com.ucredit.hermes.third.jms;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bfd.facade.Biz_industry;
import com.bfd.facade.Educationallevel;
import com.bfd.facade.Marriage;
import com.bfd.facade.MerchantBean;
import com.bfd.facade.MerchantServer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.BaiRongAuthenticationDAO;
import com.ucredit.hermes.dao.BaiRongConsumptionDAO;
import com.ucredit.hermes.dao.BaiRongDataDAO;
import com.ucredit.hermes.dao.BaiRongLocationDAO;
import com.ucredit.hermes.dao.BaiRongMediaDAO;
import com.ucredit.hermes.dao.BaiRongParamsDAO;
import com.ucredit.hermes.dao.BaiRongStabilityDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.BaiRongConsumptionTypes;
import com.ucredit.hermes.enums.BaiRongLocationTypes;
import com.ucredit.hermes.enums.BaiRongResultCode;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.BaiRongAuthentication;
import com.ucredit.hermes.model.BaiRongConsumption;
import com.ucredit.hermes.model.BaiRongData;
import com.ucredit.hermes.model.BaiRongLocation;
import com.ucredit.hermes.model.BaiRongMedia;
import com.ucredit.hermes.model.BaiRongParams;
import com.ucredit.hermes.model.BaiRongStability;
import com.ucredit.hermes.utils.BaiRongCategorys;
import com.ucredit.hermes.utils.JmsUtils;

/**
 * @author Administrator
 */
@Component("baiRongJMSQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class BaiRongJMSQueueListener implements MessageListener {
    private static final Logger log = LoggerFactory
            .getLogger(BaiRongJMSQueueListener.class);
    @Autowired
    private Variables variables;

    @Autowired
    private BaiRongParamsDAO baiRongParamsDAO;

    @Autowired
    private BaiRongStabilityDAO baiRongStabilityDAO;

    @Autowired
    private BaiRongMediaDAO baiRongMediaDAO;

    @Autowired
    private BaiRongConsumptionDAO baiRongConsumptionDAO;

    @Autowired
    private BaiRongAuthenticationDAO baiRongAuthenticationDAO;

    @Autowired
    private BaiRongDataDAO baiRongDataDAO;
    @Autowired
    private InternalSystemMessageProvider provider;

    @Autowired
    private Destination hermesPublishQueue;

    @Autowired
    private BaiRongLocationDAO baiRongLocationDAO;
    private String token;
    private boolean isSearchIng;
    private String searchString;

    public Map<String, String> getToken() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        if (this.isSearchIng) {
            Thread.sleep(2000);
            if (!this.isSearchIng) {
                return objectMapper.readValue(this.searchString, Map.class);
            }
        }

        MerchantServer ms = new MerchantServer();
        long nowTimes = System.currentTimeMillis();
        String username = this.variables.getBaiRongUserName();
        String password = this.variables.getBaiRongPassword();
        // String username = "renrendai";
        // String password = "renrendai";
        this.isSearchIng = true;
        String jsonResult = ms.login(username, password);
        this.searchString = jsonResult;
        this.isSearchIng = false;
        long durTime = System.currentTimeMillis() - nowTimes;
        BaiRongJMSQueueListener.log.debug(String.format("获取token,耗时：%d毫秒",
            durTime));

        Map<String, String> result = objectMapper.readValue(jsonResult,
            Map.class);

        return result;
    }

    public String getToken(BaiRongParams dbbaiRongParams) {
        Map<String, String> tokenMap = new HashMap<>();
        try {
            tokenMap = this.getToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String code = tokenMap.get("code");
        if ("00".equals(code)) {
            this.token = tokenMap.get("tokenid");
        } else {
            dbbaiRongParams = (BaiRongParams) this.baiRongParamsDAO.getById(
                dbbaiRongParams.getDbId(), BaiRongParams.class);
            switch (code) {
                case "100001":
                    dbbaiRongParams.setResultType(ResultType.FAILURE);
                    dbbaiRongParams
                    .setResultCode(BaiRongResultCode.BaiRong_100001);
                    dbbaiRongParams
                    .setResultCodeDesc(BaiRongResultCode.BaiRong_100001
                        .getString());
                    break;
                case "100005":
                    dbbaiRongParams.setResultType(ResultType.FAILURE);
                    dbbaiRongParams
                    .setResultCode(BaiRongResultCode.BaiRong_100005);
                    dbbaiRongParams
                    .setResultCodeDesc(BaiRongResultCode.BaiRong_100005
                        .getString());
                    break;
                case "100010":
                    dbbaiRongParams.setResultType(ResultType.FAILURE);
                    dbbaiRongParams
                    .setResultCode(BaiRongResultCode.BaiRong_1000010);
                    dbbaiRongParams
                    .setResultCodeDesc(BaiRongResultCode.BaiRong_1000010
                        .getString());
                    break;
                default:
                    dbbaiRongParams.setResultType(ResultType.FAILURE);
                    dbbaiRongParams.setResultCodeDesc(String.format(
                        "百融返回错误编码：%s", code));
                    break;
            }
            this.token = null;
            this.baiRongParamsDAO.updateEntity(dbbaiRongParams);

        }
        return this.token;
    }

//    public void doSearch(BaiRongParams dbbaiRongParams) throws Exception {
//
//        String tokenid = this.token;
//        if (tokenid == null) {
//            tokenid = this.getToken(dbbaiRongParams);
//            if (tokenid == null) {
//                return;
//            }
//        }
//        dbbaiRongParams = (BaiRongParams) this.baiRongParamsDAO.getById(
//            dbbaiRongParams.getDbId(), BaiRongParams.class);
//
//        String result = this.getResultFromBaiRong(dbbaiRongParams, tokenid);
//        if (result != null) {
//            JSONObject baiRongSearchResult = JSONObject.fromObject(result);
//            String code = baiRongSearchResult.getString("code");
//            Date queryTime = new Date();
//            dbbaiRongParams.setQueryTime(queryTime);
//            if (!"00".equals(code)) {
//                dbbaiRongParams.setResultType(ResultType.FAILURE);
//                BaiRongResultCode[] enums = BaiRongResultCode.values();
//                BaiRongResultCode baiRongResultCode = null;
//                for (BaiRongResultCode item : enums) {
//                    if (item.name().equals("BaiRong_" + code)) {
//                        baiRongResultCode = item;
//                        break;
//                    }
//                }
//                if (baiRongResultCode != null) {
//                    // "Tokenid过期" 则重置token
//                    if (baiRongResultCode == BaiRongResultCode.BaiRong_100007) {
//                        tokenid = this.getToken(dbbaiRongParams);
//                        if (tokenid == null) {
//                            return;
//                        } else {
//                            this.doSearch(dbbaiRongParams);
//                        }
//                    } else {
//                        dbbaiRongParams.setResultCode(baiRongResultCode);
//                        dbbaiRongParams.setResultCodeDesc(baiRongResultCode
//                            .getString());
//                        if (dbbaiRongParams.getResultCode() == BaiRongResultCode.BaiRong_100002) {
//                            // 没有匹配结果，也表示成功
//                            dbbaiRongParams.setResultType(ResultType.SUCCESS);
//                        }
//                        // 继续下面的修改数据操作
//                    }
//
//                } else {
//                    dbbaiRongParams.setResultCodeDesc(String.format(
//                        "百融错误，返回错误码为%s", code));
//                }
//                this.baiRongParamsDAO.updateEntity(dbbaiRongParams);
//
//            } else {
//                // 开始解析入库操作
//                this.doParseBaiRongResult(dbbaiRongParams, baiRongSearchResult);
//            }
//
//        } else {
//            // 百融方面表示他们不会返回null，但是实际中会发生请求没有成功的情况，所以如果为null，就失败，稍后重新查询
//            dbbaiRongParams.setResultType(ResultType.FAILURE);
//            dbbaiRongParams.setResultCode(BaiRongResultCode.YouXin_1000001);
//            dbbaiRongParams.setResultCodeDesc("发送查询请求失败，请稍后重新查询！");
//            this.baiRongParamsDAO.updateEntity(dbbaiRongParams);
//        }
//    }

    /**
     * @param dbbaiRongParams
     * @param systemId
     * @author ray
     * @return
     * @throws InterruptedException
     */
    public BaiRongParams doSearchWithReturn(BaiRongParams dbbaiRongParams,
            String systemId) throws InterruptedException {

        String tokenid = this.token;
        if (tokenid == null) {
            tokenid = this.getToken(dbbaiRongParams);
            if (tokenid == null) {
                return null;
            }
        }
        dbbaiRongParams = (BaiRongParams) this.baiRongParamsDAO.getById(
            dbbaiRongParams.getDbId(), BaiRongParams.class);

        String result = "";
        try {
            BaiRongJMSQueueListener.log.info("bairong_listener发起查询");
            result = this.getResultFromBaiRong(dbbaiRongParams, tokenid);
            BaiRongJMSQueueListener.log
                .info("bairong_listener发起查询结果：" + result);
        } catch (Exception e) {
            dbbaiRongParams.setResultType(ResultType.FAILURE);
            dbbaiRongParams.setErrorMsg(AsyncCode.FAILURE_CONNECTION_REFUSED
                + "_" + e.getMessage());
            dbbaiRongParams.setResultCode(BaiRongResultCode.YouXin_1000001);
            this.baiRongParamsDAO.updateEntity(dbbaiRongParams);
            e.printStackTrace();
        }
        if (result != null) {
            JSONObject baiRongSearchResult = JSONObject.fromObject(result);
            String code = baiRongSearchResult.getString("code");
            Date queryTime = new Date();
            dbbaiRongParams.setQueryTime(queryTime);
            if (!"00".equals(code)) {
                dbbaiRongParams.setResultType(ResultType.FAILURE);
                BaiRongResultCode[] enums = BaiRongResultCode.values();
                BaiRongResultCode baiRongResultCode = null;
                for (BaiRongResultCode item : enums) {
                    if (item.name().equals("BaiRong_" + code)) {
                        baiRongResultCode = item;
                        break;
                    }
                }
                if (baiRongResultCode != null) {
                    // "Tokenid过期" 则重置token
                    if (baiRongResultCode == BaiRongResultCode.BaiRong_100007) {
                        tokenid = this.getToken(dbbaiRongParams);
                        if (tokenid == null) {
                            Thread.sleep(2000);
                            this.sendMessage(dbbaiRongParams, systemId,
                                dbbaiRongParams.getKeyid(),
                                AsyncCode.FAILURE_EMPTYDATA);
                            return null;
                        } else {
                            this.doSearchWithReturn(dbbaiRongParams, systemId);
                        }
                    } else {
                        dbbaiRongParams.setResultCode(baiRongResultCode);
                        dbbaiRongParams.setResultCodeDesc(baiRongResultCode
                            .getString());
                        if (dbbaiRongParams.getResultCode() == BaiRongResultCode.BaiRong_100002) {
                            // 没有匹配结果
                            dbbaiRongParams.setResultType(ResultType.FAILURE);
                            Thread.sleep(2000);
                            this.sendMessage(dbbaiRongParams, systemId,
                                dbbaiRongParams.getKeyid(),
                                AsyncCode.FAILURE_EMPTYDATA);
                        } else {
                            Thread.sleep(2000);
                            this.sendMessage(dbbaiRongParams, systemId,
                                dbbaiRongParams.getKeyid(),
                                AsyncCode.FAILURE_EMPTYDATA);
                        }
                        // 继续下面的修改数据操作
                    }

                } else {
                    dbbaiRongParams.setResultCodeDesc(String.format(
                        "百融错误，返回错误码为%s", code));
                    Thread.sleep(2000);
                    this.sendMessage(dbbaiRongParams, systemId,
                        dbbaiRongParams.getKeyid(), AsyncCode.FAILURE_DIRTYDATA);
                }
                this.baiRongParamsDAO.updateEntity(dbbaiRongParams);

            } else {
                // 开始解析入库操作
                // ray added
                // add return dbbaiRongParams from parse function to return to
                // this function
                dbbaiRongParams = this.doParseBaiRongResult(dbbaiRongParams,
                    baiRongSearchResult);
                BaiRongJMSQueueListener.log.info("begin百融返回success：" + systemId
                    + dbbaiRongParams.getDbId());
                Thread.sleep(2000);
                this.sendMessage(dbbaiRongParams, systemId,
                    dbbaiRongParams.getKeyid(), AsyncCode.SUCCESS);
                BaiRongJMSQueueListener.log.info("end百融返回success：" + systemId
                    + dbbaiRongParams.getDbId());
            }

        } else {
            // 百融方面表示他们不会返回null，但是实际中会发生请求没有成功的情况，所以如果为null，就失败，稍后重新查询
            dbbaiRongParams.setResultType(ResultType.FAILURE);
            dbbaiRongParams.setResultCode(BaiRongResultCode.YouXin_1000001);
            dbbaiRongParams.setResultCodeDesc("发送查询请求失败，请稍后重新查询！");
            this.baiRongParamsDAO.updateEntity(dbbaiRongParams);
            Thread.sleep(2000);
            this.sendMessage(dbbaiRongParams, systemId,
                dbbaiRongParams.getKeyid(), AsyncCode.FAILURE_3RD_ERROR);
        }
        return dbbaiRongParams;
    }

    /**
     * NOTE modify void to @return BaiRongParams
     *
     * @param dbbaiRongParams
     * @param baiRongSearchResult
     * @author update by ray
     */
    private BaiRongParams doParseBaiRongResult(BaiRongParams dbbaiRongParams,
            JSONObject baiRongSearchResult) {

        /**
         * 解析流水号
         */
        String swift_number = baiRongSearchResult.getString("swift_number");

        dbbaiRongParams.setSwift_number(swift_number);
        /**
         * 身份信息核查
         */
        JSONObject authentication = baiRongSearchResult
                .getJSONObject("Authentication");
        if (!authentication.isEmpty()) {
            BaiRongAuthentication baiRongAuthentication = new BaiRongAuthentication();
            String id = null;
            String cell = null;
            String mail = null;
            String name = null;
            String tel_biz = null;
            String tel_home = null;
            String key_relation = null;
            if (authentication.containsKey("id")) {
                id = authentication.getString("id");
            }
            if (authentication.containsKey("cell")) {
                cell = authentication.getString("cell");
            }
            if (authentication.containsKey("mail")) {
                mail = authentication.getString("mail");
            }
            if (authentication.containsKey("key_relation")) {
                key_relation = authentication.getString("key_relation");
            }
            if (authentication.containsKey("name")) {
                name = authentication.getString("name");
            }
            if (authentication.containsKey("tel_biz")) {
                tel_biz = authentication.getString("tel_biz");
            }
            if (authentication.containsKey("tel_home")) {
                tel_home = authentication.getString("tel_home");
            }
            baiRongAuthentication.setBaiRongParamsId(dbbaiRongParams.getDbId());
            baiRongAuthentication.setCell(cell);
            baiRongAuthentication.setId(id);
            baiRongAuthentication.setKey_relation(key_relation);
            baiRongAuthentication.setMail(mail);
            baiRongAuthentication.setName(name);
            baiRongAuthentication.setTel_biz(tel_biz);
            baiRongAuthentication.setTel_home(tel_home);
            this.baiRongAuthenticationDAO.addEntity(baiRongAuthentication);
        }
        // 位置信息核查
        JSONObject location = baiRongSearchResult.getJSONObject("Location");
        if (!location.isNullObject() && !location.isEmpty()) {
            JSONObject home_addr = location.getJSONObject("home_addr");
            JSONObject biz_addr = location.getJSONObject("biz_addr");
            JSONObject per_addr = location.getJSONObject("per_addr");
            JSONObject apply_addr = location.getJSONObject("apply_addr");
            JSONObject oth_addr = location.getJSONObject("oth_addr");

            if (!home_addr.isNullObject() && !home_addr.isEmpty()) {
                this.doLocationParse(home_addr, BaiRongLocationTypes.HOME_ADDR,
                    dbbaiRongParams);
            }
            if (!biz_addr.isNullObject() && !biz_addr.isEmpty()) {
                this.doLocationParse(biz_addr, BaiRongLocationTypes.BIZ_ADDR,
                    dbbaiRongParams);
            }
            if (!per_addr.isNullObject() && !per_addr.isEmpty()) {
                this.doLocationParse(per_addr, BaiRongLocationTypes.PER_ADDR,
                    dbbaiRongParams);
            }
            if (!apply_addr.isNullObject() && !apply_addr.isEmpty()) {
                this.doLocationParse(apply_addr,
                    BaiRongLocationTypes.APPLY_ADDR, dbbaiRongParams);
            }
            if (!oth_addr.isNullObject() && !oth_addr.isEmpty()) {
                this.doLocationParse(oth_addr, BaiRongLocationTypes.OTHER_ADDR,
                    dbbaiRongParams);
            }
        }
        // 稳定性
        JSONObject stability = baiRongSearchResult.getJSONObject("Stability");
        if (!stability.isNullObject() && !stability.isEmpty()) {
            JSONObject id = stability.getJSONObject("id");
            String id_number = null;
            if (!id.isNullObject() && !id.isEmpty()) {
                id_number = id.getString("number");
            }

            JSONObject cell = stability.getJSONObject("cell");
            String cell_number = null;
            String cell_firstTime = null;
            if (!cell.isNullObject() && !cell.isEmpty()) {
                cell_number = cell.getString("number");
                cell_firstTime = cell.getString("firsttime");
            }

            JSONObject mail = stability.getJSONObject("mail");
            String mail_number = null;
            if (!mail.isNullObject() && !mail.isEmpty()) {
                mail_number = mail.getString("number");
            }

            JSONObject name = stability.getJSONObject("name");
            String name_number = null;
            if (!name.isNullObject() && !name.isEmpty()) {
                name_number = name.getString("number");
            }

            JSONObject tel = stability.getJSONObject("tel");
            String tel_number = null;
            if (!tel.isNullObject() && !tel.isEmpty()) {
                tel_number = tel.getString("number");
            }

            JSONObject addr = stability.getJSONObject("addr");
            String addr_number = null;
            if (!addr.isNullObject() && !addr.isEmpty()) {
                addr_number = addr.getString("number");
            }

            BaiRongStability baiRongStability = new BaiRongStability();
            baiRongStability.setAddr_number(addr_number);
            baiRongStability.setBaiRongParamsId(dbbaiRongParams.getDbId());
            baiRongStability.setCell_firstTime(cell_firstTime);
            baiRongStability.setCell_number(cell_number);
            baiRongStability.setId_number(id_number);
            baiRongStability.setMail_number(mail_number);
            baiRongStability.setName_number(name_number);
            baiRongStability.setTel_number(tel_number);
            this.baiRongStabilityDAO.addEntity(baiRongStability);
        }
        // 商品消费评估
        JSONObject consumption = baiRongSearchResult
                .getJSONObject("Consumption");
        if (!consumption.isNullObject() && !consumption.isEmpty()) {
            this.doConsumption(consumption, "month3", dbbaiRongParams);
            this.doConsumption(consumption, "month6", dbbaiRongParams);
            this.doConsumption(consumption, "month12", dbbaiRongParams);
            this.doConsumption(consumption, "level", dbbaiRongParams);
        }
        // 阅读兴趣评估
        JSONObject media = baiRongSearchResult.getJSONObject("Media");
        if (!media.isEmpty()) {
            this.doMedia(media, "month3", dbbaiRongParams);
            this.doMedia(media, "month6", dbbaiRongParams);
            this.doMedia(media, "month12", dbbaiRongParams);
        }

        dbbaiRongParams.setResultCode(BaiRongResultCode.BaiRong_00);
        dbbaiRongParams.setResultCodeDesc(BaiRongResultCode.BaiRong_00
            .getString());
        dbbaiRongParams.setResultType(ResultType.SUCCESS);
        this.baiRongParamsDAO.updateEntity(dbbaiRongParams);
        return dbbaiRongParams;
    }

    private void doMedia(JSONObject media, String type,
            BaiRongParams dbbaiRongParams) {
        JSONObject mediaForMonth = media.getJSONObject(type);
        Map<String, String> baiRongCategorys = BaiRongCategorys
                .getBaiRongCategorys();
        if (!mediaForMonth.isEmpty()) {
            Iterator<String> it = mediaForMonth.keys();
            while (it.hasNext()) {
                String category = it.next();
                JSONObject categoryJSON = mediaForMonth.getJSONObject(category);
                String visitdays = categoryJSON.getString("visitdays");

                BaiRongMedia baiRongMedia = new BaiRongMedia();
                String newCat = baiRongCategorys.get(category);
                baiRongMedia.setCategory(newCat == null ? category : newCat);
                baiRongMedia.setBaiRongParamsId(dbbaiRongParams.getDbId());
                if ("month3".equals(type)) {
                    baiRongMedia.setType(BaiRongConsumptionTypes.THREE_MONTH);
                } else if ("month6".equals(type)) {
                    baiRongMedia.setType(BaiRongConsumptionTypes.SIX_MONTH);
                } else if ("month12".equals(type)) {
                    baiRongMedia.setType(BaiRongConsumptionTypes.TWELVE_MONTH);
                }
                baiRongMedia.setVisitdays(visitdays);
                this.baiRongMediaDAO.addEntity(baiRongMedia);

            }

        }
    }

    /**
     * @param consumptionObj
     * @param type
     */
    private void doConsumption(JSONObject consumptionObj, String type,
            BaiRongParams dbbaiRongParams) {
        Map<String, String> baiRongCategorys = BaiRongCategorys
                .getBaiRongCategorys();
        if ("month3".equals(type) || "month6".equals(type)
                || "month12".equals(type)) {
            JSONObject consumptionForMonth = consumptionObj.getJSONObject(type);
            if (!consumptionForMonth.isNullObject()
                    && !consumptionForMonth.isEmpty()) {
                Iterator<String> it = consumptionForMonth.keys();

                while (it.hasNext()) {
                    String category = it.next();
                    JSONObject consumption12MonthObj = consumptionForMonth
                            .getJSONObject(category);
                    String visits = null;
                    String number = null;
                    String pay = null;
                    String maxpay = null;
                    if (consumption12MonthObj.containsKey("visits")) {
                        visits = consumption12MonthObj.getString("visits");
                    }
                    if (consumption12MonthObj.containsKey("number")) {
                        number = consumption12MonthObj.getString("number");
                    }
                    if (consumption12MonthObj.containsKey("pay")) {
                        pay = consumption12MonthObj.getString("pay");
                    }
                    if (consumption12MonthObj.containsKey("maxpay")) {
                        maxpay = consumption12MonthObj.getString("maxpay");
                    }

                    BaiRongConsumption baiRongConsumption = new BaiRongConsumption();
                    baiRongConsumption.setBaiRongParamsId(dbbaiRongParams
                        .getDbId());
                    String newCat = baiRongCategorys.get(category);
                    baiRongConsumption.setCategory(newCat == null ? category
                        : newCat);
                    baiRongConsumption.setNumber(number);
                    baiRongConsumption.setPay(pay);
                    baiRongConsumption.setVisits(visits);

                    baiRongConsumption.setMaxpay(maxpay);
                    if ("month3".equals(type)) {
                        baiRongConsumption
                        .setType(BaiRongConsumptionTypes.THREE_MONTH);
                    } else if ("month6".equals(type)) {
                        baiRongConsumption
                        .setType(BaiRongConsumptionTypes.SIX_MONTH);
                    } else if ("month12".equals(type)) {
                        baiRongConsumption
                        .setType(BaiRongConsumptionTypes.TWELVE_MONTH);
                    }

                    this.baiRongConsumptionDAO.addEntity(baiRongConsumption);
                }
            }
        } else if ("level".equals(type)) {
            JSONObject levelMonth = consumptionObj.getJSONObject("level");
            if (!levelMonth.isEmpty()) {
                Iterator<String> it = levelMonth.keys();
                while (it.hasNext()) {
                    String category = it.next();
                    String level_value = levelMonth.getString(category);

                    BaiRongConsumption baiRongConsumption = new BaiRongConsumption();
                    baiRongConsumption.setBaiRongParamsId(dbbaiRongParams
                        .getDbId());
                    String newCat = baiRongCategorys.get(category);
                    baiRongConsumption.setCategory(newCat == null ? category
                        : newCat);
                    baiRongConsumption.setLevel_value(level_value);
                    baiRongConsumption.setType(BaiRongConsumptionTypes.LEVEL);
                    this.baiRongConsumptionDAO.addEntity(baiRongConsumption);
                }
            }

        }

    }

    /**
     * 解析位置信息核查信息
     *
     * @param location
     * @param type
     * @param dbbaiRongParams
     */
    private void doLocationParse(JSONObject location,
            BaiRongLocationTypes type, BaiRongParams dbbaiRongParams) {
        Iterator<String> it = location.keys();
        while (it.hasNext()) {
            String key = it.next();
            BaiRongLocation entity = new BaiRongLocation();
            entity.setBaiRongParamsId(dbbaiRongParams.getDbId());
            entity.setType(type);
            entity.setKey(key);
            entity.setValue(location.getString(key));
            this.baiRongLocationDAO.addEntity(entity);
        }
    }

    private String getResultFromBaiRong(BaiRongParams dbbaiRongParams,
            String tokenid) throws Exception {
        MerchantServer ms = new MerchantServer();
        MerchantBean merchantBean = new MerchantBean();
        merchantBean.setTokenid(tokenid);

        merchantBean.setId(dbbaiRongParams.getId());
        merchantBean.setCell(dbbaiRongParams.getCell());
        merchantBean.setMail(dbbaiRongParams.getMail());
        merchantBean.setName(dbbaiRongParams.getName());

        merchantBean.setHome_addr(dbbaiRongParams.getHome_addr());
        merchantBean.setBiz_addr(dbbaiRongParams.getBiz_addr());
        merchantBean.setApply_addr(dbbaiRongParams.getApply_addr());

        merchantBean.setTel_biz(dbbaiRongParams.getTel_biz());
        merchantBean.setTel_home(dbbaiRongParams.getTel_home());
        merchantBean.setBiz_workfor(dbbaiRongParams.getBiz_workfor());

        merchantBean.setLinkman_name(dbbaiRongParams.getLinkman_name());
        merchantBean.setLinkman_cell(dbbaiRongParams.getLinkman_cell());
        merchantBean.setGid(dbbaiRongParams.getGid());
        if (StringUtils.isNotBlank(dbbaiRongParams.getBiz_industry())) {
            Biz_industry[] enums = Biz_industry.values();
            Biz_industry biz_industry = null;
            for (Biz_industry item : enums) {
                if (item.name().equals(dbbaiRongParams.getBiz_industry())) {
                    biz_industry = item;
                    break;
                }
            }
            merchantBean.setBiz_industry(biz_industry);

        }

        if (StringUtils.isNotBlank(dbbaiRongParams.getEducationallevel())) {
            Educationallevel[] enums = Educationallevel.values();
            Educationallevel educationallevel = null;
            for (Educationallevel item : enums) {
                if (item.name().equals(dbbaiRongParams.getEducationallevel())) {
                    educationallevel = item;
                    break;
                }
            }
            merchantBean.setEducationallevel(educationallevel);
        }
        if (StringUtils.isNotBlank(dbbaiRongParams.getMarriage())) {
            Marriage[] enums = Marriage.values();
            Marriage marriage = null;
            for (Marriage item : enums) {
                if (item.name().equals(dbbaiRongParams.getMarriage())) {
                    marriage = item;
                    break;
                }
            }
            merchantBean.setMarriage(marriage);
        }

        merchantBean.setBank_id(dbbaiRongParams.getBank_id());

        long nowTimes = System.currentTimeMillis();
        String result1 = ms.getUserPortrait(merchantBean);
        long durTime = System.currentTimeMillis() - nowTimes;
        BaiRongJMSQueueListener.log.debug(String.format("获取token,耗时：%d毫秒",
            durTime));

        // 保存返回结果
        BaiRongData baiRongData = new BaiRongData();
        baiRongData.setBaiRongParamsId(dbbaiRongParams.getDbId());
        baiRongData.setData(result1);
        baiRongData.setSearchString(JSONObject.fromObject(merchantBean)
            .toString());
        this.baiRongDataDAO.addEntity(baiRongData);

        return result1;
    }

    @Override
    public void onMessage(Message message) {
        try {
            String systemId = message
                    .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);
            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            BaiRongParams dbbaiRongParams = (BaiRongParams) vars[0];

            try {
                BaiRongParams resp = this.doSearchWithReturn(dbbaiRongParams,
                    systemId);
                // this.doSearch(dbbaiRongParams);
                if (resp == null) {
                    resp = dbbaiRongParams;
                    resp.setResultType(ResultType.FAILURE);
                    resp.setResultCode(BaiRongResultCode.YouXin_Hermes_ERROR);
                    resp.setResultCodeDesc("发送查询失败，发送至百荣的请求Token为空或百荣无响应。");

                    //TODO
                    this.sendMessage(resp, systemId, resp.getKeyid(),
                        AsyncCode.FAILURE_3RD_ERROR);
                }

            } catch (Exception e) {
                dbbaiRongParams = (BaiRongParams) this.baiRongParamsDAO
                        .getById(dbbaiRongParams.getDbId(), BaiRongParams.class);
                dbbaiRongParams.setResultCode(BaiRongResultCode.YouXin_ERROR);
                dbbaiRongParams
                .setResultCodeDesc(BaiRongResultCode.YouXin_ERROR
                    .getString());

                // ray - send exception message
                this.sendMessage(dbbaiRongParams, systemId,
                    dbbaiRongParams.getKeyid(), AsyncCode.FAILURE);
                // dbbaiRongParams.setResultType(ResultType.FAILURE);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                e.printStackTrace(new PrintStream(baos));
                String exception = baos.toString();
                dbbaiRongParams.setErrorMsg(exception);
                this.baiRongParamsDAO.updateEntity(dbbaiRongParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(BaiRongParams resp, String systemId,
            String requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.BAIRONG, requestId, asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
