package com.ucredit.hermes.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.BaiRongAuthentication;

@Repository
public class BaiRongAuthenticationDAO extends BaseSearchDAO {

    public List<BaiRongAuthentication> getBaiRongAuthenticationByParamId(
        Integer paramID) {
        String hql = "from BaiRongAuthentication where baiRongParamsId=:baiRongParamsId";
        Map<String, Object> params = new HashMap<>();
        params.put("baiRongParamsId", paramID);
        return (List<BaiRongAuthentication>) this.selectByHQL(hql, params);
    }
}
