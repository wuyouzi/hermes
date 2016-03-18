package com.ucredit.hermes.service.juxinli;

import java.lang.reflect.Field;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonObject;
import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.common.HttpUtils;
import com.ucredit.hermes.dao.LogInfoDAO;
import com.ucredit.hermes.dao.juxinli.GrantAuthorizationDAO;
import com.ucredit.hermes.dao.juxinli.ReportDataDAO;
import com.ucredit.hermes.dao.juxinli.SaveEntityDAO;
import com.ucredit.hermes.dao.juxinli.VerifyContactListDAO;
import com.ucredit.hermes.enums.AsyncCode;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.enums.JMSType;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.LogInfo;
import com.ucredit.hermes.model.ThirdQueueTaskResult;
import com.ucredit.hermes.model.juxinli.ApplicationCheck;
import com.ucredit.hermes.model.juxinli.Behavior;
import com.ucredit.hermes.model.juxinli.BehaviorCheck;
import com.ucredit.hermes.model.juxinli.CellBehavior;
import com.ucredit.hermes.model.juxinli.CollectionContact;
import com.ucredit.hermes.model.juxinli.ContactDetails;
import com.ucredit.hermes.model.juxinli.ContactList;
import com.ucredit.hermes.model.juxinli.ContactRegion;
import com.ucredit.hermes.model.juxinli.DataCheck;
import com.ucredit.hermes.model.juxinli.DataSource;
import com.ucredit.hermes.model.juxinli.DeliverAddress;
import com.ucredit.hermes.model.juxinli.DemandsInfo;
import com.ucredit.hermes.model.juxinli.EbusinessExpense;
import com.ucredit.hermes.model.juxinli.GrantAuthorizationInfo;
import com.ucredit.hermes.model.juxinli.JsonInfoTwo;
import com.ucredit.hermes.model.juxinli.JuxinliDataInfo;
import com.ucredit.hermes.model.juxinli.Person;
import com.ucredit.hermes.model.juxinli.Receiver;
import com.ucredit.hermes.model.juxinli.RecentNeed;
import com.ucredit.hermes.model.juxinli.ReportData;
import com.ucredit.hermes.model.juxinli.TripInfo;
import com.ucredit.hermes.model.juxinli.VerifyContactList;
import com.ucredit.hermes.service.ThirdQueueService;

/**
 * 聚信立
 *
 * @author zhouuwyuan
 */
@Service
@Transactional(rollbackFor = ServiceException.class)
public class GrantAuthorizationService {
    @Autowired
    private LogInfoDAO logInfoDao;
    @Autowired
    private SaveEntityDAO saveEntityDAO;
    @Autowired
    private GrantAuthorizationDAO grantAuthorizationDAO;
    @Autowired
    private ReportDataDAO reportDataDAO;
    @Autowired
    private ThirdQueueService thirdQueueService;
    @Autowired
    private VerifyContactListDAO verifyContactListDAO;

