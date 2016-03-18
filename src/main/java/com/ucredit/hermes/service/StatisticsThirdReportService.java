/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.csvreader.CsvReader;
import com.ucredit.hermes.dao.CompanyInfoDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.PengYuanReportRecordDAO;
import com.ucredit.hermes.dao.SubReportInfoDAO;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.pengyuan.PengYuanReportRecord;
import com.ucredit.hermes.model.pengyuan.SubReportInfo;
import com.ucredit.hermes.utils.POIUtils;

/**
 * @author caoming
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class StatisticsThirdReportService {
    private final static String EDU_SUB_REPORT_ID = "11100";
    private static Logger logger = LoggerFactory
        .getLogger(StatisticsThirdReportService.class);
    @Autowired
    private CompanyInfoDAO companyInfoDAO;
    @Autowired
    private SubReportInfoDAO subReportInfoDAO;
    @Autowired
    private LogInfoDAO logInfoDAO;
    @Autowired
    private PengYuanReportRecordDAO pengYuanReportRecordDAO;

    public XSSFWorkbook getStatisticsReport(MultipartFile file, Date startDate,
            Date endDate) throws UnsupportedEncodingException, IOException,
            ParseException {
        //新建一个xssfWorkbook对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet("统计");
        POIUtils.buildRowWithStringValues(sheet, 0, 0, "报告编号", "报告批次号", "查询时间",
            "查询条件", "企业报告信息（第三方）", "企业报告信息（hermes）");

        //读取csv文件中的数据
        CsvReader parse = new CsvReader(file.getInputStream(), ',',
            Charset.forName("GBK"));

        int rowNow = 1;
        int sameCount = 0;
        int notSameCount = 0;
        int dateError = 0;
        int skipindex = 4;
        int readIndex = 0;
        while (parse.readRecord()) {
            readIndex++;
            if (readIndex <= skipindex) {
                continue;
            }
            String[] values = parse.getValues();

            //报告编号
            String reportNumber = values[0];

            //报告批次号
            String reportBatchNumber = values[1];
            //查询时间
            String searchDate = values[7];

            //查询条件 eg:CORPNAME=郑州古游文化传播有限公司
            String companyName = StringUtils.substringAfter(values[4], "=");
            //查询报告
            String report = values[6];
            if (!report.contains("企业信用报告")) {
                continue;
            }
            //查询条数（互联网模式） eg:企业信用报告(1)|企业对外投资信息(0)|电话地址报告(1)|法定代表人在其他机构股权投资信息(0)|法定代表人在其他机构担任高管信息(0)
            //专线：企业法院被执行信息(0)|企业对外投资信息(0)|企业电话地址报告(1)|法定代表人在其他机构股权投资信息(1)|法定代表人在其他机构担任高管信息(1)|企业信用报告(1)
            String[] counts = StringUtils.split(report, '|');
            int thirdNumber = 0;
            for (String value : counts) {
                if (value.contains("企业信用报告")) {
                    thirdNumber = Integer.parseInt(StringUtils
                        .substringBetween(value, "(", ")"));
                    break;
                }
            }
            if (thirdNumber == 0) {
                continue;
            }
            Date realSearchDate = DateUtils.parseDate(searchDate,
                "yyyyMMdd HH:mm:ss");
            if (realSearchDate.getTime() < startDate.getTime()
                || realSearchDate.getTime() > endDate.getTime()) {
                POIUtils.buildRowWithValues(sheet, rowNow, 0, reportNumber,
                    reportBatchNumber, searchDate, companyName, ""
                        + thirdNumber, "时间错误");
                dateError++;
                rowNow++;
                continue;
            }
            String[] searchDateSplitS = searchDate.split(" ");
            String beginDateString = searchDate.replace(searchDateSplitS[1],
                "00:00:00");
            String endDateString = searchDate.replace(searchDateSplitS[1],
                "23:59:59");

            Date startDate_ = DateUtils.parseDate(beginDateString,
                new String[] { "yyyyMMdd HH:mm:ss" });
            Date endDate_ = DateUtils.parseDate(endDateString,
                new String[] { "yyyyMMdd HH:mm:ss" });

            //数据库查询次数
            Integer number = this.companyInfoDAO.getCompanyByName2Count(
                companyName, startDate_, DateUtils.addDays(endDate_, 1))
                .intValue();

            POIUtils.buildRowWithValues(sheet, rowNow, 0, reportNumber,
                reportBatchNumber, searchDate, companyName, "" + thirdNumber,
                number != 0 ? "有查" : "没有查");
            if (number != 0) {
                sameCount++;
            } else {
                notSameCount++;
            }
            rowNow++;
        }

        //统计写入最后一行
        String dateErrorString = "";
        if (dateError > 0) {
            dateErrorString = String.format("日期不在范围内：%d", dateError);
        }
        POIUtils.buildRowWithValues(sheet, rowNow, 0, "合计", "", "",
            dateErrorString, String.format("相同次数：%d", sameCount),
            String.format("不相同次数：%d", notSameCount));
        return xssfWorkbook;
    }

    /**
     * @param begin
     * @param end
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> doReportFor201412(Date begin, Date end) {
        /*
         * 查询所有企业查询信息
         */
        List<Map<String, Object>> statisResultList = this.companyInfoDAO
            .getReportAfter201412(begin, end);
        StatisticsThirdReportService.logger.info("statisResultList size: "
            + statisResultList.size());
        /*
         * 查询所有子报告
         */
        List<SubReportInfo> subReportInfos = this.subReportInfoDAO
            .getAllEntities();
        StatisticsThirdReportService.logger.info("subReportInfos size: "
            + subReportInfos.size());

        Map<String, String> reportMap = new HashMap<>();
        for (SubReportInfo subReportInfo : subReportInfos) {
            reportMap.put(subReportInfo.getCode(), subReportInfo.getName());
        }

        Map<String, Integer> xingyongbaogaoTimes = new HashMap<>();
        Map<String, Integer> dianhuaTimes = new HashMap<>();
        Map<String, Integer> putongTimes = new HashMap<>();
        Set<String> companyCreditReportID = new HashSet<>();
        Set<String> companyAddressReportID = new HashSet<>();

        /*
         * 查询时间段内查询学历报告次数
         */
        List<PengYuanReportRecord> educationRecords = this.pengYuanReportRecordDAO
            .getReportForForEducationsBetweenDaysCount(begin, end,
                StatisticsThirdReportService.EDU_SUB_REPORT_ID);

        for (Map<String, Object> item : statisResultList) {
            String subReportCode = (String) item.get("subReportCode");
            String mainId = (String) item.get("mainId");

            //企业股东信息 22101，企业高管信息22102,企业基本信息:21301
            switch (subReportCode) {
                case "22101":
                case "22102":
                case "21301":
                    if (xingyongbaogaoTimes.get(mainId) == null) {
                        companyCreditReportID.add(String.valueOf(item
                            .get("pengYuanRePortId")));
                    }
                    break;
                case "21611":
                case "21612":
                    /*
                     * 全国企业注册地址及电话信息 21611
                     * 全国企业经营地址及电话信息 21612
                     */
                    if (dianhuaTimes.get(mainId) == null) {
                        companyAddressReportID.add(String.valueOf(item
                            .get("pengYuanRePortId")));
                    }
                    break;
                default:
                    Integer times = null;
                    if (putongTimes.get(subReportCode) == null) {
                        putongTimes.put(subReportCode, 0);
                    }
                    times = putongTimes.get(subReportCode);
                    times++;
                    putongTimes.put(subReportCode, times);
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> set = putongTimes.keySet();
        for (String key : set) {
            String name = reportMap.get(key);
            Map<String, Object> data = new HashMap<>();
            if (StringUtils.isBlank(name)) {
                name = key;
            }
            data.put("name", name);
            data.put("queryTimes", putongTimes.get(key));
            result.add(data);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("name", "企业信用报告");
        data.put("queryTimes", companyCreditReportID.size());
        result.add(data);

        Map<String, Object> data2 = new HashMap<>();
        data2.put("name", "企业电话地址报告");
        data2.put("queryTimes", companyAddressReportID.size());
        result.add(data2);

        Map<String, Object> educationMap = new HashMap<>();
        educationMap.put("name", "高等教育学历信息");
        educationMap.put("subReportCode",
            StatisticsThirdReportService.EDU_SUB_REPORT_ID);
        educationMap.put("queryTimes", educationRecords.size());
        result.add(educationMap);

        return result;
    }

    /**
     * 2014年11月份的详单
     *
     * @param startDate_
     * @param endDate_
     * @return List<Map<String, Object>>
     */
//    public List<Map<String, Object>> doReportFor201411(Date startDate_,
//        Date endDate_) {
//        List<Map<String, Object>> resultList = this.hermesCountDAO
//                .statisticsBetweenDays(startDate_, endDate_);
//        BigInteger companyNewAdds = this.companyInfoDAO
//                .getCompanyNewAddsBetweenDays(startDate_, endDate_);
//        boolean containBase = false;
//        for (Map<String, Object> item : resultList) {
//            String type = (String) item.get("type");
//            switch (type) {
//                case "COMPANY_INFOS":
//                    item.put("key", "企业基本信息");
//                    item.put("searchcount", ((BigInteger) item
//                        .get("searchcount")).add(companyNewAdds));
//                    containBase = true;
//                    break;
//                case "COMPANY_SHAREHOLDER_INFOS":
//                    item.put("key", "企业股东信息");
//                    break;
//                case "COMPANY_MANAGER_INFOS":
//                    item.put("key", "企业高管信息");
//                    break;
//                case "COMPANY_OTHER_SHAREHOLDER_INFOS":
//                    item.put("key", "企业对外投资信息");
//                    break;
//                case "LEGAL_OTHERMANAGER_INFOS":
//                    item.put("key", "法定代表人在其他机构任职信息");
//                    break;
//                case "LEGAL_OTHER_SHAREHOLDER_INFOS":
//                    item.put("key", "法定代表人股权投资信息");
//                    break;
//                case "COMPANY_CONTACT_OPERATE_INFOS":
//                    item.put("key", "企业注册和经营地址及电话信息");
//                    break;
//                case "COMPANY_COURT_INFOS":
//                    item.put("key", "企业法院被执行信息");
//                    break;
//                case "POLICE_RESIDENCE_INFOS":
//                    item.put("key", "个人户籍信息");
//                    break;
//                case "EDUCATION_INFOS":
//                    item.put("key", "个人学历信息");
//                    break;
//                case "EDUCATION_IN_SCHOOL_INFOS":
//                    item.put("key", "个人学籍信息");
//                    break;
//                case "GD_SI_PERSON_INFOS":
//                    item.put("key", "个人社保信息");
//                    break;
//
//            }
//        }
//        if (!containBase && companyNewAdds.compareTo(BigInteger.ZERO) > 0) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("key", "企业基本信息");
//            item.put("searchcount", companyNewAdds);
//            resultList.add(item);
//        }
//        return resultList;
//    }

    /**
     * 查询参数时间段内的查询明细
     *
     * @param startDate_
     * @param endDate_
     * @return HSSFWorkbook
     */
    public HSSFWorkbook getSearchDetailBetweenDays(Date startDate_,
            Date endDate_) {
        List<Map<String, String>> searchSuccessRefIds = this.companyInfoDAO
            .getSearchDetailBetweenDays(startDate_, endDate_,
                ResultType.SUCCESS);

        List<Map<String, String>> searchFailureRefIds = this.companyInfoDAO
            .getSearchDetailBetweenDays(startDate_, endDate_,
                ResultType.FAILURE);

        HSSFWorkbook workbook = null;

        // 创建工作簿实例
        workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("nsrList");
        HSSFCellStyle style = StatisticsThirdReportService
            .createTitleStyle(workbook);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);

        HSSFRow firstRow = sheet.createRow((short) 0);// 建立新行
        StatisticsThirdReportService.createCell(firstRow, 0, style,
            Cell.CELL_TYPE_STRING, "查到结果的refId");
        StatisticsThirdReportService.createCell(firstRow, 1, style,
            Cell.CELL_TYPE_STRING, "没有查到结果的refId");

        /**
         * 查询时间段内查询学历报告次数
         */
        List<PengYuanReportRecord> educationRecords = this.pengYuanReportRecordDAO
            .getReportForForEducationsBetweenDaysCount(startDate_, endDate_,
                StatisticsThirdReportService.EDU_SUB_REPORT_ID);

        int successLength = searchSuccessRefIds.size();
        int failedLength = searchFailureRefIds.size();
        int max = successLength >= failedLength ? successLength : failedLength;

        int newRowNumber = 0;
        for (int i = 0; i < max; i++) {
            newRowNumber = i + 1;
            HSSFRow newRow = sheet.createRow(newRowNumber);// 建立新行

            if (i < successLength) {
                Map<String, String> successMap = searchSuccessRefIds.get(i);
                StatisticsThirdReportService.createCell(newRow, 0, style,
                    Cell.CELL_TYPE_STRING, successMap.get("refId"));
            }
            if (i < failedLength) {
                Map<String, String> failureMap = searchFailureRefIds.get(i);
                StatisticsThirdReportService.createCell(newRow, 1, style,
                    Cell.CELL_TYPE_STRING, failureMap.get("refId"));
            }
        }
        int size = educationRecords.size();
        for (int i = 0; i < size; i++) {
            newRowNumber++;
            HSSFRow newRow = sheet.createRow(newRowNumber);
            //查到结果的我司的流水号
            StatisticsThirdReportService.createCell(newRow, 0, style,
                Cell.CELL_TYPE_STRING, educationRecords.get(i).getMainId());
        }

        return workbook;
    }

    private static HSSFCellStyle createTitleStyle(HSSFWorkbook wb) {
        HSSFFont boldFont = wb.createFont();
        //boldFont.setColor(Font.COLOR_RED);
        boldFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        boldFont.setFontHeight((short) 200);
        HSSFCellStyle style = wb.createCellStyle();
        style.setFont(boldFont);
        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("###,##0.00"));
        return style;
    }

    private static void createCell(HSSFRow row, int column,
            HSSFCellStyle style, int cellType, Object value) {
        HSSFCell cell = row.createCell(column);
        if (style != null) {
            cell.setCellStyle(style);
        }
        switch (cellType) {
            case Cell.CELL_TYPE_STRING:
                cell.setCellValue(value.toString());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                // DecimalFormat format = new DecimalFormat("###,##0.00");
                // cell.setCellValue(Float.parseFloat(value.toString()));
                cell.setCellValue(Double.parseDouble(value.toString()));
                break;
            default:
                break;
        }
    }

    /**
     * 查询从开始时间到结束时间访问鹏元系统的次数
     * 此方法仅供2014年11月份明细统计。从logInfo中分析
     * add by xubaoyong
     *
     * @param startDate_
     * @param endDate_
     * @return Map<String, Object>
     * @throws DocumentException
     */
    public Map<String, Object> doReport(Date startDate_, Date endDate_)
            throws DocumentException {
        Date startDateBeforeOneDay = DateUtils.addDays(startDate_, -1);

        String startDateBeforeOneDayString = DateFormat.getDateInstance(
            DateFormat.DEFAULT).format(startDateBeforeOneDay);

        List<Map<String, Object>> logInfos = this.logInfoDAO
            .getLogInfosSearchFromPentyuanBetweenDays(startDateBeforeOneDay,
                endDate_);
        Map<String, Object> retdataMap = new HashMap<>();
        String dbRowData = null;
        Map<String, Date> searchDatemap = new HashMap<>();
        Map<String, Boolean> xinyongBaoGao2LogMap = new HashMap<>();
        Map<String, Boolean> dianhuaBaoGao2LogMap = new HashMap<>();
        Integer logId = null;
        /**
         * 打包统计的Map
         */
        Map<String, Integer> dabaoTongjiObj = new HashMap<>();
        for (Map<String, Object> logInfo : logInfos) {
            dbRowData = (String) logInfo.get("data");
            if (StringUtils.isBlank(dbRowData)) {
                continue;
            }
            logId = (Integer) logInfo.get("id");
            Document document = DocumentHelper.parseText(dbRowData);
            Element root = document.getRootElement();
            List<Element> cisReportList = root.elements("cisReport");
            //系统中默认cisReportList只有一个节点
            Element cisReport = cisReportList.get(0);

            Iterator<Element> it = cisReport.elementIterator();

            Element queryConditions = cisReport.element("queryConditions");

            List<Element> items = queryConditions.elements("item");
            String condtion = "";
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    Element item = items.get(i);
                    String corpName = item.element("name").getText();
                    String value = item.element("value").getText();
                    if (condtion.length() > 0) {
                        condtion += "@";
                    }
                    condtion += corpName + "_" + value;
                }
            }
            Date searchDate = (Date) logInfo.get("createTime");
            Date condtionTime = searchDatemap.get(condtion);
            if (condtionTime == null) {
                searchDatemap.put(condtion, searchDate);
            } else {
                //判断是否24小时内查询
                Date after24HourIs = DateUtils.addHours(condtionTime, 24);
                //如果24小时外，则重新设置查询时间为新的时间，否则跳出不计算
                if (searchDate.after(after24HourIs)) {
                    searchDatemap.put(condtion, searchDate);
                } else {
                    continue;
                }
            }
            /**
             * 如果查询日期是开始前一天的话则不计算
             */
            String searchDateString = DateFormat.getDateInstance(
                DateFormat.DEFAULT).format(searchDate);
            if (searchDateString.equals(startDateBeforeOneDayString)) {
                continue;
            }

            while (it.hasNext()) {
                Element subElement = it.next();
                String nodeName = subElement.getName();
                /**
                 * 查询条件不使用
                 */
                if ("queryConditions".equals(nodeName)) {
                    continue;
                }

                if (CollectionUtils.isEmpty(subElement.elements("item"))) {
                    continue;
                }
                /**
                 * 统计查询次数
                 */
                StatisticsThirdReportService.setRetData(logId, retdataMap,
                    nodeName, xinyongBaoGao2LogMap, dianhuaBaoGao2LogMap,
                    dabaoTongjiObj);
                subElement = null;
                nodeName = null;
            }
            root = null;
            cisReportList = null;
            cisReport = null;
        }

        Map<String, Object> transMap = new HashMap<>();
        for (String key : retdataMap.keySet()) {
            switch (key) {
                case "policeCheckInfo":
                    transMap.put("个人身份认证信息", retdataMap.get(key));
                    break;
                case "artificialNationalInfo":
                    transMap.put("个人担任法定代表人信息", retdataMap.get(key));
                    break;
                case "nationalPersonShareholderReport":
                    transMap.put("个人股权投资信息", retdataMap.get(key));
                    break;
                case "personTopManagerInfo":
                    transMap.put("个人担任高管信息", retdataMap.get(key));
                    break;
                case "corpBaseNationalInfo":
                    transMap.put("企业基本信息", retdataMap.get(key));
                    break;
                case "registerContactInfos":
                    transMap.put("企业注册地址及电话信息", retdataMap.get(key));
                    break;
                case "manageContactInfos":
                    transMap.put("企业经营地址及电话信息", retdataMap.get(key));
                    break;
                case "nationalCorpShareholderInfo":
                    transMap.put("企业股东信息", retdataMap.get(key));
                    break;
                case "corpTopManagerInfo":
                    transMap.put("企业高管信息", retdataMap.get(key));
                    break;
                case "nationalCorpOtherShareholderInfo":
                    transMap.put("企业对外股权投资信息", retdataMap.get(key));
                    break;
                case "frOtherCorpTopManagerInfo":
                    transMap.put("法定代表人在其他机构任职信息", retdataMap.get(key));
                    break;
                case "nationalFrOtherCorpShareholderInfo":
                    transMap.put("法定代表人股权投资信息", retdataMap.get(key));
                    break;
                case "telCheckInfo":
                    transMap.put("固定电话反查信息", retdataMap.get(key));
                    break;
                case "MoblieCheckInfo":
                    transMap.put("手机号码反查信息", retdataMap.get(key));
                    break;
                case "courtInfos":
                    transMap.put("企业和个人法院被执行信息之和", retdataMap.get(key));
                    break;
                case "personBaseInfo":
                    transMap.put("个人基本信息", retdataMap.get(key));
                    break;
                case "ostaInfo":
                    transMap.put("职业资格信息", retdataMap.get(key));
                    break;
                case "policeInfo":
                    transMap.put("公安户籍信息", retdataMap.get(key));
                    break;
                case "policeResidenceInfo":
                    transMap.put("公安个人户籍详细信息", retdataMap.get(key));
                    break;
                case "identityVerifyInfo":
                    transMap.put("身份证号码校验信息", retdataMap.get(key));
                    break;
                case "educationInfo2":
                    transMap.put("全国高等教育学历信息", retdataMap.get(key));
                    break;
                case "lastEducationInfo":
                    transMap.put("全国高等教育最高学历信息", retdataMap.get(key));
                    break;
                case "educationInSchool2":
                    transMap.put("全国高等教育学籍信息", retdataMap.get(key));
                    break;
                case "educationCheck":
                    transMap.put("全国高等教育学历核查", retdataMap.get(key));
                    break;
                case "educationInSchoolCheck":
                    transMap.put("全国高等教育学籍核查", retdataMap.get(key));
                    break;

            }
        }
        transMap.put("企业信用报告(企业基本信息、企业高管信息、企业股东信息打包计费打包计算)",
            dabaoTongjiObj.get("xinYongBaoGao"));
        transMap.put("地址及电话信息(企业经营地址及电话信息、企业注册地址及电话信息打包计算)",
            dabaoTongjiObj.get("dianHuaBaoGao"));
        return transMap;
    }

    /**
     * 工具方法
     */
    private static void setRetData(Integer logId, Map<String, Object> retdata,
            String key, Map<String, Boolean> xinyongBaoGao2LogMap,
            Map<String, Boolean> dianhuaBaoGao2LogMap,
            Map<String, Integer> dabaoTongjiObj) {
        String xingyongbaogaokey = "xinYongBaoGao";
        String dianHuaBaoGao = "dianHuaBaoGao";
        if ("corpBaseNationalInfo".equals(key)
            || "corpTopManagerInfo".equals(key)
            || "nationalCorpShareholderInfo".equals(key)) {
            /**
             * 企业基本信息+企业高管信息+企业股东信息打包计费为企业信用报告
             */
            if (xinyongBaoGao2LogMap.get(logId.toString()) == null) {
                //没有统计过则算一次
                Integer xinyongbaogao = dabaoTongjiObj.get(xingyongbaogaokey);
                if (xinyongbaogao == null) {
                    xinyongbaogao = 0;
                }
                xinyongbaogao++;
                dabaoTongjiObj.put(xingyongbaogaokey, xinyongbaogao);
                xinyongBaoGao2LogMap.put(logId.toString(), true);
            }
        } else if ("manageContactInfos".equals(key)
            || "registerContactInfos".equals(key)) {
            /**
             * "企业经营地址及电话信息"、"企业注册地址及电话信息"打包计费
             */
            if (dianhuaBaoGao2LogMap.get(logId.toString()) == null) {
                //没有统计过则算一次
                Integer dianhuabaogao = dabaoTongjiObj.get(dianHuaBaoGao);
                if (dianhuabaogao == null) {
                    dianhuabaogao = 0;
                }
                dianhuabaogao++;
                dabaoTongjiObj.put(dianHuaBaoGao, dianhuabaogao);
                dianhuaBaoGao2LogMap.put(logId.toString(), true);
            }
        }

        Integer searchTime = (Integer) retdata.get(key);
        if (searchTime == null) {
            searchTime = 0;
        }
        searchTime++;
        retdata.put(key, searchTime);
    }

    public HSSFWorkbook getSearchDetailBetweenDaysFor201411(Date startDate_,
            Date endDate_) throws DocumentException {

        // 创建工作簿实例
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("nsrList");
        HSSFCellStyle style = StatisticsThirdReportService
            .createTitleStyle(workbook);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);

        HSSFRow firstRow = sheet.createRow((short) 0);// 建立新行
        StatisticsThirdReportService.createCell(firstRow, 0, style,
            Cell.CELL_TYPE_STRING, "查到结果的refId(没有排重)");
        StatisticsThirdReportService.createCell(firstRow, 1, style,
            Cell.CELL_TYPE_STRING, "没有查到结果的refId(没有排重)");

        Date startDateBeforeOneDay = DateUtils.addDays(startDate_, -1);

        String startDateBeforeOneDayString = DateFormat.getDateInstance(
            DateFormat.DEFAULT).format(startDateBeforeOneDay);

        List<Map<String, Object>> logInfos = this.logInfoDAO
            .getLogInfosSearchFromPentyuanBetweenDays(startDateBeforeOneDay,
                endDate_);
        String dbRowData = null;

        int i = 1;
        for (Map<String, Object> logInfo : logInfos) {
            dbRowData = (String) logInfo.get("data");
            if (StringUtils.isBlank(dbRowData)) {
                continue;
            }

            String searchDateString = DateFormat.getDateInstance(
                DateFormat.DEFAULT).format((Date) logInfo.get("createTime"));
            if (searchDateString.equals(startDateBeforeOneDayString)) {
                searchDateString = null;
                continue;
            }
            Document document = DocumentHelper.parseText(dbRowData);
            Element root = document.getRootElement();
            List<Element> cisReportList = root.elements("cisReport");
            //系统中默认cisReportList只有一个节点
            Element cisReport = cisReportList.get(0);
            String refID = cisReport.attributeValue("refID");
            HSSFRow newRow = sheet.createRow(i++);// 建立新行
            StatisticsThirdReportService.createCell(newRow, 0, style,
                Cell.CELL_TYPE_STRING, refID);

            document = null;
            root = null;
            cisReportList = null;
            cisReport = null;

        }
        return workbook;
    }

//    public static void main(String[] args) {
//        String phone = "15555555555";
//        phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
//        System.out.println(phone);
//
//        String da = "20140724 13:51:43";
//        String[] ars = da.split(" ");
//        String de = da.replace(ars[1], "00:00:00");
//        System.out.println(de);
//    }

    public Integer do201412EducationReport(Date startDate_, Date endDate_) {
        List<LogInfo> loginfos = this.logInfoDAO
            .getLogInfosBetweenDaysForTypes(startDate_, endDate_,
                "education_infos", DataChannel.PENGYUAN);
        return loginfos.size();
    }
}
