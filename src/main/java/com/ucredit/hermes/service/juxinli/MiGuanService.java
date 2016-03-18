package com.ucredit.hermes.service.juxinli;

import java.security.Principal;
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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ucredit.hermes.common.RESTTemplate;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.juxinli.MiguanResponseDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.miguan.MiguanGridInfo;
import com.ucredit.hermes.model.miguan.MiguanIdCardAppliedInOrgs;
import com.ucredit.hermes.model.miguan.MiguanIdCardWithOtherNames;
import com.ucredit.hermes.model.miguan.MiguanIdCardWithOtherPhones;
import com.ucredit.hermes.model.miguan.MiguanPhoneAppliedInOrgs;
import com.ucredit.hermes.model.miguan.MiguanPhoneWithOtherIdcards;
import com.ucredit.hermes.model.miguan.MiguanPhoneWithOtherNames;
import com.ucredit.hermes.model.miguan.MiguanResponse;
import com.ucredit.hermes.model.miguan.MiguanResult;
import com.ucredit.hermes.model.miguan.MiguanUserIdCardSuspicion;
import com.ucredit.hermes.model.miguan.MiguanUserPhoneSuspicion;
import com.ucredit.hermes.model.miguan.MiguanUserSearchedHistoryByOrgs;

@Service
@Transactional(rollbackFor = ServiceException.class)
public class MiGuanService {
    private static Logger logger = LoggerFactory.getLogger(MiGuanService.class);
    @Autowired
    private RESTTemplate restTemplate;
    @Autowired
    private Variables variables;

    @Autowired
    private MiguanResponseDAO miguanResponseDAO;

