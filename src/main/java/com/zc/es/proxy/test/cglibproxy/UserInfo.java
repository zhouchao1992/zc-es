package com.zc.es.proxy.test.cglibproxy;

public class UserInfo {
    private String name;
    private String password;

    public   void show(String name ,String password){
        this.name =name;
        this.password=password;
        System.out.println("name-->"+name+",password-->"+password);
    }
}
