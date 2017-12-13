package com.wells.stock;

import java.io.Serializable;

import com.wells.stock.crawl.HistoryStockFromTwseUtility;
import com.wells.stock.data.StockInfoFile;
import com.wells.stock.mode.Mode_find_low_variation;
import com.wells.stock.setting.StockSetting;
import com.wells.stock.sql.SQLiteUtility;
import com.wells.stock.utility.Utility;

public class Controller implements Serializable {

    public Controller() {

    }

    public void crawlAndSaveStockData(final String stockNum, final CallBack callBack) {
        final String stockNumTable = "_" + stockNum;

        CallBack loadFromDB_callback = new CallBack() {

            @Override
            public void call(Object object) {
                // TODO Auto-generated method stub
                final StockInfoFile stockInfoFile = (StockInfoFile) object;
                if (stockInfoFile != null) {
                    int size = stockInfoFile.mStockInfoMap.size();
                    System.out.println("before crawl, load DB callback:" + size);
                }

                final HistoryStockFromTwseUtility historyStockFromTwseUtility = new HistoryStockFromTwseUtility(
                        stockNum, stockInfoFile);

                CallBack crawl_callback = new CallBack() {
                    @Override
                    public void call(final Object object) {
                        // TODO Auto-generated method stub
                        System.out.println("complete to crawl");
                        final StockInfoFile stockInfoFile = (StockInfoFile) object;
                        if (stockInfoFile != null) {
                            CallBack saveToDB_callback = new CallBack() {

                                @Override
                                public void call(Object object) {
                                    // TODO Auto-generated method stub
                                    int currenDBCount = SQLiteUtility
                                            .selectCountForStock(stockNumTable);
                                    System.out.println("complete to save to stockNum:" + stockNum
                                            + ", count:" + currenDBCount);
                                    if (callBack != null) {
                                        callBack.call(stockInfoFile);
                                    }
                                }
                            };

                            stockInfoFile.saveToDB(saveToDB_callback);
                        } else {
                            System.out.println("complete, but stockInfoFile is null");

                        }
                    }
                };

                historyStockFromTwseUtility.doCrawl(crawl_callback);
            }

        };

        StockInfoFile.loadFromDB(stockNum, loadFromDB_callback);

    }

    public void crawlAndSaveStockData(int query_StockList_index, final CallBack callBack) {
        String stockNum = StockSetting.HistoryStockList[query_StockList_index][0];
        crawlAndSaveStockData(stockNum, callBack);
    }

    public void click_11() {
        final String stockNum = "2420";
        final String stockNumTable = "_" + stockNum;
        // SQLiteUtility.runSample();
        SQLiteUtility.selectCountForStock(stockNumTable);
    }

    int query_history_StockList_index = 0;
    final CallBack callBack_click_12 = new CallBack() {
        @Override
        public void call(Object object) {
            // TODO Auto-generated method stub
            final StockInfoFile stockInfoFile = (StockInfoFile) object;
            query_history_StockList_index++;

            if (StockSetting.HistoryStockList.length > query_history_StockList_index) {
                crawlAndSaveStockData(query_history_StockList_index, callBack_click_12);
            } else {
                System.out.println("click_12() complete");
            }
        }
    };

    /**
     * sample of downloading multiple stock info
     */
    public void click_12() {
        query_history_StockList_index = 0;
        // String startStockNum =
        // StockSetting.StockList[query_StockList_index][0];
        crawlAndSaveStockData(query_history_StockList_index, callBack_click_12);

    }

    /**
     * sample of downloading one stock info
     */
    public void click_13() {
        String stockNum = "2420";
        CallBack callBack = new CallBack() {
            @Override
            public void call(Object object) {
                // TODO Auto-generated method stub
                final StockInfoFile stockInfoFile = (StockInfoFile) object;
            }
        };
        crawlAndSaveStockData(stockNum, callBack);
    }

}
