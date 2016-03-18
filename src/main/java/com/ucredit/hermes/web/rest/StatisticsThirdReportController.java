/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ucredit.hermes.service.StatisticsThirdReportService;

/**
 * @author caoming
 */
@Controller
@RequestMapping("/rest/statistics")
public class StatisticsThirdReportController {
    private static Logger logger = LoggerFactory
        .getLogger(StatisticsThirdReportController.class);
    @Autowired
    private StatisticsThirdReportService statisticsThirdReportService;

    /**
     * 获取报表
     *
     * @param file
     * @param date1
     * @param date2
     * @param response
     * @throws ParseException
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    @ResponseBody
    public void getStatisticsReport(@RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "") String date1, @RequestParam(
                    defaultValue = "") String date2,
            HttpServletResponse response) throws ParseException,
            UnsupportedEncodingException, IOException {
        if (StringUtils.isNotBlank(date1) && StringUtils.isNotBlank(date2)) {
            Date startDate = DateUtils.parseDate(date1 + " 00:00:00",
                new String[] { "yyyyMMdd HH:mm:ss" });
            Date endDate = DateUtils.parseDate(date2 + " 23:59:59",
                new String[] { "yyyyMMdd HH:mm:ss" });
            XSSFWorkbook xssfWorkbook = this.statisticsThirdReportService
                .getStatisticsReport(file, startDate, endDate);
            response.setHeader("Content-Disposition",
                "attachment; filename*=utf-8''" + "report.xlsx");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            xssfWorkbook.write(response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public String getStatisticsReport() {
        return "report";
    }

    /**
     * 统计2014年12月以后的报告
     *
     * @param beginDateString
     * @param endDateString
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/201412", method = RequestMethod.POST)
    public ModelAndView getReportAfter201412(
            @RequestParam(defaultValue = "") String beginDateString,
            @RequestParam(defaultValue = "") String endDateString)
            throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("report_201412");
        mv.addObject("beginDateString", beginDateString);
        mv.addObject("endDateString", endDateString);
        if (StringUtils.isNotBlank(beginDateString)
            && StringUtils.isNotBlank(endDateString)) {
            Date begin = DateUtils.parseDate(beginDateString,
                new String[] { "yyyy-MM-dd" });
            Date end = DateUtils.addDays(DateUtils.parseDate(endDateString,
                new String[] { "yyyy-MM-dd" }), 1);

            StatisticsThirdReportController.logger
                .info("Getting report between <" + begin + "> and <" + end
                    + ">");
            List<Map<String, Object>> data = this.statisticsThirdReportService
                .doReportFor201412(begin, end);

            StatisticsThirdReportController.logger.info("Data size: "
                + data.size());
            mv.addObject("data", data);
        }

        return mv;
    }

    /**
     * 导出下载明细
     *
     * @param beginDateString
     * @param endDateString
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/201412export", method = RequestMethod.POST)
    public void getReportFor201412Export(
            @RequestParam(defaultValue = "") String beginDateString,
            @RequestParam(defaultValue = "") String endDateString,
            HttpServletResponse response) throws Exception {
        if (StringUtils.isNotBlank(beginDateString)
            && StringUtils.isNotBlank(endDateString)) {
            String NbeginDate = beginDateString + " 00:00:00";
            Date startDate_ = DateUtils.parseDate(NbeginDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });

            String NendDate = endDateString + " 23:59:59";
            Date endDate_ = DateUtils.parseDate(NendDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });
            endDate_ = DateUtils.addDays(endDate_, 1);
            HSSFWorkbook workbook = this.statisticsThirdReportService
                .getSearchDetailBetweenDays(startDate_, endDate_);
            // 设定输出文件头
            response.setHeader(
                "Content-Disposition",
                "attachment;filename=\""
                    + String.format("%s-%s查询明细.xls", beginDateString,
                        endDateString) + "\"");

            response.setContentType("application/msexcel;charset=UTF-8");

            //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            workbook.write(response.getOutputStream());
        }
    }

    @RequestMapping(value = "/201412", method = RequestMethod.GET)
    public ModelAndView getReportFor201412() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("report_201412");
        return mv;
    }

    /**
     * 统计从开始时间到结束时间的查询次数
     *
     * @param beginDate
     * @param endDate
     * @return ModelAndView
     * @throws ParseException
     * @throws DocumentException
     */
    @RequestMapping(value = "/201411", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView getReport(
            @RequestParam(defaultValue = "") String beginDate, @RequestParam(
                    defaultValue = "") String endDate) throws ParseException,
            DocumentException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("report_201411");
        if (StringUtils.isNotBlank(beginDate)
            && StringUtils.isNotBlank(beginDate)) {
            String NbeginDate = beginDate + " 00:00:00";
            Date startDate_ = DateUtils.parseDate(NbeginDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });

            String NendDate = endDate + " 00:00:00";
            Date endDate_ = DateUtils.parseDate(NendDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });
            endDate_ = DateUtils.addDays(endDate_, 1);
            Map<String, Object> data = this.statisticsThirdReportService
                .doReport(startDate_, endDate_);

            mv.addObject("data", data);
        }
        mv.addObject("beginDate", beginDate);
        mv.addObject("endDate", endDate);
        return mv;
    }

    @RequestMapping(value = "/201411", method = RequestMethod.GET)
    public ModelAndView getReport() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("report_201411");
        return mv;
    }

    @RequestMapping(value = "/201411export", method = RequestMethod.POST)
    public void getReportFor201411Export(
            @RequestParam(defaultValue = "") String beginDateString,
            @RequestParam(defaultValue = "") String endDateString,
            HttpServletResponse response) throws Exception {
        if (StringUtils.isNotBlank(beginDateString)
            && StringUtils.isNotBlank(endDateString)) {
            String NbeginDate = beginDateString + " 00:00:00";
            Date startDate_ = DateUtils.parseDate(NbeginDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });

            String NendDate = endDateString + " 00:00:00";
            Date endDate_ = DateUtils.parseDate(NendDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });
            endDate_ = DateUtils.addDays(endDate_, 1);
            HSSFWorkbook workbook = this.statisticsThirdReportService
                .getSearchDetailBetweenDaysFor201411(startDate_, endDate_);
            // 设定输出文件头
            response.setHeader(
                "Content-Disposition",
                "attachment;filename=\""
                    + String.format("%s-%s查询明细.xls", beginDateString,
                        endDateString) + "\"");

            response.setContentType("application/msexcel;charset=UTF-8");

            //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            workbook.write(response.getOutputStream());
        }
    }

    @RequestMapping(value = "/201412education", method = RequestMethod.GET)
    public ModelAndView get201412educationReport() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("201412education");
        return mv;
    }

    @RequestMapping(value = "/201412education", method = RequestMethod.POST)
    public ModelAndView get201412educationReport(@RequestParam(
            defaultValue = "") String beginDateString, @RequestParam(
            defaultValue = "") String endDateString) throws ParseException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("201412education");
        if (StringUtils.isNotBlank(beginDateString)
            && StringUtils.isNotBlank(endDateString)) {
            String NbeginDate = endDateString + " 00:00:00";
            Date startDate_ = DateUtils.parseDate(NbeginDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });

            String NendDate = endDateString + " 23:59:59";
            Date endDate_ = DateUtils.parseDate(NendDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });
            endDate_ = DateUtils.addDays(endDate_, 1);
            Integer times = this.statisticsThirdReportService
                .do201412EducationReport(startDate_, endDate_);

            mv.addObject("data", times);
        }
        mv.addObject("beginDateString", beginDateString);
        mv.addObject("endDateString", endDateString);
        return mv;
    }

}
