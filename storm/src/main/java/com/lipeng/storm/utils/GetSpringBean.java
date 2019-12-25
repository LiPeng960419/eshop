package com.lipeng.storm.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author: lipeng 910138
 * @Date: 2019/12/25 9:58
 */
public class GetSpringBean implements ApplicationContextAware {

    private static ApplicationContext context;

    public static Object getBean(String name) {
        return context.getBean(name);
    }

    public static <T> T getBean(Class<T> c) {

        return context.getBean(c);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        if (applicationContext != null) {
            context = applicationContext;
        }
    }

}