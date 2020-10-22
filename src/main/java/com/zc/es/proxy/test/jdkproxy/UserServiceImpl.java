package com.zc.es.proxy.test.jdkproxy;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Override
    public void save(String name, String password) {
        System.out.println("用户名："+name+"密码："+password);
    }
}
