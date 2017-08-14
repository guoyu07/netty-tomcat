package com.zuicodiing.platform.tomcat.web.impl;

import com.zuicodiing.platform.tomcat.utils.LogUtil;
import com.zuicodiing.platform.tomcat.web.WebXML;
import com.zuicodiing.platform.tomcat.web.WebXMLParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p>解析 web.xml实现类</p>
 */
public class WebXMLParserImpl extends WebXMLParser {

    private LogUtil log = LogUtil.newLogUtil(WebXMLParserImpl.class);

    private String webFileName = "web.xml";

    private static final String SERVLET= "servlet";

    private static final String SERVLET_NAME = "servlet-name";

    private static final String SERVLET_CLASS = "servlet-class";

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
        File file = new File(webFileName);
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document document = db.parse(file);
            NodeList servletNodes = document.getElementsByTagName(SERVLET);
            parseServletXml(servletNodes);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return webXML;

    }

    private void parseServletXml(NodeList servletNodes){

        for (int i = 0,len = servletNodes.getLength();i < len;i++){
            Node node= servletNodes.item(i);
            if (SERVLET.equals(node.getNodeName())){
                throw new IllegalArgumentException(String.format("this node is'nt servlet node,node:{}",node.getNodeName()));
            }
            String servletName = node.getTextContent();
            if (servletName == null || "".equals(servletName.trim())){
                throw new NullPointerException("servlet name is empty");
            }
            if (log.isDebugEnabled()){
                log.d("servlet name is:{}",servletName);
            }
            if (node.hasChildNodes()){
                throw new IllegalArgumentException(String.format("[%s] servlet has not children",servletName));
            }
            NodeList children = node.getChildNodes();
            for(int j= 0,length = children.getLength(); j <length ;j++){
                Node childNode = children.item(i);
                if (SERVLET_NAME.equals(childNode.getNodeName())){
                    continue;
                }
                if (SERVLET_CLASS.equals(childNode.getNodeName())){
                    continue;
                }
            }
        }
    }
}
