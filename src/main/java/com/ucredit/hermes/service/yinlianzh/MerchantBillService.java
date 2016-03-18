package com.ucredit.hermes.service.yinlianzh;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

import javax.jms.Destination;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.common.Variables;
import com.ucredit.hermes.dao.yinlianzh.MerchantBillDAO;
import com.ucredit.hermes.dao.yinlianzh.SearchInfoDAO;
import com.ucredit.hermes.dao.yinlianzh.TransactionDetailDAO;
import com.ucredit.hermes.dao.yinlianzh.TransactionInfoDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.DataChannelSubType;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.jms.InternalSystemMessageProvider;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.yinlianzh.MerchantBill;
import com.ucredit.hermes.model.yinlianzh.MerchantBills;
import com.ucredit.hermes.model.yinlianzh.MerchantTransaction;
import com.ucredit.hermes.model.yinlianzh.YinlianzhMerchantBillBaseInfo;
import com.ucredit.hermes.model.yinlianzh.YinlianzhSearchInfo;
import com.ucredit.hermes.model.yinlianzh.YinlianzhTransactionDetail;
import com.ucredit.hermes.model.yinlianzh.YinlianzhTransactionsInfo;
import com.ucredit.hermes.service.BlackListRESTTemplate;
import com.ucredit.hermes.service.ThirdQueueService;
import com.ucredit.hermes.utils.POIUtils;
import com.ucredit.hermes.utils.RESTUtils;
import com.ucredit.hermes.utils.ServletUtils;

