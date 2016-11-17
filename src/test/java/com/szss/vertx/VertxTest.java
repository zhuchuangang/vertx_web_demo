package com.szss.vertx;

import io.vertx.core.json.JsonArray;
import org.junit.Test;

/**
 * Created by zcg on 2016/11/16.
 */
public class VertxTest {

    @Test
    public void test(){
        JsonArray params = new JsonArray().add("adsf").add(2).add("ads").add(false);
        System.out.println(params);
    }
}
