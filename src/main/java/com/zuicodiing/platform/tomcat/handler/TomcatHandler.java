package com.zuicodiing.platform.tomcat.handler;

import com.zuicodiing.platform.tomcat.http.HttpMethod;
import com.zuicodiing.platform.tomcat.http.NettyHttpRequest;
import com.zuicodiing.platform.tomcat.http.NettyHttpResponse;
import com.zuicodiing.platform.tomcat.servlet.BaseHttpServlet;
import com.zuicodiing.platform.tomcat.utils.LogUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p></p>
 */
public abstract class TomcatHandler extends ChannelInboundHandlerAdapter {

    private LogUtil log = LogUtil.newLogUtil(TomcatHandler.class);

    private Map<Pattern,Class<? extends BaseHttpServlet>> mapping = new HashMap<>();

    public TomcatHandler(){
       resiger(mapping);

    }

    /**
     * 注册 url 和serverlet的映射
     * @param mapping
     */
    public abstract void   resiger(Map<Pattern,Class<? extends BaseHttpServlet>> mapping);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            HttpRequest r  = (HttpRequest)msg;
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
                        continue;
                    }
                    if (HttpMethod.POST.name().equalsIgnoreCase(method)){
                        if (log.isDebugEnabled()){
                            log.d("finished the post method...");
                        }
                        servlet.doPost(request,response);
                    }
                    continue;
                }
            }
            //找不到相对应的 servlet,则需要 抛出 404 异常

            if (!hasMatched){
                status = HttpResponseStatus.NOT_FOUND;
            }

            response.write(String.format("404 not found by url:%s,http method:%s",url,method), status);
            return;
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.e(cause);
        ctx.close();
    }

}
