package com.ucredit.hermes.service;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.BlackListDAO;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.blacklist.BlackList;
import com.ucredit.hermes.model.blacklist.BlackListValueType;
import com.ucredit.hermes.utils.DateUtils;

/**
 * 黑名单service
 *
 * @author Liuhao
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class BlackListService {
    @Autowired
    private BlackListRESTTemplate blackListRESTTemplate;
    @Autowired
    private BlackListDAO blackListDAO;
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private Variables variables;

    /**
     * 本公司查询是否黑名单
     *
     * @param identityNo
     * @param phone
     * @param companyName
     * @param workTel
     * @param homeTel
     * @param lendRequestId
     * @param principal
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> queryFromUcredit(String identityNo,
            String phone, String companyName, String workTel, String homeTel,
            String lendRequestId, Principal principal) throws Exception {
        // 记录日志到数据库
        String username = ((Authentication) principal).getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
            .getDetails();
        LogInfo logInfo = new LogInfo(username, null,
            details.getRemoteAddress(), new Date(), "black_lists");
        logInfo.setQueryString("identityNo=" + identityNo + ",phone=" + phone
            + ",companyName=" + companyName + ",workTel=" + workTel
            + ",homeTel=" + homeTel);
        logInfo.setDataChannel(DataChannel.HERMES);
        logInfo.setResultType(ResultType.SUCCESS);
        logInfo.setEndTime(new Date());
        logInfo.setLastUpdateTime(new Date());
        // 将查询参数转换成map
        Map<BlackListValueType, String> params = BlackListService
            .buildQueryMap(identityNo, phone, companyName, workTel, homeTel);
        // 从安盛查询的返回值
        List<BlackList> blackListsFromAnsheng = this.queryOutter(params,
            lendRequestId);
        // 因为查询安盛是模糊查询，有可能返回的不是想要的黑名单，做一下判断。如果返回的黑名单value和参数中的不一致，则不是想要的黑名单，从列表中移除
        if (!blackListsFromAnsheng.isEmpty()) {
            Iterator<BlackList> it = blackListsFromAnsheng.iterator();
            while (it.hasNext()) {
                BlackList blackList = it.next();
                BlackListValueType type = blackList.getType();
                String value = blackList.getValue();
                if (!params.get(type).equals(value)) {
                    it.remove();
                }
            }
        }
        // 把安盛查出来的有效黑名单转换成小叶子需要的格式返回
        List<Map<String, String>> result = BlackListService
            .buildResult4Ucredit(blackListsFromAnsheng);
        logInfo.setData(StringUtils.join(result, ','));
        this.logInfoDao.addEntity(logInfo);
        return result;
    }

    /**
     * 外部公司查询是否黑名单
     *
     * @param queryParams
     * @param principal
     * @return
     */
    public List<Map<String, String>> queryFromOthers(JSONObject queryParams,
            Principal principal) {
        // 记录日志到数据库
        String username = ((Authentication) principal).getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
            .getDetails();
        LogInfo logInfo = new LogInfo(username, null,
            details.getRemoteAddress(), new Date(), "QUERY");
        // 如果查询参数为空,返回空map
        if (queryParams == null) {
            logInfo.setQueryString(null);
            logInfo.setData(null);
            logInfo.setResultType(ResultType.SUCCESS);
            logInfo.setEndTime(new Date());
            logInfo.setLastUpdateTime(new Date());
            this.logInfoDao.addEntity(logInfo);
            return Collections.EMPTY_LIST;
        }
        List<BlackList> blackLists = new ArrayList<>();
        // 如果查询参数不为空，将安盛的查询参数（JSON）转换成查询参数，安盛传过来的参数为list，与本公司查询不一样，做特殊处理
        Iterator<String> it = queryParams.keys();
        while (it.hasNext()) {
            String key = it.next();
            List<String> list = (List<String>) queryParams.get(key);
            switch (key) {
                case "identityNo":
                    for (String value : list) {
                        blackLists.addAll(this.queryByTypeAndValue(value,
                            BlackListValueType.ID_NO));
                    }
                    break;
                case "phone":
                    for (String value : list) {
                        blackLists.addAll(this.queryByTypeAndValue(value,
                            BlackListValueType.PHONE));
                    }
                    break;
                case "homeTel":
                    for (String value : list) {
                        blackLists.addAll(this.queryByTypeAndValue(value,
                            BlackListValueType.HOME_TELEPHONE));
                    }
                    break;
                case "workTel":
                case "companyName":
                case "name":
                    break;
            }
        }
        // 此方法的返回值，且只返回证件号，手机号，家庭电话三项数据（根据需求）
        List<Map<String, String>> result = new ArrayList<>();
        for (int i = 0; i < blackLists.size(); i++) {
            Map<String, String> map = new HashMap<>();
            BlackList blackList = blackLists.get(i);
            BlackListValueType blackListValueType = blackList.getType();
            String value = blackLists.get(i).getValue();
            String time = DateUtils.getFormatDateString(blackList
                .getEffectTime());
            map.put("time", time);
            switch (blackListValueType) {
                case ID_NO:
                    map.put("identityNo", value);
                    break;
                case PHONE:
                    map.put("phone", value);
                    break;
                case HOME_TELEPHONE:
                    map.put("homeTel", value);
                    break;
                case WORK_TELEPHONE:
                case COMPANY_NAME:
                case NAME:
                    break;
            }
            result.add(map);
        }
        // 记录日志到数据库
        logInfo.setQueryString(queryParams.toString());
        logInfo.setData(result.toString());
        logInfo.setDataChannel(DataChannel.HERMES);
        logInfo.setResultType(ResultType.SUCCESS);
        logInfo.setEndTime(new Date());
        logInfo.setLastUpdateTime(new Date());
        this.logInfoDao.addEntity(logInfo);
        return result;
    }

    /**
     * 是否属于外部系统黑名单
     *
     * @param params
     * @param lendRequestId
     * @return
     * @throws Exception
     */
    public List<BlackList> queryOutter(Map<BlackListValueType, String> params,
            String lendRequestId) throws Exception {
        // 组织参数向安盛发请求（url和json）
        String url = this.variables.getAnShengURL();
        JSONArray json = this.buildJson(params);
        // 发送http请求
        ResponseEntity<String> result = this.blackListRESTTemplate.postEntity(
            url, json);
        // 解析安盛的返回值
        List<BlackList> blackLists = this.buildBlackListsByAnShengResult(
            result, lendRequestId, params);
        // 如果返回的列表不为空，则是黑名单，返回并将黑名单存入hermes数据库
        if (!blackLists.isEmpty()) {
            for (BlackList blackList : blackLists) {
                // 如果已经有与这条相同的黑名单，就把原来的置为不可用状态
                BlackListValueType blackListValueType = blackList.getType();
                String value = blackList.getValue();
                BlackList blackListFromDB = this.blackListDAO
                    .getBlackListByTypeAndValue(blackListValueType, value);
                if (blackListFromDB != null) {
                    blackListFromDB.setEnabled(false);
                }
            }
        }
        // 入库
        this.blackListDAO.batchAddEntities(blackLists, blackLists.size());
        return blackLists;
    }

    /**
     * 根据黑名单类型和值从本系统数据库查询黑名单
     *
     * @param value
     * @param blackListValueType
     * @return
     */
    public List<BlackList> queryByTypeAndValue(String value,
            BlackListValueType blackListValueType) {
        if (StringUtils.isBlank(value) || blackListValueType == null) {
            return Collections.EMPTY_LIST;
        }
        // 如果参数中含有*号，调用blackListDAO中的模糊查询方法，返回值有可能不唯一
        if (value.contains("*")) {
            String value4Query = null;
            int length = value.length();
            switch (blackListValueType) {
                case ID_NO:
                    value4Query = StringUtils.substring(value, 0, length - 8)
                        + "____" + StringUtils.substring(value, length - 4);
                    break;
                case PHONE:
                    value4Query = StringUtils.substring(value, 0, 4) + "__"
                        + StringUtils.substring(value, 6);
                    break;
                case HOME_TELEPHONE:
                case WORK_TELEPHONE:
                    value4Query = StringUtils.substring(value, 0, length - 4)
                        + "__" + StringUtils.substring(value, length - 2);
                    break;
                case COMPANY_NAME:
                case NAME:
                    break;
            }
            return this.blackListDAO.getBlackList4Others(blackListValueType,
                value4Query);
        } else {
            // 如果参数中不含有*号，调用blackListDAO中的精确查询方法，返回值唯一
            BlackList blackList = this.blackListDAO.getBlackListByTypeAndValue(
                blackListValueType, value);
            if (blackList != null) {
                List<BlackList> blackLists = new ArrayList<>();
                blackLists.add(blackList);
                return blackLists;
            }
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 本公司往本系统里添加黑名单
     *
     * @param identityNo
     * @param phone
     * @param companyName
     * @param workTel
     * @param homeTel
     * @param lendRequestId
     * @param effectTime
     * @param principal
     * @return
     */
    public boolean addBlackList(String identityNo, String phone,
            String companyName, String workTel, String homeTel,
            String lendRequestId, Date effectTime, Principal principal) {
        // 记录日志到数据库
        String username = ((Authentication) principal).getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
            .getDetails();
        LogInfo logInfo = new LogInfo(username, null,
            details.getRemoteAddress(), new Date(), "black_lists");
        logInfo.setDataChannel(DataChannel.UCREDIT);
        logInfo.setResultType(ResultType.SUCCESS);
        logInfo.setEndTime(new Date());
        logInfo.setLastUpdateTime(new Date());
        this.logInfoDao.addEntity(logInfo);
        // 调用本service中的buildBlackList方法，最终得到一个黑名单集合
        List<BlackList> blackLists = new ArrayList<>();
        if (StringUtils.isNotBlank(identityNo)) {
            blackLists.add(this.buildBlackList(lendRequestId, effectTime,
                identityNo, BlackListValueType.ID_NO));
        }
        if (StringUtils.isNotBlank(phone)) {
            blackLists.add(this.buildBlackList(lendRequestId, effectTime,
                phone, BlackListValueType.PHONE));
        }
        if (StringUtils.isNotBlank(companyName)) {
            blackLists.add(this.buildBlackList(lendRequestId, effectTime,
                companyName, BlackListValueType.COMPANY_NAME));
        }
        if (StringUtils.isNotBlank(workTel)) {
            blackLists.add(this.buildBlackList(lendRequestId, effectTime,
                workTel, BlackListValueType.WORK_TELEPHONE));
        }
        if (StringUtils.isNotBlank(homeTel)) {
            blackLists.add(this.buildBlackList(lendRequestId, effectTime,
                homeTel, BlackListValueType.HOME_TELEPHONE));
        }
        if (!blackLists.isEmpty()) {
            this.blackListDAO.batchAddEntities(blackLists, blackLists.size());
            return true;
        }
        return false;
    }

    /**
     * 根据参数创建一个黑名单对象返回
     *
     * @param lendRequestId
     * @param effectTime
     * @param value
     * @param blackListValueType
     * @return
     */
    public BlackList buildBlackList(String lendRequestId, Date effectTime,
            String value, BlackListValueType blackListValueType) {
        // 用黑名单类型和值查询是否已经有相同黑名单，如果有相同的，把查出来的置为不可用
        BlackList blackListFromDB = this.blackListDAO
            .getBlackListByTypeAndValue(blackListValueType, value);
        if (blackListFromDB != null) {
            blackListFromDB.setEnabled(false);
            this.blackListDAO.updateEntity(blackListFromDB);
        }
        // 新建一个黑名单
        BlackList blackList = new BlackList();
        blackList.setLendRequestId(lendRequestId);
        blackList.setEffectTime(effectTime);
        blackList.setDataChannel(DataChannel.UCREDIT);
        blackList.setCreateTime(new Date());
        blackList.setEnabled(true);
        blackList.setValue(value);
        blackList.setType(blackListValueType);
        return blackList;
    }

    /**
     * 将安盛返回的值解析为本系统黑名单返回
     *
     * @param result
     * @param lendRequestId
     * @param params
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     * @throws Exception
     */
    public List<BlackList> buildBlackListsByAnShengResult(
            ResponseEntity<String> result, String lendRequestId,
            Map<BlackListValueType, String> params)
            throws JsonProcessingException, IOException, Exception {
        // 如果安盛返回的结果body为空，本方法返回空集合
        String body = result.getBody();
        if ("[]".equals(body)) {
            return Collections.EMPTY_LIST;
        }
        // 用ObjectMapper读取
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Map<String, Object>> maps = mapper.readValue(body,
            Map.class);
        List<String> resultList = (List<String>) maps.get("blacklist");
        // 如果返回的有黑名单，将返回值转换成我们的黑名单类型返回给此方法调用者
        List<BlackList> blackLists = new ArrayList<>();
        if (!resultList.isEmpty()) {
            for (int i = 0; i < resultList.size(); i++) {
                Object obj = resultList.get(i);
                String str = obj.toString();
                String type = str.substring(1, str.indexOf('='));
                String value = str.substring(str.indexOf('=') + 1,
                    str.indexOf(','));
                String time = str.substring(str.lastIndexOf('=') + 1,
                    str.indexOf('}'));
                BlackList blackList = new BlackList();
                blackList.setCreateTime(new Date());
                blackList.setEffectTime(new SimpleDateFormat("yyyy-MM-dd")
                    .parse(time));
                blackList.setValue(value);
                blackList.setDataChannel(DataChannel.ANSHENG);
                blackList.setEnabled(true);
                blackList.setLendRequestId(lendRequestId);
                switch (type) {
                    case "identity_card":
                        blackList.setType(BlackListValueType.ID_NO);
                        break;
                    case "mobilePhone":
                        blackList.setType(BlackListValueType.PHONE);
                        break;
                    case "phone":
                        // 安盛的返回不区分家庭座机和工作座机，所以要判断和原始参数做一下特殊处理
                        String workTel = params
                            .get(BlackListValueType.WORK_TELEPHONE);
                        String homeTel = params
                            .get(BlackListValueType.HOME_TELEPHONE);
                        if (StringUtils.isNotBlank(workTel)
                            && workTel.equals(value)) {
                            blackList
                                .setType(BlackListValueType.WORK_TELEPHONE);
                        }
                        if (StringUtils.isNotBlank(homeTel)
                            && homeTel.equals(value)) {
                            blackList
                                .setType(BlackListValueType.HOME_TELEPHONE);
                        }
                        break;
                    case "company":
                        blackList.setType(BlackListValueType.COMPANY_NAME);
                        break;
                    default:
                        break;
                }
                // 返回的座机黑名单的类型有可能因为与原始数据不匹配而为空，所以要判断一下，不为空才添加到返回的list中
                if (blackList.getType() != null) {
                    blackLists.add(blackList);
                }
            }
        }
        return blackLists;
    }

    /**
     * 将map格式查询参数转成安盛需要的JSON格式参数
     *
     * @param params
     * @return
     */
    public JSONArray buildJson(Map<BlackListValueType, String> params) {
        // 安盛要求的参数格式
        JSONArray json = new JSONArray();
        // 原始参数
        String identityNo = params.get(BlackListValueType.ID_NO);
        String phone = params.get(BlackListValueType.PHONE);
        String compName = params.get(BlackListValueType.COMPANY_NAME);
        String workTel = params.get(BlackListValueType.WORK_TELEPHONE);
        String homeTel = params.get(BlackListValueType.HOME_TELEPHONE);
        // 隐藏需要隐藏的部分数值
        if (StringUtils.isNotBlank(identityNo)) {
            String identityNo4AnSheng = StringUtils
                .substring(identityNo, 0, 10)
                + "****"
                + StringUtils.substring(identityNo, 14);
            json.add(identityNo4AnSheng);
        }
        if (StringUtils.isNotBlank(phone)) {
            String phone4AnSheng = StringUtils.substring(phone, 0, 4) + "**"
                + StringUtils.substring(phone, 6);
            json.add(phone4AnSheng);
        }
        if (StringUtils.isNotBlank(workTel) || StringUtils.isNotBlank(homeTel)) {
            if (StringUtils.isNotBlank(workTel)) {
                String[] workTelStrs = workTel.split("-");
                String workTel4AnSheng = workTelStrs[0]
                    + "-"
                    + StringUtils.substring(workTelStrs[1], 0,
                        workTelStrs[1].length() - 4)
                    + "**"
                    + StringUtils.substring(workTelStrs[1],
                        workTelStrs[1].length() - 2);
                json.add(workTel4AnSheng);
            }
            if (StringUtils.isNotBlank(homeTel)) {
                String[] homeTelStrs = homeTel.split("-");
                String homeTel4AnSheng = homeTelStrs[0]
                    + "-"
                    + StringUtils.substring(homeTelStrs[1], 0,
                        homeTelStrs[1].length() - 4)
                    + "**"
                    + StringUtils.substring(homeTelStrs[1],
                        homeTelStrs[1].length() - 2);
                json.add(homeTel4AnSheng);
            }
        }
        if (StringUtils.isNotBlank(compName)) {
            json.add(compName);
        }
        return json;
    }

    /**
     * 将查询参数转换成map格式返回
     *
     * @param identityNo
     * @param phone
     * @param companyName
     * @param workTel
     * @param homeTel
     * @return
     */
    private static Map<BlackListValueType, String> buildQueryMap(
            String identityNo, String phone, String companyName,
            String workTel, String homeTel) {
        Map<BlackListValueType, String> result = new HashMap<>();
        if (StringUtils.isNotBlank(identityNo)) {
            result.put(BlackListValueType.ID_NO, identityNo);
        }
        if (StringUtils.isNotBlank(phone)) {
            result.put(BlackListValueType.PHONE, phone);
        }
        if (StringUtils.isNotBlank(companyName)) {
            result.put(BlackListValueType.COMPANY_NAME, companyName);
        }
        if (StringUtils.isNotBlank(workTel)) {
            result.put(BlackListValueType.WORK_TELEPHONE, workTel);
        }
        if (StringUtils.isNotBlank(homeTel)) {
            result.put(BlackListValueType.HOME_TELEPHONE, homeTel);
        }
        return result;
    }

    /**
     * 将黑名单转换成小叶子需要的数据格式
     *
     * @param blackLists
     * @return
     */
    private static List<Map<String, String>> buildResult4Ucredit(
            List<BlackList> blackLists) {
        List<Map<String, String>> result = new ArrayList<>();
        //去重
        Set<BlackList> set = new LinkedHashSet<>();
        set.addAll(blackLists);
        blackLists.clear();
        blackLists.addAll(set);
        for (BlackList bl : blackLists) {
            Map<String, String> map = new HashMap<>();
            map.put("blackListId", bl.getId().toString());
            map.put("mold", bl.getType().toString());
            map.put("content", bl.getValue());
            map.put("createTime",
                DateUtils.getFormatDateString(bl.getCreateTime()));
            map.put("effectTime",
                DateUtils.getFormatDateString(bl.getEffectTime()));
            map.put("dataChannel", bl.getDataChannel().toString());
            if (bl.getLendRequestId() != null) {
                map.put("lendRequestId", bl.getLendRequestId().toString());
            }
            result.add(map);
        }
        return result;
    }

}