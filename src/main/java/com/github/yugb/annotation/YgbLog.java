package com.github.yugb.annotation;


import com.github.yugb.bean.enums.OperatorType;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 日志拦截注解
 *
 * @author hzz
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface YgbLog {

    /**
     * 模块名称
     *
     * @return 返回结果
     */
    String module() default "";

    /**
     * 描述
     * @return
     */
    String desc() default "";

    /**
     * 操作类型，增删改查
     * @return 返回结果
     */
    OperatorType type();
}