/**
 * 银联智惠商户交易信息查询
 *
 * @author zhouwuyuan
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class MerchantBillService {
    private static Logger logger = LoggerFactory
            .getLogger(MerchantBillService.class);
    @Autowired
    private MerchantBillDAO dao;
    @Autowired
    private TransactionInfoDAO transdao;
    @Autowired
    private TransactionDetailDAO detaildao;
    @Autowired
    private SearchInfoDAO searchdao;
    @Autowired
    private Variables variables;
    @Autowired
    private BlackListRESTTemplate blackListRESTTemplate;
    @Autowired
    private ThirdQueueService thirdQueueService;
    @Autowired
    private InternalSystemMessageProvider provider;
    @Autowired
    private Destination hermesPublishQueue;

    /**
     * 获取商户账单信息
     *
     * @param type
     *        是否返回账单明细 0不返回，1返回
     * @param category
     *        商户账单多个mid查询使用 1：按账户编号mid查询，输入时mid必填；2,：按准确的商户名称查询，输入时name必填
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
     * @param lendRequestId
     * @param constraint
     * @param activeDays
     * @param principal
     * @return
     * @throws ParseException
     * @throws Exception
     */
    public YinlianzhMerchantBillBaseInfo getMerchantBillBaseInfo(Integer type,
            String category, String mid, String mName, String posId,
            String businessCode, String name, String mobile, String cid,
            String beginDate, String endDate, String lendRequestId,
            boolean constraint, int activeDays, Principal principal)
                    throws ParseException {
        //记录每次查询记录，以及返回的数据信息
        YinlianzhSearchInfo searchInfo = new YinlianzhSearchInfo();
        searchInfo.setAccount(this.variables.getYinlianzhAccount());
        searchInfo.setBeginDate(beginDate);
        searchInfo.setEndDate(endDate);
        searchInfo.setBusinessCode(businessCode);
        searchInfo.setCategory(category);
        searchInfo.setCid(cid);
        searchInfo.setMid(mid);
        searchInfo.setmName(mName);
        searchInfo.setMobile(mobile);
        searchInfo.setName(name);
        searchInfo.setPosId(posId);
        searchInfo.setType(type);
        searchInfo.setLendRequestId(lendRequestId);
        searchInfo.setCreateDate(new Date());
        String username = ((Authentication) principal).getName();
        searchInfo.setUsername(username);
        this.searchdao.addEntity(searchInfo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //查询数据库是否已满足当前查询条件的数据
        YinlianzhMerchantBillBaseInfo merchantBillBaseInfo = this.dao
                .getMerchantBillBaseInfo(type, category, mid, mName, posId,
                    businessCode, name, mobile, cid, sdf.parse(beginDate),
                    sdf.parse(endDate));
        //数据库没有满足当前查询条件的数据
        if (merchantBillBaseInfo == null) {
            merchantBillBaseInfo = this.BaseInfoSearch(type, category, mid,
                mName, posId, businessCode, name, mobile, cid, beginDate,
                endDate, lendRequestId, searchInfo, username);
        } else {
            //判断是否过有效期或者是否强制刷新，是的话重新发起查询
            Date lastUpdateTime = merchantBillBaseInfo.getBackTime();
            Date activeTime = DateUtils.addDays(lastUpdateTime, activeDays);
            Date now = new Date();
            List<String> existMid = new ArrayList<>();
            Set<YinlianzhTransactionsInfo> transactionsInfos = merchantBillBaseInfo
                    .getTransactionsInfos();
            for (YinlianzhTransactionsInfo transactionsInfo : transactionsInfos) {
                existMid.add(transactionsInfo.getMid());
            }
            //判断是否需要重查
            String[] mids = mid.split(",");
            boolean needReSearch = false;
            for (String m : mids) {
                if (!existMid.contains(m)) {
                    needReSearch = true;
                    break;
                }
            }
            if (constraint || now.after(activeTime) || needReSearch) {
                merchantBillBaseInfo.setEnabled(false);
                this.BaseInfoSearch(type, category, mid, mName, posId,
                    businessCode, name, mobile, cid, beginDate, endDate,
                    lendRequestId, searchInfo, username);
            } else {
                //数据库有满足当前查询的数据，将下载的流水明细uuid插入队列
                ThirdQueueTaskResult result = new ThirdQueueTaskResult(
                    merchantBillBaseInfo.getFileUuid() + ";"
                            + sdf.format(merchantBillBaseInfo.getBeginDate()) + ";"
                        + endDate, lendRequestId, "");
                if ("0003".equals(merchantBillBaseInfo.getResCode())) {
                    merchantBillBaseInfo = this.dao.getMerchantBillBaseInfo(
                        merchantBillBaseInfo,
                        merchantBillBaseInfo.getSearchMid());
                    Set<YinlianzhTransactionsInfo> transInfos = merchantBillBaseInfo
                        .getTransactionsInfos();
                    for (YinlianzhTransactionsInfo transInfo : transInfos) {
                        transInfo.setTransactionDetails(null);
                    }
                    merchantBillBaseInfo.setTransactionsInfos(transInfos);
                    result.setMerchantBillBaseInfo(merchantBillBaseInfo);
                }
                MerchantBillService.logger.info("--------银联智惠下载uuid："
                    + result.getFileUuid());
                this.sendMessage(result, username,
                    merchantBillBaseInfo.getId(), AsyncCode.SUCCESS);
            }
        }
        return merchantBillBaseInfo;
    }

    public YinlianzhMerchantBillBaseInfo BaseInfoSearch(Integer type,
            String category, String mid, String mName, String posId,
            String businessCode, String name, String mobile, String cid,
            String beginDate, String endDate, String lendRequestId,
            YinlianzhSearchInfo searchInfo, String username)
                    throws ParseException {
        YinlianzhMerchantBillBaseInfo merchantBillBaseInfo = new YinlianzhMerchantBillBaseInfo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        merchantBillBaseInfo.setCreateTime(new Date());
        merchantBillBaseInfo.setCreateTimeString(sdf1.format(new Date()));
        merchantBillBaseInfo.setmName(mName);
        merchantBillBaseInfo.setPosId(posId);
        merchantBillBaseInfo.setBusinessCode(businessCode);
        merchantBillBaseInfo.setName(name);
        merchantBillBaseInfo.setMobile(mobile);
        merchantBillBaseInfo.setCid(cid);
        this.dao.addEntity(merchantBillBaseInfo);
        String resultBack = "";
        String params = this.getParams(type, category, mid, mName, posId,
            businessCode, name, mobile, cid, beginDate, endDate);
        String searchType = "single";
        if (category != null) {//多个查询,包含用多个mid查询或者商户名称mName进行查询
            if ("1".equals(category)) {//按照mid多个查询
                //由于按照多个mid进行查询，第三方放回的数据当对应的mid没有交易流水
                //信息时不会返回shortMid，则不能和发起的mid进行对应，所以对每个mid发起一次没有流水信息的查询
                //记录mid对应的商户信息
                String result = this.addTransactionInfo(
                    mid,
                    beginDate,
                    sdf.format(DateUtils.addDays(
                        DateUtils.parseDate(endDate, "yyyy-mm-dd"), 1)),
                        searchInfo, username, lendRequestId);
                if ("".equals(result)) {
                    merchantBillBaseInfo.setResCode("");
                    merchantBillBaseInfo.setResMsg("访问第三方出现问题");
                    return merchantBillBaseInfo;
                }
            }
            searchType = "multiple";
        }
        //获得查询返回的json信息
        merchantBillBaseInfo.setQueryTime(new Date());
        try {
            resultBack = this.getBillsResultFromYinlianzh(params, searchType);
            merchantBillBaseInfo.setBackTime(new Date());
        } catch (Exception e) {//链接第三方出现异常，记录查询日志
            searchInfo.setBackMsg(HermesConsts.HERMES_RESPONSE_ERROR);
            merchantBillBaseInfo.setEnabled(false);
            merchantBillBaseInfo.setResMsg(HermesConsts.HERMES_RESPONSE_ERROR
                + ":" + e.getMessage());
            this.searchdao.updateEntity(searchInfo);
            e.printStackTrace();
            return merchantBillBaseInfo;
        }
        if ("single".equals(searchType)) {//若是单个查询则调用单个查询的解析方法
            merchantBillBaseInfo = this.getFromResultSingle(
                merchantBillBaseInfo, resultBack, type, beginDate, endDate,
                searchInfo, username, lendRequestId);
        } else {//否则调用多个查询的解析方法
            merchantBillBaseInfo = this.decideReSend(merchantBillBaseInfo,
                resultBack, type, beginDate, endDate, category, searchInfo,
                mid, username, lendRequestId);
        }
        return merchantBillBaseInfo;
    }

    /**
     * @param type
     *        是否返回账单明细 0不返回，1返回
     * @param category
     *        商户账单多个mid查询使用 1：按账户编号mid查询，输入时mid必填；2,：按准确的商户名称查询，输入时name必填
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
     * @return
     */
    public String getParams(Integer type, String category, String mid,
            String mName, String posId, String businessCode, String name,
            String mobile, String cid, String beginDate, String endDate) {
        TreeMap<String, String> paramsMap = new TreeMap<>();
        paramsMap.put("account", this.variables.getYinlianzhAccount());
        paramsMap.put("type", type + "");
        paramsMap.put("beginDate", beginDate);
        paramsMap.put("endDate", endDate);
        StringBuilder params = new StringBuilder("?account="
                + this.variables.getYinlianzhAccount() + "&type=" + type
                + "&beginDate=" + beginDate + "&endDate=" + endDate);
        if (posId != null) {
            paramsMap.put("posId", posId);
            params.append("&posId");
            params.append(posId);
        }
        if (mid != null) {
            paramsMap.put("mid", mid);
            params.append("&mid=");
            params.append(mid);
        }
        if (category == null) {//单个查询
            //商户名称
            paramsMap.put("mName", mName);
            params.append("&mName=");
            params.append(mName);

            if (businessCode != null) {
                paramsMap.put("businessCode", businessCode);
                params.append("&businessCode");
                params.append(businessCode);
            }
            if (name != null) {
                //商户负责人姓名
                paramsMap.put("name", name);
                params.append("&name");
                params.append(name);
            }
            if (mobile != null) {
                paramsMap.put("mobile", mobile);
                params.append("&mobile");
                params.append(mobile);
            }
            if (cid != null) {
                paramsMap.put("cid", cid);
                params.append("&cid");
                params.append(cid);
            }
        } else {//多个查询,包含用多个mid查询或者商户名称mName进行查询
            paramsMap.put("category", category);
            params.append("&category=");
            params.append(category);
            if (mName != null) {//商户名称
                paramsMap.put("name", mName);
                params.append("&name=");
                params.append(mName);
            }
        }
        //获得数字签名
        String sign = this.getSign(paramsMap);
        params.append("&sign=");
        params.append(sign);
        return params.toString();
    }

    /**
     * 根据不返回商户流水的查询做商户信息的记录
     *
     * @param mid
     * @param beginDate
     * @param endDate
     * @throws ParseException
     * @throws Exception
     */
    public String addTransactionInfo(String mid, String beginDate,
            String endDate, YinlianzhSearchInfo searchInfo, String username,
            String lendRequestId) throws ParseException {
        String[] mids = mid != null ? mid.split(",") : new String[] {};
        String resultBack = "";
        for (String id : mids) {
            YinlianzhMerchantBillBaseInfo merchantBillBaseInfo = new YinlianzhMerchantBillBaseInfo();
            this.dao.addEntity(merchantBillBaseInfo);
            StringBuilder params = new StringBuilder("?account="
                    + this.variables.getYinlianzhAccount()
                    + "&type=0&category=1&mid=" + id + "&beginDate=" + beginDate
                    + "&endDate=" + endDate);
            Map<String, String> paramsMap = new TreeMap<>();
            paramsMap.put("account", this.variables.getYinlianzhAccount());
            paramsMap.put("type", "0");
            paramsMap.put("category", "1");
            paramsMap.put("mid", id);
            paramsMap.put("beginDate", beginDate);
            paramsMap.put("endDate", endDate);
            String sign = this.getSign(paramsMap);
            params.append("&sign=");
            params.append(sign);
            //返回不带交易流水的商户信息
            merchantBillBaseInfo.setQueryTime(new Date());
            try {
                resultBack = this.getBillsResultFromYinlianzh(
                    params.toString(), "multiple");
                merchantBillBaseInfo.setBackTime(new Date());
            } catch (Exception e) {
                merchantBillBaseInfo.setEnabled(false);
                merchantBillBaseInfo
                .setResMsg(HermesConsts.HERMES_RESPONSE_ERROR + ":"
                        + e.getMessage());
            }
            if ("".equals(resultBack)) {
                return "";
            }
            //将返回的json商户信息解析入库
            this.decideReSend(merchantBillBaseInfo, resultBack, 0, beginDate,
                endDate, "1", searchInfo, id, username, lendRequestId);

        }
        return resultBack;

    }

    public void sendMessage(ThirdQueueTaskResult resp, String systemId,
            int requestId, AsyncCode asyncCode) {
        String messageBody;
        try {
            messageBody = InternalSystemMessageProvider.packageResponse(resp,
                systemId, DataChannelSubType.YINLIANZH, requestId, asyncCode);
            this.provider.sendTextMessage(messageBody, systemId,
                this.hermesPublishQueue);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得数字签名
     *
     * @param paramsMap
     * @return
     * @throws UnsupportedEncodingException
     */
    public String getSign(Map<String, String> paramsMap) {
        StringBuilder str = new StringBuilder();
        for (Object element : paramsMap.keySet()) {
            String key = (String) element;
            String value = paramsMap.get(key);
            str = str.append(key + value);
        }
        String privateKey = this.variables.getYinlianzhPrivateKey();
        try {
            byte[] s1 = (str.toString() + privateKey).getBytes("UTF-8");
            return DigestUtils.md5Hex(s1).toUpperCase();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 根据链接和参数返回调用的json结果
     *
     * @param params
     * @param searchtype
     * @return
     * @throws Exception
     */
    public String getBillsResultFromYinlianzh(String params, String searchtype)
            throws Exception {
        String url = this.variables.getYinlianzhihuiBillsUrl();
        if ("single".equals(searchtype)) {
            url = this.variables.getYinlianzhihuiBillUrl();
            try {
                return this.blackListRESTTemplate.getEntity(url + params,
                    String.class);
            } catch (Exception e) {
                MerchantBillService.logger.error("单个mid查询第三方错误："
                        + e.getMessage());
                e.printStackTrace();
            }
        }
        MerchantBillService.logger.info("--------------" + params);
        return this.blackListRESTTemplate.getEntity(url + params, String.class);
    }

    /**
     * 单个查询对结果进行解析
     *
     * @param merchantBillBaseInfo
     * @param result
     *        返回的结果
     * @param type
     *        是否是带交易流水的查询
     * @param beginDate
     *        查询开始时间
     * @param endDate
     *        查询结束时间
     * @param searchInfo
     *        记录每次查询的日志
     * @param username
     *        当前登陆的用户
     * @param lendRequestId
     *        进件号
     * @return 商户信息
     * @throws ParseException
     */
    public YinlianzhMerchantBillBaseInfo getFromResultSingle(
            YinlianzhMerchantBillBaseInfo merchantBillBaseInfo, String result,
            Integer type, String beginDate, String endDate,
            YinlianzhSearchInfo searchInfo, String username,
            String lendRequestId) throws ParseException {
        searchInfo.setBackData(result);
        JSONObject json = JSONObject.fromObject(result);
        MerchantBill mer = (MerchantBill) JSONObject.toBean(json,
            MerchantBill.class);
        searchInfo.setBackCode(mer.getResCode());
        searchInfo.setBackMsg(mer.getResMsg());
        //设置商户查询的基本信息
        merchantBillBaseInfo.setResCode(mer.getResCode());
        merchantBillBaseInfo.setResMsg(mer.getResMsg());
        this.dao.addEntity(merchantBillBaseInfo);
        //除返回结果为0000的消息，其他返回均是提交失败
        if (!"0000".equals(mer.getResCode())) {
            if ("0003".equals(mer.getResCode())) {//商户交易起止期内的的交易笔数过大，超过输出条数限制，按每月分批查询
                ThirdQueueTaskResult resultSearch = new ThirdQueueTaskResult(
                    merchantBillBaseInfo, JMSType.YINLIANZH_JMS_SEARCH_QUEUE,
                    username);
                this.thirdQueueService.sendMessage(resultSearch,
                    DataChannel.YINLIANZH, null);
            }
            return merchantBillBaseInfo;
        }
        merchantBillBaseInfo.setFileUuid(mer.getUuid());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        merchantBillBaseInfo.setEnabled(true);
        merchantBillBaseInfo.setFileUuid(mer.getUuid());
        merchantBillBaseInfo.setResCode(mer.getResCode());
        merchantBillBaseInfo.setResMsg(mer.getResMsg());
        merchantBillBaseInfo.setSign(mer.getSign());
        merchantBillBaseInfo.setType(type);
        merchantBillBaseInfo.setBeginDate(sdf.parse(beginDate));
        merchantBillBaseInfo.setEndDate(sdf.parse(endDate));
        merchantBillBaseInfo.setLendRequestId(lendRequestId);
        this.dao.addEntity(merchantBillBaseInfo);
        //设置商户的基本信息
        YinlianzhTransactionsInfo transactionsInfo = new YinlianzhTransactionsInfo();
        transactionsInfo.setMcc(mer.getMcc());
        transactionsInfo.setAddress(mer.getAddress());
        transactionsInfo.setCity(mer.getCity());
        transactionsInfo.setFileUuid(mer.getUuid());
        if (!"".equals(mer.getFirstTransDate())) {
            transactionsInfo.setFirstTransDate(sdf.parse(mer
                .getFirstTransDate()));
        }
        if (!"".equals(mer.getLastTransDate())) {
            transactionsInfo
            .setLastTransDate(sdf.parse(mer.getLastTransDate()));
        }
        transactionsInfo.setMid(mer.getMid());
        transactionsInfo.setName(mer.getmName());
        String pIds = "";
        ArrayList<String> pIdsList = (ArrayList<String>) mer.getPosIds();
        for (int i = 0; i < pIdsList.size(); i++) {
            pIds = pIds + pIdsList.get(i);
        }
        transactionsInfo.setPosIds(pIds);
        transactionsInfo.setPosCount(mer.getPosCount());
        transactionsInfo.setReceipt(mer.getBorrow());
        transactionsInfo.setExpense(mer.getLend());
        transactionsInfo.setTransCount(mer.getTransCount());
        transactionsInfo
        .setYinlianzhMerchantBillBaseInfoId(merchantBillBaseInfo.getId());
        this.transdao.addEntity(transactionsInfo);
        //设置商户的交易流水信息
        Set<YinlianzhTransactionsInfo> transactionsInfos = new HashSet<>();
        transactionsInfos.add(transactionsInfo);
        //返回的交易流水List<String>信息集合
        ArrayList<String> dataInfo = (ArrayList<String>) mer.getData();
        if (dataInfo != null && !dataInfo.isEmpty()) {
            //将List<String>集合插入数据库对已交易流水信息
            this.setDatas(dataInfo, transactionsInfo, false, -1, "");
        }
        merchantBillBaseInfo.setTransactionsInfos(transactionsInfos);
        //若返回有下载uuid则将uuid插入队列进行异步处理
        if (!mer.getUuid().isEmpty()) {
            ThirdQueueTaskResult resultMQ = new ThirdQueueTaskResult(
                mer.getUuid(), JMSType.YINLIANZH_JMS_QUEUE, username);
            this.thirdQueueService.sendMessage(resultMQ, DataChannel.YINLIANZH,
                null);
        }
        return merchantBillBaseInfo;
    }

    public YinlianzhMerchantBillBaseInfo decideReSend(
            YinlianzhMerchantBillBaseInfo merchantBillBaseInfo, String result,
            Integer type, String beginDate, String endDate, String category,
            YinlianzhSearchInfo searchInfo, String mid, String username,
            String lendRequestId) throws ParseException {
        searchInfo.setBackData(result);
        Map<String, Object> classMap = new HashMap<>();
        classMap.put("transactionsInfo", MerchantTransaction.class);
        JSONObject json = JSONObject.fromObject(result);
        //json转成对应的bean便于解析
        MerchantBills mers = (MerchantBills) JSONObject.toBean(json,
            MerchantBills.class, classMap);
        searchInfo.setBackCode(mers.getResCode());
        searchInfo.setBackMsg(mers.getResMsg());
        merchantBillBaseInfo.setResCode(mers.getResCode());
        merchantBillBaseInfo.setResMsg(mers.getResMsg());
        merchantBillBaseInfo.setCategory(category);
        merchantBillBaseInfo.setType(type);
        merchantBillBaseInfo.setSearchMid(mid);
        merchantBillBaseInfo.setUsername(username);
        Date beginDateSeach = DateUtils.parseDate(beginDate, "yyyy-MM-dd");
        Date endDateSeach = DateUtils.parseDate(endDate, "yyyy-MM-dd");
        merchantBillBaseInfo.setBeginDate(beginDateSeach);
        merchantBillBaseInfo.setEndDate(endDateSeach);
        merchantBillBaseInfo.setLendRequestId(lendRequestId);
        if (!"0000".equals(mers.getResCode())) {
            if ("0003".equals(mers.getResCode())) {//商户交易起止期内的的交易笔数过大，超过输出条数限制，按每月分批查询
                ThirdQueueTaskResult resultSearch = new ThirdQueueTaskResult(
                    merchantBillBaseInfo, JMSType.YINLIANZH_JMS_SEARCH_QUEUE,
                    username);
                this.thirdQueueService.sendMessage(resultSearch,
                    DataChannel.YINLIANZH, null);
            }
            return merchantBillBaseInfo;
        }
        return this.getFromResultMultiple(merchantBillBaseInfo, mers, type,
            mid, username, "0");
    }

    /**
     * 多个商户查询
     *
     * @param merchantBillBaseInfo
     * @param mers
     *        gson对象
     * @param type
     *        查询类型 返回流水或不返回
     * @param mid
     *        查询的mid
     * @param username
     * @param fileUuids
     * @param sendType
     * @return
     * @throws ParseException
     */
    public YinlianzhMerchantBillBaseInfo getFromResultMultiple(
            YinlianzhMerchantBillBaseInfo merchantBillBaseInfo,
            MerchantBills mers, Integer type, String mid, String username,
            String sendType) throws ParseException {
        if (!"0000".equals(mers.getResCode())) {
            merchantBillBaseInfo.setResCode(mers.getResCode());
            merchantBillBaseInfo.setResMsg(mers.getResMsg() + "分拆查询");
            return merchantBillBaseInfo;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        merchantBillBaseInfo.setTotalReceipt(mers.getTotalReceipt());
        merchantBillBaseInfo.setTotalExpence(mers.getTotalExpense());
        merchantBillBaseInfo.setTotalCount(mers.getTotalCount());
        if (mers.getFileUuid() != null && !mers.getFileUuid().isEmpty()) {
            merchantBillBaseInfo.setFileUuid(mers.getFileUuid());
        }
        if (merchantBillBaseInfo.getFileUuids() != null) {
            merchantBillBaseInfo.setFileUuids(merchantBillBaseInfo
                .getFileUuids() + "," + mers.getFileUuid());
        } else {
            merchantBillBaseInfo.setFileUuids(mers.getFileUuid());
        }
        //发生下载查询加1
        if (mers.getFileUuid() != null && !mers.getFileUuid().isEmpty()) {
            merchantBillBaseInfo.setSearchTotal(merchantBillBaseInfo
                .getSearchTotal() + 1);
        }
        merchantBillBaseInfo.setSign(mers.getSign());
        List<MerchantTransaction> merchantTransactions = mers
            .getTransactionsInfo();
        //获得商户信息
        Set<YinlianzhTransactionsInfo> transactionsInfos = new HashSet<>();
        if (merchantTransactions != null) {
            for (MerchantTransaction merchantTransaction : merchantTransactions) {
                YinlianzhTransactionsInfo transactionsInfo = new YinlianzhTransactionsInfo();
                YinlianzhTransactionsInfo transactionsInfoBefore = this.transdao
                    .getTransactionInfoByshortEncryptMid(merchantTransaction
                        .getMid());
                transactionsInfo.setEncryptMid(merchantTransaction.getMid());
                transactionsInfo.setName(merchantTransaction.getName());
                transactionsInfo.setPosCount(merchantTransaction.getPosCount());
                transactionsInfo.setPosIds(merchantTransaction.getPosIds()
                    .toString());
                transactionsInfo.setMcc(merchantTransaction.getMcc());
                transactionsInfo.setCity(merchantTransaction.getCity());
                transactionsInfo.setAddress(merchantTransaction.getAddress());
                transactionsInfo.setReceipt(merchantTransaction.getReceipt());
                transactionsInfo.setExpense(merchantTransaction.getExpense());
                transactionsInfo.setTransCount(merchantTransaction
                    .getTransCount());
                String bTime = sdf.format(new Date());
                transactionsInfo.setBackTimeString(bTime);
                transactionsInfo.setType(type);
                //根据之前插入的设置mid
                if (transactionsInfoBefore != null) {
                    transactionsInfo.setMid(transactionsInfoBefore.getMid());
                }
                if (!"".equals(merchantTransaction.getFirstTransDate())) {
                    transactionsInfo.setFirstTransDate(new java.sql.Date(
                        DateUtils.parseDate(
                            merchantTransaction.getFirstTransDate(),
                            "yyyy-MM-dd").getTime()));
                }
                if (!"".equals(merchantTransaction.getLastTransDate())) {
                    transactionsInfo.setLastTransDate(new java.sql.Date(
                        DateUtils.parseDate(
                            merchantTransaction.getLastTransDate(),
                            "yyyy-MM-dd").getTime()));
                }
                transactionsInfo.setFileUuid(merchantTransaction.getFileUuid());
                transactionsInfo
                .setYinlianzhMerchantBillBaseInfoId(merchantBillBaseInfo
                    .getId());
                this.transdao.addEntity(transactionsInfo);
                //获得 当前商户信息下的交易流水信息
                List<String> transactions = merchantTransaction
                    .getTransactions();
                String[] searchMids = mid == null ? new String[] {} : mid
                    .split(",");
                //当进行单个mid，不查询流水信息时
                if (type == 0 && searchMids.length == 1) {
                    transactionsInfo.setMid(searchMids[0]);
                }
                if (transactions != null && !transactions.isEmpty()) {
                    //将流水信息入库
                    this.setDatas(transactions, transactionsInfo, false, -1, "");
                }
                //设置商户对应mid的信息
                transactionsInfos.add(transactionsInfo);
            }
        }
        //设置本次查询的mid信息
        merchantBillBaseInfo.setTransactionsInfos(transactionsInfos);
        merchantBillBaseInfo.setIfDowload(false);
        //将商户流水信息进入队列查询

        if (!mers.getFileUuid().isEmpty()) {
            for (YinlianzhTransactionsInfo t : transactionsInfos) {
                if (t.getFileUuid() != null && !t.getFileUuid().isEmpty()) {
                    if (!"1".equals(sendType)) {
                        ThirdQueueTaskResult resultMQ = new ThirdQueueTaskResult(
                            t.getFileUuid(), JMSType.YINLIANZH_JMS_QUEUE,
                            username);
                        resultMQ.setMerchantBillBaseInfo(merchantBillBaseInfo);
                        this.thirdQueueService.sendMessage(resultMQ,
                            DataChannel.YINLIANZH, null);
                    }

                }
            }

        }

        return merchantBillBaseInfo;
    }

    /**
     * 设置账单明细
     *
     * @param dataInfo
     *        商户对应mid下的流水信息
     * @param transInfoId
     *        商户 对应的mid具体信息
     * @param isDownLoad
     *        是否是下载请求
     */
    public void setDatas(List<String> dataInfo,
            YinlianzhTransactionsInfo transInfo, boolean isDownLoad,
            int baseInfoId, String uuid) {
        if (transInfo == null) {
            transInfo = this.transdao
                .getTransactionInfoByUuid(baseInfoId, uuid);
        }
        int cycleNum = dataInfo.size() % 1000 == 0 ? dataInfo.size() / 1000
            : dataInfo.size() / 1000 + 1;
        int begin = 0;
        for (int k = 1; k <= cycleNum; k++) {
            int dataCount = k * 1000 < dataInfo.size() ? k * 1000 : dataInfo
                .size();
            List<String> dataInfoOnce = new ArrayList<>();
            for (int i = begin; i < dataCount; i++) {
                dataInfoOnce.add(dataInfo.get(i));
            }
            this.detaildao.insertDetails(dataInfoOnce, transInfo.getId(),
                transInfo.getMid(), isDownLoad);
            begin = dataCount;

        }

        //this.detaildao.insertDetails(dataInfo, transInfo.getId(),
        //  transInfo.getMid(), isDownLoad);
        //遍历商户流水信息集合
    }

    /**
     * 更具uuid下载交易流水的解析
     *
     * @param uuid
     * @param resp
     */
    public Map<String, Object> downLoad(String uuid, HttpServletResponse resp)
            throws ParseException {
        Map<String, Object> result = new HashMap<>();
        String[] strs = uuid.split(";");
        YinlianzhMerchantBillBaseInfo baseInfo = this.dao
                .getMerchantBillBaseInfoFromBaseUuid(strs[0]);
        List<Integer> transInfoIds = this.detaildao
                .getTransIdByBaseUuid(strs[0]);
        //当ifDowload为false表示该对应的uuid还未下载过，需要下载入库在返回文件
        if (baseInfo != null) {
            try {
                this.downLoadExcel(transInfoIds,
                    DateUtils.parseDate(strs[1], "yyyy-MM-dd"),
                    DateUtils.parseDate(strs[2], "yyyy-MM-dd"), resp);
            } catch (IOException e) {
                e.printStackTrace();
                result.put(HermesConsts.MSG, "文件输出失败，原因：" + e.getMessage());
                return result;
            }
        }
        result.put(HermesConsts.MSG, AsyncCode.SUCCESS);
        return result;

    }

    /**
     * 获得下载接口返回的结果
     *
     * @param uuid
     * @return
     */
    public byte[] getDownLoadBack(String uuid) {
        TreeMap<String, String> paramsMap = new TreeMap<>();
        paramsMap.put("account", this.variables.getYinlianzhAccount());
        paramsMap.put("uuid", uuid);
        String sign = this.getSign(paramsMap);
        String params = "?account=" + this.variables.getYinlianzhAccount()
                + "&uuid=" + uuid + "&sign=" + sign;
        String url = this.variables.getYinlianzhihuiDownloadUrl();
        //以二进制获得返回的数据
        try {
            byte[] backFile = this.blackListRESTTemplate.getEntity(
                url + params, byte[].class);
            return backFile;
        } catch (Exception e) {
            e.printStackTrace();
            YinlianzhSearchInfo searchInfo = new YinlianzhSearchInfo();
            searchInfo.setUuid(uuid);
            searchInfo.setBackMsg(HermesConsts.HERMES_RESPONSE_ERROR);
            this.searchdao.addEntity(searchInfo);
        }
        return new byte[] {};
    }

    /**
     * 构造返回的文件
     *
     * @param transInfoIds
     * @param beginDate
     * @param endDate
     * @param resp
     * @return
     * @throws IOException
     */
    public ResponseEntity<?> downLoadExcel(List<Integer> transInfoIds,
        Date beginDate, Date endDate, HttpServletResponse resp)
                throws IOException {
        List<YinlianzhTransactionDetail> list = this.detaildao
                .getTransDetailByDate(transInfoIds, beginDate, endDate);
        StringBuilder string = new StringBuilder();

        string.append(this.lineToString(this.createRowTitle()));
        string.append(System.getProperty(POIUtils.LINE_SEPARATOR));
        for (YinlianzhTransactionDetail detail : list) {
            List<String> line = MerchantBillService
                    .wrapTransactionDetail(detail);
            string.append(this.lineToString(line));
            string = string.append(System.getProperty(POIUtils.LINE_SEPARATOR));
        }
        String fileName = "DetailList_";
        fileName = fileName + "_" + UUID.randomUUID() + ".csv";
        ServletUtils.setAttachmentHeader(resp,
            ServletUtils.APPLICATION_TYPE_CSV, fileName, 0);

        try (OutputStream out = resp.getOutputStream();
                OutputStreamWriter write = new OutputStreamWriter(out, "gbk");) {
            write.write(string.toString());
        }

        return RESTUtils.buildNotFoundResponse();

    }

    private static List<String> wrapTransactionDetail(
        YinlianzhTransactionDetail detail) {
        if (detail == null) {
            return Collections.EMPTY_LIST;
        }
        List<String> line = new ArrayList<>();
        line.add(detail.getCarDistinguishCode());
        line.add(DateFormatUtils.format(detail.getTransactionDate(),
            "yyyy-MM-dd HH:mm:ss"));
        line.add(DateFormatUtils.format(detail.getAccountDate(), "yyyy-MM-dd"));

        line.add(detail.getYinlianzhCurrencyType() == null ? "" : detail
            .getYinlianzhCurrencyType().getCurrencyName());

        line.add(detail.getYinlianzhResponseCode() == null ? "" : detail
            .getYinlianzhResponseCode().getOutDisplay());

        line.add(detail.getYinlianzhTransactionType() == null ? "" : detail
            .getYinlianzhTransactionType().getName());

        line.add(detail.getIncome() + "");
        line.add(detail.getOutcome() + "");
        line.add(detail.getMerchantDistinguishCode());
        line.add(detail.getTerminalDistinguishCode());
        line.add(detail.getCardLocation());

        line.add(detail.getYinlianzhCardNature() == null ? "" : detail
            .getYinlianzhCardNature().getName());

        line.add(detail.getYinlianzhCardGrade() == null ? "" : detail
            .getYinlianzhCardGrade().getName());

        line.add(detail.getYinlianzhCardProduct() == null ? "" : detail
            .getYinlianzhCardProduct().getName());
        line.add(detail.getTrueMid() == null ? detail.getShortMid() : detail
            .getTrueMid());
        return line;
    }

    public StringBuilder lineToString(List<String> list) {
        StringBuilder string = new StringBuilder();
        for (String s : list) {
            ServletUtils.buildCSVStringValue(string, s);
        }
        return string;
    }

    public List<String> createRowTitle() {
        List<String> title = new ArrayList<>();
        title.add("卡号识别码");
        title.add("交易时间");
        title.add("记账日期");
        title.add("交易币种");
        title.add("交易状态");
        title.add("交易类型");
        title.add("收");
        title.add("支");
        title.add("商户编号识别码");
        title.add("终端号识别码");
        title.add("发卡行所在地");
        title.add("卡性质");
        title.add("卡等级");
        title.add("卡产品");
        title.add("商户编号");
        return title;
    }

}
