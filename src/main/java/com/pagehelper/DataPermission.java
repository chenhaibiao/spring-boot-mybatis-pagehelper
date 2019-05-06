package com.pagehelper;

import java.lang.annotation.*;

/**
 * @author hb
 * @description 数据权限自定义注解
 * @create 2018-10-24-下午2:00
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

}
