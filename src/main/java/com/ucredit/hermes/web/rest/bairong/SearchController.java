package com.ucredit.hermes.web.rest.bairong;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ucredit.hermes.dto.BaiRongResult;
import com.ucredit.hermes.enums.BaiRongResultCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.BaiRongParams;
import com.ucredit.hermes.service.BaiRongService;

@Controller
@RequestMapping("/rest/bairong")
public class SearchController {

    @Autowired
    public BaiRongService baiRongService;

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public BaiRongResult getById(@PathVariable Integer id) {
        try {
            return this.baiRongService.getBaiRongResultById(id);
        } catch (Exception e) {
            BaiRongResult result = new BaiRongResult();
            result.setResultType(ResultType.FAILURE);
            result.setResultCode(BaiRongResultCode.YouXin_DB_ERROR);
            result.setResultCodeDesc(BaiRongResultCode.YouXin_DB_ERROR
                .getString());
            return result;
        }

    }

    @RequestMapping(value = "/getByKeyid/{keyid}", method = RequestMethod.GET)
    @ResponseBody
    public BaiRongResult getByKeyid(@PathVariable String keyid) {
        try {
            return this.baiRongService.getBaiRongResultByKeyId(keyid);
        } catch (Exception e) {
            BaiRongResult result = new BaiRongResult();
            result.setResultType(ResultType.FAILURE);
            result.setResultCode(BaiRongResultCode.YouXin_DB_ERROR);
            result.setResultCodeDesc(BaiRongResultCode.YouXin_DB_ERROR
                .getString());
            return result;
        }

    }

    /**
     * 百融查询功能
     *
     * @return String
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String search_page() {
        return "bairong_search";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public BaiRongResult dosearch(BaiRongParams baiRongParams,
            Principal principal) {
        BaiRongResult result = null;
        try {
            result = this.baiRongService.doSearch(baiRongParams, principal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public BaiRongResult dosearch1(BaiRongParams baiRongParams,
            Principal principal) {
        BaiRongResult result = null;
        try {
            result = this.baiRongService.doSearch(baiRongParams, principal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 百融公司统计页面
     *
     * @return String
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public String statistics() {
        return "bairong_statistics";
    }

    /**
     * 统计时间段内的查询百融公司数据的次数
     *
     * @param beginDateString
     * @param endDateString
     * @return ModelAndView
     * @throws Exception
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.POST)
    public ModelAndView statistics(
            @RequestParam(defaultValue = "") String beginDateString,
            @RequestParam(defaultValue = "") String endDateString)
                    throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("bairong_statistics");
        mv.addObject("beginDateString", beginDateString);
        mv.addObject("endDateString", endDateString);
        if (StringUtils.isNotBlank(beginDateString)
                && StringUtils.isNotBlank(endDateString)) {
            String NbeginDate = beginDateString + " 00:00:00";
            Date startDate_ = DateUtils.parseDate(NbeginDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });

            String NendDate = endDateString + " 00:00:00";
            Date endDate_ = DateUtils.parseDate(NendDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });
            endDate_ = DateUtils.addDays(endDate_, 1);
            Long times = this.baiRongService.doStatistics(startDate_, endDate_);

            mv.addObject("times", times);
        }
        return mv;
    }

    /**
     * 根据百融公司给的流水进行对帐比较
     *
     * @param file
     * @param beginDateString
     * @param endDateString
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/statisticsexport", method = RequestMethod.POST)
    public void statisticsexport(@RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "") String beginDateString,
            @RequestParam(defaultValue = "") String endDateString,
            HttpServletResponse response) throws Exception {

        if (StringUtils.isNotBlank(beginDateString)
                && StringUtils.isNotBlank(endDateString) && file != null
                && !file.isEmpty()) {
            String NbeginDate = beginDateString + " 00:00:00";
            Date startDate_ = DateUtils.parseDate(NbeginDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });

            String NendDate = endDateString + " 00:00:00";
            Date endDate_ = DateUtils.parseDate(NendDate,
                new String[] { "yyyy-MM-dd HH:mm:ss" });
            endDate_ = DateUtils.addDays(endDate_, 1);
            HSSFWorkbook workbook = this.baiRongService.doRtatisticsexport(
                startDate_, endDate_, file);

            response.setHeader(
                "Content-Disposition",
                "attachment;filename=\""
                        + String.format("bairong_%s-%s百融查询明细.xls", beginDateString,
                            endDateString) + "\"");
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();

        }

    }
}
