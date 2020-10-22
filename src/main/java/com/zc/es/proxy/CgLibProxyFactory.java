package com.zc.es.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib代理实现
 */
public class CgLibProxyFactory  implements MethodInterceptor {
    private  Object target;

    public CgLibProxyFactory(Object target) {
        this.target = target;
    }

    /**
     * 创建代理对象
     * @return
     */
    public Object getProxyInstance(){
        //1.工具类
        Enhancer en = new Enhancer();
        //2.设置父类
        en.setSuperclass(target.getClass());
        //3.设置回调函数
        en.setCallback(this);
        //4.创建子类(代理对象)
        return en.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        StringBuilder param= new StringBuilder();
        for (Object arg : args) {
            param.append(arg);
            param.append(",");
        }
        System.out.println("请求参数："+param.toString());
        //执行目标对象的方法
        Object returnValue = method.invoke(target, args);
        return returnValue;
    }
}
