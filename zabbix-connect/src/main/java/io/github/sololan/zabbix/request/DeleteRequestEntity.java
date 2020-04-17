package io.github.sololan.zabbix.request;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

public class DeleteRequestEntity {
    private String jsonrpc = "2.0";

    private List params = new ArrayList();

    private String method = "";

    private String auth;

    private Integer id;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public List getParams() {
        return params;
    }

    public void putParams(Object value) {
        this.params.add(value);
    }

    public void setParams(List value) {
        this.params = value;
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
