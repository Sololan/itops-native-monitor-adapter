package com.wejoyclass.itops.local.service.impl;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.host.HostParam;
import com.wejoyclass.itops.constant.Constant;
import com.wejoyclass.itops.local.service.HostService;
import com.wejoyclass.itops.local.service.ZabbixService;
import com.wejoyclass.itops.util.ToNativeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HostServiceImpl implements HostService {
    @Value("${server-monitor}")
    String serverMonitor;

    @Autowired
    ZabbixService zabbixService;

    @Override
    public ToNativeEntity getHostById(Integer id) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.getHostById(id);
                if(map.get("result") != null){
                    r.setData(map.get("result"));
                }else {
                    r.setCode(-1);
                    r.setMessage(((Map) map.get("error")).get("data").toString());
                }
            });
        }else {
            return ToNativeUtil.exe(r -> {
                r.setCode(-1);
                r.setMessage("配置了不存在的服务器监控软件，请检查server-monitor");
            });
        }
    }

    @Override
    public ToNativeEntity addHost(HostParam hostParam) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.addHost(hostParam);
                if(map.get("result") != null){
                    r.setData(((List) ((Map) map.get("result")).get("hostids")).get(0));
                }else {
                    r.setCode(-1);
                    r.setMessage(((Map) map.get("error")).get("data").toString());
                }
            });
        }else {
            return ToNativeUtil.exe(r -> {
                r.setCode(-1);
                r.setMessage("配置了不存在的服务器监控软件，请检查server-monitor");
            });
        }
    }

    @Override
    public ToNativeEntity forbidHost(Integer id) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.forbidHost(id);
                if(map.get("result") != null){
                    r.setData("成功禁用");
                }else {
                    r.setCode(-1);
                    r.setMessage(((Map) map.get("error")).get("data").toString());
                }
            });
        }else {
            return ToNativeUtil.exe(r -> {
                r.setCode(-1);
                r.setMessage("配置了不存在的服务器监控软件，请检查server-monitor");
            });
        }
    }

    @Override
    public ToNativeEntity unforbidHost(Integer id) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.unforbidHost(id);
                if(map.get("result") != null){
                    r.setData("成功启用");
                }else {
                    r.setCode(-1);
                    r.setMessage(((Map) map.get("error")).get("data").toString());
                }
            });
        }else {
            return ToNativeUtil.exe(r -> {
                r.setCode(-1);
                r.setMessage("配置了不存在的服务器监控软件，请检查server-monitor");
            });
        }
    }

    @Override
    public ToNativeEntity deleteHost(Integer id) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.deleteHost(id);
                if(map.get("result") != null){
                    r.setData("成功删除主机");
                }else {
                    r.setCode(-1);
                    r.setMessage(((Map) map.get("error")).get("data").toString());
                }
            });
        }else {
            return ToNativeUtil.exe(r -> {
                r.setCode(-1);
                r.setMessage("配置了不存在的服务器监控软件，请检查server-monitor");
            });
        }
    }

    @Override
    public ToNativeEntity updateHost(Integer id, HostParam hostParam) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.updateHost(id,hostParam);
                if(map.get("result") != null){
                    r.setData("成功更新主机");
                }else {
                    r.setCode(-1);
                    r.setMessage(((Map) map.get("error")).get("data").toString());
                }
            });
        }else {
            return ToNativeUtil.exe(r -> {
                r.setCode(-1);
                r.setMessage("配置了不存在的服务器监控软件，请检查server-monitor");
            });
        }
    }
}
