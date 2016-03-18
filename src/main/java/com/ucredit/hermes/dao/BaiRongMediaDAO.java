package com.ucredit.hermes.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.BaiRongMedia;

@Repository
public class BaiRongMediaDAO extends BaseSearchDAO {

    public List<BaiRongMedia> getBaiRongMediaByParamId(Integer paramID) {
        String hql = "from  BaiRongMedia where baiRongParamsId=:baiRongParamsId";
        Map<String, Object> params = new HashMap<>();
        params.put("baiRongParamsId", paramID);
        return (List<BaiRongMedia>) this.selectByHQL(hql, params);
    }
}
