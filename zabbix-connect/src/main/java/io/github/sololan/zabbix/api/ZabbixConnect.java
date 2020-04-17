package io.github.sololan.zabbix.api;

import com.alibaba.fastjson.JSONObject;
import io.github.sololan.zabbix.request.RequestEntity;

public interface ZabbixConnect {

    ZabbixConnect init();

    ZabbixConnect destroy();

    ZabbixConnect login(String user, String password);
}
