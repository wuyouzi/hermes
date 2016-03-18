package com.ucredit.hermes.webservice.gzt;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

/**
 * This class was generated by Apache CXF 2.7.10
 * 2014-04-22T11:41:08.160+08:00
 * Generated source version: 2.7.10
 */
public final class QueryValidatorServices_QueryValidatorServices_Client {

    private static final QName SERVICE_NAME = new QName(
        "http://app.service.validator.businesses.gboss.id5.cn",
        "QueryValidatorServicesService");

    private QueryValidatorServices_QueryValidatorServices_Client() {
    }

    public static void main(String args[]) {
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

        QueryValidatorServicesService ss = new QueryValidatorServicesService(
            wsdlURL,
            QueryValidatorServices_QueryValidatorServices_Client.SERVICE_NAME);
        QueryValidatorServices port = ss.getQueryValidatorServices();

        System.out.println("Invoking querySingle...");
        String _querySingle_userName = "";
        String _querySingle_password = "";
        String _querySingle_type = "";
        String _querySingle_param = "";
        String _querySingle__return = port.querySingle(_querySingle_userName,
            _querySingle_password, _querySingle_type, _querySingle_param);
        System.out.println("querySingle.result=" + _querySingle__return);

        System.out.println("Invoking queryBatch...");
        String _queryBatch_userName = "";
        String _queryBatch_password = "";
        String _queryBatch_type = "";
        String _queryBatch_param = "";
        String _queryBatch__return = port.queryBatch(_queryBatch_userName,
            _queryBatch_password, _queryBatch_type, _queryBatch_param);
        System.out.println("queryBatch.result=" + _queryBatch__return);
    }
}
