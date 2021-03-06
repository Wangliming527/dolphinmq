package com.flowyun.dolphinmq.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
/**
 * Bean和Map类型转换工具类
 * @author  Barry
 * @since  2021/7/6 10:24
 **/
public final class BeanMapUtils {

    /**
     * Converts a map to a JavaBean.
     *
     * @param type type to convert
     * @param map  map to convert
     * @return JavaBean converted
     * @throws IntrospectionException    failed to get class fields
     * @throws IllegalAccessException    failed to instant JavaBean
     * @throws InstantiationException    failed to instant JavaBean
     * @throws InvocationTargetException failed to call setters
     */
    public static final Object toBean(Class<?> type, Map<Object, ? extends Object> map)
            throws IntrospectionException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        Object obj = type.getDeclaredConstructor().newInstance();
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            Object propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {
                Object value = map.get(propertyName);
                Object[] args = new Object[1];
                args[0] = value;
                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * Converts a JavaBean to a map.
     *
     * @param bean JavaBean to convert
     * @return map converted
     * @throws IntrospectionException    failed to get class fields
     * @throws IllegalAccessException    failed to instant JavaBean
     * @throws InvocationTargetException failed to call setters
     */
    public static final Map<String, Object> toMap(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }

    /**
     * 把Map<String, Object> map 转成Map<Object, Object>
     *
     * @param map 待转map
     * @return 转换后map
     * @author Barry
     * @since 2021/7/1 10:35
     **/
    public static final Map<Object, Object> getObjectObjectMap(Map<String, Object> map) {
        Map<Object, Object> omap = new HashMap<>();
        map.forEach((str, obj) -> {
            omap.put((Object) str, obj);
        });
        return omap;
    }
}