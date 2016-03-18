package com.ucredit.hermes.third.jms;

import java.util.Date;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.dao.IdentityInfoDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.guozhengtong.IdentityInfo;
import com.ucredit.hermes.service.IdentityInfoService;
import com.ucredit.hermes.utils.JmsUtils;

@Component("thirdGZTQueueListener")
@Transactional(rollbackFor = ServiceException.class)
public class ThirdGZTQueueListener implements MessageListener {
    private static Logger logger = LoggerFactory
        .getLogger(ThirdGZTQueueListener.class);
    private final static String IDENTITYINFO = "identity_infos";
    @Autowired
    private IdentityInfoDAO dao;
    @Autowired
    private JmsFactory factory;
    @Autowired
    private LogInfoDAO logInfoDAO;
    @Autowired
    private IdentityInfoService identityInfoService;

    @Override
    public void onMessage(Message message) {
        ThirdGZTQueueListener.logger.debug("JMS执行相应代码......");
        IdentityInfo identityInfo = new IdentityInfo();

        try {

            String systemId = message
                .getStringProperty(JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY);

            Object[] vars = (Object[]) ((ObjectMessage) message).getObject();
            ThirdQueueTaskResult result = (ThirdQueueTaskResult) vars[0];
            DataChannel type = (DataChannel) vars[1];
            LogInfo logInfo = (LogInfo) vars[2];
            identityInfo = this.dao.getIdentityInfosByName(result.getName(),
                result.getNumber());
            logInfo.setTableName(ThirdGZTQueueListener.IDENTITYINFO);
            logInfo.setDataChannel(type);
            String queryString = result.getName() + "," + result.getNumber();
            logInfo.setQueryString(queryString);
            ThirdGenerator thirdGenerator = this.factory.createFactory(type);

            try {
                String resultXml = thirdGenerator.sendXML(queryString);
                logInfo.setData(resultXml);
                ThirdGZTQueueListener.logger.debug("国政通返回的xml为：" + resultXml);

                if (StringUtils.isBlank(resultXml)) {
                    identityInfo.setErrorMessage("系统返回错误，请重试！");
                    throw new Exception("三方返回数据为空，请重试......");
                }

                this.identityInfoService.setIdentityInfo(resultXml,
                    identityInfo, logInfo, systemId, false);
            } catch (Exception e) {
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setEndTime(new Date());
                logInfo.setRecordId(identityInfo.getId());
                logInfo.setErrorMessage(HermesConsts.HERMES_GZT_RESPONSE_ERROR);
                identityInfo.setEnabled(false);
                identityInfo
                    .setErrorMessage(HermesConsts.HERMES_GZT_RESPONSE_ERROR);
                identityInfo.setResultType(ResultType.FAILURE);
                identityInfo.setCreateTime(new Date());
                this.dao.updateEntity(identityInfo);
                this.logInfoDAO.addEntity(logInfo);
                ThirdGZTQueueListener.logger.debug("add LogInfo <"
                    + logInfo.getId() + ">");
            }
        } catch (Exception ex) {
            ThirdGZTQueueListener.logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
