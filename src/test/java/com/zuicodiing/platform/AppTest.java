package com.zuicodiing.platform;

import com.zuicodiing.platform.tomcat.TomcatServer;
import com.zuicodiing.platform.tomcat.web.WebXMLParser;
import com.zuicodiing.platform.tomcat.web.impl.WebXMLParserImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private TomcatServer server;
    @Before
    public void before() throws Exception {
        WebXMLParser parser = new WebXMLParserImpl();

        server = new TomcatServer()
                .build();

    }

    @Test
    public void start() throws Exception {
        server.start();
    }

}
