package com.ucredit.hermes.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.BaiRongLocation;

@Repository
public class BaiRongLocationDAO extends BaseSearchDAO {

    public List<BaiRongLocation> getBaiRongLocationByParamId(Integer paramID) {
        String hql = "from BaiRongLocation where baiRongParamsId=:baiRongParamsId";
        Map<String, Object> params = new HashMap<>();
        params.put("baiRongParamsId", paramID);
        return (List<BaiRongLocation>) this.selectByHQL(hql, params);
    }
}
