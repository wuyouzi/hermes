package com.ucredit.hermes.web.rest.yinlianzh;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.model.yinlianzh.YinlianzhMerchantBillBaseInfo;
import com.ucredit.hermes.service.yinlianzh.MerchantBillService;

@Controller
@RequestMapping("/rest/yinlianzh")
public class MerchantBillRESTController {
    @Autowired
    private MerchantBillService merchantBillService;

    /**
     * @param type
     *        是否返回账单明细 0不返回，1返回
     * @param category
     *        商户账单多个mid查询使用 1：按账户编号mid查询，输入时mid必填；2,：按准确的商户名称查询，输入时mName必填
     * @param mid
     *        商户编号
     * @param mName
     *        商户准确名称
     * @param posId
     *        终端编号，可以多个用英文逗号隔开
     * @param businessCode
     *        营业执照注册编号
     * @param name
     *        商户负责人姓名
     * @param mobile
     *        商户负责人手机号码
     * @param cid
     *        商户负责人身份证号码
     * @param beginDate
     *        账单查询开始日期 格式yyyy-mm-dd
     * @param endDate
     *        账单查询结束日期 格式yyyy-mm-dd
     * @param constraint
     *        是否强制刷新
     * @param activeDays
     *        有效期
     * @param principal
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/queryBill", method = RequestMethod.GET)
    public YinlianzhMerchantBillBaseInfo getBillInfo(
            @RequestParam(defaultValue = "1", required = false) Integer type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String mid,
            @RequestParam(required = false) String mName,
            @RequestParam(required = false) String posId,
            @RequestParam(required = false) String businessCode,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String mobile,
            @RequestParam(required = false) String cid,
            @RequestParam(required = true) String beginDate,
            @RequestParam(required = true) String endDate,
            @RequestParam(required = false) String lendRequestId,
            @RequestParam(defaultValue = "false", required = false) boolean constraint,
            @RequestParam(defaultValue = "30", required = false) int activeDays,
            Principal principal) throws ParseException {
        return this.merchantBillService.getMerchantBillBaseInfo(type, category,
            mid, mName, posId, businessCode, name, mobile, cid, beginDate,
            endDate, lendRequestId, constraint, activeDays, principal);
    }

    /**
     * @param uuid
     *        下载唯一标示
     * @param resp
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/downLoad", method = RequestMethod.GET)
    public Map<String, Object> downLoad(
        @RequestParam(required = true) String uuid, HttpServletResponse resp)
                throws Exception {
        return this.merchantBillService.downLoad(uuid, resp);

    }

}
