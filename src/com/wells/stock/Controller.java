package com.wells.stock;

import java.io.Serializable;

import com.wells.stock.crawl.HistoryStockFromTwseUtility;
import com.wells.stock.data.StockInfoFile;
import com.wells.stock.mode.Mode_find_low_variation;

public class Controller implements Serializable {

    public Controller() {

    }

    public void click_12() {
        CallBack callback = new CallBack() {
            @Override
            public void call(final Object object) {
                // TODO Auto-generated method stub
                final StockInfoFile stockInfoFile = (StockInfoFile) object;

                if (stockInfoFile != null) {
                    if (!stockInfoFile.isSaved()) {
                        stockInfoFile.save();
                    }

                    Mode_find_low_variation mode_find_low_variation = new Mode_find_low_variation(
                            stockInfoFile.mStockInfoMap, stockInfoFile.mAllDayKey);

                    mode_find_low_variation.execute();
                }

            }

        };

        String stockNum = "2420";

        StockInfoFile stockInfoFile = StockInfoFile.load(stockNum);
        if (stockInfoFile == null) {
            final HistoryStockFromTwseUtility historyStockFromTwseUtility = new HistoryStockFromTwseUtility(
                    stockNum);

            historyStockFromTwseUtility.doCrawl(callback);
        } else {
            callback.call(stockInfoFile);
        }

    }

}
