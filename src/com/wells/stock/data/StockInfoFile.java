package com.wells.stock.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class StockInfoFile implements Serializable {
    String mStockNum;
    public ArrayList<String> mAllDayKey;// 每一日, mStockInfoMap的key, ex 20171105
    public HashMap<String, HistoryStockInfo> mStockInfoMap;// 每一天相對應的HistoryStockInfo
    boolean mIsSaved = false;

    public StockInfoFile(String stockNum, ArrayList<String> dayKey,
            HashMap<String, HistoryStockInfo> map) {
        mStockNum = stockNum;
        setAllDayKey(dayKey);
        setStockInfoMap(map);
    }

    public void setAllDayKey(ArrayList<String> dayKey) {
        mAllDayKey = dayKey;
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
        System.out.println("+isIncludeTheMonth:" + yyyymmdd);
        String subString = yyyymmdd.substring(0, 6);
        System.out.println("subString:" + subString);
        for (String eachDay : mAllDayKey) {
            if (eachDay.substring(0, 6).equals(subString)) {
                return true;
            }

        }

        return false;

    }
}
