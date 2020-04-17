package io.github.sololan.zabbix.request;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class RequestEntity {
    private String jsonrpc = "2.0";

    private Map<String, Object> params = new HashMap<>();

    private String method = "";

    private String auth;

    private Integer id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void putParams(String key, Object value) {
        this.params.put(key, value);
    }

    public void setParams(Map params) {
        this.params = params;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