    /**
     * 蜜罐访问
     *
     * @param name
     * @param idcard
     * @param phone
     * @param principal
     * @return
     */
    public Map<String, Object> searchReport(String name, String idcard,
        String phone, String apply_id, String contactType,
        @SuppressWarnings("unused") Principal principal) {
        Map<String, Object> result = new HashMap<>();
        MiguanResponse miguanResponse = new MiguanResponse();
        miguanResponse.setResultType(ResultType.QUERY);
        miguanResponse.setName(name);
        miguanResponse.setIdcard(idcard);
        miguanResponse.setPhone(phone);
        miguanResponse.setApply_id(apply_id);
        miguanResponse.setContactType(contactType);
        String client_secret = this.variables.getMiguanClientSecret();
        String access_token = this.variables.getMiguanAccessToken();
        String url = this.variables.getMiguanUrl();
        String queryString = url + "?client_secret=" + client_secret
            + "&access_token=" + access_token + "&name=" + name + "&idCard="
            + idcard + "&phone=" + phone;
        String data = "";
        String errorMessage = "";
        try {//访问第三方
            data = this.restTemplate.getEntity(queryString, String.class);
        } catch (Exception e) {
            MiGuanService.logger.info("访问第三方出错");
            errorMessage = errorMessage + e.getMessage();
            e.printStackTrace();
        }
        try {
            if (!"".equals(data)) {//返回正常，解析入库
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                MiguanResponse reportInfo = gson.fromJson(data,
                    MiguanResponse.class);
                reportInfo.setName(name);
                reportInfo.setIdcard(idcard);
                reportInfo.setPhone(phone);
                reportInfo.setBackTime(new Date());
                reportInfo.setApply_id(apply_id);
                reportInfo.setContactType(contactType);
                reportInfo.setResultType(ResultType.SUCCESS);
                if ("false".equals(reportInfo.getSuccess())) {
                    reportInfo.setResultType(ResultType.FAILURE);
                    reportInfo.setErrorCode(AsyncCode.FAILURE_3RD_RETURN_ERROR);
                    reportInfo.setErrorMessage(reportInfo.getNote());
                } else {
                    MiguanGridInfo miguanGridInfo = reportInfo.getGrid_info();
                    if (!"fail".equals(miguanGridInfo.getStatus())) {
                        MiguanResult miguanResult = miguanGridInfo.getResult();
                        //blacklist_category赋值
                        List<String> blacklist_category = miguanResult
                            .getUser_blacklist().getBlacklist_category();
                        String blacklist_categoryStr = "";
                        if (!blacklist_category.isEmpty()) {
                            for (String str : blacklist_category) {
                                blacklist_categoryStr = blacklist_categoryStr
                                        + str + ",";
                            }
                            blacklist_categoryStr = blacklist_categoryStr
                                    .substring(0,
                                        blacklist_categoryStr.length() - 1);
                            miguanResult
                            .getUser_blacklist()
                            .setBlacklist_categoryStr(blacklist_categoryStr);
                        }
                        this.miguanResponseDAO.addEntity(reportInfo);
                        MiguanUserPhoneSuspicion miguanUserPhoneSuspicion = miguanResult
                            .getUser_phone_suspicion();
                        List<MiguanPhoneWithOtherIdcards> miguanPhoneWithOtherIdcards = miguanUserPhoneSuspicion
                            .getPhone_with_other_idcards();
                        for (MiguanPhoneWithOtherIdcards m : miguanPhoneWithOtherIdcards) {
                            m.setPhone_suspicion_id(miguanUserPhoneSuspicion
                                .getId());
                        }

                        List<MiguanPhoneWithOtherNames> miguanPhoneWithOtherNames = miguanUserPhoneSuspicion
                                .getPhone_with_other_names();
                        for (MiguanPhoneWithOtherNames m : miguanPhoneWithOtherNames) {
                            m.setPhone_suspicion_id(miguanUserPhoneSuspicion
                                .getId());
                        }

                        List<MiguanPhoneAppliedInOrgs> miguanPhoneAppliedInOrgs = miguanUserPhoneSuspicion
                            .getPhone_applied_in_orgs();
                        for (MiguanPhoneAppliedInOrgs m : miguanPhoneAppliedInOrgs) {
                            m.setPhone_suspicion_id(miguanUserPhoneSuspicion
                                .getId());
                        }

                        List<MiguanUserSearchedHistoryByOrgs> miguanUserSearchedHistoryByOrgs = miguanResult
                                .getUser_searched_history_by_orgs();
                        for (MiguanUserSearchedHistoryByOrgs m : miguanUserSearchedHistoryByOrgs) {
                            m.setResult_id(miguanResult.getId());
                        }
                        MiguanUserIdCardSuspicion miguanUserIdCardSuspicion = miguanResult
                            .getUser_idcard_suspicion();
                        List<MiguanIdCardWithOtherNames> miguanIdCardWithOtherNames = miguanUserIdCardSuspicion
                            .getIdcard_with_other_names();
                        for (MiguanIdCardWithOtherNames m : miguanIdCardWithOtherNames) {
                            m.setCard_suspicion_id(miguanUserIdCardSuspicion
                                .getId());
                        }

                        List<MiguanIdCardWithOtherPhones> miguanIdCardWithOtherPhones = miguanUserIdCardSuspicion
                            .getIdcard_with_other_phones();
                        for (MiguanIdCardWithOtherPhones m : miguanIdCardWithOtherPhones) {
                            m.setCard_suspicion_id(miguanUserIdCardSuspicion
                                .getId());
                        }

                        List<MiguanIdCardAppliedInOrgs> miguanIdCardAppliedInOrgs = miguanUserIdCardSuspicion
                            .getIdcard_applied_in_orgs();
                        for (MiguanIdCardAppliedInOrgs m : miguanIdCardAppliedInOrgs) {
                            m.setCard_suspicion_id(miguanUserIdCardSuspicion
                                .getId());
                        }

                        //register_orgs赋值
                        List<String> register_orgs = miguanResult
                            .getUser_register_orgs().getRegister_orgs();
                        String register_orgsStr = "";
                        if (!register_orgs.isEmpty()) {
                            for (String str : register_orgs) {
                                register_orgsStr = register_orgsStr + str + ",";
                            }
                            register_orgsStr = register_orgsStr.substring(0,
                                register_orgsStr.length() - 1);
                            miguanResult.getUser_register_orgs()
                            .setRegister_orgsStr(register_orgsStr);
                        }
                    } else {
                        reportInfo.setResultType(ResultType.FAILURE);
                        reportInfo
                            .setErrorCode(AsyncCode.FAILURE_3RD_RETURN_ERROR);
                        reportInfo.setErrorMessage(reportInfo.getGrid_info()
                            .getError_msg());
                    }
                    this.miguanResponseDAO.updateEntity(reportInfo);
                    result.put("msg", reportInfo);
                    return result;
                }
                this.miguanResponseDAO.addEntity(reportInfo);
                result.put("msg", reportInfo);
            } else {//返回异常，返回信息
                MiGuanService.logger.info("data 为空");
                result.put("msg", errorMessage);
                miguanResponse
                .setErrorCode(AsyncCode.FAILURE_CONNECTION_REFUSED);
                miguanResponse.setErrorMessage(errorMessage);
                miguanResponse.setResultType(ResultType.FAILURE);
                this.miguanResponseDAO.addEntity(miguanResponse);
                return result;
            }
        } catch (Exception e) {//hermes解析出错
            MiGuanService.logger.info("hermes解析出错" + e.getMessage());
            result.put("msg", "hermes解析出错");
            miguanResponse.setErrorCode(AsyncCode.FAILURE_HERMES_ERROR);
            miguanResponse.setErrorMessage("hermes解析出错" + e.getMessage());
            miguanResponse.setResultType(ResultType.FAILURE);
            this.miguanResponseDAO.addEntity(miguanResponse);
            e.printStackTrace();
        }
        return result;
    }

}
