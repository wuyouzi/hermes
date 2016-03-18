package com.ucredit.hermes.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.bfd.facade.Biz_industry;
import com.bfd.facade.Educationallevel;
import com.bfd.facade.Marriage;
import com.csvreader.CsvReader;
import com.ucredit.hermes.dao.BaiRongAuthenticationDAO;
import com.ucredit.hermes.dao.BaiRongConsumptionDAO;
import com.ucredit.hermes.dao.BaiRongLocationDAO;
import com.ucredit.hermes.dao.BaiRongMediaDAO;
import com.ucredit.hermes.dao.BaiRongParamsDAO;
import com.ucredit.hermes.dao.BaiRongStabilityDAO;
import com.ucredit.hermes.dto.BaiRongResult;
import com.ucredit.hermes.enums.BaiRongResultCode;
import com.ucredit.hermes.enums.ResultType;
import com.ucredit.hermes.model.BaiRongAuthentication;
import com.ucredit.hermes.model.BaiRongConsumption;
import com.ucredit.hermes.model.BaiRongLocation;
import com.ucredit.hermes.model.BaiRongMedia;
import com.ucredit.hermes.model.BaiRongParams;
import com.ucredit.hermes.model.BaiRongStability;
import com.ucredit.hermes.utils.JmsUtils;

@Service
@Transactional(rollbackFor = ServiceException.class)
public class BaiRongService {
    private static final Logger log = LoggerFactory
            .getLogger(BaiRongService.class);
    @Autowired
    private BaiRongParamsDAO baiRongParamsDAO;

    @Autowired
    private BaiRongStabilityDAO baiRongStabilityDAO;

    @Autowired
    private BaiRongMediaDAO baiRongMediaDAO;

    @Autowired
    private BaiRongConsumptionDAO baiRongConsumptionDAO;

    @Autowired
    private BaiRongAuthenticationDAO baiRongAuthenticationDAO;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Destination bairongJMSQueue;

    @Autowired
    private BaiRongLocationDAO baiRongLocationDAO;

