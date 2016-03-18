package com.ucredit.hermes.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.BaiRongStability;

@Repository
public class BaiRongStabilityDAO extends BaseSearchDAO {

    public List<BaiRongStability> getBaiRongStabilityByParamId(Integer paramID) {
        String hql = "from  BaiRongStability where baiRongParamsId=:baiRongParamsId";
        Map<String, Object> params = new HashMap<>();
        params.put("baiRongParamsId", paramID);
        return (List<BaiRongStability>) this.selectByHQL(hql, params);
    }
}
