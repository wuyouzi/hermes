package com.ucredit.hermes.webservice.gzt;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ucredit.hermes.common.HermesConsts;
import com.ucredit.hermes.utils.DESUtils;

/**
 * 国政通接口访问测试
 * 
 * @author caoming
 */
public class GztTest {

    private static final QName SERVICE_NAME = new QName(
        "http://app.service.validator.businesses.gboss.id5.cn",
        "QueryValidatorServicesService");

    public static void main(String[] args) throws UnsupportedEncodingException,
            Exception {
        URL wsdlURL = QueryValidatorServicesService.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) {
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        System.setProperty("javax.net.ssl.trustStore", "CheckID.keystore");
        QueryValidatorServicesService ss = new QueryValidatorServicesService(
            wsdlURL, GztTest.SERVICE_NAME);
        QueryValidatorServices port = ss.getQueryValidatorServices();
        String single = port.querySingle(
            DESUtils.encode(HermesConsts.GZT_DES_KEY,
                "youzhongxinye".getBytes("GB18030")),
            DESUtils.encode(HermesConsts.GZT_DES_KEY,
                "youzhongxinye_L*H3#h~3".getBytes("GB18030")),
            DESUtils.encode(HermesConsts.GZT_DES_KEY,
                "1A020201".getBytes("GB18030")),
            DESUtils.encode(HermesConsts.GZT_DES_KEY,
                "赵祥,370784198506137414".getBytes("GB18030")));
        String result = DESUtils.decode(HermesConsts.GZT_DES_KEY,
            single.getBytes("GB18030"));
        System.out.println(result);
        Document parseText = DocumentHelper.parseText(result);
        Element rootElement = parseText.getRootElement();
        String photo = rootElement.element("policeCheckInfos")
            .element("policeCheckInfo").elementText("checkPhoto");

//        StringReader reader = new StringReader(result);
//        JAXBContext jaxbContext = JAXBContext.newInstance(Data.class);
//        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
//        Data data = (Data) unmarshaller.unmarshal(reader);
        byte[] decodeBase64 = Base64.decodeBase64(photo);
        @SuppressWarnings("resource")
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
            decodeBase64);
        @SuppressWarnings("resource")
        OutputStream outputStream = new FileOutputStream("c://a.jpg");
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }
}
