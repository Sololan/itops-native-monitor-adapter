package io.github.sololan.zabbix.request;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestEntityBuilder {
    private static final AtomicInteger nextId = new AtomicInteger(1);

    private RequestEntity requestEntity = new RequestEntity();

    private RequestEntityBuilder() {

    }

    public static RequestEntityBuilder newBuilder() {
        return new RequestEntityBuilder();
    }

    public RequestEntityBuilder method(String method) {
        this.requestEntity.setMethod(method);
        return this;
    }

    public RequestEntityBuilder methodClear() {
        this.requestEntity.setMethod("");
        return this;
    }

    public RequestEntityBuilder auth(String auth) {
        this.requestEntity.setAuth(auth);
        return this;
    }

    public RequestEntity build(){
//        if(requestEntity.getId() == null){
//            requestEntity.setId(nextId.getAndIncrement());
//        }
        requestEntity.setId(1);
        return requestEntity;
    }

    public RequestEntityBuilder paramEntry(String key, Object value){
        requestEntity.putParams(key, value);
        return this;
    }

    public RequestEntityBuilder paramSet(Map params){
        requestEntity.setParams(params);
        return this;
    }

    public RequestEntityBuilder paramClear(){
        requestEntity.getParams().clear();
        return this;
    }

    public RequestEntityBuilder version(String version){
        requestEntity.setJsonrpc(version);
        return this;
    }

}
