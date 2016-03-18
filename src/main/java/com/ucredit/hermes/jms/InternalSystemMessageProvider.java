package com.ucredit.hermes.jms;

import java.text.SimpleDateFormat;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucredit.hermes.dto.CommonResult;
import com.ucredit.hermes.dto.Operation;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.utils.JmsUtils;

@Component("provider")
public class InternalSystemMessageProvider {
    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendTextMessage(final String messageBody,
            final String systemId, final Destination destination)
                    throws UnsupportedOperationException {
        this.jmsTemplate.send(destination, new MessageCreator() {

            @Override
            public Message createMessage(Session arg0) throws JMSException {
                Message message = arg0.createTextMessage(messageBody);
                if (StringUtils.isNotEmpty(systemId)) {
                    message.setStringProperty(
                        JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY, systemId);
                }
                return message;
            }

        });
    }

    public void sendTextMessage(final String messageBody,
            final Destination destination) throws UnsupportedOperationException {
        this.jmsTemplate.send(destination, new MessageCreator() {
            @Override
            public Message createMessage(Session arg0) throws JMSException {
                return arg0.createTextMessage(messageBody);
            }
        });
    }

    public void sendTransObjectMessage(final Destination destination,
            final Object param) {
        TransactionSynchronizationManager
        .registerSynchronization(new TransactionSynchronizationAdapter() {
            /**
             * 提交前向jms发请求，避免因为amq没有启动出现脏数据
             */
            @Override
            public void afterCommit() {
                try {
                    InternalSystemMessageProvider.this.jmsTemplate.send(
                        destination, new MessageCreator() {
                            @Override
                            public Message createMessage(Session session)
                                    throws JMSException {
                                return session
                                        .createObjectMessage(new Object[] { param });
                            }
                        });
                } catch (Exception e) {
                    /**
                     * 记录jms异常
                     */
                    // BaiRongParams newdbPaiRongParams =
                    // (BaiRongParams)
                    // BaiRongService.this.baiRongParamsDAO
                    // .getById(dbPaiRongParams.getDbId(),
                    // dbPaiRongParams.getClass());
                    // newdbPaiRongParams
                    // .setResultType(ResultType.FAILURE);
                    // newdbPaiRongParams
                    // .setResultCode(BaiRongResultCode.YouXin_1000003);
                    // newdbPaiRongParams
                    // .setResultCodeDesc(BaiRongResultCode.YouXin_1000003
                    // .getString());
                    // BaiRongService.this.baiRongParamsDAO
                    // .updateEntity(newdbPaiRongParams);
                    e.printStackTrace();
                    // throw e;
                }

            }
        });
    }

    public static <T> String packageResponse(T t, String systemId,
            DataChannelSubType type, int requestId, AsyncCode asyncCode)
                    throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        CommonResult<T> c = new CommonResult<>();
        c.setResult(t);
        Operation opt = new Operation();
        opt.setFromSysId(DataChannel.HERMES.toString());
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System
            .currentTimeMillis());
        opt.setResponseTime(now);
        opt.setRequestType(type.toString());
        opt.setToSysId(systemId);
        opt.setRequestId(String.valueOf(requestId));
        opt.setResponseStatus(asyncCode.toString());
        c.setOperation(opt);
        return m.writeValueAsString(c);
    }

    public static <T> String packageResponse(T t, String systemId,
            DataChannelSubType type, String requestId, AsyncCode asyncCode)
                    throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        CommonResult<T> c = new CommonResult<>();
        c.setResult(t);
        Operation opt = new Operation();
        opt.setFromSysId(DataChannel.HERMES.toString());
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System
            .currentTimeMillis());
        opt.setResponseTime(now);
        opt.setRequestType(type.toString());
        opt.setToSysId(systemId);
        opt.setRequestId(String.valueOf(requestId));
        opt.setResponseStatus(asyncCode.toString());
        c.setOperation(opt);
        return m.writeValueAsString(c);
    }

}
