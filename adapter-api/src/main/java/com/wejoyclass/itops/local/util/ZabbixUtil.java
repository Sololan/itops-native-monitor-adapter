package com.wejoyclass.itops.local.util;

import com.alibaba.fastjson.JSONObject;
import io.github.sololan.zabbix.api.DefaultZabbixConnect;
import io.github.sololan.zabbix.request.RequestEntity;
import io.github.sololan.zabbix.request.RequestEntityBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ZabbixUtil {
    @Value("${zabbix.login-url}")
    private String zabbixLoginUrl;

    @Value("${zabbix.user}")
    private String user;

    @Value("${zabbix.password}")
    private String password;

    /**
     * @author     ：liuzt
     * @date       ：Created in 2020/1/16 18:51
     * @description：根据模板名list获取模板idlist
     * @param
     * @return
     */
    public List<String> getTemplateIdByName(List<String> nameList){
        //返回的list
        ArrayList<String> idList = new ArrayList<>();
        //如果为空，直接返回空
        if(nameList.size() < 1){
            return idList;
        }
        //过滤器参数
        Map<String, Object> filter = new HashMap<>();
        filter.put("name", nameList);
        //构造请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("template.get")
                .paramEntry("output", "extend").paramEntry("filter",filter).build();
        //调用请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        //返回的call有三个参数，获取其中信息result中
        List result = (List)call.get("result");
        //遍历list，获取其中的模板id
        for (Object re:
                result) {
            idList.add(((Map) re).get("templateid").toString());
        }
        return idList;
    }

    public String getItemId(String hostId,String key){
        Map<Object, Object> map = new HashMap<>();
        map.put("key_",key);
        //构造请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("item.get")
                .paramEntry("output", "itemid")
                .paramEntry("hostids",hostId)
                .paramEntry("search",map).build();
        //调用请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        return ((Map) ((List) call.get("result")).get(0)).get("itemid").toString();
    }

    public List<Map> getHostNameById(List<String> hostIds){
        Map<Object, Object> map = new HashMap<>();
        map.put("hostid",hostIds);
        List<String> list = new ArrayList();
        list.add("name");
        list.add("host");
        //构造请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("host.get")
                .paramEntry("output", list)
                .paramEntry("filter",map).build();
        //调用请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        //筛选需要的数据返回
        List<String> returnList = new ArrayList();
        List<Map> result = (List) call.get("result");
        System.out.println(result);
        return result;
    }

    /**
     * @author     ：liuzt
     * @date       ：Created in 2020/1/16 18:52
     * @description：查看有无当前主机组
     * @param
     * @return
     */
    public Boolean isExistHostGroup(String hostGroup){

        return false;
    }

    /**
     * @author     ：liuzt
     * @date       ：Created in 2020/2/17 22:52
     * @description：去除模糊查询结果
     * @param
     * @return
     */
    public void filterResultList(List<Map> list,String key){
        List<Map> tempList = new ArrayList<>();
        if(key.equals("system.cpu.util")){
            for (Map<String,String> map1 :
                    list) {
                if(!map1.get("key_").equals(key)){
                    tempList.add(map1);
                }
            }
            list.removeAll(tempList);
        }
        if(key.equals("vfs")){
            for (Map<String, String> map2 :
                    list) {
                if(!map2.get("key_").contains("pused")){
                    tempList.add(map2);
                }
            }
            list.removeAll(tempList);
        }
    }

    /**
     * @author     ：liuzt
     * @date       ：Created in 2020/2/26 14:37
     * @description：根据itemid查询单位
     * @param
     * @return
     */
    public String getUnitsById(String id){
        List<String> list = new ArrayList<>();
        list.add("units");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("item.get")
                .paramEntry("output",list)
                .paramEntry("itemids",id).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        List<Map<String,String>> result = (List<Map<String,String>>) call.get("result");
        // 如果没有就返回空字符串
        if(result.size() == 0){
            return "";
        }
        return result.get(0).get("units");
    }

    /**
     * @author     ：liuzt
     * @date       ：Created in 2020/2/26 16:37
     * @description：根据itemid查询单位
     * @param
     * @return
     */
    public List<Map<String,String>> getTemplates(){
        List<String> list = new ArrayList<>();
        list.add("templateid");
        list.add("name");
        //构建请求体
        RequestEntity requestEntity = RequestEntityBuilder.newBuilder().method("template.get")
                .paramEntry("output",list).build();
        //发送请求
        JSONObject call = DefaultZabbixConnect.newZabbixConnect(zabbixLoginUrl).init().login(user, password).call(requestEntity);
        List<Map<String,String>> result = (List<Map<String,String>>) call.get("result");
        return result;
    }
}