    /**
     * 保存app端返回的授权信息
     *
     * @param id
     * @param name
     * @param idcard
     * @param phone
     * @param home_tel
     * @param home_addr
     * @param work_tel
     * @param couple_phone_num
     * @param contact_list
     * @param principal
     */
    public void saveGrantAuthorizationInfo(String id, String name,
            String idcard, String phone, String home_tel, String home_addr,
            String work_tel, String couple_phone_num, String contact_list,
            String keyid, Principal principal) {
        Map<String, Object> result = new HashMap<>();
        //记录日志到数据库
        String userName = ((Authentication) principal).getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
            .getDetails();
        LogInfo logInfo = new LogInfo(userName, null,
            details.getRemoteAddress(), new Date(), "grant_authorization_infos");
        List<GrantAuthorizationInfo> grantAuthorizationInfoExist = this.grantAuthorizationDAO
            .GrantAuthorizationInfo(name, idcard, phone);
        if (grantAuthorizationInfoExist.isEmpty()) {
            try {
                logInfo.setDataChannel(DataChannel.JUXINLI);
                logInfo.setEndTime(new Date());
                logInfo.setLastUpdateTime(new Date());
                this.logInfoDao.addEntity(logInfo);
                GrantAuthorizationInfo grantAuthorizationInfo = new GrantAuthorizationInfo(
                    name, idcard, phone);
                grantAuthorizationInfo.setAppId(id);
                grantAuthorizationInfo.setEnabled(true);
                grantAuthorizationInfo.setKeyid(keyid);
                //将之前用户授权的信息设为无效
                this.grantAuthorizationDAO.updateBeforeGrant(name, idcard,
                    phone);
                //添加此次用户授权信息
                this.saveEntityDAO.addEntity(grantAuthorizationInfo);
                logInfo.setResultType(ResultType.SUCCESS);
                this.logInfoDao.addEntity(logInfo);
                result.put(HermesConsts.MSG, AsyncCode.SUCCESS);
                this.sendToQueue(name, idcard, phone, userName,
                    details.getRemoteAddress(), 1,
                    grantAuthorizationInfo.getId(),
                    grantAuthorizationInfo.getKeyid(), home_tel, home_addr,
                    work_tel, couple_phone_num, contact_list, JMSType.JU_XIN_LI);
            } catch (Exception e) {
                logInfo.setResultType(ResultType.FAILURE);
                logInfo.setErrorMessage("hermes保存出错");
                e.printStackTrace();
            }
        }

    }

    /**
     * @param name
     *        姓名
     * @param idcard
     *        身份证
     * @param phone
     *        电话
     * @param userName
     *        登陆账号
     * @param ip
     *        登陆ip
     * @param reportNum
     *        查询的报告编号
     * @param fkId
     *        授权用户的id
     * @param home_tel
     *        住宅电话
     * @param home_addr
     *        住宅的地址
     * @param work_tel
     *        公司电话
     * @param couple_phone_num
     *        配偶电话
     * @param contact_list
     *        联系人数组
     * @param keyid
     * @param jt
     */
    public void sendToQueue(String name, String idcard, String phone,
            String userName, String ip, int reportNum, int fkId, String keyid,
            String home_tel, String home_addr, String work_tel,
            String couple_phone_num, String contact_list, JMSType jt) {
        ThirdQueueTaskResult resultQueue = new ThirdQueueTaskResult();
        resultQueue.setName(name);
        resultQueue.setIdcard(idcard);
        resultQueue.setPhone(phone);
        resultQueue.setUserName(userName);
        resultQueue.setType(jt);
        resultQueue.setIp(ip);
        resultQueue.setReportNum(reportNum);
        resultQueue.setFkId(fkId);
        resultQueue.setHome_addr(home_addr);
        resultQueue.setHome_tel(home_tel);
        resultQueue.setWork_tel(work_tel);
        resultQueue.setCouple_phone_num(couple_phone_num);
        resultQueue.setContact_list(contact_list);
        resultQueue.setKeyid(keyid);
        this.thirdQueueService.sendMessage(resultQueue, DataChannel.JUXINLI,
            null);
    }

