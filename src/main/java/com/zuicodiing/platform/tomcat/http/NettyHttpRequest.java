package com.zuicodiing.platform.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p></p>
 */
public class NettyHttpRequest {

   private ChannelHandlerContext ctx;
   private HttpRequest request;
    public NettyHttpRequest(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public String getMethod(){
        return request.getMethod().name();
    }
    public String getUrl(){
        return request.getUri();
    }

    public Map<String,List<String>> getParameters(){
        QueryStringDecoder decoder = new QueryStringDecoder(request.getUri());
        return decoder.parameters();
    }

    public String getParameter(String name){
        Map<String,List<String>> map = getParameters();
        if (map == null) return null;
        List<String> list = map.get(name);
        if (list == null || list.size() == 0) return null;
        return list.get(0);
    }
}
