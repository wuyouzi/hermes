/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.dao.CompanyOtherShareholderInfoDAO;
import com.ucredit.hermes.dao.LegalOtherManagerInfoDAO;
import com.ucredit.hermes.dao.LegalOtherShareholderInfoDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.PositionInfoDAO;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.pengyuan.CompanyOtherShareholderInfo;
import com.ucredit.hermes.model.pengyuan.LegalOtherManagerInfo;
import com.ucredit.hermes.model.pengyuan.LegalOtherShareholderInfo;
import com.ucredit.hermes.model.pengyuan.PositionInfo;

/**
 * 修正数据错误接口: service
 *
 * @author liuqianqian
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class UpdateCompanyInfoDataService {
    @Autowired
    private CompanyInfoBatchService companyInfoBatchService;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private CompanyOtherShareholderInfoDAO companyOtherShareholderInfoDao;
    @Autowired
    private LegalOtherManagerInfoDAO legalOtherManagerInfoDao;
    @Autowired
    private LegalOtherShareholderInfoDAO legalOtherShareholderInfoDao;
    @Autowired
    private PositionInfoDAO positionInfoDAO;

    /**
     * 获取log_infos表中的Data字段数据，解析并修改
     *
     * @throws DocumentException
     */
    public void updateCompanyInfoData() throws DocumentException {
        //企业对外股权投资信息
        List<Integer> companyOtherShareholderInfoCid = this.companyOtherShareholderInfoDao
            .getCompanyOtherShareholderByStatus();
        //法定代表人在其他机构任职信息
        List<Integer> legalOtherManagerInfoCid = this.legalOtherManagerInfoDao
            .getLegalOtherManagerByStatus();
        //法定代表人股权投资信息
        List<Integer> legalOtherShareholderInfoCid = this.legalOtherShareholderInfoDao
            .getLegalOtherShareholderByStatus();

        //获取log_infos表中的Data
        if (companyOtherShareholderInfoCid != null) {
            this.updateLogInfoData(companyOtherShareholderInfoCid, 1);
        }
        if (legalOtherManagerInfoCid != null) {
            this.updateLogInfoData(legalOtherManagerInfoCid, 2);
        }
        if (legalOtherShareholderInfoCid != null) {
            this.updateLogInfoData(legalOtherShareholderInfoCid, 3);
        }
    }

    /**
     * 获取log_infos表中的Data字段数据，解析并修改
     *
     * @param recordId
     * @param type
     * @throws DocumentException
     */
    private void updateLogInfoData(List<Integer> recordIds, int type)
            throws DocumentException {
        List<String> allList = new LinkedList<>();
        //获取log_infos表中的Data
        for (int recordId : recordIds) {
            //通过recordId获得日志对象
            LogInfo logInfo = this.logInfoDao.getLogInfoByRecordId(recordId);
            //获取data数据
            String dataString = logInfo.getData();
            if (StringUtils.isNotBlank(dataString)) {
                Element rootElement = DocumentHelper.parseText(dataString)
                    .getRootElement();
                String status = rootElement.elementText("status");
                if ("1".equals(status)) {// 返回成功
                    Document document = DocumentHelper.parseText(rootElement
                        .elementText("returnValue"));
                    Element root = document.getRootElement();
                    //第一级节点
                    List<Element> cisReportList = root.elements("cisReport");
                    for (Element elementList : cisReportList) {
                        boolean treatResult = this.companyInfoBatchService
                            .getTreatResultValue(elementList);//是否有返回内容
                        if (treatResult) {//有返回内容
                            if (type == 1) {
                                //解析封装成对象
                                List<CompanyOtherShareholderInfo> companyOtherShareholderInfoList = this.companyInfoBatchService
                                    .companyOtherShareholderInfoEntity(
                                        elementList, null, allList);
                                for (CompanyOtherShareholderInfo companyOtherShareholderInfo : companyOtherShareholderInfoList) {
                                    //修改status值
                                    this.companyOtherShareholderInfoDao
                                        .updateCompanyOtherShareholderByCId(
                                            logInfo.getRecordId(),
                                            companyOtherShareholderInfo);
                                }
                            } else if (type == 2) {
                                List<LegalOtherManagerInfo> legalOtherManagerInfoList = this.companyInfoBatchService
                                    .legalOtherManagerEntity(elementList, null,
                                        allList);
                                for (LegalOtherManagerInfo legalOtherManagerInfo : legalOtherManagerInfoList) {
                                    PositionInfo positionInfo = new PositionInfo();
                                    int positionId = 0;
                                    //获取职务记录
                                    if (legalOtherManagerInfo.getPosition() != null) {
                                        positionInfo = this.positionInfoDAO
                                            .getPositionByCode(legalOtherManagerInfo
                                                .getPosition().getCode());
                                        positionId = positionInfo.getId();
                                    }
                                    //修改status值
                                    this.legalOtherManagerInfoDao
                                        .updateLegalOtherManagerByCId(
                                            logInfo.getRecordId(), positionId,
                                            legalOtherManagerInfo);
                                }
                            } else if (type == 3) {
                                List<LegalOtherShareholderInfo> legalOtherShareholderInfoList = this.companyInfoBatchService
                                    .legalOtherShareholderEntity(elementList,
                                        null, allList);

                                for (LegalOtherShareholderInfo legalOtherShareholderInfo : legalOtherShareholderInfoList) {
                                    //修改status值
                                    this.legalOtherShareholderInfoDao
                                        .updateLegalOtherShareholderByCId(
                                            logInfo.getRecordId(),
                                            legalOtherShareholderInfo);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
