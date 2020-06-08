package com.yx.tx.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Author jiangyunxiong
 * @Date 2020/6/7 7:22 PM
 */
public class SpringApplicationHolder implements ApplicationContextAware {

    private static ApplicationContext context = null;

    public synchronized static Object getBean(String beanName){
        return context.getBean(beanName);
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
