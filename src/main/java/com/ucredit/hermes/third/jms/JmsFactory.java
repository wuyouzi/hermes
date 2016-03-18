/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.third.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ucredit.hermes.enums.DataChannel;

/**
 * @author caoming
 */
@Component
public class JmsFactory {
    @Autowired
    private ThirdPengYuanGenerator thirdPengYuanGenerator;
    @Autowired
    private ThirdGuoZhengTongGenerator thirdGuoZhengTongGenerator;

    /**
     * 返回相应的generator
     *
     * @param type
     * @return
     */
    public ThirdGenerator createFactory(DataChannel type) {
        if (type == null) {
            throw new IllegalArgumentException("type should not be NULL");
        }
        switch (type) {
            case PENGYUAN:
                return this.thirdPengYuanGenerator;
            case GUOZHENGTONG:
                return this.thirdGuoZhengTongGenerator;
            default:
                return null;
        }
    }
}
