package com.wells.stock.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.wells.stock.CallBack;
import com.wells.stock.sql.SQLiteUtility;

public class StockInfoFile implements Serializable {

    String mStockNum;
    public HashMap<String, HistoryStockInfo> mStockInfoMap;// key: 每一日;e.g.
                                                           // 20171105
    boolean mIsSaved = false;

    public StockInfoFile(String stockNum, HashMap<String, HistoryStockInfo> map) {
        mStockNum = stockNum;
        setStockInfoMap(map);
    }

    public void setStockInfoMap(HashMap<String, HistoryStockInfo> map) {
        mStockInfoMap = map;
    }

    public static File getFolerToSave() {
        String work_path = System.getProperty("user.dir");
        File targetFile = new File(work_path, "save_data");
        return targetFile;
    }

    public boolean isSaved() {
        File folder = getFolerToSave();
        File targetFile = new File(folder, mStockNum);
        if (targetFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean save() {
        boolean result = false;
        ObjectOutputStream oos = null;
        try {// 儲存在工作目錄中
            File folder = getFolerToSave();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File targetFile = new File(folder, mStockNum);
            if (targetFile.exists()) {
                targetFile.delete();
            }

            FileOutputStream fos = new FileOutputStream(targetFile);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(StockInfoFile.this);
            oos.flush();
            oos.close();
            oos = null;
            result = true;
        } catch (Exception e) {
            System.out.println("write object e:" + e);
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                oos = null;
            }
        }
        return result;
    }

    public void saveToDB(final CallBack stockInfoFile_Callback) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    SQLiteUtility sQLiteUtility = new SQLiteUtility();
                    Connection con = sQLiteUtility.getConnection();
                    String stockNum = "_" + mStockNum;

                    // 建立table
                    sQLiteUtility.createTable(con, stockNum, false);

                    // 新增資料
                    ArrayList<String> keyAllDay = new ArrayList<String>(mStockInfoMap.keySet());
                    Collections.sort(keyAllDay);

                    for (String eachDay : keyAllDay) {
                        HistoryStockInfo eachHistoryStockInfo = mStockInfoMap.get(eachDay);
                        if (eachHistoryStockInfo != null) {
                            sQLiteUtility.insert(con, stockNum, eachHistoryStockInfo.date,
                                    eachHistoryStockInfo.transactionNumByStockUnit,
                                    eachHistoryStockInfo.transactionStockMoney,
                                    eachHistoryStockInfo.startPrice,
                                    eachHistoryStockInfo.highPrice, eachHistoryStockInfo.lowPrice,
                                    eachHistoryStockInfo.endPrice, eachHistoryStockInfo.gapPrice,
                                    eachHistoryStockInfo.transactionNumByStockCount);
                        }
                    }

                    con.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (stockInfoFile_Callback != null) {
                    stockInfoFile_Callback.call(null);
                }

            }
        }).start();

    }

    public static void loadFromDB(final String _stockNum, final CallBack stockInfoFile_Callback) {
        final StockInfoFile stockInfoFile = new StockInfoFile(_stockNum,
                new HashMap<String, HistoryStockInfo>());

        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    SQLiteUtility sQLiteUtility = new SQLiteUtility();
                    Connection con = sQLiteUtility.getConnection();
                    String stockNumTable = "_" + _stockNum;

                    if (sQLiteUtility.isTableExists(con, stockNumTable)) {
                        ResultSet rs = sQLiteUtility.selectAll(con, stockNumTable);
                        while (rs.next()) {
                            String date = rs.getString("date");
                            String transaction_num_stock_unit = rs
                                    .getString("transaction_num_stock_unit");
                            String transaction_stock_money = rs
                                    .getString("transaction_stock_money");
                            String start_price = rs.getString("start_price");
                            String high_price = rs.getString("high_price");
                            String low_price = rs.getString("low_price");
                            String end_price = rs.getString("end_price");
                            String gap_price = rs.getString("gap_price");
                            String transaction_num_of_count = rs
                                    .getString("transaction_num_of_count");

                            HistoryStockInfo historyStockInfo = new HistoryStockInfo(_stockNum,
                                    date, transaction_num_stock_unit, transaction_stock_money,
                                    start_price, high_price, low_price, end_price, gap_price,
                                    transaction_num_of_count);

                            stockInfoFile.mStockInfoMap.put(date, historyStockInfo);
                        }
                    }

                    con.close();

                    if (stockInfoFile_Callback != null) {
                        stockInfoFile_Callback.call(stockInfoFile);
                    }
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public static String getSavedName(String stockName, ArrayList<String> dayKey) {
        String fileName = null;
        if (dayKey != null) {
            if (dayKey.size() >= 2) {
                fileName = stockName + "_" + dayKey.get(0) + "_" + dayKey.get(dayKey.size() - 1);
            } else if (dayKey.size() == 1) {
                fileName = stockName + "_" + dayKey.get(0);
            } else {
                fileName = stockName;
            }
        } else {
            fileName = stockName;
        }
        return fileName;
    }

    public static StockInfoFile load(String fileName) {
        StockInfoFile stockInfoFile = null;

        try {
            // String work_path = System.getProperty("user.dir");
            File folder = getFolerToSave();
            // System.out.println("work_path: " + work_path);
            File targetFile = new File(folder, fileName);
            if (targetFile.exists()) {
                FileInputStream fis = new FileInputStream(targetFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                stockInfoFile = (StockInfoFile) ois.readObject();
                ois.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("read serial e:" + e);
        }

        return stockInfoFile;
    }

    public boolean isIncludeTheMonth(String yyyymmdd) {
        String subString = yyyymmdd.substring(0, 6);
        // System.out.println("compare subString:" + subString);

        ArrayList<String> keyAllDay = new ArrayList<String>(mStockInfoMap.keySet());
        Collections.sort(keyAllDay);
        for (String eachDay : keyAllDay) {
            // System.out.println("eachDay.substring(0, 6):" +
            // eachDay.substring(0, 6));
            if (eachDay.substring(0, 6).equals(subString)) {
                return true;
            }

        }

        return false;

    }
}
