package com.ucredit.hermes.service.tongdun;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.common.RESTTemplate;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.juxinli.SaveEntityDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.enums.TongdunType;
import com.ucredit.hermes.model.tongdun.Condition;
import com.ucredit.hermes.model.tongdun.FraudApiResponse;
import com.ucredit.hermes.model.tongdun.HitRules;
import com.ucredit.hermes.model.tongdun.HitRulesBack;
import com.ucredit.hermes.model.tongdun.PolicySet;
import com.ucredit.hermes.model.tongdun.TongdunFraudRecord;
import com.ucredit.hermes.utils.TongDunUtils;

@Service
@Transactional(rollbackFor = ServiceException.class)
public class TongDunService {
    private static Logger logger = LoggerFactory
            .getLogger(TongDunService.class);
    @Autowired
    private Variables variables;
    @Autowired
    private RESTTemplate restTemplate;
    @Autowired
    private SaveEntityDAO saveEntityDAO;

    /*
     * @Autowired
     * private FraudApiResponseDAO fraudApiResponseDAO;
     */

    /**
     * 风险决策服务
     *
     * @param condition
     *        参数
     * @return
     */
    public Map<String, Object> riskDecisionService(Condition condition) {
        Map<String, Object> result = new HashMap<>();
        /*
         * 每次查询发起新查询
         * TongdunFraudRecord tf = this.fraudApiResponseDAO
         * .searchFraudApiResponse(condition);
         * if (tf != null) {
         * Date activeDate = DateUtils.addDays(tf.getBackTime(), 30);
         * //判断是否已过30天有效期
         * if (activeDate.after(new Date())) {//未过去返回已存在的结果
         * result.put(HermesConsts.MSG, tf.getFraudApiResponse());
         * result.put(HermesConsts.MSG, tf);
         * return result;
         * } else {//已过期将原来的查询结果设为无效
         * tf.setEnabled(false);
         * }
         * }
         */
        String partnerCode = this.variables.getTongdunPartnerCode();
        String secretKey = "";
        if ("android".equals(condition.getDevice_type())) {
            secretKey = this.variables.getTongdunAndroidSecretKey();
        } else if ("ios".equals(condition.getDevice_type())) {
            secretKey = this.variables.getTongdunIosSecretKey();
        } else {
            secretKey = this.variables.getTongdunSecretKey();
        }
        String url = this.variables.getTongdunUrl();
        long timestamp = System.currentTimeMillis();
        String tokenId = "";

        TongdunFraudRecord tongdunFraudRecord = new TongdunFraudRecord();
        tongdunFraudRecord.setCondition(condition);
        this.saveEntityDAO.addEntity(tongdunFraudRecord);
        try {//根据要求获取token
            tokenId = TongDunUtils.generateEncodedToken(partnerCode, "appName",
                secretKey, timestamp);
        } catch (NoSuchAlgorithmException e) {//获取token失败
            tongdunFraudRecord.setResultType(ResultType.FAILURE);
            tongdunFraudRecord.setErrorCode(AsyncCode.FAILURE_TOKEN_ERROR);
            tongdunFraudRecord.setErrorMessage("获取tokenId失败");
            tongdunFraudRecord.setResultType(ResultType.FAILURE);
            tongdunFraudRecord.setEnabled(false);
            TongDunService.logger.debug("获取tokenId失败" + e.getMessage());
            e.printStackTrace();
            result.put(HermesConsts.MSG, "获取tokenId失败");
            return result;
        }
        //设置查询条件condition
        condition.setPartner_code(partnerCode);
        condition.setSecret_key(secretKey);
        condition.setToken_id(tokenId);
        String params = "";
        try {//condition转换成发起查询的参数
            params = TongDunUtils.convertToParams(condition);
        } catch (IllegalArgumentException | IllegalAccessException e) {//转换失败
            tongdunFraudRecord.setResultType(ResultType.FAILURE);
            tongdunFraudRecord.setErrorCode(AsyncCode.RESPONSE_NOT_MATHCING);
            tongdunFraudRecord.setErrorMessage("参数转换失败" + e.getMessage());
            tongdunFraudRecord.setResultType(ResultType.FAILURE);
            tongdunFraudRecord.setEnabled(true);
            TongDunService.logger.debug("参数转换失败" + e.getMessage());
            e.printStackTrace();
            result.put(HermesConsts.MSG, "参数转换失败");
            return result;
        }
        FraudApiResponse fraudApiResponse = null;
        tongdunFraudRecord.setQueryTime(new Date());
        try {//获取返回结果
            fraudApiResponse = this.restTemplate.getEntity(url + "?" + params,
                FraudApiResponse.class);
            tongdunFraudRecord.setBackTime(new Date());
            fraudApiResponse.setBackTime(new Date());
        } catch (Exception e) {//获取返回结果失败
            tongdunFraudRecord.setResultType(ResultType.FAILURE);
            tongdunFraudRecord
            .setErrorCode(AsyncCode.FAILURE_CONNECTION_REFUSED);
            tongdunFraudRecord.setErrorMessage("无法连接到第三方" + e.getMessage());
            tongdunFraudRecord.setResultType(ResultType.FAILURE);
            tongdunFraudRecord.setEnabled(false);
            e.printStackTrace();
            TongDunService.logger.debug("同盾获取数据失败" + e.getMessage());
            e.printStackTrace();
            result.put(HermesConsts.MSG, "同盾获取数据失败");
            return result;
        }
        if (fraudApiResponse != null) {//解析返回的数据
            boolean ifSuccess = fraudApiResponse.isSuccess();//判断查询是否成功
            if (ifSuccess) {//查询成功的解析
                tongdunFraudRecord.setResultType(ResultType.SUCCESS);
                tongdunFraudRecord.setFraudApiResponse(fraudApiResponse);//设置外键关联
                fraudApiResponse//设置查询时间
                .setQueryTime(tongdunFraudRecord.getQueryTime());
                List<HitRulesBack> hitRulesb = fraudApiResponse.getHit_rules();
                List<HitRules> hitRules = new ArrayList<>();
                this.hitRulesConvert(hitRulesb, hitRules);//将返回的属性转成存储的属性
                fraudApiResponse.setHit_rules_convert(hitRules);
                List<PolicySet> policySet = fraudApiResponse.getPolicy_set();
                for (PolicySet ps : policySet) {
                    List<HitRulesBack> pshitRulesb = ps.getHit_rules();
                    List<HitRules> pshitRules = new ArrayList<>();
                    this.hitRulesConvert(pshitRulesb, pshitRules);
                    ps.setHit_rules_convert(pshitRules);
                }
                this.saveEntityDAO.addEntity(fraudApiResponse);//保存查询结果
            } else {//返回的查询结果失败
                tongdunFraudRecord.setResultType(ResultType.FAILURE);
                tongdunFraudRecord.setErrorCode(AsyncCode.FAILURE_3RD_ERROR);
                tongdunFraudRecord
                    .setErrorMessage("第三方返回success为：false。reason_code:"
                        + fraudApiResponse.getReason_code());
            }
        }
        result.put(HermesConsts.MSG, tongdunFraudRecord);
        return result;
    }

