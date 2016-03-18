package com.ucredit.hermes.service.qianhai;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.dao.UserDAO;
import com.ucredit.hermes.dao.qianhai.QianhaiDAO;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.User;
import com.ucredit.hermes.model.qianhai.QianhaiSearchRecord;
import com.ucredit.hermes.service.ThirdQueueService;

@Service
@Transactional(rollbackFor = Exception.class)
public class QianhaiBlackService {
    private static Logger logger = LoggerFactory
        .getLogger(QianhaiBlackService.class);
    @Autowired
    private UserDAO userDao;
    @Autowired
    private QianhaiDAO qianhaiDAO;
    @Autowired
    private ThirdQueueService thirdQueueService;

    public Map<String, Object> searchBlackList(String idNo, String idType,
            String name, String reasonCode, String apply_id, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        /*
         * qianhaiSearchRecord = this.qianhaiDAO
         * .getQianhaiSearchRecord(name, idType, idNo,
         * apply_id);
         */
        QianhaiSearchRecord qianhaiSearchRecord = new QianhaiSearchRecord();
        qianhaiSearchRecord.setName(name);
        qianhaiSearchRecord.setIdType(idType);
        qianhaiSearchRecord.setIdNo(idNo);
        qianhaiSearchRecord.setApply_id(apply_id);
        qianhaiSearchRecord.setResultType(ResultType.QUERY);
        int id = this.qianhaiDAO.addEntity(qianhaiSearchRecord);
        QianhaiBlackService.logger.info("插入search_info  id:" + id);
        String username = ((Authentication) principal).getName();
        User user = this.userDao.getUserByUserName(username);
        user.getGroup();
        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
            .getDetails();
        String ip = details.getRemoteAddress();
        // 通过jms发请求
        ThirdQueueTaskResult taskResult = new ThirdQueueTaskResult();
        taskResult.setFkId(id);
        taskResult.setName(name);
        taskResult.setUserName(username);
        taskResult.setIdcard(idNo);
        taskResult.setType(JMSType.QIANHAI_JMS_BLACK);
        taskResult.setSearchType(idType);
        taskResult.setIp(ip);
        taskResult.setLendRequestId(apply_id);
        taskResult.setCode(reasonCode);
        QianhaiBlackService.logger.info("发送前海mq  id:" + id);
        this.thirdQueueService.sendMessage(taskResult, DataChannel.QIANHAI,
            null, username);
        result.put("msg", qianhaiSearchRecord);
        return result;

    }

    public Map<String, Object> getBlackList(String idNo, String idType,
            String name, String apply_id) {
        Map<String, Object> result = new HashMap<>();
        QianhaiSearchRecord qianhaiSearchRecord = this.qianhaiDAO
            .getExistRecord(name, idType, idNo, apply_id);
        result.put("msg", qianhaiSearchRecord);
        return result;
    }

}
