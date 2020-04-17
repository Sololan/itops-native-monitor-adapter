package com.wejoyclass.itops.bean.history;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryParam {
    // 监控项id
    String itemId;
    // limit
    Integer limit;
    // time from
    Integer timeFrom;
}
