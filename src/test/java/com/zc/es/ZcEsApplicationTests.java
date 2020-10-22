package com.zc.es;

import com.zc.es.proxy.CgLibProxyFactory;
import com.zc.es.proxy.ProxyFactory;
import com.zc.es.proxy.test.cglibproxy.UserInfo;
import com.zc.es.proxy.test.jdkproxy.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ZcEsApplicationTests {
    @Resource
    IUserService userService;
    /**
     * jdk动态代理测试
     */
    @Test
    public  void jdkproxytest(){
        IUserService proxyInstance = (IUserService)new ProxyFactory(userService).getProxyInstance();
        proxyInstance.save("zhouchao","123456");
    }

    /**
     * cglib动态代理测试
     */
    @Test
    public  void cglibproxy(){
        UserInfo proxyInstance = (UserInfo)new CgLibProxyFactory(new UserInfo()).getProxyInstance();
        proxyInstance.show("zhouchao","123456");
    }
}
