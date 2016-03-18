package com.ucredit.hermes.service.crawl;

import java.util.Date;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.dao.CompanyInfoDAO;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.QueryType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.UserGroup;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.service.ThirdQueueService;
import com.ucredit.hermes.third.jms.ThirdGenerator;

@Service
@Transactional(rollbackFor = ServiceException.class)
public class PengyuanCompanyServace {
    private static Logger logger = LoggerFactory
        .getLogger(PengyuanCompanyServace.class);
    @Autowired
    private CompanyInfoDAO companyInfoDao;
    @Autowired
    private ThirdQueueService thirdQueueService;

    public void searchFromPengyuan(String operationName, UserGroup userGroup,
            String ip, String lendRequestId, String systemId,
            String companyname, String keyid) {
        // 插入数据库一条记录，状态为查询中
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setCompanyName(companyname);
        companyInfo.setKeyid(keyid);
        companyInfo.setOrgCode(null);
        companyInfo.setRegisterNo(null);
        Date now = new Date();
        companyInfo.setCreateTime(now);
        companyInfo.setLastUpdateTime(now);
        companyInfo.setDataChannel(DataChannel.PENGYUAN);
        companyInfo.setResultType(ResultType.QUERY);
        String refId = ThirdGenerator.getBatchNumber(QueryType.CHECK_COMPANY,
            DataChannel.PENGYUAN);
        companyInfo.setRefId(refId);
        companyInfo.setLendRequestId(lendRequestId);
        companyInfo.setPengYuanCompanyName(companyname);
        companyInfo.setKeyid(companyInfo.getKeyid());
        int companyid = this.companyInfoDao.addEntity(companyInfo);
        //爬取信息失败通过jms发请求，查询鹏元
        this.thirdQueueService.sendMessageV2(companyid, operationName,
            userGroup, ip, lendRequestId, systemId);
        PengyuanCompanyServace.logger.info("爬取平台返回或解析失败（工商），发起鹏元查询插入mq成功");

    }

}
