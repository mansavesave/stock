package com.wells.stock.setting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.wells.stock.utility.Utility;

public class StockInfo implements Serializable {

    public StockInfo(String _numName, String _companyType) {
        numName = _numName;
        companyType = _companyType;
    }

    public String numName;// 股票代號, ex 2002
    public String fullName;// 全名
    public String shortName;// 簡稱
    public String currentPrice;// 價格

    public String startPrice; // 開盤
    public String highPrice; // 最高
    public String lowPrice; // 最低

    public String quantum_total;// 總量
    public String quantum_temporal;// 單量
    public String timeStamp;// Timestamp
    public String companyType = "tse"; // tse or otc

    public String getDisplayLine() {
        String result = String.format(StockSetting.DisplayFormat, shortName + "(" + numName + ")", currentPrice, startPrice, highPrice, lowPrice, quantum_total,
                Utility.formatTime(Long.parseLong(timeStamp)));

        return result;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        String result = null;
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(this);
            so.flush();
            result = bo.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    public static StockInfo decodeString(String str) {
        StockInfo stockInfo = null;
        try {
            byte b[] = str.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            stockInfo = (StockInfo) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return stockInfo;
    }

}
