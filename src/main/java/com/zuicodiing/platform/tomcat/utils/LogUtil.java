package com.zuicodiing.platform.tomcat.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Stephen.lin on 2017/7/20.
 * <p>
 * Description :<p></p>
 */
public class LogUtil {

    private Logger logger;

    private LogUtil(Class clazz){
        logger = LoggerFactory.getLogger(clazz);
    }
    private LogUtil(String name){
        logger = LoggerFactory.getLogger(name);
    }

    public static LogUtil newLogUtil(Class clazz){

        return new LogUtil(clazz);
    }

    public static LogUtil newLogUtil(String clazz){

        return new LogUtil(clazz);
    }

    public boolean isDebugEnabled(){

        return logger.isDebugEnabled();
    }

    public void d(String log){
        logger.debug(log);
    }
    public void d(String log,Object... args){
        logger.debug(log, args);
    }

    public void d(String log,Throwable thr){
        logger.debug(log, thr);
    }

    public void i(String log){
        logger.info(log);
    }

    public void i(String log,Object... args){
        logger.info(log, args);
    }

    public void i(String log,Throwable thr){
        logger.info(log, thr);
    }

    public void e(String log){
        logger.error(log);
    }

    public void e(Throwable thr){
        logger.error(null,thr);
    }

    public void e(String log,Throwable thr){
        logger.error(log, thr);
    }

    public void e(String log,Object... args){
        logger.error(log, args);
    }
}
