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
    public ArrayList<String> mAllDayKey;//每一日, mStockInfoMap的key, ex 20171105
    public HashMap<String, HistoryStockInfo> mStockInfoMap;//每一天相對應的HistoryStockInfo
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

    public boolean isSaved() {
        String work_path = System.getProperty("user.dir");
        System.out.println("work_path: " + work_path);
        File targetFile = new File(work_path, mStockNum);
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
            String work_path = System.getProperty("user.dir");
            System.out.println("work_path: " + work_path);
            File targetFile = new File(work_path, mStockNum);

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
            String work_path = System.getProperty("user.dir");
            System.out.println("work_path: " + work_path);
            File targetFile = new File(work_path, fileName);
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
}
