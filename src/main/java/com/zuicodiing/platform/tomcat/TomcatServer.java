package com.zuicodiing.platform.tomcat;

import com.zuicodiing.platform.tomcat.handler.WebXmlTomcatHandler;
import com.zuicodiing.platform.tomcat.utils.LogUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p></p>
 */
public class TomcatServer {

    private LogUtil log = LogUtil.newLogUtil(TomcatServer.class);

    private int port = 8080;
    private ServerBootstrap bootstrap;
    private ChannelFuture future;
    private EventLoopGroup bossGroup, workGroup;

    private WebXmlTomcatHandler webXmlTomcatHandler;

    public TomcatServer() {

    }

    public TomcatServer setPort(int port){
        this.port = port;
        return this;
    }

    public TomcatServer build() throws Exception {

        webXmlTomcatHandler = new WebXmlTomcatHandler();
        webXmlTomcatHandler.registeServlets();

        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();
        //server 处理过程
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new HttpResponseEncoder());
                        sc.pipeline().addLast(new HttpRequestDecoder());
                       sc.pipeline().addLast(webXmlTomcatHandler);

                    }


                })
                //配置工作线程数为128个
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        future = bootstrap.bind(this.port).sync();

        return this;
    }


    public void start() throws Exception {
        try {
            log.i("server started by port is:{}",this.port);
            //开启服务
            future.channel().closeFuture().sync();

        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            log.i("server shutdown success...");
        }

    }




    public static void main(String[] args) throws Exception {
        new TomcatServer().build().start();
    }
}