    /**
     * 查询报告
     *
     * @param name
     * @param idcard
     * @param phone
     * @param activeDays
     * @param constraint
     * @param grantAuthorizationId
     * @param home_tel
     * @param home_addr
     * @param work_tel
     * @param couple_phone_num
     * @param contact_list
     * @param keyid
     * @param principal
     * @return
     */
    public Map<String, Object> searchReport(String name, String idcard,
        String phone, int activeDays, boolean constraint,
        GrantAuthorizationInfo grantAuthorizationInfo, String home_tel,
        String home_addr, String work_tel, String couple_phone_num,
        String contact_list, String keyId, Principal principal) {
        int grantAuthorizationId = grantAuthorizationInfo.getId();
        Map<String, Object> result = new HashMap<>();
        ReportData reportData1 = this.reportDataDAO.getReportData(
            grantAuthorizationId, 1);
        String userName = ((Authentication) principal).getName();
        WebAuthenticationDetails details = (WebAuthenticationDetails) ((Authentication) principal)
                .getDetails();
        if (reportData1 == null) {//报告一没有返回不发起报告二的查询，给出提示
            result.put(HermesConsts.MSG, "用户未授权，报告一未返回");
            result.put("id", keyId);
            return result;
        } else if (contact_list == null || contact_list.isEmpty()) {//没有填写联系人直接返回报告一
            JuxinliDataInfo juxinliReportData = new JuxinliDataInfo();
            juxinliReportData.setReportData1(reportData1);
            result.put(HermesConsts.MSG, juxinliReportData);
            return result;
        } else {
            //查询验真信息
            VerifyContactList verifyContactList = this.verifyContactListDAO
                    .getVerifyContactListInfo(grantAuthorizationId);
            if (verifyContactList != null
                    && AsyncCode.RESPONSE_NO_DETAILS.equals(verifyContactList
                        .getErrorCode())) {
                //报告过期
                Date lastUpdateTime = verifyContactList.getBackTime();
                Date activeTime = DateUtils.addDays(lastUpdateTime, activeDays);
                Date now = new Date();
                if (constraint || now.after(activeTime)) {
                    grantAuthorizationInfo.setEnabled(false);
                    this.grantAuthorizationDAO
                    .updateEntity(grantAuthorizationInfo);
                    reportData1.setEnabled(false);
                    this.reportDataDAO.updateEntity(reportData1);
                    this.saveGrantAuthorizationInfo("", name, idcard, phone,
                        home_tel, home_addr, work_tel, couple_phone_num,
                        contact_list, keyId, principal);
                    result.put(HermesConsts.MSG, "用户未授权，报告一未返回");
                    result.put("id", keyId);
                    return result;
                }

                JuxinliDataInfo juxinliReportData = new JuxinliDataInfo();
                juxinliReportData.setReportData1(reportData1);
                result.put(HermesConsts.MSG, juxinliReportData);
                result.put("errormsg",
                    "报告二" + verifyContactList.getErrorMessage());
                result.put("id", keyId);
                return result;
            }
            //查询报告2信息
            ReportData reportData2 = this.reportDataDAO.getReportData(
                grantAuthorizationId, 2);
            //若已有报告2信息，直接返回报告一和报告二
            if (reportData2 != null) {
                JuxinliDataInfo juxinliReportData = new JuxinliDataInfo();
                juxinliReportData.setReportData1(reportData1);
                juxinliReportData.setReportData2(reportData2);
                result.put(HermesConsts.MSG, juxinliReportData);
                if (verifyContactList != null) {
                    Date lastUpdateTime = verifyContactList.getBackTime();
                    Date activeTime = DateUtils.addDays(lastUpdateTime,
                        activeDays);
                    Date now = new Date();
                    if (constraint || now.after(activeTime)) {//强制刷新或者超过有效期需要重新发起报告2的查询
                        //将过期的信息设为无效
                        grantAuthorizationInfo.setEnabled(false);
                        this.grantAuthorizationDAO
                        .updateEntity(grantAuthorizationInfo);
                        reportData1.setEnabled(false);
                        reportData2.setEnabled(false);
                        this.reportDataDAO.updateEntity(reportData1);
                        this.reportDataDAO.updateEntity(reportData2);
                        //发起报告的新查询
                        this.saveGrantAuthorizationInfo("", name, idcard,
                            phone, home_tel, home_addr, work_tel,
                            couple_phone_num, contact_list, keyId, principal);
                        result.put(HermesConsts.MSG, "用户未授权，报告一未返回");
                        result.put("id", keyId);
                        return result;
                    }
                } else {
                    result.put(HermesConsts.MSG, "报告2无验证消息，hermes出错");
                    result.put("id", keyId);
                }
            } else {
                //报告2为空，发起报告二的查询
                result.put(HermesConsts.MSG, "信息验证中，请稍后访问");
                result.put("id", keyId);
                this.sendToQueue(name, idcard, phone, userName,
                    details.getRemoteAddress(), 2, grantAuthorizationId, keyId,
                    home_tel, home_addr, work_tel, couple_phone_num,
                    contact_list, JMSType.JU_XIN_LI_VERIFY);
            }
        }
        return result;

    }

