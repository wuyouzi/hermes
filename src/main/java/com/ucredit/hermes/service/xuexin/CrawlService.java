/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service.xuexin;

import net.sf.json.JSONObject;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ucredit.hermes.common.RESTTemplate;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.crawl.SchoolInfoDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.crawl.SchoolInfo;
import com.ucredit.hermes.model.crawl.UndergraduateCourseInfo;

/**
 * 学历信息的service
 *
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class CrawlService {
    @Autowired
    private Variables variables;
    @Autowired
    private RESTTemplate restTemplate;
    @Autowired
    private SchoolInfoDAO schoolInfoDAO;

    /**
     * 获取爬取平台信息
     *
     * @param idNo
     * @return
     */
    public UndergraduateCourseInfo getEducationInfo(String idNo) {
        String crawUrl = this.variables.getCrawlXuexinUrl();
        String userChisResponse = "";
        UndergraduateCourseInfo undergraduateCourseInfo = new UndergraduateCourseInfo();
        try {
            userChisResponse = this.restTemplate.getEntity(crawUrl + "?idNo="
                    + idNo, String.class);
        } catch (Exception e) {//链接爬取平台异常
            undergraduateCourseInfo
            .setErrorCode(AsyncCode.FAILURE_CONNECTION_REFUSED);
            undergraduateCourseInfo
            .setErrorMessage("链接爬取平台异常" + e.getMessage());
            undergraduateCourseInfo.setResultType(ResultType.FAILURE);
            e.printStackTrace();
            return undergraduateCourseInfo;
        }
        if (!"".equals(userChisResponse)) {
            JSONObject msgJSON = null;
            try {//转换成undergraduateCourseInfo失败，表示没有爬取到信息
                msgJSON = JSONObject.fromObject(userChisResponse);
                JSONObject result = (JSONObject) msgJSON.get("result");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                undergraduateCourseInfo = gson.fromJson(result.toString(),
                    UndergraduateCourseInfo.class);
            } catch (Exception e) {//没有爬取到信息的原因解析
                if (msgJSON != null) {
                    String result = (String) msgJSON.get("result");
                    undergraduateCourseInfo
                        .setErrorCode(AsyncCode.FAILURE_3RD_RETURN_ERROR);
                    undergraduateCourseInfo.setErrorMessage(result);
                    undergraduateCourseInfo.setResultType(ResultType.FAILURE);
                    return undergraduateCourseInfo;
                }
                e.printStackTrace();
            }
            //查询对应的学校信息，赋值返回
            try {
                String schoolName = undergraduateCourseInfo.getSchool_name();
                SchoolInfo schoolInfo = this.schoolInfoDAO
                    .getSchoolInfoByName(schoolName);
                undergraduateCourseInfo.setSchoolInfo(schoolInfo);
                undergraduateCourseInfo.setResultType(ResultType.SUCCESS);
                return undergraduateCourseInfo;
            } catch (Exception e) {
                undergraduateCourseInfo
                .setErrorCode(AsyncCode.FAILURE_HERMES_ERROR);
                undergraduateCourseInfo.setErrorMessage("hermes平台异常"
                    + e.getMessage());
                undergraduateCourseInfo.setResultType(ResultType.FAILURE);
                e.printStackTrace();
                return undergraduateCourseInfo;
            }
        } else {
            undergraduateCourseInfo.setErrorCode(AsyncCode.FAILURE_EMPTYDATA);
            undergraduateCourseInfo.setErrorMessage("爬取平台返回数据为空");
            undergraduateCourseInfo.setResultType(ResultType.FAILURE);
            return undergraduateCourseInfo;
        }

    }
}
