package com.wejoyclass.itops.local.service;

import com.wejoyclass.itops.bean.history.HistoryParam;
import com.wejoyclass.itops.bean.host.HostParam;
import com.wejoyclass.itops.bean.item.ItemParam;
import com.wejoyclass.itops.bean.item.ItemTopNParam;

import java.util.List;
import java.util.Map;

public interface ZabbixService {
    //根据id获取主机信息
    Map getHostById(Integer id);
    //添加主机
    Map addHost(HostParam hostParam);
    //禁用主机
    Map forbidHost(Integer id);
    //启用主机
    Map unforbidHost(Integer id);
    //删除主机
    Map deleteHost(Integer id);
    //更新主机
    Map updateHost(Integer id,HostParam hostParam);
    //获取历史数据信息
    Map getHistory(HistoryParam historyParam);
    //根据key获取topN
    Map getItemTopN(ItemTopNParam itemTopNParam);
    //根据主机id获取监控项
    Map getItemsByHostId(Long id);
    //根据监控项id获取监控项
    Map getItemsById(Long id);
    //根据key和hostid获取监控项数据
    Map getItemByKeyAndHostId(ItemParam itemParam);
    //根据多个监控项id获取格式化的监控值
    Map getHistoryWithFormat(Integer time, List<String> idList);
    //根据监控项idList获取监控最新值
    Map getItems(List<String> idList);
}
