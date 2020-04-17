package com.wejoyclass.itops.local.service;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.host.HostParam;

public interface HostService {
    // 根据id获取主机信息
    ToNativeEntity getHostById(Integer id);
    // 创建主机
    ToNativeEntity addHost(HostParam hostParam);
    // 禁用主机
    ToNativeEntity forbidHost(Integer id);
    // 启用主机
    ToNativeEntity unforbidHost(Integer id);
    // 删除主机
    ToNativeEntity deleteHost(Integer id);
    // 更新主机
    ToNativeEntity updateHost(Integer id,HostParam hostParam);
}
