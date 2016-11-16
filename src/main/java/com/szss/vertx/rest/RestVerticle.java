package com.szss.vertx.rest;

import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Created by zcg on 2016/11/14.
 */
public class RestVerticle extends AbstractVerticle {

    private UserDao userDao = new UserDao();

    @Override
    public void start(Future<Void> startFuture) throws Exception {
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
        HttpServerResponse response = routingContext.response();
        response.putHeader("content-type", "application/json,charset=utf-8")
                .end(Json.encodePrettily(userDao.findAll()));
    }

    private void addUser(RoutingContext routingContext){
        HttpServerRequest request=routingContext.request();
        String username=request.getParam("username");
        Integer age=Integer.valueOf(request.getParam("age"));
        String province=request.getParam("province");
        Boolean gender=Boolean.valueOf(request.getParam("gender"));

        User user=new User(UserDao.users.size(),username,age,gender,province);
        userDao.addUser(user);
        HttpServerResponse response = routingContext.response();
        response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                .end("{\"success\":true}");
    }

    private void updateUser(RoutingContext routingContext){
        String id = routingContext.request().getParam("id");
        User user=Json.decodeValue(routingContext.getBodyAsString(),User.class);
        UserDao.users.remove(Integer.valueOf(id));
        user.setId(Integer.valueOf(id));
        UserDao.users.set(user.getId(),user);
        HttpServerResponse response=routingContext.response();
        response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                .end("{\"success\":true}");
    }

    private void deleteUser(RoutingContext routingContext){
        String id = routingContext.request().getParam("id");
        int index=Integer.valueOf(id);
        for (User u:UserDao.users){
            if (u.getId()==index){
                UserDao.users.remove(u);
                break;
            }
        }
        HttpServerResponse response=routingContext.response();
        response.setStatusCode(200).putHeader("content-type", "text/json,charset=utf-8")
                .end("{\"success\":true}");
    }
}
