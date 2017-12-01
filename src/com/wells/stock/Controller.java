package com.wells.stock;

import java.io.Serializable;

import com.wells.stock.crawl.HistoryStockUtility;
import com.wells.stock.mode.Mode_find_low_variation;

public class Controller implements Serializable {

    public Controller() {

    }

    public void click_12() {
        CallBack callback = new CallBack() {
            @Override
            public void call(final Object object) {
                // TODO Auto-generated method stub
                final HistoryStockUtility local_historyStockUtility = (HistoryStockUtility) object;

                Mode_find_low_variation mode_find_low_variation = new Mode_find_low_variation(
                        local_historyStockUtility.getHistoryStockInfoMap(),
                        local_historyStockUtility.getAll_Info_by_Day());

                mode_find_low_variation.execute();
            }

        };

        final HistoryStockUtility historyStockUtility = HistoryStockUtility.getInstance("2420",
                callback);
    }

}
