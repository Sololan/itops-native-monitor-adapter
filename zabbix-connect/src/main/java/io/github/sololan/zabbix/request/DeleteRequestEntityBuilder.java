package io.github.sololan.zabbix.request;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DeleteRequestEntityBuilder {
    private static final AtomicInteger nextId = new AtomicInteger(1);

    private DeleteRequestEntity deleteRequestEntity = new DeleteRequestEntity();

    private DeleteRequestEntityBuilder() {

    }

    public static DeleteRequestEntityBuilder newBuilder() {
        return new DeleteRequestEntityBuilder();
    }

    public DeleteRequestEntityBuilder method(String method) {
        this.deleteRequestEntity.setMethod(method);
        return this;
    }

    public DeleteRequestEntityBuilder methodClear() {
        this.deleteRequestEntity.setMethod("");
        return this;
    }

    public DeleteRequestEntityBuilder auth(String auth) {
        this.deleteRequestEntity.setAuth(auth);
        return this;
    }

    public DeleteRequestEntity build(){
//        if(requestEntity.getId() == null){
//            requestEntity.setId(nextId.getAndIncrement());
//        }
        deleteRequestEntity.setId(1);
        return deleteRequestEntity;
    }

    public DeleteRequestEntityBuilder paramEntry(Object value){
        deleteRequestEntity.putParams(value);
        return this;
    }

    public DeleteRequestEntityBuilder paramSet(List params){
        deleteRequestEntity.setParams(params);
        return this;
    }

    public DeleteRequestEntityBuilder paramClear(){
        deleteRequestEntity.getParams().clear();
        return this;
    }

    public DeleteRequestEntityBuilder version(String version){
        deleteRequestEntity.setJsonrpc(version);
        return this;
    }
}
