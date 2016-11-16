package com.szss.vertx.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcg on 2016/11/15.
 */
@Data
@NoArgsConstructor
public class Page<T> {
    /**
     * 当前页号
     */
    private Integer draw;
    /**
     * 总记录数
     */
    private Integer recordsTotal;
    /**
     * 过滤后的记录数
     */
    private Integer recordsFiltered;
    /**
     * 数据
     */
    private List<T> data=new ArrayList<T>();

}