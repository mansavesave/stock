package com.wells.stock.crawl;

import java.util.ArrayList;
import java.util.HashMap;

import com.wells.stock.CallBack;
import com.wells.stock.data.HistoryStockInfo;
import com.wells.stock.data.StockInfoFile;

public abstract class HistoryCrawl {
    public String mStockNum;

    public ArrayList<String> mKeyAllDay;
    public HashMap<String, HistoryStockInfo> mMapStockInfo;

    public abstract void doCrawl(CallBack callBack);

    public StockInfoFile getStockInfoFile() {
        StockInfoFile result = null;
        if (mStockNum == null || mKeyAllDay == null || mMapStockInfo == null) {
            return null;
        }

        result = new StockInfoFile(mStockNum, mKeyAllDay, mMapStockInfo);
        return result;
    }

}
