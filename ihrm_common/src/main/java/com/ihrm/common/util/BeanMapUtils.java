package com.ihrm.common.util;

import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BeanMapUtils {
    /**
     * 讲对象属性转化为map集合
     * */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key+"", beanMap.get(key));
            }
        }
        return map;
    }






    /**
     * 将map集合中的数据转化为指定对象的同名属性
     * */
    public static <T>T mapToBean(Map<String,Object> map,Class<T> clazz)throws Exception{
        T bean = clazz.getDeclaredConstructor().newInstance();
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;


    }
}
