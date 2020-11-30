package annotation;


import bean.enums.OperatorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志拦截注解
 *
 * @author hzz
 */

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemRequestLog {
    //日志记录的事件名称，自定义业务名称或特定名称都行
    String name();

    //操作类型，增删改查
    OperatorType type();
}
