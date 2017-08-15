package com.zuicodiing.platform.tomcat.handler;

import com.zuicodiing.platform.tomcat.http.HttpMethod;
import com.zuicodiing.platform.tomcat.http.NettyHttpRequest;
import com.zuicodiing.platform.tomcat.http.NettyHttpResponse;
import com.zuicodiing.platform.tomcat.servlet.BaseHttpServlet;
import com.zuicodiing.platform.tomcat.utils.LogUtil;
import com.zuicodiing.platform.tomcat.web.ServletXML;
import com.zuicodiing.platform.tomcat.web.WebXML;
import com.zuicodiing.platform.tomcat.web.WebXMLParser;
import com.zuicodiing.platform.tomcat.web.impl.WebXMLParserImpl;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p></p>
 */
@ChannelHandler.Sharable
public  class WebXmlTomcatHandler extends ChannelInboundHandlerAdapter {

    private LogUtil log = LogUtil.newLogUtil(WebXmlTomcatHandler.class);

    private Map<Pattern,Class<? extends BaseHttpServlet>> mapping = new HashMap<>();

    private WebXMLParser webXMLParser = new WebXMLParserImpl();

    private static final String CONTENT_TYPE = "text/html";

    private static final String DEFAULT_HTML="<html><body>%s</body></html>";

    public WebXmlTomcatHandler() {
    }

    public WebXmlTomcatHandler(WebXMLParser webXMLParser) {
        this.webXMLParser = webXMLParser;
    }

    /**
     * 注册 servlet
     */
    public void registeServlets(){
        try {
            WebXML webXML = webXMLParser.parse();
            List<ServletXML> servlets = webXML.getServlets();
            for (ServletXML servlet : servlets) {
                mapping.put(Pattern.compile(servlet.getUrlPattern()),
                        (Class<? extends BaseHttpServlet>) Class.forName(servlet.getServletClass()));
            }
            log.i("finish registe servlet...");
        }catch (Exception e){
            log.e(e);
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest r  = (HttpRequest)msg;
            if (!r.headers().contains(HttpHeaders.Names.CONTENT_TYPE)){
                r.headers().set(HttpHeaders.Names.CONTENT_TYPE,CONTENT_TYPE);
            }
            NettyHttpRequest request = new NettyHttpRequest(ctx,r);
            NettyHttpResponse response = new NettyHttpResponse(ctx,r);
            boolean hasMatched = false;
            HttpResponseStatus status = HttpResponseStatus.OK;
            String url = request.getUrl();
            String method = request.getMethod();
            if (log.isDebugEnabled()){
                log.d("http url is : {},http method is :{}",url,method);

            }
            for (Map.Entry<Pattern, Class<? extends BaseHttpServlet>> entry : mapping.entrySet()) {
                if (entry.getKey().matcher(url).matches()){
                    hasMatched = true;
                    BaseHttpServlet servlet = entry.getValue().newInstance();
                    if (log.isDebugEnabled()){
                        log.d("metched servlet class is:{}",servlet.getClass().toString());
                    }
                    if (HttpMethod.GET.name().equalsIgnoreCase(method)){
                        servlet.doGet(request,response);
                       if (log.isDebugEnabled()){
                           log.d("finished the get method...");
                       }
                        return;
                    }
                    if (HttpMethod.POST.name().equalsIgnoreCase(method)){
                        if (log.isDebugEnabled()){
                            log.d("finished the post method...");
                        }
                        servlet.doPost(request,response);
                        return;
                    }
                }
            }
            //找不到相对应的 servlet,则需要 抛出 404 异常

            if (!hasMatched){
                response.write(String.format(DEFAULT_HTML, String.format("404 not found page,by url:%s,method:%s", url,method)), HttpResponseStatus.NOT_FOUND);
            }
            return;
        }
        if(msg instanceof LastHttpContent)
        {
            HttpContent content = (HttpContent)msg;
            ByteBuf buf = content.content();
            System.out.println(buf.toString(io.netty.util.CharsetUtil.UTF_8));
            buf.release();
            ctx.write(content);
            ctx.flush();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE); //当flush完成后关闭channel ;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.e(cause);
        ctx.close();
    }

    public WebXMLParser getWebXMLParser() {
        return webXMLParser;
    }

    public void setWebXMLParser(WebXMLParser webXMLParser) {
        this.webXMLParser = webXMLParser;
    }


}
