package com.zuicodiing.platform.tomcat.web;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p>servlet xml定义的对象</p>
 */
public class ServletXML {

    private String servetName;
    private String servletClass;

    public ServletXML() {
    }

    public ServletXML(String servetName, String servletClass) {
        this.servetName = servetName;
        this.servletClass = servletClass;
    }

    public String getServetName() {
        return servetName;
    }

    public void setServetName(String servetName) {
        this.servetName = servetName;
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }
}
