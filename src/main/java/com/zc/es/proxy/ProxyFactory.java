package com.zc.es.proxy;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理实现方式（jdk代理）
 */
public class ProxyFactory {
    /**
     * 需要代理的对象
     */
    private Object target;

    public ProxyFactory(Object object) {
        this.target = object;
    }

    public  Object getProxyInstance(){
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                StringBuilder param= new StringBuilder();
                for (Object arg : args) {
                    param.append(arg);
                    param.append(",");
                }
                System.out.println("请求参数："+param.toString());
                //执行目标对象方法
                Object returnValue = method.invoke(target, args);
                return returnValue;
            }
        });
    }
}
