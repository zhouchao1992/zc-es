package com.zc.es.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib代理实现 优点：不需要实现任何接口，它是以子类的形式实现代理的。
 *
 * jdk 动态代理缺点：目标类必须要实现某个或多个接口，静态代理也是；
 *
 * 静态代理 缺点： 必须和目标类实现相同的接口，当接口变化时目标类和代理类都需要维护，繁琐，
 * 而且每个接口都要需要重新添加代理类接口多了难维护
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
