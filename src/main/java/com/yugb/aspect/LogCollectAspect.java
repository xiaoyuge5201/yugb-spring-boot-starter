package com.yugb.aspect;

import com.yugb.annotation.SystemRequestLog;
import com.yugb.bean.RequestLog;
import com.yugb.bean.enums.OperatorType;
import com.yugb.dao.RequestLogDao;
import com.yugb.util.InsertLogThread;
import com.yugb.util.LoggerUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * log采集，记录某些比较重要的用户请求并存入到数据库
 * @author hzz
 *
 */
@Aspect
@Component
@Order(1)
public class LogCollectAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());
   
    private static final ThreadLocal<RequestLog> logThreadLocal =  new NamedThreadLocal<RequestLog>("AspectLog");

    @Autowired(required = false)
    HttpServletRequest request;
   
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RequestLogDao requestLogDao;
   
   //申明一个切点 里面是 execution表达式
   @Pointcut("@annotation(annotation.SystemRequestLog)")
   public void RequestAspect(){}
 
 
   //请求method前打印内容
   @Before(value = "RequestAspect()")
   public void methodBefore() {
       try {
           RequestLog logObj = new RequestLog();
           logObj.setCreate_date(new Date());
           logThreadLocal.set(logObj);
           logger.debug("@Before:日志拦截对象：{}", logObj.toString());
       } catch (Exception ex) {
           ex.printStackTrace();
       }
   }
   
   @After("RequestAspect()")
   public synchronized void doAfter(JoinPoint joinPoint) {
	  RequestLog logObj = logThreadLocal.get();
      if(logObj != null) {
          logObj.setRequest_uri(request.getRequestURL().toString());
          logObj.setMethod(request.getMethod());
          logObj.setRemote_addr(LoggerUtil.getCliectIp(request));
          logObj.setLog_type("info");
          logObj = getTypeInfo(joinPoint, logObj);
          Map<String, String[]> parameterMap = request.getParameterMap();
          logObj.setMapToParams(parameterMap);
          threadPoolTaskExecutor.execute(new InsertLogThread(logObj, requestLogDao));
          logger.debug("@After:日志拦截对象：{}", logObj.toString());
      }
   }

   /**
    *  异常通知 记录操作报错日志
    * @param joinPoint
    * @param e
    */
   @AfterThrowing(pointcut = "RequestAspect()", throwing = "e")
   public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
       logger.error("进入日志切面异常通知,异常信息为：{}", e.getMessage());
	   RequestLog logObj = logThreadLocal.get();
	   if(logObj != null) {
		   logObj.setLog_type("error");
           logObj.setRequest_uri(request.getRequestURL().toString());
           logObj.setMethod(request.getMethod());
           logObj.setRemote_addr(LoggerUtil.getCliectIp(request));
           logObj.setLog_type("info");
		   logObj.setException(e.toString());
           logObj = getTypeInfo(joinPoint, logObj);
           threadPoolTaskExecutor.execute(new InsertLogThread(logObj, requestLogDao));
           logger.error("@AfterThrowing:日志拦截对象：{}", logObj.toString());
	   }
   }

    /**
     * 解析注解参数
     *
     * @param point
     * @param logObject
     * @return
     */
    public static RequestLog getTypeInfo(JoinPoint point, RequestLog logObject) {
	   MethodSignature signature = (MethodSignature)point.getSignature();
	   Method method = signature.getMethod();
	   SystemRequestLog systemRequestLog = method.getAnnotation(SystemRequestLog.class);
	   OperatorType type = systemRequestLog.type();
	   switch(type) {
           case Create:
               logObject.setOperater_type("增加操作");
               break;
           case Update:
               logObject.setOperater_type("修改操作");
               break;
           case Delete:
               logObject.setOperater_type("删除操作");
               break;
           case Retrieve:
               logObject.setOperater_type("检索操作");
               break;
           case LOGIN:
               logObject.setOperater_type("登录操作");
               break;
           case DownLoad:
               logObject.setOperater_type("下载操作");
               break;
           case UpLoad:
               logObject.setOperater_type("上传操作");
               break;
           case PAGE:
               logObject.setOperater_type("进入页面操作");
               break;
           case COMMAND:
               logObject.setOperater_type("指令下发操作");
               break;
       }
        logObject.setName(systemRequestLog.name());
        logObject.setOperater_username(systemRequestLog.username());
        return logObject;
   }
}