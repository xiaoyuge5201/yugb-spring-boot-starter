package aspect;

import annotation.SystemRequestLog;
import bean.RequestLog;
import bean.enums.OperatorType;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import service.RequestLogService;
import util.InsertLogThread;
import util.LoggerUtil;
import util.UpdateLogThread;

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
   private Logger log = LoggerFactory.getLogger(getClass());
   
   private static final ThreadLocal<RequestLog> logThreadLocal =  new NamedThreadLocal<RequestLog>("AspectLog");

    @Autowired(required = false)
   HttpServletRequest request;
   
   @Autowired(required=false)
   private RequestLogService requestLogService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
   
   //申明一个切点 里面是 execution表达式
   @Pointcut("@annotation(com.sd.log.annotation.SystemRequestLog)")
   private void RequestAspect(){}
 
 
   //请求method前打印内容
   @Before(value = "RequestAspect()")
   public void methodBefore(JoinPoint joinPoint){
       try {
           UserDetails userDetails = (UserDetails) SecurityContextHolder
                   .getContext().getAuthentication().getPrincipal();
           String username = userDetails.getUsername();
           RequestLog logObj = new RequestLog();
           logObj.setCreate_date(new Date());
           logObj.setOperater_username(username);
           logThreadLocal.set(logObj);
           log.debug("@Before:日志拦截对象：{}",logObj.toString());
       } catch (Exception ex) {

       }
   }
   
   @After("RequestAspect()")
   public synchronized void doAfter(JoinPoint joinPoint) {
	  RequestLog logObj = logThreadLocal.get();
      if(logObj != null) {
    	  logObj.setRequestUri(request.getRequestURL().toString());
          logObj.setMethod(request.getMethod());
          logObj.setRemoteAddr(LoggerUtil.getCliectIp(request));
          logObj.setLog_type("info");
          logObj.setName(getNameInfo(joinPoint));
          logObj.setOperater_type(getTypeInfo(joinPoint));
          Map<String, String[]> parameterMap = request.getParameterMap();
          logObj.setMapToParams(parameterMap);
          threadPoolTaskExecutor.execute(new InsertLogThread(logObj, requestLogService));
          log.debug("@After:日志拦截对象：{}",logObj.toString());
      }
   }
 
 
/*   //在方法执行完结后打印返回内容
   @AfterReturning(returning = "o",pointcut = "RequestAspect()")
   public void methodAfterReturing(Object o ){
	   log.info("@AfterReturning:日志拦截对象：{}");
	  RequestLog logObj = logThreadLocal.get();
	  if(logObj != null) {
		  String result = (String)o;
		  logObj.setResultParams(result);
		  log.info("@AfterReturning:日志拦截对象：{}",logObj.toString());
	  }
   }*/
   
   /**
    *  异常通知 记录操作报错日志
    * @param joinPoint
    * @param e
    */
   @AfterThrowing(pointcut = "RequestAspect()", throwing = "e")
   public  void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
	   log.error("进入日志切面异常通知,异常信息为：{}",e.getMessage());
	   RequestLog logObj = logThreadLocal.get();
	   if(logObj != null) {
		   logObj.setLog_type("error");
		   logObj.setException(e.toString());
		   threadPoolTaskExecutor.execute(new UpdateLogThread(logObj, requestLogService));
		   log.error("@AfterThrowing:日志拦截对象：{}",logObj.toString());
	   }
   }
   
   public static String getNameInfo(JoinPoint point) {
	   MethodSignature signature = (MethodSignature)point.getSignature();
	   Method method = signature.getMethod();
	   SystemRequestLog systemRequestLog = method.getAnnotation(SystemRequestLog.class);
	   String name = systemRequestLog.name();
	   return name;
   }
   
   public static String getTypeInfo(JoinPoint point) {
	   MethodSignature signature = (MethodSignature)point.getSignature();
	   Method method = signature.getMethod();
	   SystemRequestLog systemRequestLog = method.getAnnotation(SystemRequestLog.class);
	   OperatorType type = systemRequestLog.type();
	   String name = "";
	   switch(type) {
		   case Create :
			   name="增加操作";break;
		   case Update :
			   name="修改操作";break;
		   case Delete :
			   name="删除操作";break;
		   case Retrieve :
			   name="检索操作";break;
           case LOGIN :
               name="登录操作";break;
           case DownLoad :
               name="下载操作";break;
           case UpLoad :
               name="上传操作";break;
           case PAGE :
               name="进入页面操作";break;
           case COMMAND :
               name="指令下发操作";break;
	   }
	   return name;
   }
}