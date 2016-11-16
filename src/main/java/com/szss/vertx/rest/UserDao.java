package com.szss.vertx.rest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcg on 2016/11/14.
 */
public class UserDao {

    public static List<User> users;

    static {
        users=new ArrayList<>();
        users.add(new User(0,"马云",49,true,"浙江"));
        users.add(new User(1,"马化腾",43,true,"广东"));
        users.add(new User(2,"李彦宏",48,true,"北京"));
        users.add(new User(3,"雷军",44,true,"北京"));
        users.add(new User(4,"任正非",70,true,"广东"));
    }

    public Page<User> findAll(){
        Page<User> p=new Page<User>();
        p.setRecordsTotal(users.size());
        p.setRecordsFiltered(users.size());
        p.setData(users);
        return p;
    }

    public void addUser(User user){
        users.add(user);
    }
}
