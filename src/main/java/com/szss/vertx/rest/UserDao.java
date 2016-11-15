package com.szss.vertx.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcg on 2016/11/14.
 */
public class UserDao {

    private static List<User> users;
    static {
        users=new ArrayList<>();
        users.add(new User(1,"马云",49,true,"浙江"));
        users.add(new User(2,"马化腾",43,true,"广东"));
        users.add(new User(3,"李彦宏",48,true,"北京"));
        users.add(new User(4,"雷军",44,true,"北京"));
        users.add(new User(5,"任正非",70,true,"广东"));
    }

    public Page<User> findAll(){
        Page<User> page=new Page<>();
        page.setRecordsTotal(users.size());
        page.setRecordsFiltered(users.size());
        page.setData(users);
        return page;
    }
}
