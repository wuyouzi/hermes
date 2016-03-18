package com.ucredit.hermes.dao;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ucredit.hermes.model.pengyuan.PengYuanReportRecord;

@Repository
public class PengYuanReportRecordDAO extends
        BaseDAO<PengYuanReportRecord, Integer> {

    /**
     * 统计一段时间段内学历查询成功的次数
     *
     * @param begin
     * @param end
     * @param subReportCode
     * @return List<PengYuanReportRecord>
     */
    public List<PengYuanReportRecord> getReportForForEducationsBetweenDaysCount(
            Date begin, Date end, String subReportCode) {
        return this.sessionFactory
            .getCurrentSession()
            .createQuery(
                "from PengYuanReportRecord where subReportCode = ? and resultTime >= ? and resultTime <= ?  ")
            .setString(0, subReportCode).setDate(1, begin).setDate(2, end)
            .list();
    }

}