    /**
     * 根据查询参数查询
     *
     * @return BaiRongResult
     * @param baiRongParams
     * @param principal
     * @throws InterruptedException
     */
    @Transactional(rollbackFor = Exception.class)
    public BaiRongResult doSearch(BaiRongParams baiRongParams,
            Principal principal) {
        String user = principal.getName();
        /**
         * 判断必填参数是否有
         */
        String idCard = baiRongParams.getId();
        String phone = baiRongParams.getCell();
        String mail = baiRongParams.getMail();
        if (StringUtils.isBlank(idCard) && StringUtils.isBlank(phone)
            && StringUtils.isBlank(mail)) {
            // 提示参数不足
            BaiRongResult result = new BaiRongResult();
            result.setResultType(ResultType.FAILURE);
            result.setResultCode(BaiRongResultCode.YouXin_1000002);
            result.setResultCodeDesc(BaiRongResultCode.YouXin_1000002
                .getString());
            return result;
        }

        if (StringUtils.isNotBlank(baiRongParams.getEducationallevel())) {
            Educationallevel[] enums = Educationallevel.values();
            Educationallevel educationallevel = null;
            for (Educationallevel item : enums) {
                if (item.name().equals(baiRongParams.getEducationallevel())) {
                    educationallevel = item;
                    break;
                }
            }
            if (educationallevel == null) {
                // 提示学历参数 不对
                BaiRongResult result = new BaiRongResult();
                result.setResultType(ResultType.FAILURE);
                result.setResultCode(BaiRongResultCode.YouXin_1000004);
                result.setResultCodeDesc(BaiRongResultCode.YouXin_1000004
                    .getString());
                return result;
            }
        }

        if (StringUtils.isNotBlank(baiRongParams.getBiz_industry())) {
            Biz_industry[] enums = Biz_industry.values();
            Biz_industry biz_industry = null;
            for (Biz_industry item : enums) {
                if (item.name().equals(baiRongParams.getBiz_industry())) {
                    biz_industry = item;
                    break;
                }
            }
            if (biz_industry == null) {
                // 提示 参数 不对
                BaiRongResult result = new BaiRongResult();
                result.setResultType(ResultType.FAILURE);
                result
                    .setResultCode(BaiRongResultCode.YouXin_Biz_INDUSTRY_ERROR);
                result
                    .setResultCodeDesc(BaiRongResultCode.YouXin_Biz_INDUSTRY_ERROR
                        .getString());
                return result;
            }
        }
        if (StringUtils.isNotBlank(baiRongParams.getMarriage())) {
            Marriage[] enums = Marriage.values();
            Marriage marriage = null;
            for (Marriage item : enums) {
                if (item.name().equals(baiRongParams.getMarriage())) {
                    marriage = item;
                    break;
                }
            }
            if (marriage == null) {
                // 提示 参数 不对
                BaiRongResult result = new BaiRongResult();
                result.setResultType(ResultType.FAILURE);
                result.setResultCode(BaiRongResultCode.YouXin_Marriage_ERROR);
                result
                    .setResultCodeDesc(BaiRongResultCode.YouXin_Marriage_ERROR
                        .getString());
                return result;
            }
        }

        List<BaiRongParams> result_baiRongParams = this.baiRongParamsDAO
            .getBaiRongParamsBySearchParams(baiRongParams);
        Date now = new Date();
        if (!CollectionUtils.isEmpty(result_baiRongParams)) {
            /**
             * 查询时已经排序，第一条是最新的记录
             */
            BaiRongParams dbbaiRongParams = result_baiRongParams.get(0);
            // 与当前月份比较，如果超过30天的结果则重新查询，并把当前所有的查询到的结果设置为enable=false;
            Date createTime = dbbaiRongParams.getCreateTime();
            // 创建时间加上30天为过期时间
            Date expireTime = DateUtils.addDays(createTime, 30);
            // 如果没有过期
            if (now.before(expireTime)) {
                switch (dbbaiRongParams.getResultType()) {
                    case QUERY:
                        // 判断状态，如果状态是查询中，且创建时间超过10分钟则重新查询，避免因为MAQ没有启动产生脏数据
                        Date tenMinuteLater = DateUtils.addMinutes(createTime,
                            10);
                        if (now.before(tenMinuteLater)) {
                            return this.assembleUtil(dbbaiRongParams);
                        }
                        dbbaiRongParams.setEnabled(false);
                        this.baiRongParamsDAO.updateEntity(dbbaiRongParams);

                        // 向jms发请求
                        // 异步请求重新查询，原因为上次查询超过10分钟且再次请求了相同查询
                        BaiRongService.log.info("---------------bairong查询入mq1");
                        this.sendMessage(baiRongParams, user);
                        break;
                    default:
                        if (!dbbaiRongParams.getResultCode().equals(
                            BaiRongResultCode.BaiRong_00)) {
                            //异步请求查询第三方
                            dbbaiRongParams.setEnabled(false);
                            this.baiRongParamsDAO.updateEntity(dbbaiRongParams);
                            baiRongParams.setCreateTime(now);
                            baiRongParams.setEnabled(true);
                            baiRongParams.setResultType(ResultType.QUERY);
                            this.baiRongParamsDAO.addEntity(baiRongParams);
                            BaiRongService.log
                                .info("---------------bairong查询入mq2");
                            this.sendMessage(baiRongParams, user);
                        }
                        if (dbbaiRongParams.getKeyid() == null) {
                            dbbaiRongParams.setKeyid(baiRongParams.getKeyid());
                        }
                        return this.assembleUtil(dbbaiRongParams);
                }
            } else {
                // 如果没有过期，以往的都设置为enable=false，重新查询
                for (BaiRongParams item : result_baiRongParams) {
                    item.setEnabled(false);
                    this.baiRongParamsDAO.updateEntity(item);
                }
            }
        }

        baiRongParams.setCreateTime(now);
        baiRongParams.setEnabled(true);
        baiRongParams.setResultType(ResultType.QUERY);

        this.baiRongParamsDAO.addEntity(baiRongParams);
        // 向jms发请求
        // this.sendMessage(baiRongParams);
        //异步请求查询第三方
        BaiRongService.log.info("---------------bairong查询入mq3");
        this.sendMessage(baiRongParams, user);

        return this.assembleUtil(baiRongParams);
    }

