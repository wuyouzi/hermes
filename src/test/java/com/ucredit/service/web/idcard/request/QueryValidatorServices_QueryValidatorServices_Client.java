package com.ucredit.service.web.idcard.request;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ucredit.service.web.idcard.WSIdCardUtil;
import com.ucredit.service.web.idcard.response.Data;

/**
 * 国政通身份证查询接口类
 * querySingle 单独查询姓名+身份证号,格式为:"姓名,身份证号", 返回结果为是否符合,如果符合返回图片
 * queryBatch 批量查询姓名+身份证号,格式为:"姓名,身份证号;姓名,身份证号", 返回结果为是否符合,如果符合返回图片
 * 两个查询的所有查询参数和返回结果都加密,加密是先des再base64，解密是先base64再des,中文采用GB18030编码
 * 返回结果中的图片是Base64编码,图片有淡蓝色的条纹(公安要求)
 *
 * @author luzongwei
 * @see WSIdCardUtil
 */
@Service
public class QueryValidatorServices_QueryValidatorServices_Client {
    private static Logger log = LoggerFactory
        .getLogger(QueryValidatorServices_QueryValidatorServices_Client.class);
    public static final QName SERVICE_NAME = new QName(
        "http://app.service.validator.businesses.gboss.id5.cn",
        "QueryValidatorServicesService");
    /**
     * 厂商用户名
     */
    public static String username;
    /**
     * 厂商密码
     */
    public static String password;
    /**
     * 身份认证信息
     */
    public static String queryType = "1A020201";
    /**
     * 接口用字符集
     */
    public static final String CHARSET = "GB18030";
    /**
     * 国政通
     */
    public static final String AUTH_METHOD = "guozhengtong";

    static {
        try (InputStream fis = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("idcard_auth.properties");) {
            // 配置文件路径,加载配置文件
            Properties prop = new Properties();
            prop.load(fis);
            // 加载配置
            QueryValidatorServices_QueryValidatorServices_Client.username = prop
                .getProperty("username");
            QueryValidatorServices_QueryValidatorServices_Client.password = prop
                .getProperty("password");
            QueryValidatorServices_QueryValidatorServices_Client.queryType = prop
                .getProperty("query_type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
    }

    public Data querySingle(String realName, String idcardNo) {
        try {
            if (StringUtils.isBlank(realName) || StringUtils.isBlank(idcardNo)) {
                return null;
            }
            //        System.setProperty("javax.net.ssl.trustStore", "CheckID.keystore");
            URL wsdlURL = QueryValidatorServicesService.wsdlLocation;
            QueryValidatorServicesService ss = new QueryValidatorServicesService(
                wsdlURL,
                QueryValidatorServices_QueryValidatorServices_Client.SERVICE_NAME);
            QueryValidatorServices port = ss.getQueryValidatorServices();

            java.lang.String _querySingle_userName = QueryValidatorServices_QueryValidatorServices_Client.username;
            java.lang.String _querySingle_password = QueryValidatorServices_QueryValidatorServices_Client.password;
            java.lang.String _querySingle_type = QueryValidatorServices_QueryValidatorServices_Client.queryType;
            java.lang.String _querySingle_param = realName + "," + idcardNo;
            java.lang.String _querySingle__return = port
                .querySingle(
                    WSIdCardUtil
                        .encode(
                            WSIdCardUtil.DES_KEY,
                            _querySingle_userName
                                .getBytes(QueryValidatorServices_QueryValidatorServices_Client.CHARSET)),
                    WSIdCardUtil.encode(
                        WSIdCardUtil.DES_KEY,
                        _querySingle_password
                            .getBytes(QueryValidatorServices_QueryValidatorServices_Client.CHARSET)),
                    WSIdCardUtil.encode(
                        WSIdCardUtil.DES_KEY,
                        _querySingle_type
                            .getBytes(QueryValidatorServices_QueryValidatorServices_Client.CHARSET)),
                    WSIdCardUtil.encode(
                        WSIdCardUtil.DES_KEY,
                        _querySingle_param
                            .getBytes(QueryValidatorServices_QueryValidatorServices_Client.CHARSET)));
            QueryValidatorServices_QueryValidatorServices_Client.log
                .trace("_querySingle__return:" + _querySingle__return);

            String decodeResult = WSIdCardUtil
                .decode(
                    WSIdCardUtil.DES_KEY,
                    _querySingle__return
                        .getBytes(QueryValidatorServices_QueryValidatorServices_Client.CHARSET));
            QueryValidatorServices_QueryValidatorServices_Client.log
                .debug("querySingle.result=" + decodeResult);
            StringReader reader = new StringReader(decodeResult);

            JAXBContext jc2 = JAXBContext
                .newInstance("com.renrendai.service.web.idcard.response");
            Unmarshaller u = jc2.createUnmarshaller();

            Data responseObj = (Data) u.unmarshal(reader);

            return responseObj;
        } catch (Exception e) {
            QueryValidatorServices_QueryValidatorServices_Client.log
                .error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

}
