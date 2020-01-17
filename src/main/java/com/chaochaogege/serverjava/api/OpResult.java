package com.chaochaogege.serverjava.api;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class OpResult {
    public static int STATUS_SUCCEED = 0;
    public static int STATUS_FAILED = 1;
    public static int STATUS_FAILED_SQL = 2; //SQL 操作出错
    public static int STATUS_FAILED_NO_ENOUGH_ARGS = 4;
    public static int STATUS_FAILED_WRONG_POST_DATA = 8;
    public static int STATUS_FAILED_WRONG_UID = 16;
    public int code;
    public JsonArray data;
    public OpResult(int code, JsonArray data) {
        this.code  = code;
        this.data = data;
    }
    public Buffer encode() {
        JsonObject o = new JsonObject().put("code",code).put("data",data);
        return Json.encodeToBuffer(o);
    }
    public static void succeedWithCode(HttpServerResponse response) {
        response.end(Json.encodeToBuffer(new JsonObject().put("code",STATUS_SUCCEED)));
    }
    public static Buffer failedResponseWith(int c) {
        JsonObject o = new JsonObject().put("code",STATUS_FAILED | c);
        return Json.encodeToBuffer(o);
    }
    public static void failedDirectlyWithCause(HttpServerResponse httpResponse, int code, String s){
        JsonObject o = new JsonObject().put("code",STATUS_FAILED | code).put("data",s);
        httpResponse.end(Json.encodeToBuffer(o));
    }
    public static void failedDirectlyWithCause(HttpServerResponse httpResponse, int code, Throwable cause){
        failedDirectlyWithCause(httpResponse,code,cause.toString());

    }
}
