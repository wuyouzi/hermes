package com.ucredit.hermes.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.BaiRongConsumption;

@Repository
public class BaiRongConsumptionDAO extends BaseSearchDAO {

    public List<BaiRongConsumption> getBaiRongConsumptionByParamId(
        Integer paramID) {
        String hql = "from BaiRongConsumption where baiRongParamsId=:baiRongParamsId";
        Map<String, Object> params = new HashMap<>();
        params.put("baiRongParamsId", paramID);
        return (List<BaiRongConsumption>) this.selectByHQL(hql, params);
    }
}
