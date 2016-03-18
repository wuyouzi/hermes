package com.ucredit.hermes.web.rest.crawl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.model.crawl.UndergraduateCourseInfo;
import com.ucredit.hermes.service.xuexin.CrawlService;

/**
 * 爬取平台--学历信息
 *
 * @author zhouwuyuan
 */
@Controller
@RequestMapping("/rest/chis")
public class ChisRESTController {
    @Autowired
    private CrawlService crawlService;

    /**
     * 学历信息的service
     *
     * @param idNo
     *        身份证号码
     * @return
     */
    @RequestMapping(value = "/getChis", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEducationInfo(
        @RequestParam(required = true) String idNo) {
        Map<String, Object> result = new HashMap<>();
        UndergraduateCourseInfo undergraduateCourseInfo = this.crawlService
                .getEducationInfo(idNo);
        result.put("msg", undergraduateCourseInfo);
        return result;
    }

}
