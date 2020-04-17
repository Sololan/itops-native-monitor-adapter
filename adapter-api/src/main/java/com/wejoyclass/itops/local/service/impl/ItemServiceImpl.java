package com.wejoyclass.itops.local.service.impl;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.item.ItemChartParam;
import com.wejoyclass.itops.bean.item.ItemParam;
import com.wejoyclass.itops.bean.item.ItemTopNParam;
import com.wejoyclass.itops.constant.Constant;
import com.wejoyclass.itops.local.service.ItemService;
import com.wejoyclass.itops.local.service.ZabbixService;
import com.wejoyclass.itops.local.util.ZabbixUtil;
import com.wejoyclass.itops.util.ToNativeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.VfsUtils;
import org.springframework.stereotype.Service;
import com.wejoyclass.itops.local.util.VfsUtil;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {
    @Value("${server-monitor}")
    String serverMonitor;

    @Autowired
    ZabbixService zabbixService;

    @Autowired
    ZabbixUtil zabbixUtil;

    @Override
    public ToNativeEntity getItemTopN(ItemTopNParam itemTopNParam) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.getItemTopN(itemTopNParam);
                if(map.get("result") != null){
                    List<Map> list = (List) (map.get("result"));
                    //去除模糊查询的结果
                    zabbixUtil.filterResultList(list,itemTopNParam.getKey());
                    //把id转换成hostname和host
                    List<String> hostidList = new ArrayList<>();
                    List<Map> hostnameList;
                    for(int i = 0;i<list.size();i++){
                        hostidList.add(list.get(i).get("hostid").toString());
                    }
                    hostnameList = zabbixUtil.getHostNameById(hostidList);
                    for(int i = 0;i<hostnameList.size();i++){
                        for(int j = 0;j<list.size();j++){
                            if(hostnameList.get(i).get("hostid").equals(list.get(j).get("hostid"))){
                                list.get(j).put("hostname",hostnameList.get(i).get("name"));
                                list.get(j).put("host",hostnameList.get(i).get("host"));
                            }
                        }
                    }
                    //去除掉没有hostname不存在的部分
                    List<Map> tempList = new ArrayList<>();
                    for (Map<String,String> map1 :
                            list) {
                        if(!map1.containsKey("host")){
                            tempList.add(map1);
                        }
                    }
                    list.removeAll(tempList);
                    //网络进口数据处理
                    if(list.size() > 0 && list.get(0).get("key_").toString().contains("net.if.in")){
                        netdataInHandler(list);
                    }
                    //网络出口数据处理
                    if(list.size() > 0 && list.get(0).get("key_").toString().contains("net.if.out")){
                        netdataOutHandler(list);
                    }
                    //磁盘数据处理
                    if(list.size() > 0 && list.get(0).get("key_").toString().contains("vfs")){
                        vfsHandler(list);
                    }
                    //判断给定的n是否大于共计返回项的数量，如果大于，则直接用共计返回项的数量
                    int count;
                    if(itemTopNParam.getN()>list.size()){
                        count = list.size();
                    }else {
                        count = itemTopNParam.getN();
                    }
                    //冒泡排序按照lastvalue排序得到的结果
                    Map temp;
                    for(int i = 0;i<count;i++){
                        for(int j = list.size()-1;j>i;j--){
                            if(Double.parseDouble(list.get(j).get("lastvalue").toString())>Double.parseDouble(list.get(j-1).get("lastvalue").toString())){
                                temp = list.get(j);
                                list.set(j,list.get(j-1));
                                list.set(j-1,temp);
                            }
                        }
                    }
                    //截取需要数量的值返回
                    List<Map> newList = list.subList(0, count);
                    //精度处理为保留两位小数点
                    doubleHandler(list);
                    r.setData(newList);
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
    public ToNativeEntity getItemsByHostId(Long id) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.getItemsByHostId(id);
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
    public ToNativeEntity getItemsById(Long id) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.getItemsById(id);
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
    public ToNativeEntity getItemByKeyAndHostId(ItemParam itemParam) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.getItemByKeyAndHostId(itemParam);
                // 特殊数据处理器（1：b 2：bps）
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
    public ToNativeEntity getItems(List<ItemChartParam> itemChartParamList) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                List<String> idList = new ArrayList<>();
                itemChartParamList.stream().forEach(s -> {
                    idList.add(s.getId());
                });
                Map map = zabbixService.getItems(idList);
                System.out.println(map);
                if(map.get("result") != null){
                    List<Map<String,String>> zabbixData = (List<Map<String,String>>) map.get("result");
                    //获取当前时间
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // 返回的map
                    Map<String, Object> returnMap = new HashMap<>();
                    //单位处理器，把b转化为mb，bps转化为Kbps，时间戳转化为时间
                    String units = unitsHandler((List<Map<String, String>>) map.get("result"));
                    returnMap.put("units",units);
                    List<Object> dataList = new ArrayList<>();
                    // 当前时间
                    returnMap.put("now",sdf.format(now));
                    for (ItemChartParam itemChartParam:
                            itemChartParamList){
                        HashMap<Object, Object> map1 = new HashMap<>();
                        map1.put("name",itemChartParam.getName());
                        for (Map map2:zabbixData){
                            if(itemChartParam.getId().equals(map2.get("itemid"))){
                                map1.put("lastvalue",map2.get("lastvalue"));
                            }
                        }
                        dataList.add(map1);
                    }
                    returnMap.put("dataList",dataList);
                    r.setData(returnMap);
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

    private void netdataInHandler(List<Map> list){
        for (Map map:
             list) {
            String tempkey = map.get("key_").toString().substring(11);
            String finalkey = tempkey.substring(0, tempkey.length() - 2);
            map.put("hostname",map.get("hostname").toString() + "(" + finalkey + ")");
            map.put("lastvalue", String.valueOf((Double.valueOf((String) map.get("lastvalue")))/1000L));
        }
    }

    private void netdataOutHandler(List<Map> list){
        for (Map map:
                list) {
            String tempkey = map.get("key_").toString().substring(12);
            String finalkey = tempkey.substring(0, tempkey.length() - 2);
            map.put("hostname",map.get("hostname").toString() + "(" + finalkey + ")");
            map.put("lastvalue", String.valueOf((Double.valueOf((String) map.get("lastvalue")))/1000L));
        }
    }

    private void vfsHandler(List<Map> list){
        for (Map map:
                list) {
            if(map.get("key_").toString().contains("vfs.fs.pused")){
                String tempkey = map.get("key_").toString().substring(map.get("key_").toString().length()-2,map.get("key_").toString().length()-1);
                String finalkey = VfsUtil.toPartition(tempkey);
                map.put("hostname",map.get("hostname").toString() + "(" + finalkey + ")");
            }else if(map.get("key_").toString().contains("vfs.fs.size")){
                String tempkey = map.get("key_").toString().substring(12);
                String finalkey = tempkey.substring(0, tempkey.length() - 7);
                map.put("hostname",map.get("hostname").toString() + "(" + finalkey + ")");
            }
        }
    }

    private void doubleHandler(List<Map> list){
        for (Map map:
                list) {
            Double value = Double.valueOf((String) map.get("lastvalue"));
            // 保留两位小数输出
            map.put("lastvalue", String.format("%.2f", value));
        }
    }


    String unitsHandler(List<Map<String,String>> result){
        String units = "";
        // 如果单位等于B，那么转化为MB
        for (Map<String,String> map:
                result) {
            if(units.equals("") || units.equals(map.get("units"))){
                units = map.get("units");
            }else {
                units = "unknownUnits";
            }
            if(map.get("units").equals("B")){
                Double doubleValue = Double.valueOf(map.get("lastvalue"));
                map.put("lastvalue",String.valueOf(doubleValue/1024/1024/1024));
                units = "GB";
                precisionHandler(map);
            }
            if(map.get("units").equals("bps")){
                Double doubleValue = Double.valueOf(map.get("lastvalue"));
                map.put("lastvalue",String.valueOf(doubleValue/1024));
                units = "Kbps";
                precisionHandler(map);
            }
            if(map.get("units").contains("time")){
                Long timeSecond = Long.valueOf(map.get("lastvalue"));
                Long dayLong = timeSecond/60/60/24;
                Long hourLong = (timeSecond - dayLong*60*60*24)/60/60;
                Long minuteLong = (timeSecond - dayLong*60*60*24 - hourLong*60*60)/60;
                Long secondLong = timeSecond - dayLong*60*60*24 - hourLong*60*60 - minuteLong*60;
                String resultTime = dayLong + "天 " + hourLong + "小时" + minuteLong + "分钟" + secondLong.toString() + "秒";
                map.put("lastvalue",resultTime);
            }
        }
        return units;
    }

    void precisionHandler(Map<String,String> map){
        Double doubleValue = Double.valueOf(map.get("lastvalue"));
        map.put("lastvalue",String.format("%.2f", doubleValue));
    }
}
