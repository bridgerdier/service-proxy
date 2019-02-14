package com.predic8.membrane.interceptor;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.HeaderField;
import com.predic8.membrane.core.http.Request;
import com.predic8.membrane.core.interceptor.HeaderInserterInterceptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HeaderInterceptorTest {

    HeaderInserterInterceptor interceptor;

    @Before
    public void setUp() throws Exception {
        interceptor = new HeaderInserterInterceptor();
    }

    @Test
    public void constantInsertionTest() throws Exception{
        Exchange exc = (new Request.Builder()).body("").buildExchange();

        interceptor.setName("foo");
        interceptor.setValue("42");
        interceptor.handleRequest(exc);

        HeaderField[] allHeaderFields = exc.getRequest().getHeader().getAllHeaderFields();
        assertEquals("foo", allHeaderFields[0].getHeaderName().toString());
        assertEquals("42", allHeaderFields[0].getValue());
    }

    @Test
    public void jsonPathBasicInsertionTest() throws Exception{
        Exchange exc = (new Request.Builder()).body("{\"bar\": \"23\", \"python\": [\"Eric\", \"Graham\", \"John\", \"Michael\", \"Terry\", \"Terry\"]}").buildExchange();

        interceptor.setName("X-foo");
        interceptor.setValue("jsonpath:$['bar']");
        interceptor.handleRequest(exc);

        HeaderField[] allHeaderFields = exc.getRequest().getHeader().getAllHeaderFields();
        assertEquals("X-foo", allHeaderFields[0].getHeaderName().toString());
        assertEquals("23", allHeaderFields[0].getValue());
    }


    @After
    public void tearDown() throws Exception {

    }
}
