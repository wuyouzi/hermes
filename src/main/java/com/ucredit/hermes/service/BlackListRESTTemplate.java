/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.common.RESTTemplate;

/**
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class BlackListRESTTemplate extends RESTTemplate {

}
