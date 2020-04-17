package com.wejoyclass.itops.local.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wejoyclass.itops.bean.history.HistoryParam;
import com.wejoyclass.itops.bean.host.HostParam;
import com.wejoyclass.itops.bean.item.ItemParam;
import com.wejoyclass.itops.bean.item.ItemTopNParam;
import com.wejoyclass.itops.local.service.ZabbixService;
import com.wejoyclass.itops.local.util.ZabbixUtil;
import io.github.sololan.zabbix.api.DefaultZabbixConnect;
import io.github.sololan.zabbix.request.DeleteRequestEntity;
import io.github.sololan.zabbix.request.DeleteRequestEntityBuilder;
import io.github.sololan.zabbix.request.RequestEntity;
import io.github.sololan.zabbix.request.RequestEntityBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ZabbixServiceImpl implements ZabbixService {
    @Value("${zabbix.login-url}")
    private String zabbixLoginUrl;

    @Value("${zabbix.user}")
    private String user;

    @Value("${zabbix.password}")
    private String password;

    @Autowired
    ZabbixUtil zabbixUtil;

    @Override
    public Map getHostById(Integer id){
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("host.get")
                .paramEntry("output","extend")
                .paramEntry("hostids",id)
                .build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map addHost(HostParam hostParam) {
        ArrayList<Object> list2 = new ArrayList<>();
        HashMap<Object, Object> group = new HashMap<>();
        group.put("groupid","1");
        list2.add(group);
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("host.create")
                .paramEntry("host", hostParam.getHostName())
                .paramEntry("templates",hostParam.getTemplateList())
                .paramEntry("groups", group)
                .paramEntry("ipmi_username", hostParam.getIpmiUsername())
                .paramEntry("ipmi_password",hostParam.getIpmiPassword())
                .paramEntry("interfaces",hostParam.getInterfacesList())
                .paramEntry("name",hostParam.getName())
                .paramEntry("macros",hostParam.getMacrosList()).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map forbidHost(Integer id) {
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("host.update")
                .paramEntry("hostid",id)
                .paramEntry("status", 1)
                .build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map unforbidHost(Integer id) {
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("host.update")
                .paramEntry("hostid",id)
                .paramEntry("status", 0)
                .build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map deleteHost(Integer id) {
        List<Integer> list = new ArrayList<>();
        list.add(id);
        //构建请求体
        DeleteRequestEntity deleteRequestEntity = DeleteRequestEntityBuilder.newBuilder().method("host.delete")
                .paramSet(list)
                .build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).callDelete(deleteRequestEntity);
        return call;
    }

    @Override
    public Map updateHost(Integer id,HostParam hostParam) {
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("host.update")
                .paramEntry("hostid",id.toString())
                .paramEntry("host", hostParam.getHostName())
                .paramEntry("ipmi_username", hostParam.getIpmiUsername())
                .paramEntry("ipmi_password",hostParam.getIpmiPassword())
                .paramEntry("templates",hostParam.getTemplateList())
                .paramEntry("templates_clear",hostParam.getTemplateOldList())
                .paramEntry("interfaces",hostParam.getInterfacesList())
                .paramEntry("name",hostParam.getName())
                .paramEntry("macros",hostParam.getMacrosList()).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map getHistory(HistoryParam historyParam) {
        //计算三分钟前
        Long time1= new Date().getTime();
        String time2 = String.valueOf(time1);
        time2 = time2.substring(0, 10);
        time1 = Long.valueOf(time2).longValue();
        Long time3 = time1 - (60L * historyParam.getTimeFrom().longValue());
        String timeFrom = time3.toString();

        //需要的字段
        List<String> list = new ArrayList<>();
        list.add("clock");
        list.add("value");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("history.get")
                .paramEntry("output",list)
                .paramEntry("history", 0)
                .paramEntry("itemids", historyParam.getItemId())
                .paramEntry("sortfield","clock")
                .paramEntry("sortorder","DESC")
                .paramEntry("time_from",timeFrom)
                .paramEntry("limit",historyParam.getLimit()).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map getItemTopN(ItemTopNParam itemTopNParam) {
        Map<Object, Object> map = new HashMap<>();
        //此处判断是否为内存，如果是内存，则取出虚拟内存和物理内存数据
        //此处判断是否为硬盘，如果是硬盘，则取出agent和snmp键值下的数据
        String key = itemTopNParam.getKey();
        if(key.equals("memory")){
            ArrayList<String> list = new ArrayList<>();
            list.add("vm.memory.size[pavailable]");
            list.add("vm.memory.util");
            map.put("key_",list);
        }else if(key.equals("vfs")){
            ArrayList<String> list = new ArrayList<>();
            list.add("vfs.fs.pused");
            list.add("vfs.fs.size");
            map.put("key_",list);
        }else {
            map.put("key_",key);
        }
        List<String> list = new ArrayList<>();
        list.add("lastvalue");
        list.add("hostid");
        list.add("key_");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("item.get")
                .paramEntry("output",list)
                .paramEntry("searchByAny", true)
                .paramEntry("search",map).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);

        return call;
    }

    @Override
    public Map getItemsByHostId(Long id) {
        List<String> list = new ArrayList<>();
        list.add("name");
        list.add("description");
        list.add("itemid");
        list.add("key_");
        list.add("units");
        list.add("lastvalue");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("item.get")
                .paramEntry("output",list)
                .paramEntry("hostids",id.toString()).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map getItemsById(Long id) {
        List<String> list = new ArrayList<>();
        list.add("name");
        list.add("lastvalue");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("item.get")
                .paramEntry("output","extend")
                .paramEntry("itemids",id.toString()).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map getItemByKeyAndHostId(ItemParam itemParam) {
        Map<Object, Object> map = new HashMap<>();
        map.put("key_",itemParam.getKey());
        List<String> list = new ArrayList<>();
        list.add("name");
        list.add("lastvalue");
        list.add("key_");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("item.get")
                .paramEntry("output",list)
                .paramEntry("hostids",itemParam.getHostId())
                .paramEntry("search",map).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }

    @Override
    public Map getHistoryWithFormat(Integer time,List<String> idList) {
        //计算time分钟前
        Long time1= new Date().getTime();
        String time2 = String.valueOf(time1);
        time2 = time2.substring(0, 10);
        time1 = Long.valueOf(time2).longValue();
        Long time3 = time1 - (60L * time.longValue() + 60);
        String timeFrom = time3.toString();

        //需要的字段
        List<String> list = new ArrayList<>();
        list.add("clock");
        list.add("value");
        list.add("itemid");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("history.get")
                .paramEntry("output",list)
                .paramEntry("history", 0)
                .paramEntry("itemids", idList)
                .paramEntry("sortfield","clock")
                .paramEntry("sortorder","DESC")
                .paramEntry("time_from",timeFrom).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        if( ((List) call.get("result")).size() == 0 ){
            //构建请求体
            RequestEntity requestEntity1 = RequestEntityBuilder.newBuilder().method("history.get")
                    .paramEntry("output",list)
                    .paramEntry("history", 3)
                    .paramEntry("itemids", idList)
                    .paramEntry("sortfield","clock")
                    .paramEntry("sortorder","DESC")
                    .paramEntry("time_from",timeFrom).build();
            //发送请求
            call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity1);
        }
        return call;
    }

    @Override
    public Map getItems(List<String> idList) {
        List<String> list = new ArrayList<>();
        list.add("itemid");
        list.add("lastvalue");
        list.add("units");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("item.get")
                .paramEntry("output",list)
                .paramEntry("itemids",idList).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return call;
    }
}
