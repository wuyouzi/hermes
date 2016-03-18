package com.ucredit.hermes.web.rest.qianhai;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.service.qianhai.QianhaiBlackService;

@Controller
@RequestMapping("/rest/qianhai")
public class QianhaiBlackRESTController {
    @Autowired
    private QianhaiBlackService qianhaiBlackService;

    /**
     * 查询前海征信
     *
     * @param idNo
     * @param idType
     * @param name
     * @param reasonCode
     * @param apply_id
     * @param principal
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/blacklist", method = RequestMethod.GET)
    public Map<String, Object> searchBlackList(
            @RequestParam(required = false) String idNo, @RequestParam(
                    defaultValue = "0", required = false) String idType,
            @RequestParam(required = false) String name, @RequestParam(
                    defaultValue = "01", required = false) String reasonCode,
            @RequestParam(required = false) String apply_id, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        if (idNo == null || "".equals(idNo)) {
            result.put("msg", "证件号不能为空！");
            return result;
        }
        if (idType == null || "".equals(idType)) {
            result.put("msg", "证件类型不能为空！");
            return result;
        }
        if (name == null || "".equals(name)) {
            result.put("msg", "姓名不能为空！");
            return result;
        }
        return this.qianhaiBlackService.searchBlackList(idNo, idType, name,
            reasonCode, apply_id, principal);
    }

    /**
     * 查询本地已存在
     *
     * @param idNo
     * @param idType
     * @param name
     * @param apply_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getblacklist", method = RequestMethod.GET)
    public Map<String, Object> getBlackList(
            @RequestParam(required = false) String idNo, @RequestParam(
                    defaultValue = "0", required = false) String idType,
            @RequestParam(required = false) String name, @RequestParam(
                    required = false) String apply_id) {
        Map<String, Object> result = new HashMap<>();
        if (idNo == null || "".equals(idNo)) {
            result.put("msg", "证件号不能为空！");
            return result;
        }
        if (idType == null || "".equals(idType)) {
            result.put("msg", "证件类型不能为空！");
            return result;
        }
        if (name == null || "".equals(name)) {
            result.put("msg", "姓名不能为空！");
            return result;
        }
        return this.qianhaiBlackService.getBlackList(idNo, idType, name,
            apply_id);
    }

}
