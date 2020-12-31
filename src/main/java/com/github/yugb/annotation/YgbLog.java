package com.github.yugb.annotation;


import com.github.yugb.bean.enums.OperatorType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 日志拦截注解
 *
 * @author hzz
 */

@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface YgbLog {

    /**
     * 日志记录的事件名称，自定义业务名称或特定名称都行
     *
     * @return 返回结果
     */
    String name();

    /**
     * 操作类型，增删改查
     * @return 返回结果
     */
    OperatorType type();
    /**
     * 操作用户名
     * @return 返回结果
     */
    String username();
    /**
     * 用户id
     * @return 返回结果
     */
    int userId();
}
