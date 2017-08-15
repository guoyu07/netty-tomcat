package com.zuicodiing.platform.tester;

import com.zuicodiing.platform.tomcat.http.NettyHttpRequest;
import com.zuicodiing.platform.tomcat.http.NettyHttpResponse;
import com.zuicodiing.platform.tomcat.servlet.BaseHttpServlet;
import com.zuicodiing.platform.tomcat.utils.LogUtil;

/**
 * Created by Stephen.lin on 2017/8/15.
 * <p>
 * Description :<p></p>
 */
public class FirstServlet extends BaseHttpServlet {
    private static LogUtil log = LogUtil.newLogUtil(FirstServlet.class);
    @Override
    public void doGet(NettyHttpRequest request, NettyHttpResponse response) {
        doPost(request,response);
    }

    @Override
    public void doPost(NettyHttpRequest request, NettyHttpResponse response) {
        log.i("自己定义的servlet");
        try {
            response.write("自己定义的servlet");
        }catch (Exception e){
            log.e(e);
        }

    }
}
