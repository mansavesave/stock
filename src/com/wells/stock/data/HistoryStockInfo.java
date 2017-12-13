package com.wells.stock.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wells.stock.setting.StockSetting;
import com.wells.stock.utility.Utility;

public class HistoryStockInfo implements Serializable {

    /**
     * "fields":["日期","成交股數","成交金額","開盤價","最高價","最低價","收盤價","漲跌價差","成交筆數"],
     * ["106/09/01","13,662,138","343,988,883","25.15","25.25","25.10",
     * "25.15","-0.15","4,757"]
     * 
     * @param jsonString
     *            : Stock info of one day of history
     */
    public HistoryStockInfo(String stockNum, String jsonString) {
        mStockNum = stockNum;
        mJsonRawString = jsonString;
        parser_json();
    }

    public HistoryStockInfo(String stockNum, String _date, String _transactionNumByStockUnit,
            String _transactionStockMoney, String _startPrice, String _highPrice, String _lowPrice,
            String _endPrice, String _gapPrice, String _transactionNumByStockCount) {
        mStockNum = stockNum;
        date = _date;
        transactionNumByStockUnit = _transactionNumByStockUnit;
        transactionStockMoney = _transactionStockMoney;
        startPrice = _startPrice;
        highPrice = _highPrice;
        lowPrice = _lowPrice;
        endPrice = _endPrice;
        gapPrice = _gapPrice;
        transactionNumByStockCount = _transactionNumByStockCount;

    }

    public String mJsonRawString;
    public String mStockNum;// 股票號碼
    public String date;// 日期
    public String transactionNumByStockUnit;// 成交股數
    public String transactionStockMoney;// 成交金額
    public String startPrice; // 開盤價
    public String highPrice; // 最高價
    public String lowPrice; // 最低價
    public String endPrice; // 收盤價
    public String gapPrice;// 漲跌價差
    public String transactionNumByStockCount;// 成交筆數

    public void parser_json() {
        try {
            if (mJsonRawString == null || mJsonRawString.isEmpty()) {
                return;
            }
            JSONArray msgArray = new JSONArray(mJsonRawString);
            if (msgArray != null) {
                if (1 <= msgArray.length()) {
                    date = msgArray.getString(0);
                    // System.out.println("parser raw date:" + date);
                    String[] dateParameter = date.split("/");
                    int tw_year = Integer.parseInt(dateParameter[0]);
                    int month = Integer.parseInt(dateParameter[1]);
                    int day = Integer.parseInt(dateParameter[2]);
                    // System.out.println("tw_year:" + tw_year + " month:" +
                    // month + " day:" + day);

                    Date dateType = new Date(tw_year + 1911 - 1900, month - 1, day, 12, 0);
                    date = StockSetting.QueryDateFormat.format(dateType);
                    // System.out.println("date format:" + date);
                }
                if (2 <= msgArray.length()) {
                    transactionNumByStockUnit = msgArray.getString(1);
                }
                if (3 <= msgArray.length()) {
                    transactionStockMoney = msgArray.getString(2);
                }
                if (4 <= msgArray.length()) {
                    startPrice = msgArray.getString(3);
                }
                if (5 <= msgArray.length()) {
                    highPrice = msgArray.getString(4);
                }
                if (6 <= msgArray.length()) {
                    lowPrice = msgArray.getString(5);
                }
                if (7 <= msgArray.length()) {
                    endPrice = msgArray.getString(6);
                }
                if (8 <= msgArray.length()) {
                    gapPrice = msgArray.getString(7);
                }
                if (9 <= msgArray.length()) {
                    transactionNumByStockCount = msgArray.getString(8);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mJsonRawString + "\n");
        stringBuilder.append(date + "\t" + transactionNumByStockUnit + "\t" + transactionStockMoney
                + "\t" + startPrice + "\t" + highPrice + "\t" + lowPrice + "\t" + endPrice + "\t"
                + gapPrice + "\t" + transactionNumByStockCount);
        return stringBuilder.toString();
    }

}
