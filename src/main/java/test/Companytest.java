package test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ucredit.hermes.enums.CompanyOperateType;
import com.ucredit.hermes.enums.CompanyStatus;
import com.ucredit.hermes.enums.DataChannel;
import com.ucredit.hermes.model.crawl.company.CompanyContactOperateInfos;
import com.ucredit.hermes.model.crawl.company.CompanyInfos;
import com.ucredit.hermes.model.crawl.company.CompanyManagerInfos;
import com.ucredit.hermes.model.crawl.company.CompanyShareholderInfos;
import com.ucredit.hermes.model.pengyuan.CompanyContactOperateInfo;
import com.ucredit.hermes.model.pengyuan.CompanyInfo;
import com.ucredit.hermes.model.pengyuan.CompanyManagerInfo;
import com.ucredit.hermes.model.pengyuan.CompanyShareholderInfo;

public class Companytest {
    public static void main(String[] args) throws ParseException {
        String json = "{\"artificialName\":\"廖建洲\",\"artificialName2\":\"廖建洲\",\"companyContactOperateInfos\":{\"address\":\"广州高新技术产业开发区科学城神舟路9号内（倒班宿舍101、102房） \",\"areaDesc\":\"广州高新技术产业开发区科学城神舟路9号内（倒班宿舍101、102房） \",\"companyInfos_id\":154,\"createTime\":1452753209000,\"id\":100,\"tel\":\"13719487819\",\"type\":\"REGISTER\"},\"companyName\":\"广州南方高科有限公司\",\"companyid\":\"776\",\"corpType_id\":\"1190\",\"createTime\":1452753209000,\"dataChannel\":\"数仓\",\"enabled\":\"1\",\"hasSystemError\":\"FALSE\",\"id\":154,\"ip\":\"776\",\"lastUpdateTime\":1452753209000,\"lendRequestId\":\"\",\"manageBeginDate\":\"1999-06-17\",\"manageEndDate\":\"2015-06-16\",\"manageRange\":\" 电子信息实业投资，开发研制、生产电子信息设备和终端产品、无线电通信设备、机电设备、广播电视设备及相关产品并提供技术咨询服务；批发和零售商品贸易（除国家专控商品外）。企业自产产品及技术的出口；自身生产所需的原辅材料、仪器仪表、机械设备、零部件及技术的进口（国家禁止进出口的商品及技术除外）；承接进料加工和“三来一补”业务。\",\"manageRangeFashion\":\" 电子信息实业投资，开发研制、生产电子信息设备和终端产品、无线电通信设备、机电设备、广播电视设备及相关产品并提供技术咨询服务；批发和零售商品贸易（除国家专控商品外）。企业自产产品及技术的出口；自身生产所需的原辅材料、仪器仪表、机械设备、零部件及技术的进口（国家禁止进出口的商品及技术除外）；承接进料加工和“三来一补”业务。\",\"managerInfolist\":[{\"companyInfos_id\":154,\"createTime\":1452753209000,\"id\":203,\"name\":\"万永乐\",\"position_id\":\"432A\",\"subReportType_id\":\"23\"},{\"companyInfos_id\":154,\"createTime\":1452753209000,\"id\":204,\"name\":\"陈震\",\"position_id\":\"432A\",\"subReportType_id\":\"23\"},{\"companyInfos_id\":154,\"createTime\":1452753209000,\"id\":205,\"name\":\"钟红梅\",\"position_id\":\"432A\",\"subReportType_id\":\"23\"},{\"companyInfos_id\":154,\"createTime\":1452753209000,\"id\":206,\"name\":\"廖建洲\",\"position_id\":\"431A\",\"subReportType_id\":\"23\"},{\"companyInfos_id\":154,\"createTime\":1452753209000,\"id\":207,\"name\":\"朱其志\",\"position_id\":\"432A\",\"subReportType_id\":\"23\"},{\"companyInfos_id\":154,\"createTime\":1452753209000,\"id\":208,\"name\":\"何可玉\",\"position_id\":\"431B\",\"subReportType_id\":\"23\"}],\"openDate\":\"1999-05-31\",\"operationName\":\"123\",\"orgCode\":\" 71632693-8\",\"pengYuanCompanyName\":\"广州南方高科有限公司\",\"queryTime\":1452753209000,\"registDate\":\"1999-05-31\",\"registFund\":\"22307.100000\",\"registFund2\":\"22307.100000\",\"registerDepartment\":\"广州市工商行政管理局萝岗分局\",\"registerNo\":\" 440108000030111\",\"resultType\":\"success\",\"shareholderInfolist\":[{\"companyInfos_id\":154,\"contributiveFund\":\"400.000000\",\"createTime\":1452753209000,\"fundCurrency_id\":\"8\",\"id\":162,\"name\":\"北京华虹集成电路设计有限责任公司\"},{\"companyInfos_id\":154,\"contributiveFund\":\"407.900000\",\"createTime\":1452753209000,\"fundCurrency_id\":\"8\",\"id\":163,\"name\":\"广州市电信实业总公司\"},{\"companyInfos_id\":154,\"contributiveFund\":\"1000.000000\",\"createTime\":1452753209000,\"fundCurrency_id\":\"8\",\"id\":164,\"name\":\"广东凌海投资有限公司\"},{\"companyInfos_id\":154,\"contributiveFund\":\"1631.500000\",\"createTime\":1452753209000,\"fundCurrency_id\":\"8\",\"id\":165,\"name\":\"广州金鹏集团有限公司\"},{\"companyInfos_id\":154,\"contributiveFund\":\"4551.800000\",\"createTime\":1452753209000,\"fundCurrency_id\":\"8\",\"id\":166,\"name\":\"中国电子科技集团公司第七研究所\"},{\"companyInfos_id\":154,\"contributiveFund\":\"14315.900000\",\"createTime\":1452753209000,\"fundCurrency_id\":\"8\",\"id\":167,\"name\":\"广州机电工业资产经营有限公司\"}],\"status\":\"OPENED\",\"status2\":\"OPENED\",\"systemid\":\"admin\",\"tradeCode_id\":\"2122\"}";
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        CompanyInfos com = gson.fromJson(json, CompanyInfos.class);
        CompanyInfo comL = new CompanyInfo();
        comL = (CompanyInfo) Companytest.convertConpany(com, comL);
        comL.setDataChannel(DataChannel.CRAWL);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-mm-dd");
        if (com.getManageBeginDate() != null
                && !"".equals(com.getManageBeginDate())) {
            comL.setManageBeginDate(sf.parse(com.getManageBeginDate()));
        }
        if (com.getManageEndDate() != null
            && !"".equals(com.getManageEndDate())) {
            comL.setManageEndDate(sf.parse(com.getManageEndDate()));
        }
        if (com.getOpenDate() != null && !"".equals(com.getOpenDate())) {
            comL.setOpenDate(sf.parse(com.getOpenDate()));
        }
        if (com.getRegistDate() != null && !"".equals(com.getRegistDate())) {
            comL.setRegistDate(sf.parse(com.getRegistDate()));
        }
        if (com.getRegistFund() != null) {
            comL.setRegistFund(new BigDecimal(com.getRegistFund().trim()));
        }
        if (com.getRegistFund2() != null) {
            comL.setRegistFund2(new BigDecimal(com.getRegistFund2().trim()));
        }
        String comStatus = com.getStatus();
        String comStatus2 = com.getStatus();
        comL.setStatus(CompanyStatus.getCompanyStatus(comStatus));
        comL.setStatus2(CompanyStatus.getCompanyStatus(comStatus2));
        //companyContactOperateInfo赋值
        CompanyContactOperateInfos companyContactOperateInfos = com
            .getCompanyContactOperateInfos();
        CompanyContactOperateInfo companyContactOperateInfo = new CompanyContactOperateInfo();
        companyContactOperateInfo = (CompanyContactOperateInfo) Companytest
            .convertConpany(companyContactOperateInfos,
                companyContactOperateInfo);
        String type = companyContactOperateInfos.getType();
        companyContactOperateInfo.setType(CompanyOperateType
            .getCompanyOperateType(type));
        Set<CompanyContactOperateInfo> companyContactOperateInfosL = new HashSet<>();
        companyContactOperateInfosL.add(companyContactOperateInfo);
        comL.setCompanyContactOperateInfos(companyContactOperateInfosL);

        //managerInfolist 赋值
        List<CompanyManagerInfos> companyManagerInfos = com
            .getManagerInfolist();
        Set<CompanyManagerInfo> companyManagerInfosL = new HashSet<>();
        for (CompanyManagerInfos c : companyManagerInfos) {
            CompanyManagerInfo cl = new CompanyManagerInfo();
            cl = (CompanyManagerInfo) Companytest.convertConpany(c, cl);
            //position赋值

            companyManagerInfosL.add(cl);
        }
        comL.setCompanyManagerInfos(companyManagerInfosL);
        //shareholderInfolist 赋值
        List<CompanyShareholderInfos> shareholderInfolist = com
            .getShareholderInfolist();
        Set<CompanyShareholderInfo> shareholderInfolistL = new HashSet<>();
        for (CompanyShareholderInfos c : shareholderInfolist) {
            CompanyShareholderInfo cl = new CompanyShareholderInfo();
            cl = (CompanyShareholderInfo) Companytest.convertConpany(c, cl);
            String contributiveFund = c.getContributiveFund();
            if (contributiveFund != null) {
                cl.setContributiveFund(new BigDecimal(contributiveFund));
            }
            String contributivePercent = c.getContributivePercent();
            if (contributivePercent != null) {
                cl.setContributivePercent(new BigDecimal(contributivePercent));
            }
            shareholderInfolistL.add(cl);
        }
        comL.setCompanyShareholderInfos(shareholderInfolistL);
        System.out.println(comL);
    }

    public static Object convertConpany(Object crawl, Object local) {
        //获得爬取对象的属性
        Field[] fieldsCrawl = crawl.getClass().getDeclaredFields();
        //获得本地存储对象的属性
        Field[] fieldsCom = local.getClass().getDeclaredFields();
        for (Field field : fieldsCrawl) {
            String fieldName = field.getName();
            for (Field fieldC : fieldsCom) {
                String fieldNameC = fieldC.getName();
                //当crawl对象和本地对象的属性和类型都相同时，值做映射
                if (fieldName.equals(fieldNameC)
                        && field.getType().toString()
                        .equals(fieldC.getType().toString())
                    && !"id".equals(fieldNameC)) {
                    try {
                        Field fieldData = local.getClass().getDeclaredField(
                            fieldNameC);
                        fieldData.setAccessible(true);
                        field.setAccessible(true);
                        Object value = field.get(crawl);
                        fieldData.set(local, value);
                    } catch (NoSuchFieldException | SecurityException
                            | IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return local;
    }
}
