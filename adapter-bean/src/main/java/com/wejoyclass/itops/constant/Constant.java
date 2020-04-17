package com.wejoyclass.itops.constant;

public class Constant {
    /**
     * 监控软件类型（1：Zabbix）
     */
    public static final Integer MONITOR_TYPE_ZABBIX = 1;

    /**
     * 状态码（1：成功，-1失败）
     */
    public static final Integer CODE_SUCCESS = 1;
    public static final Integer CODE_FAIL = -1;

    /**
     * 操作类型（1，创建 2，删除 3，修改 4，查询 5，禁用 6，启用）
     */
    public static final Integer OPERATION_TYPE_CREATE = 1;
    public static final Integer OPERATION_TYPE_DELETE = 2;
    public static final Integer OPERATION_TYPE_UPDATE = 3;
    public static final Integer OPERATION_TYPE_GET = 4;
    public static final Integer OPERATION_TYPE_FORBID = 5;
    public static final Integer OPERATION_TYPE_UNFORBID = 6;

    /**
     * 操作类型（1，主机 2，告警）
     */
    public static final Integer OPERATION_OBJECT_HOST = 1;
    public static final Integer OPERATION_OBJECT_ALERT = 2;

    /**
     * 服务器监控器（1，Zabbix ）
     */
    public static final String ZABBIX = "zabbix";

    /**
     * 默认错误信息
     */
    public static final String DEFAULT_ERROR = "底层调用错误";
}
