package com.wejoyclass.itops.local.service;

import com.wejoyclass.itops.bean.ToNativeEntity;
import com.wejoyclass.itops.bean.item.ItemChartParam;
import com.wejoyclass.itops.bean.item.ItemParam;
import com.wejoyclass.itops.bean.item.ItemTopNParam;

import java.util.List;

public interface ItemService {
    ToNativeEntity getItemTopN(ItemTopNParam itemTopNParam);

    ToNativeEntity getItemsByHostId(Long id);

    ToNativeEntity getItemsById(Long id);

    ToNativeEntity getItemByKeyAndHostId(ItemParam itemParam);

    ToNativeEntity getItems(List<ItemChartParam> itemChartParamList);
}
