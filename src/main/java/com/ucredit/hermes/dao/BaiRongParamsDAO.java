package com.ucredit.hermes.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.ucredit.hermes.enums.BaiRongResultCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.BaiRongParams;

/**
 */
@Repository
public class BaiRongParamsDAO extends BaseSearchDAO {

    public List<BaiRongParams> getBaiRongParamsBySearchParams(
            BaiRongParams baiRongParams) {
        StringBuffer hql = new StringBuffer(
                "from BaiRongParams where enabled = :enabled");

        Map<String, Object> params = new HashMap<>();
        params.put("enabled", true);

        if (StringUtils.isNoneBlank(baiRongParams.getApply_addr())) {

            hql.append(" and  apply_addr =:apply_addr");
            params.put("apply_addr", baiRongParams.getApply_addr());
        }

        if (StringUtils.isNoneEmpty(baiRongParams.getBank_id())) {

            hql.append(" and  bank_id =:bank_id");
            params.put("bank_id", baiRongParams.getBank_id());
        }

        if (StringUtils.isNoneEmpty(baiRongParams.getBiz_addr())) {

            hql.append(" and  biz_addr =:biz_addr");
            params.put("biz_addr", baiRongParams.getBiz_addr());
        }
        if (StringUtils.isNoneEmpty(baiRongParams.getBiz_industry())) {

            hql.append(" and  biz_industry =:biz_industry");
            params.put("biz_industry", baiRongParams.getBiz_industry());
        }

        if (StringUtils.isNoneEmpty(baiRongParams.getBiz_workfor())) {

            hql.append(" and  biz_workfor =:biz_workfor");
            params.put("biz_workfor", baiRongParams.getBiz_workfor());
        }

        if (StringUtils.isNoneEmpty(baiRongParams.getCell())) {

            hql.append(" and  cell =:cell");
            params.put("cell", baiRongParams.getCell());
        }

        if (StringUtils.isNoneEmpty(baiRongParams.getEducationallevel())) {

            hql.append(" and  educationallevel =:educationallevel");
            params.put("educationallevel", baiRongParams.getEducationallevel());
        }

        if (StringUtils.isNoneEmpty(baiRongParams.getHome_addr())) {

            hql.append(" and  home_addr =:home_addr");
            params.put("home_addr", baiRongParams.getHome_addr());
        }

        if (StringUtils.isNoneEmpty(baiRongParams.getId())) {

            hql.append(" and  id =:id");
            params.put("id", baiRongParams.getId());
        }

        if (StringUtils.isNotBlank(baiRongParams.getLinkman_cell())) {

            hql.append(" and  linkman_cell =:linkman_cell");
            params.put("linkman_cell", baiRongParams.getLinkman_cell());
        }
        if (StringUtils.isNotBlank(baiRongParams.getLinkman_name())) {

            hql.append(" and  linkman_name =:linkman_name");
            params.put("linkman_name", baiRongParams.getLinkman_name());
        }

        if (StringUtils.isNotBlank(baiRongParams.getMail())) {

            hql.append(" and  mail =:mail");
            params.put("mail", baiRongParams.getMail());
        }
        if (StringUtils.isNotBlank(baiRongParams.getMarriage())) {

            hql.append(" and  marriage =:marriage");
            params.put("marriage", baiRongParams.getMarriage());
        }

        if (StringUtils.isNotBlank(baiRongParams.getName())) {

            hql.append(" and  name =:name");
            params.put("name", baiRongParams.getName());
        }

        if (StringUtils.isNotBlank(baiRongParams.getTel_biz())) {

            hql.append(" and  tel_biz =:tel_biz");
            params.put("tel_biz", baiRongParams.getTel_biz());
        }
        if (StringUtils.isNotBlank(baiRongParams.getTel_home())) {

            hql.append(" and  tel_home =:tel_home");
            params.put("tel_home", baiRongParams.getTel_home());
        }
        hql.append(" order by createTime desc ");
        String hqlString = hql.toString();

        return (List<BaiRongParams>) this.selectByHQL(hqlString, params);
    }

    public BaiRongParams getBaiRongParamsByKeyid(String keyid) {
        String hql = "from BaiRongParams where keyid=:keyid and enabled=1";
        BaiRongParams baiRongParams = (BaiRongParams) this.sessionFactory
                .getCurrentSession().createQuery(hql).setParameter("keyid", keyid)
                .setFirstResult(0).setMaxResults(1).uniqueResult();
        return baiRongParams;

    }

    public Long doStatistics(Date startDate_, Date endDate_) {

        String hql = "select count(id) from BaiRongParams where resultType=:resultType and resultCode = :resultCode and queryTime between :startDate  and :endDate ";
        Map<String, Object> params = new HashMap<>();
        params.put("resultType", ResultType.SUCCESS);
        params.put("resultCode", BaiRongResultCode.BaiRong_00);
        params.put("startDate", startDate_);
        params.put("endDate", endDate_);
        List<Long> temp = (List<Long>) this.selectByHQL(hql, params);
        if (temp != null && !temp.isEmpty()) {
            return temp.get(0);
        }
        return 0L;
    }

    /**
     * 时间段内根据百融公司的流水号查询
     *
     * @param switchNo
     * @param startDate_
     * @param endDate_
     * @return
     */
    public BaiRongParams getBySwitchNo(String switchNo, Date startDate_,
            Date endDate_) {
        String hql = "from BaiRongParams where swift_number=:swift_number and queryTime between :startDate  and :endDate ";
        Map<String, Object> params = new HashMap<>();
        params.put("swift_number", switchNo);
        params.put("startDate", startDate_);
        params.put("endDate", endDate_);
        List<BaiRongParams> results = (List<BaiRongParams>) this.selectByHQL(
            hql, params);
        return CollectionUtils.isEmpty(results) ? null : results.get(0);
    }
}
