package com.zuicodiing.platform.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.Map;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p></p>
 */
public class NettyHttpResponse {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public NettyHttpResponse(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public void  write(String msg) throws Exception {
        write(msg,HttpResponseStatus.OK);
    }

    public void write(String msg, HttpResponseStatus status) throws Exception {

        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(msg.getBytes("UTF-8")));
        //设置header
        HttpHeaders headers = request.headers();
        for (Map.Entry<String, String> header : headers) {
            System.err.println(String.format("heander-name:%s,header-value:%s", header.getKey(),header.getValue()));
            response.headers().add(headers);
        }

        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }
        ctx.writeAndFlush(response);

    }
}
