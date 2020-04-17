package com.wejoyclass.itops.local.service.impl;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.history.HistoryParam;
import com.wejoyclass.itops.bean.history.HistoryChartParam;
import com.wejoyclass.itops.constant.Constant;
import com.wejoyclass.itops.local.service.HistoryService;
import com.wejoyclass.itops.local.service.ZabbixService;
import com.wejoyclass.itops.local.util.ZabbixUtil;
import com.wejoyclass.itops.util.ToNativeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HistoryServiceImpl implements HistoryService {
    @Value("${server-monitor}")
    String serverMonitor;

    @Autowired
    ZabbixService zabbixService;

    @Autowired
    ZabbixUtil zabbixUtil;

    @Override
    public ToNativeEntity getHistory(HistoryParam historyParam) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                Map map = zabbixService.getHistory(historyParam);
                if(map.get("result") != null){
                    System.out.println(map);
                    //获取当前时间
                    Date d = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // 组合history和当前时间给返回的map
                    Map<String, Object> returnMap = new HashMap<>();
                    returnMap.put("history",map.get("result"));
                    returnMap.put("now",sdf.format(d));
                    //转换秒数为可读时间
                    List<Map<String, String>> list = (List) returnMap.get("history");
                    for (Map tempMap :
                            list) {
                        Long miLong = Long.valueOf(tempMap.get("clock") + "000").longValue();
                        Date date = new Date();
                        date.setTime(miLong);
                        tempMap.put("clock",new SimpleDateFormat("HH:mm:ss").format(date));
                    }
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

    @Override
    public ToNativeEntity getHistoryWithFormat(Integer time,List<HistoryChartParam> historyChartParamList) {
        if(serverMonitor.equals(Constant.ZABBIX)){
            return ToNativeUtil.exe(r -> {
                r.setSoftware(Constant.ZABBIX);
                // 先判断时间范围是否符合预定义的范围，不符合就抛出异常
                if(time == null || (time != 5 && time != 10 && time != 15 && time != 30 && time != 60 && time != 1440)){
                    throw new IllegalStateException("time value can set by 5,10,15,30,60,1440");
                }
                //获取idList
                List<String> idList = new ArrayList<>();
                historyChartParamList.stream().forEach(s -> {
                    idList.add(s.getId());
                });
                Map map = zabbixService.getHistoryWithFormat(time,idList);
                System.out.println(map);
                if(map.get("result") != null){
                    //获取单位
                    String units = zabbixUtil.getUnitsById(idList.get(0));
                    //处理掉！
                    units = units.replace("!", "");
                    //单位处理器，把b转化为mb，bps转化为Kbps
                    units = unitsHandler((List<Map<String,String>>) map.get("result"),units);
                    //精度处理器，保留两位小数
                    precisionHandler((List<Map<String,String>>) map.get("result"));
                    //获取当前时间
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
                    // 组合history和当前时间给返回的map
                    Map<String, Object> returnMap = new HashMap<>();
                    returnMap.put("units",units);
                    // 当前时间
                    returnMap.put("now",sdf.format(now));
                    // x轴坐标
                    List<String> xdata = new ArrayList<>();
                    // 计算time分钟之前
                    long before = now.getTime() - ((long) time * 60 * 1000);
                    // 插入时间间隔
                    xdata.add(sf.format(new Date(before)));
                    for(int i=1;i<time;i++){
                        Date tempDate = new Date(before + i * 60 * 1000);
                        xdata.add(sf.format(tempDate));
                    }
                    returnMap.put("xdata",xdata);
                    // 把zabbix返回数据中的clock转换成 HH:mm 的形式
                    List<Map<String,String>> zabbixData = (List<Map<String,String>>) map.get("result");
                    for (Map tempMap :
                            zabbixData) {
                        Long miLong = Long.valueOf(tempMap.get("clock") + "000").longValue();
                        Date date = new Date();
                        date.setTime(miLong);
                        tempMap.put("clock",sf.format(date));
                    }
                    List<Object> dataList = new ArrayList<>();
                    // 根据itemid分割不同的数据;
                    //itemsParamList，list.size多大就循环多少次，最终就有多少个datalist
                    for (HistoryChartParam historyChartParam:
                            historyChartParamList) {
                        Map<String, Object> map1 = new HashMap<>();
                        map1.put("dataName",historyChartParam.getName());
                        String id = historyChartParam.getId();
                        // 构造值列表，循环时间，有符合itemid和time都相同的，就插入，没有就补-
                        List<String> valueList = new ArrayList<>();
                        for (String time1:
                                xdata){
                            boolean flag = true;
                            for (Map<String,String> map2:
                                    zabbixData){
                                if(id.equals(map2.get("itemid")) && time1.equals(map2.get("clock"))){
                                    valueList.add(map2.get("value"));
                                    flag = false;
                                }
                            }
                            if(flag){
                                valueList.add("-");
                            }
                        }
                        map1.put("data",valueList);
                        dataList.add(map1);
                    }
                    returnMap.put("dataList",dataList);
                    //转换秒数为可读时间
//                    List<Map<String, String>> list = (List) returnMap.get("history");
//                    for (Map tempMap :
//                            list) {
//                        Long miLong = Long.valueOf(tempMap.get("clock") + "000").longValue();
//                        Date date = new Date();
//                        date.setTime(miLong);
//                        tempMap.put("clock",sf.format(date));
//                    }
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

    String unitsHandler(List<Map<String,String>> result,String units){
        String retureUnits = units;
        // 如果单位等于B，那么转化为MB
        if(units.equals("B")){
            for (Map<String,String> map:
                    result) {
                Double doubleValue = Double.valueOf(map.get("value"));
                map.put("value",String.valueOf(doubleValue/1024/1024/1024));
            }
            retureUnits = "GB";
        }
        // 如果单位等于bps，那么转化为Kbps
        if(units.equals("bps")){
            for (Map<String,String> map:
                    result) {
                Double doubleValue = Double.valueOf(map.get("value"));
                map.put("value",String.valueOf(doubleValue/1024));
            }
            retureUnits = "Kbps";
        }
        return retureUnits;
    }

    void precisionHandler(List<Map<String,String>> result){
        for (Map<String,String> map:
                result) {
            Double doubleValue = Double.valueOf(map.get("value"));
            map.put("value",String.format("%.2f", doubleValue));
        }
    }
}