    /**
     * 解析推送的json信息
     *
     * @param jsonInfo
     * @param reportData
     * @param reportNum
     *        报告号 1,2
     * @return
     */
    public Map<String, String> saveReport(JsonInfoTwo jsonInfo,
            ReportData reportData, int reportNum) {
        Map<String, String> result = new HashMap<>();
        //report 报告基本信息
        reportData.setBackTime(new Date());
        reportData.setReportNum(reportNum);
        this.saveEntityDAO.addEntity(reportData);
        //person 申请人信息
        Person person = jsonInfo.getPerson();
        person.set$oid(jsonInfo.get_id());
        person.setReport_data_id(reportData.getId());
        this.saveEntityDAO.addEntity(person);
        reportData.setPerson(person);
        //Behavior_check 数据检查点
        List<BehaviorCheck> behaviorChecks = jsonInfo.getBehavior_check();
        List<DataCheck> dataCheckB = this.convertToDataCheck(behaviorChecks,
            "behavior_check");
        this.saveEntity(dataCheckB, reportData.getId(), "report_data_id");
        //Application_check 数据检查点
        List<ApplicationCheck> applicationChecks = jsonInfo
            .getApplication_check();
        List<DataCheck> dataCheckA = this.convertToDataCheck(applicationChecks,
            "application_check");
        this.saveEntity(dataCheckA, reportData.getId(), "report_data_id");
        //Data_source  绑定数据源信息
        List<DataSource> dataSources = jsonInfo.getData_source();
        this.saveEntity(dataSources, reportData.getId(), "report_data_id");
        //ebusiness_expense 电商月消费
        List<EbusinessExpense> ebusinessExpenses = jsonInfo
            .getEbusiness_expense();
        this.saveEntity(ebusinessExpenses, reportData.getId(), "report_data_id");
        //Recent_need 近期需求
        List<RecentNeed> recentNeeds = jsonInfo.getRecent_need();
        recentNeeds = (List<RecentNeed>) this.saveEntity(recentNeeds,
            reportData.getId(), "report_data_id");
        for (RecentNeed r : recentNeeds) {
            List<DemandsInfo> demandsInfos = r.getDemands_info();
            this.saveEntity(demandsInfos, r.getId(), "recent_need_id");
        }
        //collection_contact 联系人信息
        List<CollectionContact> collectionContacts = jsonInfo
            .getCollection_contact();
        collectionContacts = (List<CollectionContact>) this.saveEntity(
            collectionContacts, reportData.getId(), "report_data_id");
        for (CollectionContact c : collectionContacts) {
            List<ContactDetails> contactDetails = c.getContact_details();
            this.saveEntity(contactDetails, c.getId(), "collection_cantact_id");
        }
        //main_service 常用服务
        /*
         * List<MainService> mainServices = jsonInfo.getMain_service();
         * mainServices = (List<MainService>) this.saveEntity(mainServices,
         * reportData.getId(), "report_data_id");
         * for (MainService m : mainServices) {
         * List<ServiceDetails> serviceDetails = m.getService_details();
         * serviceDetails = (List<ServiceDetails>) this.saveEntity(
         * serviceDetails, m.getId(), "service_details_id");
         * for (ServiceDetails s : serviceDetails) {
         * List<MthDetails> mthDetails = s.getMth_details();
         * this.saveEntity(mthDetails, s.getId(), "service_details_id");
         * }
         * }
         */

        //Contact_region 联系人区域汇总
        List<ContactRegion> contactRegions = jsonInfo.getContact_region();
        this.saveEntity(contactRegions, reportData.getId(), "report_data_id");

        //Trip_info  出行分析
        List<TripInfo> tripInfos = jsonInfo.getTrip_info();
        this.saveEntity(tripInfos, reportData.getId(), "report_data_id");

        //deliver_address 送货地址列表
        List<DeliverAddress> deliverAddress = jsonInfo.getDeliver_address();
        deliverAddress = (List<DeliverAddress>) this.saveEntity(deliverAddress,
            reportData.getId(), "report_data_id");
        for (DeliverAddress d : deliverAddress) {
            List<Receiver> receivers = d.getReceiver();
            for (Receiver r : receivers) {
                String receiver_phone_nums = "";
                for (String s : r.getPhone_num_list()) {
                    receiver_phone_nums = receiver_phone_nums + s + ",";
                }
                r.setReceiver_phone_nums(receiver_phone_nums);
            }
            this.saveEntity(receivers, d.getId(), "deliver_address_id");
        }
        //contact_list 运营商联系人列表
        List<ContactList> contactLists = jsonInfo.getContact_list();
        this.saveEntity(contactLists, reportData.getId(), "report_data_id");
        //cell_behavior 通话行为分析
        List<CellBehavior> cellbehaviors = jsonInfo.getCell_behavior();
        for (CellBehavior c : cellbehaviors) {
            List<Behavior> Behaviors = c.getBehavior();
            this.saveEntity(Behaviors, reportData.getId(), "report_data_id");
        }
        //TODO 没有数据字典personal_cell_collect

        //TODO 第三方返回始终为空unionpay_expense
        return result;

    }

