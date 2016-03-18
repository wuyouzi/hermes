package com.ucredit.hermes.service;

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

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.UserGroup;
import com.ucredit.hermes.utils.JmsUtils;

@Component("thirdQueueService")
public class ThirdQueueService {
    @Autowired
    private JmsTemplate template;
    @Autowired
    private Destination thirdJMSQueue;
    @Autowired
    private Destination guoZhengTongJMSQueue;
    @Autowired
    private Destination policeJMSQueue;
    @Autowired
    private Destination educationJMSQueue;
    @Autowired
    private Destination inSchoolEducationJMSQueue;
    @Autowired
    private Destination gdsiJMSQueue;
    @Autowired
    private Destination juxinliReport1Queue;
    @Autowired
    private Destination juxinliVerifyQueue;
    @Autowired
    private Destination yinlianzhJMSQueue;
    @Autowired
    private Destination yinlianzhSearchJMSQueue;
    @Autowired
    private Destination qianhaiJMSQueue;
    @Autowired
    private Destination crawlCompanyJMSQueue;

    /**
     * 访问JMS
     *
     * @param result
     * @param type
     * @param logInfo
     */
    public void sendMessage(final ThirdQueueTaskResult result,
            final DataChannel type, final LogInfo logInfo) {
        this.sendMessage(result, type, logInfo, null);
    }

