package com.szss.vertx.rest;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * Created by zcg on 2016/11/16.
 */
public class JdbcConfig {
    private static JsonObject config;
    private static JDBCClient client;

    static {
        config = new JsonObject()
                .put("url", "jdbc:mariadb://localhost:3306/test")
                .put("driver_class", "org.mariadb.jdbc.Driver")
                .put("max_pool_size", 10)
                .put("user","root")
                .put("password","123456");
    }

    public static JDBCClient jdbcClient(Vertx vertx) {
        if (client == null) {
            client = JDBCClient.createShared(vertx, config);
        }
        return client;
    }
}
