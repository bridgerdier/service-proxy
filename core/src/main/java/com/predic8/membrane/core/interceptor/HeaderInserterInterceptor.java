package com.predic8.membrane.core.interceptor;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.predic8.membrane.annot.MCAttribute;
import com.predic8.membrane.annot.MCElement;
import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Header;
import com.predic8.membrane.core.http.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description Inserts an arbitrary (constant) header
 * @topic 4. Interceptors/Features
 */
@MCElement(name="headerInsert")
public class HeaderInserterInterceptor extends AbstractInterceptor {
    private static final Logger log = LoggerFactory.getLogger(HeaderInserterInterceptor.class);

    private String name;
    private String value;
    private static final String jsonpathPrefix = "jsonpath:";

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {
        handleMessage(exc.getRequest());
        return Outcome.CONTINUE;
    }

    @Override
    public Outcome handleResponse(Exchange exc) throws Exception {
        handleMessage(exc.getResponse());
        return Outcome.CONTINUE;
    }

    private void handleMessage(Message msg) {
        if (msg == null)
            return;
        log.debug("Inserting HTTP header " + name);
        Header header = new Header();
        if (value.startsWith(jsonpathPrefix)) {
            log.debug("JSONPath parsing active");
            String jsonPathExpression = value.substring(jsonpathPrefix.length());
            DocumentContext jsonContext = JsonPath.parse(msg.getBody().toString());
            header.add(name, jsonContext.read(jsonPathExpression));
        } else {
            header.add(name, value);
        }
        msg.setHeader(header);
    }


    @MCAttribute
    public void setName(String name) {
        this.name = name;
    }

    @MCAttribute
    public void setValue(String value) {
        this.value = value;
    }
}
