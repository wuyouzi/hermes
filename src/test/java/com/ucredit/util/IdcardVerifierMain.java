package com.ucredit.util;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.ucredit.service.web.idcard.request.QueryValidatorServices_QueryValidatorServices_Client;
import com.ucredit.service.web.idcard.response.Data;
import com.ucredit.service.web.idcard.response.Message;
import com.ucredit.service.web.idcard.response.PoliceCheckInfo;

/**
 * 账户金额对账入口 。
 *
 * @author CLH
 */
@Component
public class IdcardVerifierMain {

    protected static Logger logger = LoggerFactory
        .getLogger(IdcardVerifierMain.class);

    public static ApplicationContext context;

    public static QueryValidatorServices_QueryValidatorServices_Client guozhengtongIdCardService;

    /**
     * 入口，需要输入时间参数(2013-01-01零点以后)，仅对时间参数之后的资金记录进行对账。
     *
     * @author CLH
     * @param args
     */
    public static void main(String[] args) {
        try {

            // 加载配置文件
            try {
                String[] cfgFiles = new String[] { "applicationContext.xml" };
                IdcardVerifierMain.context = new ClassPathXmlApplicationContext(
                    cfgFiles);
                IdcardVerifierMain.guozhengtongIdCardService = IdcardVerifierMain.context
                    .getBean(QueryValidatorServices_QueryValidatorServices_Client.class);
            } catch (Exception e) {
                System.out.println(e);
            }

            Data response = IdcardVerifierMain.guozhengtongIdCardService
                .querySingle("路宗威", "11010319830310121X");

            try {
                if (response != null
                    && response.getMessage() != null
                    && response.getPoliceCheckInfos() != null
                    && response.getPoliceCheckInfos().getPoliceCheckInfo() != null
                    && response.getPoliceCheckInfos().getPoliceCheckInfo()
                        .getMessage() != null
                    && Message.PROCESS_CODE.equals(response.getMessage()
                        .getStatus())
                    && Message.PROCESS_CODE.equals(response
                        .getPoliceCheckInfos().getPoliceCheckInfo()
                        .getMessage().getStatus())) {
                    PoliceCheckInfo policeCheckInfo = response
                        .getPoliceCheckInfos().getPoliceCheckInfo();
                    if (policeCheckInfo.getCompStatus().equals(
                        Message.AUTH_CODE_VALID)) {
                        policeCheckInfo.getName();
                        new SimpleDateFormat("yyyyMMdd").parse(policeCheckInfo
                            .getBirthday2().length() == 8 ? policeCheckInfo
                            .getBirthday2() : "19"
                            + policeCheckInfo.getBirthday2());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