    /**
     * HitRulesBack到hitRules的转换
     *
     * @param hitRulesb
     *        返回的hitRuleBack
     * @param hitRules
     *        存储的hitRules
     */
    public void hitRulesConvert(List<HitRulesBack> hitRulesb,
            List<HitRules> hitRules) {
        if (hitRulesb != null) {
            for (HitRulesBack hrb : hitRulesb) {
                HitRules hr = new HitRules();
                hr.setBack_id(hrb.getId());
                hr.setDecision(hrb.getDecision());
                hr.setName(hrb.getName());
                hr.setScore(hrb.getScore());
                hr.setUuid(hrb.getUuid());
                if (hrb.getName().contains("身份证")) {
                    hr.setType(TongdunType.IDENTITYNO);
                } else if (hrb.getName().contains("手机号")) {
                    hr.setType(TongdunType.MOBILE);
                } else if (hrb.getName().contains("联系人一")) {
                    hr.setType(TongdunType.CONTACT1_MBILE);
                } else if (hrb.getName().contains("联系人二")) {
                    hr.setType(TongdunType.CONTACT2_MBILE);
                } else if (hrb.getName().contains("联系人三")) {
                    hr.setType(TongdunType.CONTACT3_MBILE);
                } else if (hrb.getName().contains("联系人四")) {
                    hr.setType(TongdunType.CONTACT3_MBILE);
                } else {
                    hr.setType(TongdunType.OTHER);
                }
                hitRules.add(hr);
            }
        } else {
            hitRules = null;
        }
    }

}
