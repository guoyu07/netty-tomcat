package com.zuicodiing.platform.tomcat.web;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p>servlet xml定义的对象</p>
 */
public class ServletXML {

    private String servletName;
    private String servletClass;
    private String urlPattern;

    public ServletXML() {
    }

    public ServletXML(String servletName, String servletClass) {
        this.servletName = servletName;
        this.servletClass = servletClass;
    }

    public ServletXML(String servletName, String servletClass, String urlPattern) {
        this.servletName = servletName;
        this.servletClass = servletClass;
        this.urlPattern = urlPattern;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public String getServletClass() {
        return servletClass;
    }

    public void setServletClass(String servletClass) {
        this.servletClass = servletClass;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern) {
        this.urlPattern = urlPattern;
    }
}
