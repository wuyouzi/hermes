/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.utils;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;

/**
 * @author caoming
 */
public class JmsUtils {

	/**
	 * 访问第三方的生产者
	 *
	 * @param type
	 * @param result
	 * @param logInfo
	 * @return
	 */
	public static MessageCreator buildThirdQueueMessageCreator(
			final ThirdQueueTaskResult result, final DataChannel type,
			final LogInfo logInfo) {
		return JmsUtils.buildThirdQueueMessageCreator(result, type, logInfo,
				null);
	}

	public static MessageCreator buildThirdQueueMessageCreator(
			final ThirdQueueTaskResult result, final DataChannel type,
			final LogInfo logInfo, final String systemId) {
		return new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				Message message = session.createObjectMessage(new Object[] {
						result, type, logInfo });
				if (systemId != null) {
					message.setStringProperty(
							JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY, systemId);
				}
				return message;
			}
		};
	}

	public static final String JMS_MESSAGE_SELECTOR_STRING_KEY = "SourceSystemID";

}
