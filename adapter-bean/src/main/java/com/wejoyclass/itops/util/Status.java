package com.wejoyclass.itops.util;

public enum  Status {
    SUCCESS(0, "调用成功"),
    FAIL(-1, "调用失败");

    private Integer code;
    private String desc;

    private Status(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