    private BaiRongResult assembleUtil(BaiRongParams dbbaiRongParams) {
        BaiRongResult result = new BaiRongResult();
        ResultType resultType = dbbaiRongParams.getResultType();
        Integer paramID = dbbaiRongParams.getDbId();
        result.setResultType(resultType);
        result.setResultCode(dbbaiRongParams.getResultCode());
        result.setResultCodeDesc(dbbaiRongParams.getResultCodeDesc());
        result.setSearchId(paramID);

        if (resultType.equals(ResultType.SUCCESS)
            && dbbaiRongParams.getResultCode() != BaiRongResultCode.BaiRong_100002) {

            List<BaiRongAuthentication> baiRongAuthentications = this.baiRongAuthenticationDAO
                .getBaiRongAuthenticationByParamId(paramID);
            List<BaiRongLocation> locations = this.baiRongLocationDAO
                .getBaiRongLocationByParamId(paramID);

            List<BaiRongStability> baiRongStabilitys = this.baiRongStabilityDAO
                .getBaiRongStabilityByParamId(paramID);
            List<BaiRongConsumption> baiRongConsumptions = this.baiRongConsumptionDAO
                .getBaiRongConsumptionByParamId(paramID);

            List<BaiRongMedia> baiRongMedias = this.baiRongMediaDAO
                .getBaiRongMediaByParamId(paramID);

            if (!CollectionUtils.isEmpty(baiRongAuthentications)) {
                BaiRongAuthentication authentication = baiRongAuthentications
                    .get(0);
                result.setAuthentication(authentication);
            }
            result.setLocations(locations);
            if (!CollectionUtils.isEmpty(baiRongStabilitys)) {
                BaiRongStability baiRongStability = baiRongStabilitys.get(0);
                result.setStability(baiRongStability);
            }
            result.setConsumptions(baiRongConsumptions);
            result.setMedias(baiRongMedias);

        }
        return result;
    }

    public void sendMessage(final BaiRongParams dbPaiRongParams) {
        this.sendMessage(dbPaiRongParams, null);
    }

    /**
     * @param dbPaiRongParams
     * @param systemId
     * @author ray
     */
    public void sendMessage(final BaiRongParams dbPaiRongParams,
            final String systemId) {
        TransactionSynchronizationManager
            .registerSynchronization(new TransactionSynchronizationAdapter() {
                /**
                 * 提交前向jms发请求，避免因为maq没有启动出现脏数据
                 */
                @Override
                public void afterCommit() {
                    try {
                        BaiRongService.this.jmsTemplate.send(
                            BaiRongService.this.bairongJMSQueue,
                            new MessageCreator() {
                                @Override
                                public Message createMessage(Session session)
                                        throws JMSException {
                                    Message msg = session
                                        .createObjectMessage(new Object[] { dbPaiRongParams });
                                    if (StringUtils.isNotEmpty(systemId)) {
                                        msg.setStringProperty(
                                            JmsUtils.JMS_MESSAGE_SELECTOR_STRING_KEY,
                                            systemId);
                                        return msg;
                                    } else {
                                        return msg;
                                    }
                                }
                            });
                    } catch (Exception e) {
                        /**
                         * 记录jms异常
                         */
                        BaiRongParams newdbPaiRongParams = (BaiRongParams) BaiRongService.this.baiRongParamsDAO
                            .getById(dbPaiRongParams.getDbId(),
                                dbPaiRongParams.getClass());
                        newdbPaiRongParams.setResultType(ResultType.FAILURE);
                        newdbPaiRongParams
                            .setResultCode(BaiRongResultCode.YouXin_1000003);
                        newdbPaiRongParams
                            .setResultCodeDesc(BaiRongResultCode.YouXin_1000003
                                .getString());
                        BaiRongService.this.baiRongParamsDAO
                            .updateEntity(newdbPaiRongParams);

                        // throw e;
                    }

                }
            });
    }

    /**
     * 统计一段时间内查询百融公司数据的次数
     *
     * @param startDate_
     * @param endDate_
     * @return Integer
     */
    public Long doStatistics(Date startDate_, Date endDate_) {

        return this.baiRongParamsDAO.doStatistics(startDate_, endDate_);
    }

