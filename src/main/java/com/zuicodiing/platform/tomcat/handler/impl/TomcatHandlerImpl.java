package com.zuicodiing.platform.tomcat.handler.impl;

import com.zuicodiing.platform.tomcat.handler.TomcatHandler;
import com.zuicodiing.platform.tomcat.servlet.BaseHttpServlet;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p></p>
 */
public class TomcatHandlerImpl extends TomcatHandler {

    /**
     * 我们可以读取 web.xml配置 注册 servlet
     * @param mapping
     */
    @Override
    public void resiger(Map<Pattern, Class<? extends BaseHttpServlet>> mapping) {

    }
}