    public void sendMessage(final String response,
            final Destination destination, final String systemId) {
        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    try {
                        ThirdQueueService.this.template.send(destination,
                            ThirdQueueService.this.buildTextMessageCreator(
                                response, systemId));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    public void sendMessage(final ThirdQueueTaskResult result,
            final DataChannel type, final LogInfo logInfo, final String systemId) {
        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    try {
                        ThirdQueueService.this.sendJMS(result, type, logInfo,
                            systemId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }

    @SuppressWarnings("unused")
    private void sendJMS(ThirdQueueTaskResult result, DataChannel type,
            LogInfo logInfo) throws Exception {
        this.sendJMS(result, type, logInfo, null);
    }

    private void sendJMS(ThirdQueueTaskResult result, DataChannel type,
            LogInfo logInfo, String systemId) throws Exception {
        JMSType jmsType = result.getType();
        if (JMSType.THIRD_JMS_QUEUE == jmsType) {
            throw new Exception(" 不再支持 ");
            // this.template.send(this.thirdJMSQueue,
            // JmsUtils.buildThirdQueueMessageCreator(result, type, logInfo));
        } else if (JMSType.GUO_ZHENG_TONG_JMS_QUEUE == jmsType) {
            this.template
                .send(this.guoZhengTongJMSQueue, JmsUtils
                    .buildThirdQueueMessageCreator(result, type, logInfo,
                        systemId));
        } else if (JMSType.POLICE_JMS_QUEUE == jmsType) {
            this.template
                .send(this.policeJMSQueue, JmsUtils
                    .buildThirdQueueMessageCreator(result, type, logInfo,
                        systemId));
        } else if (JMSType.EDUCATION_JMS_QUEUE == jmsType
            || JMSType.LAST_EDUCATION_JMS_QUEUE == jmsType) {
            this.template
                .send(this.educationJMSQueue, JmsUtils
                    .buildThirdQueueMessageCreator(result, type, logInfo,
                        systemId));
        } else if (JMSType.IN_SCHOOL_EDUCATION_JMS_QUEUE == jmsType) {
            this.template
                .send(this.inSchoolEducationJMSQueue, JmsUtils
                    .buildThirdQueueMessageCreator(result, type, logInfo,
                        systemId));
        } else if (JMSType.GD_SI_JMS_QUEUE == jmsType) {
            this.template
                .send(this.gdsiJMSQueue, JmsUtils
                    .buildThirdQueueMessageCreator(result, type, logInfo,
                        systemId));
        } else if (JMSType.JU_XIN_LI == jmsType) {
            this.template
                .send(this.juxinliReport1Queue, JmsUtils
                    .buildThirdQueueMessageCreator(result, type, logInfo,
                        systemId));
        } else if (JMSType.JU_XIN_LI_VERIFY == jmsType) {
            this.template
            .send(this.juxinliVerifyQueue, JmsUtils
                .buildThirdQueueMessageCreator(result, type, logInfo,
                    systemId));
        } else if (JMSType.YINLIANZH_JMS_QUEUE == jmsType) {
            this.template.send(this.yinlianzhJMSQueue,

                JmsUtils.buildThirdQueueMessageCreator(result, type, logInfo));

        } else if (JMSType.YINLIANZH_JMS_SEARCH_QUEUE == jmsType) {
            this.template.send(this.yinlianzhSearchJMSQueue,

                JmsUtils.buildThirdQueueMessageCreator(result, type, logInfo));

        } else if (JMSType.QIANHAI_JMS_BLACK == jmsType) {
            this.template.send(this.qianhaiJMSQueue,

                JmsUtils.buildThirdQueueMessageCreator(result, type, logInfo));

        }
    }

    public void sendMessageV2(final int companyid, final String operationName,
            final UserGroup userGroup, final String ip,
            final String lendRequestId) {
        this.sendMessageV2(companyid, operationName, userGroup, ip,
            lendRequestId, null);

    }

    public void sendMessageV2(final int companyid, final String operationName,
            final UserGroup userGroup, final String ip,
            final String lendRequestId, final String systemId) {
        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    ThirdQueueService.this.template.send(
                        ThirdQueueService.this.thirdJMSQueue,
                        new MessageCreator() {
                            @Override
                            public Message createMessage(Session session)
                                    throws JMSException {
                                if (StringUtils.isNotEmpty(systemId)) {
                                    Message message = session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId });
                                    message
                                        .setStringProperty(
                                            JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY,
                                            systemId);
                                    return message;
                                } else {
                                    return session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId });
                                }
                            }
                        });
                }
            });

    }

    public void sendMessageV2(final int companyid, final String operationName,
            final UserGroup userGroup, final String ip,
            final String lendRequestId, final String systemId,
            final String reportType) {
        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    ThirdQueueService.this.template.send(
                        ThirdQueueService.this.thirdJMSQueue,
                        new MessageCreator() {
                            @Override
                            public Message createMessage(Session session)
                                    throws JMSException {
                                if (StringUtils.isNotEmpty(systemId)) {
                                    Message message = session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId,
                                            reportType });
                                    message
                                        .setStringProperty(
                                            JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY,
                                            systemId);
                                    return message;
                                } else {
                                    return session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId,
                                            reportType });
                                }
                            }
                        });
                }
            });

    }

    public void sendMessageCrawl(final int companyid,
            final String operationName, final UserGroup userGroup,
            final String ip, final String lendRequestId, final String systemId) {
        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    ThirdQueueService.this.template.send(
                        ThirdQueueService.this.crawlCompanyJMSQueue,
                        new MessageCreator() {
                            @Override
                            public Message createMessage(Session session)
                                    throws JMSException {
                                if (StringUtils.isNotEmpty(systemId)) {
                                    Message message = session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId });
                                    message
                                        .setStringProperty(
                                            JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY,
                                            systemId);
                                    return message;
                                } else {
                                    return session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId });
                                }
                            }
                        });
                }
            });

    }

    public void sendMessageV2(final int companyid, final String operationName,
            final UserGroup userGroup, final String ip,
            final String lendRequestId, final int num, final String systemId) {
        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    ThirdQueueService.this.template.send(
                        ThirdQueueService.this.thirdJMSQueue,
                        new MessageCreator() {
                            @Override
                            public Message createMessage(Session session)
                                    throws JMSException {
                                if (StringUtils.isNotEmpty(systemId)) {
                                    Message message = session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId, num });
                                    message
                                        .setStringProperty(
                                            JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY,
                                            systemId);
                                    return message;
                                } else {
                                    return session
                                        .createObjectMessage(new Object[] {
                                            companyid, operationName,
                                            userGroup, ip, lendRequestId });
                                }
                            }
                        });
                }
            });

    }

    public MessageCreator buildTextMessageCreator(final String response) {
        return this.buildTextMessageCreator(response, null);
    }

    public MessageCreator buildTextMessageCreator(final String response,
            final String systemId) {
        return new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message message = session.createTextMessage(response);
                if (systemId != null) {
                    message.setStringProperty(
                        JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY, systemId);
                }
                return message;
            }
        };
    }
}