    /**
     * 根据百融公司给的流水进行对帐比较
     *
     * @param startDate_
     * @param endDate_
     * @param file
     * @return HSSFWorkbook
     * @throws IOException
     */
    public HSSFWorkbook doRtatisticsexport(Date startDate_, Date endDate_,
            MultipartFile file) throws IOException {
        HSSFWorkbook workbook = null;

        // 创建工作簿实例
        workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("统计报告");
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        HSSFRow firstRow = sheet.createRow((short) 0);// 建立新行

        CsvReader parse = new CsvReader(file.getInputStream(), ',',
            Charset.forName("GBK"));
        BaiRongService.createCell(firstRow, 0, null, Cell.CELL_TYPE_STRING,
            "流水号");
        BaiRongService.createCell(firstRow, 1, null, Cell.CELL_TYPE_STRING,
            "百融的结果");
        BaiRongService.createCell(firstRow, 2, null, Cell.CELL_TYPE_STRING,
            "友信的结果");

        int skipindex = 1;
        int readIndex = 0;
        int righteNo = 0;
        int failureNo = 0;
        while (parse.readRecord()) {
            readIndex++;
            if (readIndex <= skipindex) {
                continue;
            }
            HSSFRow newRow = sheet.createRow(readIndex);// 建立新行

            String[] values = parse.getValues();

            String switchNo = values[1];// 流水号
            String baiRongResult = values[2];// 百融结果
            BaiRongParams dbPaiRongParams = this.baiRongParamsDAO
                .getBySwitchNo(switchNo, startDate_, endDate_);

            BaiRongService.createCell(newRow, 0, null, Cell.CELL_TYPE_STRING,
                switchNo);
            BaiRongService.createCell(newRow, 1, null, Cell.CELL_TYPE_STRING,
                baiRongResult);

            if (dbPaiRongParams == null) {
                failureNo++;
                BaiRongService.createCell(newRow, 2, null,
                    Cell.CELL_TYPE_STRING, "没有查到");
            } else {
                ResultType resultType = dbPaiRongParams.getResultType();
                String ourStatus = "匹配成功";
                if (resultType != ResultType.SUCCESS) {
                    ourStatus = "其他";
                }
                BaiRongService.createCell(newRow, 2, null,
                    Cell.CELL_TYPE_STRING, ourStatus);

                if (ourStatus.equals(StringUtils.trim(baiRongResult))) {
                    righteNo++;
                } else {
                    failureNo++;
                }
            }
        }
        readIndex++;
        HSSFRow lastRow = sheet.createRow((short) readIndex);// 建立新行

        BaiRongService.createCell(lastRow, 0, null, Cell.CELL_TYPE_STRING,
            "合计：");
        BaiRongService.createCell(lastRow, 1, null, Cell.CELL_TYPE_STRING,
            String.format("相同的结果：%d", righteNo));
        BaiRongService.createCell(lastRow, 2, null, Cell.CELL_TYPE_STRING,
            String.format("不相同的结果：%d", failureNo));
        return workbook;
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
     * 根据Id查询返回结果
     *
     * @param id
     * @return BaiRongResult
     */
    public BaiRongResult getBaiRongResultById(Integer id) {
        BaiRongParams dbbaiRongParams = (BaiRongParams) this.baiRongParamsDAO
            .getById(id, BaiRongParams.class);
        if (dbbaiRongParams == null) {
            BaiRongResult result = new BaiRongResult();
            result.setResultType(ResultType.FAILURE);
            result.setResultCode(BaiRongResultCode.YouXin_GET_NULL_BY_ID_ERROR);
            result
                .setResultCodeDesc(BaiRongResultCode.YouXin_GET_NULL_BY_ID_ERROR
                    .getString());
            return result;
        }
        return this.assembleUtil(dbbaiRongParams);
    }

    /**
     * 根据keyid查询返回结果
     *
     * @param keyid
     * @return BaiRongResult
     */
    public BaiRongResult getBaiRongResultByKeyId(String keyid) {
        BaiRongParams dbbaiRongParams = this.baiRongParamsDAO
            .getBaiRongParamsByKeyid(keyid);
        if (dbbaiRongParams == null) {
            BaiRongResult result = new BaiRongResult();
            result.setResultType(ResultType.FAILURE);
            result.setResultCode(BaiRongResultCode.YouXin_GET_NULL_BY_ID_ERROR);
            result
                .setResultCodeDesc(BaiRongResultCode.YouXin_GET_NULL_BY_ID_ERROR
                    .getString());
            return result;
        }
        return this.assembleUtil(dbbaiRongParams);
    }

}
