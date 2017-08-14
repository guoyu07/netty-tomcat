package com.zuicodiing.platform.tomcat.web;

import java.util.List;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p>web.xml对象</p>
 */
public class WebXML {


    private List<ServletXML> servlets;

    public List<ServletXML> getServlets() {
        return servlets;
    }

    public void setServlets(List<ServletXML> servlets) {
        this.servlets = servlets;
    }
}
