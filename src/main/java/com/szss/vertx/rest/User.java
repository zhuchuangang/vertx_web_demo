package com.szss.vertx.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by zcg on 2016/11/14.
 */
@Data
@NoArgsConstructor
public class User implements Serializable{
    private Integer id;
    private String username;
    private Integer age;
    private Boolean gender;
    private String province;

    public User(Integer id,String username,Integer age,Boolean gender,String province){
        this.id=id;
        this.username=username;
        this.age=age;
        this.gender=gender;
        this.province=province;
    }

    public User(String username,Integer age,Boolean gender,String province){
        this.username=username;
        this.age=age;
        this.gender=gender;
        this.province=province;
    }
}
