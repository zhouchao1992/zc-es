package com.zc.es.asyn;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AsynImportData {

    /**
     * 异步执行的方法，主线程执行到该方法的时候不会等待，直接往下执行
     */
    @Async
    void importData(){
        System.out.println("11111111111");
    }

    /**
     * 定时任务
     */
    //@Scheduled(cron = "0/1 * * * * ?")
    void demo(){
        System.out.println("执行定时任务！");
    }

    //@PostConstruct
    void  show(){
       // importData();
        //putV();
        //remV();
        listset();
        linkedList();
    }

    ConcurrentHashMap<String, String> val = new ConcurrentHashMap<>();

    @Async
   // @Scheduled(cron = "0/1 * * * * ? ")
    void putV(){
        for (int i = 0; i < 5 ; i++) {
            val.put(i+"","添加数据："+i);
            System.out.println("添加数据："+i);
        }
        System.out.println("集合size为："+val.size());
    }

    @Async
   // @Scheduled(cron = "0/1 * * * * ? ")
    void remV(){
        for (int i = 0; i < 5 ; i++) {
            System.out.println("移除数据："+val.get(i+""));
            val.remove(i+"");
        }
        System.out.println("集合size为："+val.size());
    }

    @Async
    void listset(){
        long starttime = System.currentTimeMillis();
        ArrayList list = new ArrayList<String>();
        for (int i = 0; i <10000 ; i++) {
            list.add(i+"-->list集合测试数据");
        }
        System.out.println(list.size());
        System.out.println("list测试数据添加耗时："+(System.currentTimeMillis()-starttime));
        list.forEach(val-> System.out.println(val));
    }

    @Async
    void linkedList(){
        LinkedList<String> linkedList = new LinkedList();
        long starttime = System.currentTimeMillis();
        for (int i = 0; i <10000 ; i++) {
            linkedList.add(i+"-->linkedList集合测试数据");
        }
        System.out.println(linkedList.size());
        System.out.println("linkedList测试数据添加耗时："+(System.currentTimeMillis()-starttime));
    }

    public static void main(String[] args) {
        Map hashmap = new HashMap();
        hashmap.put(null,null);
        System.out.println(hashmap.get(null));
        TreeMap<String, String> map = new TreeMap<>();
        map.put("1151212",null);
        System.out.println(map.get(null));
    }
}
