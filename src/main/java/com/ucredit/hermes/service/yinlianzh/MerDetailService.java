package com.ucredit.hermes.service.yinlianzh;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ucredit.hermes.dao.yinlianzh.TransactionDetailDAO;

@Service
@Transactional(rollbackFor = ServiceException.class)
@Scope("prototype")
public class MerDetailService {
    @Autowired
    private TransactionDetailDAO detaildao;

    public void insertDetails(List<String> dataInfo, int transId,
            String trueMid, boolean isdownload) {
        this.detaildao.insertDetails(dataInfo, transId, trueMid, isdownload);

    }

}
