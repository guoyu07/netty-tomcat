package com.zuicodiing.platform.tomcat.web.impl;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
import com.zuicodiing.platform.tomcat.utils.LogUtil;
import com.zuicodiing.platform.tomcat.web.ServletXML;
import com.zuicodiing.platform.tomcat.web.WebXML;
import com.zuicodiing.platform.tomcat.web.WebXMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p>解析 web.xml实现类</p>
 */
public class WebXMLParserImpl extends WebXMLParser {

    private LogUtil log = LogUtil.newLogUtil(WebXMLParserImpl.class);

    private String webFileName = "web.xml";

    private static final String WEB_APP = "web-app";
    private static final String SERVLET= "servlet";

    private static final String SERVLET_NAME = "servlet-name";

    private static final String SERVLET_CLASS = "servlet-class";

    private static final String SERVLET_MAPPING = "servlet-mapping";
    private static final String URL_PATTERN = "url-pattern";



    private DocumentBuilderFactory dbf = null;
    private DocumentBuilder db = null;

    public WebXMLParserImpl() {

        init();
    }

    private void init(){
        try {
            dbf = DocumentBuilderFactory.newInstance();
            db = dbf.newDocumentBuilder();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public WebXMLParserImpl(String webFileName) {
        this.webFileName = webFileName;
    }

    /**
     * <code>
     *    &lt;servlet>
            &lt;servlet-name>springServlet &lt;/servlet-name>
            &lt;servlet-class>org.springframework.web.servlet.DispatcherServlet &lt;/servlet-class>
            &lt;init-param>
                 &lt;param-name>contextConfigLocation &lt;/param-name>
                 &lt;param-value>classpath:servlet-mvc.xml &lt;/param-value>
            &lt;/init-param>
            &lt;load-on-startup>1 &lt;/load-on-startup>
            &lt;/servlet>
     * </code>
     */
    @Override
    public WebXML parse() {
        WebXML webXML = new WebXML();
        try {
            Document document = db.parse(load());
            NodeList webAppNode = document.getElementsByTagName(WEB_APP);
            if (webAppNode.getLength() != 1){
                throw new IllegalArgumentException("web.xml file error,by web-app node need one");
            }
            NodeList servletNodes = document.getElementsByTagName(SERVLET);
            NodeList servletMappingNodes = document.getElementsByTagName(SERVLET_MAPPING);
            Map<String,String> urlMapping = parseServletMappingXml(servletMappingNodes);
            List<ServletXML> servlets = parseServletXml(servletNodes);
            for (int i = 0; i < servlets.size(); i++) {
                ServletXML servlet = servlets.get(i);
                servlets.get(i).setUrlPattern(urlMapping.get(servlet.getServletName()));
            }
            webXML.setServlets(servlets);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return webXML;

    }



    private File load() throws URISyntaxException {
        URL url =this.getClass().getClassLoader().getResource(webFileName);

        return new File(url.toURI());
    }

    private List<ServletXML> parseServletXml(NodeList servletNodes){
        List<ServletXML> servlets = new ArrayList<>(servletNodes.getLength());
        ServletXML servlet = null;
        for (int i = 0,len = servletNodes.getLength();i < len;i++){

            Node node= servletNodes.item(i);
            if (!SERVLET.equals(node.getNodeName())){
                throw new IllegalArgumentException(String.format("this node is'nt servlet node,node:{}",node.getNodeName()));
            }
            if (!node.hasChildNodes()){
                throw new IllegalArgumentException("servlet has not children");
            }

            NodeList servletNameNodes = ((DeferredElementImpl)node).getElementsByTagName(SERVLET_NAME);
            if (servletNameNodes.getLength() != 1){
                throw new IllegalArgumentException("servlet node muste need one servlet-name node");
            }
            NodeList servletClassNodes = ((DeferredElementImpl)node).getElementsByTagName(SERVLET_CLASS);
            if (servletClassNodes.getLength() != 1){
                throw new IllegalArgumentException("servlet node muste need one servlet-class node");
            }
            servlet = new ServletXML(servletNameNodes.item(0).getTextContent(),
                    servletClassNodes.item(0).getTextContent());
            servlets.add(servlet);


        }
        return servlets;
    }
    private Map<String,String> parseServletMappingXml(NodeList nodeList){

        Map<String,String> map = new HashMap<>();
        for (int i = 0,len = nodeList.getLength(); i < len; i++){
            Node node = nodeList.item(i);
            if (!SERVLET_MAPPING.equalsIgnoreCase(node.getNodeName())){
                throw new IllegalArgumentException("this node is'nt servlet-mapping node,by node is:"+node.getNodeName());
            }
            if (!node.hasChildNodes()){
                throw new IllegalArgumentException("servlet-mapping has not children node");
            }
            NodeList servletNameNodes = ((DeferredElementImpl)node).getElementsByTagName(SERVLET_NAME);
            if (servletNameNodes.getLength() != 1){
                throw new IllegalArgumentException("servlet-mapping node must need one servlet-name node ");
            }
            NodeList urlPatternNodes = ((DeferredElementImpl)node).getElementsByTagName(URL_PATTERN);
            if (urlPatternNodes.getLength() != 1){
                throw new IllegalArgumentException("servlet-mapping node muste need one url-pattern node");
            }

            map.put(servletNameNodes.item(0).getTextContent(),
                    urlPatternNodes.item(0)
                            .getTextContent()
                            .replaceAll("\\*",".*"));

        }


        return map;
    }
}
