package com.zuicodiing.platform.tester;

import com.zuicodiing.platform.tomcat.http.NettyHttpRequest;
import com.zuicodiing.platform.tomcat.http.NettyHttpResponse;
import com.zuicodiing.platform.tomcat.servlet.BaseHttpServlet;

/**
 * Created by Stephen.lin on 2017/8/15.
 * <p>
 * Description :<p></p>
 */
public class SecondServlet extends BaseHttpServlet {
    @Override
    public void doGet(NettyHttpRequest request, NettyHttpResponse response) {
        super.doGet(request, response);
    }

    @Override
    public void doPost(NettyHttpRequest request, NettyHttpResponse response) {
        super.doPost(request, response);
    }
}
