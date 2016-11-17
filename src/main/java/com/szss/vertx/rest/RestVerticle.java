package com.szss.vertx.rest;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by zcg on 2016/11/14.
 */
public class RestVerticle extends AbstractVerticle {
    private Logger log= LoggerFactory.getLogger(RestVerticle.class);

    private UserDao userDao = new UserDao();

    private JDBCClient jdbcClient;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        jdbcClient=JdbcConfig.jdbcClient(vertx);
        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/html").end("Hello World!");
        });

        router.route("/assets/*").handler(StaticHandler.create("assets"));
        //webjar资源映射
        router.route("/webjars/*").handler(StaticHandler.create("META-INF/resources/webjars"));

        router.get("/api/users").handler(this::findAll);

        router.route("/api/user*").handler(BodyHandler.create());
        router.post("/api/user").handler(this::addUser);
        router.put("/api/user/:id").handler(this::updateUser);
        router.delete("/api/user/:id").handler(this::deleteUser);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                startFuture.complete();
                            }
                            if (result.failed()) {
                                startFuture.fail(result.cause());
                            }
                        });

    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        super.stop(stopFuture);
    }


    private void findAll(RoutingContext routingContext) {
        jdbcClient.getConnection(conn -> {
            if (conn.failed()) {
                System.err.println(conn.cause().getMessage());
                return;
            }
            final SQLConnection connection = conn.result();
            String sql="select id,username,age,province,gender from t_user";
            connection.query(sql,res -> {
                if (res.succeeded()) {
                    ResultSet result = res.result();
                    Page<JsonObject> page=new Page();
                    page.setData(result.getRows());
                    page.setRecordsTotal(result.getNumRows());
                    page.setRecordsFiltered(result.getNumRows());
                    HttpServerResponse response = routingContext.response();
                    response.putHeader("content-type", "application/json,charset=utf-8")
                            .end(Json.encodePrettily(page));
                } else {
                    // Failed!
                }
            });
        });
    }

    private void addUser(RoutingContext routingContext){
        HttpServerRequest request=routingContext.request();
        String username=request.getParam("username");
        Integer age=Integer.valueOf(request.getParam("age"));
        String province=request.getParam("province");
        Boolean gender=Boolean.valueOf(request.getParam("gender"));

        jdbcClient.getConnection(conn -> {
            if (conn.failed()) {
                System.err.println(conn.cause().getMessage());
                return;
            }
            final SQLConnection connection = conn.result();
            String sql="insert into t_user(username,age,province,gender) values (?,?,?,?)";
            JsonArray params = new JsonArray().add(username).add(age).add(province).add(gender);
            connection.updateWithParams(sql, params,res -> {
                if (res.succeeded()) {
                    UpdateResult result = res.result();
                    HttpServerResponse response = routingContext.response();
                    response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                            .end("{\"success\":true}");
                } else {
                    // Failed!
                }
            });
        });
    }

    private void updateUser(RoutingContext routingContext){
        String id = routingContext.request().getParam("id");
        User user=Json.decodeValue(routingContext.getBodyAsString(),User.class);

        jdbcClient.getConnection(conn -> {
            if (conn.failed()) {
                System.err.println(conn.cause().getMessage());
                return;
            }
            final SQLConnection connection = conn.result();
            String sql="update t_user set username=?,age=?,province=?,gender=? where id=?";
            JsonArray params = new JsonArray().add(user.getUsername()).add(user.getAge()).add(user.getProvince()).add(user.getGender()).add(id);
            connection.updateWithParams(sql, params,res -> {
                if (res.succeeded()) {
                    UpdateResult result = res.result();
                    HttpServerResponse response = routingContext.response();
                    if (result.getUpdated()>0) {
                        response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                                .end("{\"success\":true}");
                    }else{
                        response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                                .end("{\"success\":false}");
                    }
                } else {
                    // Failed!
                }
            });
        });
    }

    private void deleteUser(RoutingContext routingContext){
        String id = routingContext.request().getParam("id");
        jdbcClient.getConnection(conn -> {
            if (conn.failed()) {
                System.err.println(conn.cause().getMessage());
                return;
            }
            final SQLConnection connection = conn.result();
            String sql="delete from t_user where id=?";
            JsonArray params = new JsonArray().add(id);
            connection.updateWithParams(sql, params,res -> {
                if (res.succeeded()) {
                    UpdateResult result = res.result();
                    HttpServerResponse response = routingContext.response();
                    if (result.getUpdated()>0) {
                        response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                                .end("{\"success\":true}");
                    }else{
                        response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                                .end("{\"success\":false}");
                    }
                } else {
                    // Failed!
                }
            });
        });
    }
}
