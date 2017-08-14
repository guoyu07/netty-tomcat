package com.zuicodiing.platform.tomcat.servlet;

import com.zuicodiing.platform.tomcat.http.NettyHttpRequest;
import com.zuicodiing.platform.tomcat.http.NettyHttpResponse;

/**
 * Created by Stephen.lin on 2017/8/14.
 * <p>
 * Description :<p>核心servlet,和 j2EE的servlet标准一样</p>
 */
public abstract class BaseHttpServlet {

    public void init(){

    }

    public void doGet(NettyHttpRequest request, NettyHttpResponse response){

    }

    public void doPost(NettyHttpRequest request,NettyHttpResponse response){

    }

    public void destory(){

    }
}
