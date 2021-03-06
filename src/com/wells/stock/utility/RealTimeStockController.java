package com.wells.stock.utility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.wells.stock.setting.StockInfo;
import com.wells.stock.setting.StockSetting;

public class RealTimeStockController implements Serializable{

    ArrayList<StockInfo> mStockInfoList;
    HashMap<StockInfo, RealTimeStockUtility> mRealTimeStockMap;

    public RealTimeStockController() {
        mStockInfoList = new ArrayList<StockInfo>();
        mRealTimeStockMap = new HashMap<StockInfo, RealTimeStockUtility>();

        for (String stockNum[] : StockSetting.StockList) {
            StockInfo stockInfo = new StockInfo(stockNum[0], stockNum[1]);
            mStockInfoList.add(stockInfo);

            RealTimeStockUtility realTimeStock = new RealTimeStockUtility(stockInfo);
            realTimeStock.init();
            mRealTimeStockMap.put(stockInfo, realTimeStock);
        }
    }

    // 回傳顯示的資訊表格
    public String updateCurrentStatus() {
        StringBuilder stringbuilder = new StringBuilder();
        String title = String.format(StockSetting.DisplayFormat, StockSetting.Field_name, StockSetting.Field_price, StockSetting.Field_start_price, StockSetting.Field_hight_price,
                StockSetting.Field_low_price, StockSetting.Field_quantum_total, StockSetting.Field_current_time);

        // name, price, time, quantum
        stringbuilder.append(title + "\n");

        for (StockInfo eachStockInfo : mStockInfoList) {
            RealTimeStockUtility realTimeStock = mRealTimeStockMap.get(eachStockInfo);
            StockInfo stockInfo = realTimeStock.getData();
            if (stockInfo != null) {
                stringbuilder.append(stockInfo.getDisplayLine() + "\n");
            }

        }
        return stringbuilder.toString();

    }
}
