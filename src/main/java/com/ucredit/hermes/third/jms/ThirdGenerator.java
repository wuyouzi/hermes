/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.QueryType;
import com.ucredit.hermes.model.ThirdQueueTaskResult;

/**
 * @author caoming
 */
@Component
public abstract class ThirdGenerator {
    protected abstract String buildConditionsXML(ThirdQueueTaskResult result)
            throws Exception;

    public abstract String sendXML(String query) throws Exception;

    /**
     * @param type
     * @param dataType
     * @return String
     */
    public static String getBatchNumber(QueryType type, DataChannel dataType) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmssSSS");
        String batchNumber = format.format(date);

        switch (type) {
            case PERSON:
                batchNumber = "P" + batchNumber;
                break;
            case PERSON_EDUCATION:
                batchNumber = "PE" + batchNumber;
                break;
            case CHECK_EDUCATION:
                batchNumber = "CE" + batchNumber;
                break;
            case CHECK_ROLL:
                batchNumber = "CR" + batchNumber;
                break;
            case CHECK_COMPANY:
                batchNumber = "CC" + batchNumber;
                break;
            default:
                break;
        }

        switch (dataType) {
            case PENGYUAN:
                batchNumber = "PY_" + batchNumber;
                break;
            case GUOZHENGTONG:
                batchNumber = "GZT_" + batchNumber;
                break;
            default:
                break;
        }
        return batchNumber;
    }

}
