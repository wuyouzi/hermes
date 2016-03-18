package com.ucredit.hermes.web.rest.tongdun;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.model.tongdun.Condition;
import com.ucredit.hermes.service.tongdun.TongDunService;

/**
 * 同盾
 *
 * @author zhouwuyuan
 */
@Controller
@RequestMapping("/rest/tongdun")
public class TongDunRSETController {
    @Autowired
    private TongDunService tongDunService;

    /**
     * 风险决策服务
     *
     * @param type
     * @param condition
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/riskDecision", method = RequestMethod.GET)
    public Map<String, Object> riskDecisionService(Condition condition) {
        Map<String, Object> result = new HashMap<>();
        if (condition.getEvent_id() == null) {
            result.put(HermesConsts.MSG, "请传入事件类型event_id");
            return result;
        }
        /*
         * if (condition.getBlack_box() == null) {
         * result.put(HermesConsts.MSG, "请传入同盾SDK采集信息black_box");
         * return result;
         * }
         */
        return this.tongDunService.riskDecisionService(condition);
    }

}
