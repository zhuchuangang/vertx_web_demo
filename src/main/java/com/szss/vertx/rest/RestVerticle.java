package com.szss.vertx.rest;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
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
}
