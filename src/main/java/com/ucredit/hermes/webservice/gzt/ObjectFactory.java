package com.ucredit.hermes.webservice.gzt;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the cn.id5.gboss.businesses.validator.service.app package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 */
@XmlRegistry
public class ObjectFactory {
    /**
     * Create an instance of {@link QuerySingleResponse }
     *
     * @return
     */
    public QuerySingleResponse createQuerySingleResponse() {
        return new QuerySingleResponse();
    }

    /**
     * Create an instance of {@link QuerySingle }
     *
     * @return
     */
    public QuerySingle createQuerySingle() {
        return new QuerySingle();
    }

    /**
     * Create an instance of {@link QueryBatch }
     *
     * @return
     */
    public QueryBatch createQueryBatch() {
        return new QueryBatch();
    }

    /**
     * Create an instance of {@link QueryBatchResponse }
     *
     * @return
     */
    public QueryBatchResponse createQueryBatchResponse() {
        return new QueryBatchResponse();
    }

}