    /**
     * 批量保存实体以及外键关系
     *
     * @param list
     *        需要保存的实体List
     * @param fkID
     *        外键名称
     * @param fkName
     *        外键值
     * @return
     */
    public List<?> saveEntity(List<?> list, Object fkID, String fkName) {
        for (Object o : list) {
            try {
                Field field = o.getClass().getDeclaredField(fkName);
                field.setAccessible(true);
                field.set(o, fkID);
                this.saveEntityDAO.addEntity(o);
            } catch (NoSuchFieldException | SecurityException
                    | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * 转成DateCheck 类型存储
     *
     * @param list
     *        转换的List
     * @param convertFrom
     *        特殊标识
     * @return list
     */
    public List<DataCheck> convertToDataCheck(List<?> list, String convertFrom) {
        List<DataCheck> dataChecks = new ArrayList<>();
        //遍历需要转换的对象
        for (Object o : list) {
            DataCheck d = new DataCheck();
            //获得需要转换的属性
            Field[] fields = o.getClass().getDeclaredFields();
            //将转换前后对象的属性值对应
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                try {
                    Field fieldData = d.getClass().getDeclaredField(fieldName);
                    fieldData.setAccessible(true);
                    Object value = field.get(o);
                    fieldData.set(d, value);
                    //转换后的特殊标识设置
                    Field fieldType = d.getClass().getDeclaredField("type");
                    fieldType.setAccessible(true);
                    fieldType.set(d, convertFrom);
                } catch (NoSuchFieldException | SecurityException
                        | IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            dataChecks.add(d);

        }
        return dataChecks;
    }

    public List<GrantAuthorizationInfo> GrantAuthorizationInfo(String name,
            String idcard, String phone) {
        return this.grantAuthorizationDAO.GrantAuthorizationInfo(name, idcard,
            phone);
    }

    public static void main(String[] args) {
        int count = 1;
        for (int i = 0; i < 80; i++) {
            String url = "https://www.juxinli.com/api/access_report_data?name=赖丕游&idcard=352601196210093515&phone=13806998938&client_secret=26f5ccaf16244b5e95d69f129d0c80b4&access_token=5e7518d5ee534aa4968a9b38b6dfa34d";

            JsonObject json = HttpUtils.getJsonResponse(url);
            if (json.get("success").getAsBoolean()) {
                System.out.println("成功" + count++);
            } else {
                System.out.println(json);
            }
        }
    }
}
