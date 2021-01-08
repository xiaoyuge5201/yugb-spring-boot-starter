package com.github.yugb.annotation;


import com.github.yugb.bean.enums.OperatorType;
import com.github.yugb.config.LogConnectionAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 日志拦截注解
 *
 * @author xiaoyuge
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(LogConnectionAutoConfiguration.class)
public @interface YgbLog {

    /**
     * @return 模块名称
     */
    String module() default "";

    /**
     * @return 描述
     */
    String desc() default "";

    /**
     * @return 操作类型，增删改查
     */
    OperatorType type();
}
