package com.wejoyclass.itops.bean;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ToNativeEntity<T> implements Serializable {
    // 状态码
    private Integer code = 0;
    // 信息
    private String message;
    // 数据
    private T data;
    // 花费时间
    private String cost;
    // 监控软件
    private String software;

    public ToNativeEntity<T> end(long time){
        this.cost = time + " millis";
        return this;
    }
}
