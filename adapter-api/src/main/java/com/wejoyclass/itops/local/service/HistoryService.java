package com.wejoyclass.itops.local.service;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.history.HistoryChartParam;
import com.wejoyclass.itops.bean.history.HistoryParam;

import java.util.List;

public interface HistoryService {
    // 获取历史数据
    ToNativeEntity getHistory(HistoryParam historyParam);
    // 获取格式化的
    ToNativeEntity getHistoryWithFormat(Integer time, List<HistoryChartParam> historyChartParamList);
}
