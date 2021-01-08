package com.github.yugb.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.yugb.annotation.YgbLog;
import com.github.yugb.bean.RequestLog;
import com.github.yugb.bean.enums.OperatorType;
import com.github.yugb.dao.RequestLogDao;
import com.github.yugb.util.DateUtils;
import com.github.yugb.util.InsertLogThread;
import com.github.yugb.util.LoggerUtil;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 记录某些比较重要的用户请求并存入到数据库
 * @author xiaoyuge
 */
@Aspect
@Component
@Order(1)
public class LogCollectAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final ThreadLocal<RequestLog> logThreadLocal = new NamedThreadLocal<RequestLog>("YgbLog");

    @Autowired(required = false)
    HttpServletRequest request;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RequestLogDao requestLogDao;

    /**
     * 申明一个切点 里面是 execution表达式
     */
    @Pointcut("@annotation(com.github.yugb.annotation.YgbLog)")
    public void RequestAspect() {
        System.out.println("-----point cut----");
    }


    /**
     * 请求method前打印内容
     */
    @Before(value = "RequestAspect()")
    public void methodBefore(JoinPoint joinPoint) {
        try {
            RequestLog logObj = new RequestLog();
            logObj.setCreateDate(DateUtils.formatToYmdhms());
            logThreadLocal.set(logObj);
            logger.debug("@Before:日志拦截对象：{}", logObj.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @After("RequestAspect()")
    public synchronized void doAfter(JoinPoint joinPoint) {
        RequestLog logObj = logThreadLocal.get();
        if (logObj != null) {
            logObj.setRequestUri(request.getRequestURL().toString());
            logObj.setMethod(request.getMethod());
            logObj.setRemoteAddr(LoggerUtil.getCliectIp(request));
            logObj.setLogType("info");
            logObj = getTypeInfo(joinPoint, logObj);
            Map<String, String[]> parameterMap = request.getParameterMap();
            logObj.setMapToParams(parameterMap);
            threadPoolTaskExecutor.execute(new InsertLogThread(logObj, requestLogDao));
            logger.debug("@After:日志拦截对象：{}", logObj.toString());
        }
    }

    /**
     *  *  异常通知 记录操作报错日志
     *  * @param joinPoint
     *  * @param e
     */
    @AfterThrowing(pointcut = "RequestAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        logger.error("进入日志切面异常通知,异常信息为：{}", e.getMessage());
        RequestLog logObj = logThreadLocal.get();
        if (logObj != null) {
            logObj.setLogType("error");
            logObj.setRequestUri(request.getRequestURL().toString());
            logObj.setMethod(request.getMethod());
            logObj.setRemoteAddr(LoggerUtil.getCliectIp(request));
            logObj.setException(e.toString());
            logObj = getTypeInfo(joinPoint, logObj);
            threadPoolTaskExecutor.execute(new InsertLogThread(logObj, requestLogDao));
            logger.error("@AfterThrowing:日志拦截对象：{}", logObj.toString());
        }
    }

    /**
     * 解析注解参数
     *
     * @param point 切入点
     * @param logObject 对象
     * @return 返回结果
     */
    public static RequestLog getTypeInfo(JoinPoint point, RequestLog logObject) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        YgbLog ygbLog = method.getAnnotation(YgbLog.class);
        OperatorType type = ygbLog.type();
        switch (type) {
            case Create:
                logObject.setOperatorType("增加操作");
                break;
            case Update:
                logObject.setOperatorType("修改操作");
                break;
            case Delete:
                logObject.setOperatorType("删除操作");
                break;
            case Retrieve:
                logObject.setOperatorType("检索操作");
                break;
            case LOGIN:
                logObject.setOperatorType("登录操作");
                break;
            case DownLoad:
                logObject.setOperatorType("下载操作");
                break;
            case UpLoad:
                logObject.setOperatorType("上传操作");
                break;
            case PAGE:
                logObject.setOperatorType("进入页面操作");
                break;
            case COMMAND:
                logObject.setOperatorType("指令下发操作");
                break;
            case API:
                logObject.setOperatorType("接口调用");
                break;
            case TOKEN:
                logObject.setOperatorType("获取TOKEN");
                break;
            case CHECK:
                logObject.setOperatorType("验证");
                break;
            default:
                break;
        }
        //下面两个方法在没有使用JSF的项目中是没有区别的
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        //从session里面获取对应的值
        String username = (String) requestAttributes.getAttribute("username", RequestAttributes.SCOPE_SESSION);
        logObject.setModule(ygbLog.module());
        logObject.setDescription(ygbLog.desc());
        logObject.setUsername(username);
        return logObject;
    }

    /**
     * 获取注解中传递的动态参数的参数值
     *
     * @param joinPoint 切入点
     * @param name 名称
     * @return 返回结果
     */
    public String getAnnotationValue(JoinPoint joinPoint, String name) {
        String paramName = name;
        // 获取方法中所有的参数
        Map<String, Object> params = getParams(joinPoint);
        // 参数是否是动态的:#{paramName}
        if (paramName.matches("^#\\{\\D*\\}")) {
            // 获取参数名
            paramName = paramName.replace("#{", "").replace("}", "");
            // 是否是复杂的参数类型:对象.参数名
            if (paramName.contains(".")) {
                String[] split = paramName.split("\\.");
                // 获取方法中对象的内容
                Object object = getValue(params, split[0]);
                // 转换为JsonObject
                if (object != null) {
                    JSONObject jsonObject = JSON.parseObject(object.toString());
                    Object o = jsonObject.get(split[1]);
                    return String.valueOf(o);
                }
                return null;
            }
            // 简单的动态参数直接返回
            return String.valueOf(getValue(params, paramName));
        }
        // 非动态参数直接返回
        return name;
    }

    /**
     * 根据参数名返回对应的值
     *
     * @param map 对象
     * @param paramName 参数名称
     * @return 返回结果
     */
    public Object getValue(Map<String, Object> map, String paramName) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals(paramName)) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 获取方法的参数名和值
     *
     * @param joinPoint
     * @return 返回结果
     */
    public Map<String, Object> getParams(JoinPoint joinPoint) {
        Map<String, Object> params = new HashMap<String, Object>(8);
        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] names = signature.getParameterNames();
        for (int i = 0; i < args.length; i++) {
            params.put(names[i], args[i]);
        }
        return params;
    }
}