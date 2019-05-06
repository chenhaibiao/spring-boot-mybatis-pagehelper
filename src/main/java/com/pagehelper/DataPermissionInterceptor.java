package com.pagehelper;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Mybatis 拦截器-数据权限
 *
 * @author hb
 **/

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
})
public class DataPermissionInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(DataPermissionInterceptor.class);

    private static final String PARAM_KEY = "address";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        RowBounds rowBounds = (RowBounds) args[2];
        ResultHandler resultHandler = (ResultHandler) args[3];
        Executor executor = (Executor) invocation.getTarget();
        CacheKey cacheKey;
        BoundSql boundSql;
        DataPermission dataPermission = getDataPermission(ms);
        logger.info("dataPermission: {}", dataPermission);
        if (null != dataPermission) {
            parameter = processParam(parameter);
        }
        logger.info("parameter: {}", parameter);
        //由于逻辑关系，只会进入一次
        if (args.length == 4) {
            //4 个参数时
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            //6 个参数时
            cacheKey = (CacheKey) args[4];
            boundSql = (BoundSql) args[5];
        }

        //TODO 自己要进行的各种处理
        return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 获取方法上注解
     *
     * @param mappedStatement
     * @return
     */
    private DataPermission getDataPermission(MappedStatement mappedStatement) throws ClassNotFoundException {
        DataPermission dataPermission = null;
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1, id.length());
        final Class clzz = Class.forName(className);
        final Method[] methods = clzz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.isAnnotationPresent(DataPermission.class)) {
                dataPermission = method.getAnnotation(DataPermission.class);
                break;
            }
        }
        return dataPermission;
    }

    private Object processParam(Object parameterObject) throws IllegalAccessException {
        // 处理参数对象  如果是 map 且map的key 中没有 PARAM_KEY，添加到参数map中
        // 如果参数是bean，反射设置值
        if (parameterObject instanceof Map) {
            ((Map) parameterObject).putIfAbsent(PARAM_KEY, "1");
        } else {
            parameterObject = objectToMap(parameterObject);
            ((Map) parameterObject).putIfAbsent(PARAM_KEY, "1");
        }
        return parameterObject;
    }

    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        if (null == obj) {
            return map;
        }
        Class<?> clazz = obj.getClass();
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            if (fieldList.size() > 0) {
                for (Field field : fieldList) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object value = field.get(obj);
                    if (!map.containsKey(fieldName)) {
                        map.put(fieldName, value);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }

        return map;
    }
}

