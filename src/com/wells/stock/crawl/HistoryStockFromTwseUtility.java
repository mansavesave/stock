package com.wells.stock.crawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wells.stock.CallBack;
import com.wells.stock.data.HistoryStockInfo;
import com.wells.stock.data.StockInfoFile;
import com.wells.stock.setting.StockSetting;

public class HistoryStockFromTwseUtility extends HistoryCrawl implements Serializable {

    /**
     * 取得某一個月的範例
     * http://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date
     * =20170921&stockNo=2002
     */

    String QueryFormat = "http://www.twse.com.tw/exchangeReport/STOCK_DAY?response=json&date=%s&stockNo=%s";
    String mQueryURL;

    ArrayList<String> mTotal_querry_date_string_list = new ArrayList<String>();

    public HistoryStockFromTwseUtility(String stockNum) {
        // query is by month unit, we will parser it to day unit, total is 5
        // years

        mStockNum = stockNum;
        mKeyAllDay = new ArrayList<String>();
        mMapStockInfo = new HashMap<String, HistoryStockInfo>();

        mTotal_querry_date_string_list = new ArrayList<String>();

        // 收集所有查詢的日期，單位是月
        ArrayList<Date> total_querry_date_list = new ArrayList<Date>();
        Date temp = null;
        Date startDate = null;
        try {
            startDate = StockSetting.QueryDateFormat.parse(StockSetting.HISTORY_QueryDate_START);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while (true) {
            if (temp == null) {
                try {
                    temp = StockSetting.QueryDateFormat.parse(StockSetting.HISTORY_QueryDate_END);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                int year = temp.getYear();
                int month = temp.getMonth();
                if (month == 0) {
                    month = 11;
                    year = year - 1;
                } else {
                    month = month - 1;
                }
                temp = new Date(year, month, 1, 12, 0);

                if (temp.getTime() < startDate.getTime()) {
                    break;
                }
            }

            total_querry_date_list.add(temp);
        }

        for (int i = 0; i < total_querry_date_list.size(); i++) {
            String dateString = new SimpleDateFormat("yyyyMMdd").format(total_querry_date_list
                    .get(i));
            mTotal_querry_date_string_list.add(dateString);
        }

    }

    /**
     * 
     * @return json date
     * 
     *         {"stat":"OK","date":"20170921","title":
     *         "106年09月 2002 中鋼             各日成交資訊"
     *         ,"fields":["日期","成交股數","成交金額",
     *         "開盤價","最高價","最低價","收盤價","漲跌價差","成交筆數"
     *         ],"data":[["106/09/01","13,662,138"
     *         ,"343,988,883","25.15","25.25",
     *         "25.10","25.15","-0.15","4,757"],["106/09/04"
     *         ,"8,328,226","209,773,338"
     *         ,"25.15","25.25","25.15","25.20","+0.05"
     *         ,"2,847"],["106/09/05","16,371,288"
     *         ,"412,393,750","25.15","25.30",
     *         "25.15","25.30","+0.10","3,521"],["106/09/06"
     *         ,"17,886,645","449,675,394"
     *         ,"25.25","25.25","25.05","25.05","-0.25"
     *         ,"4,240"],["106/09/07","12,149,042"
     *         ,"304,733,884","25.10","25.20",
     *         "25.00","25.15","+0.10","3,759"],["106/09/08"
     *         ,"9,560,864","240,218,209"
     *         ,"25.10","25.20","25.00","25.20","+0.05"
     *         ,"2,984"],["106/09/11","9,330,718"
     *         ,"235,527,156","25.20","25.30","25.10"
     *         ,"25.30","+0.10","3,206"],["106/09/12"
     *         ,"8,352,698","210,634,155","25.30"
     *         ,"25.30","25.10","25.30"," 0.00"
     *         ,"2,605"],["106/09/13","5,789,392"
     *         ,"145,415,057","25.30","25.30","25.05"
     *         ,"25.05","-0.25","2,570"],["106/09/14"
     *         ,"6,999,423","175,225,075","25.00"
     *         ,"25.10","25.00","25.00","-0.05"
     *         ,"2,848"],["106/09/15","33,538,848"
     *         ,"831,275,980","24.95","24.95",
     *         "24.75","24.75","-0.25","6,834"],["106/09/18"
     *         ,"11,174,742","278,790,450"
     *         ,"24.75","25.10","24.75","25.05","+0.30"
     *         ,"4,568"],["106/09/19","8,789,063"
     *         ,"218,640,649","25.00","25.00","24.85"
     *         ,"24.85","-0.20","3,964"],["106/09/20"
     *         ,"26,784,640","659,038,129",
     *         "24.85","24.85","24.50","24.55","-0.30"
     *         ,"9,248"],["106/09/21","12,273,612"
     *         ,"301,401,526","24.50","24.75",
     *         "24.35","24.60","+0.05","4,348"],["106/09/22"
     *         ,"20,481,801","498,495,609"
     *         ,"24.40","24.45","24.25","24.40","-0.20"
     *         ,"6,567"],["106/09/25","12,902,922"
     *         ,"313,053,485","24.40","24.45",
     *         "24.20","24.25","-0.15","5,672"],["106/09/26"
     *         ,"10,076,307","244,244,628"
     *         ,"24.20","24.35","24.15","24.25"," 0.00"
     *         ,"3,495"],["106/09/27","9,034,893"
     *         ,"219,363,877","24.25","24.35","24.20"
     *         ,"24.25"," 0.00","2,942"],["106/09/28"
     *         ,"12,385,903","301,096,449",
     *         "24.25","24.45","24.20","24.35","+0.10"
     *         ,"4,017"],["106/09/29","11,253,180"
     *         ,"274,193,234","24.35","24.45",
     *         "24.30","24.35"," 0.00","3,077"],["106/09/30"
     *         ,"1,510,358","36,864,753"
     *         ,"24.40","24.45","24.35","24.45","+0.10",
     *         "910"]],"notes":["符號說明:+/-/X表示漲/跌/不比價"
     *         ,"當日統計資訊含一般、零股、盤後定價、鉅額交易，不含拍賣、標購。"
     *         ,"ETF證券代號第六碼為K、M、S、C者，表示該ETF以外幣交易。"]}
     */
    public String getStockDataFromWeb(String date) {
        String result = "";
        mQueryURL = String.format(QueryFormat, date, mStockNum);
        // System.out.println("get data, urlString:: " + urlString);
        URL url;
        try {
            System.out.println("mQueryURL:" + mQueryURL);
            url = new URL(mQueryURL);
            HttpURLConnection httpFetchStockURLConnection = (HttpURLConnection) url
                    .openConnection();
            // httpFetchStockURLConnection.setRequestMethod("GET");
            httpFetchStockURLConnection.setRequestMethod("POST");
            httpFetchStockURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpFetchStockURLConnection.setRequestProperty("Content-type",
                    "text/html;charset=UTF-8");
            httpFetchStockURLConnection.setConnectTimeout(5000);
            httpFetchStockURLConnection.setReadTimeout(5000);

            int responseCode = httpFetchStockURLConnection.getResponseCode();
            // System.out.println("GET Response Code :: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        httpFetchStockURLConnection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // print result
                result = response.toString();
                // System.out.println(response.toString());
            } else {
                System.out.println("GET stock, request not worked:" + date);
            }

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            System.out.println(date + ", MalformedURLException:" + e);
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println(date + ", IOException:" + e);
            e.printStackTrace();
        }

        return result;

    }

    public HistoryStockInfo[] parserStockJson(String jsonString) {
        // System.out.println("+parserStockJson:" + jsonString);
        ArrayList<HistoryStockInfo> result = new ArrayList();
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        StringBuilder stringbuilder = new StringBuilder();

        try {
            JSONObject jSONObject = new JSONObject(jsonString);
            if (!jSONObject.isNull("data")) {
                JSONArray msgArray = jSONObject.getJSONArray("data");
                // System.out.println("msgArray:" + msgArray.length());
                if (msgArray != null) {
                    for (int i = 0; i < msgArray.length(); i++) {
                        // System.out.println("msgArray:" + msgArray);
                        JSONArray eachJson = msgArray.getJSONArray(i);
                        HistoryStockInfo historyStockInfo = new HistoryStockInfo(mStockNum,
                                eachJson.toString());
                        result.add(historyStockInfo);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("jsonString:" + jsonString);
        }

        return result.toArray(new HistoryStockInfo[0]);
    }

    @Override
    public void doCrawl(final CallBack callBack) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < mTotal_querry_date_string_list.size(); i++) {
                    String queryString = getStockDataFromWeb(mTotal_querry_date_string_list.get(i));
                    HistoryStockInfo[] infoArray = parserStockJson(queryString);
                    if (infoArray != null && infoArray.length > 0) {
                        for (int j = 0; j < infoArray.length; j++) {
                            mMapStockInfo.put(infoArray[j].date, infoArray[j]);
                        }
                        // System.out.println("infoArray:" + infoArray[0]);
                    }

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                // 最得mHistoryStockInfoMap的key，並排列
                mKeyAllDay = new ArrayList<String>(mMapStockInfo.keySet());
                Collections.sort(mKeyAllDay);

                if (callBack != null) {
                    StockInfoFile stockInfoFile = HistoryStockFromTwseUtility.this
                            .getStockInfoFile();
                    callBack.call(stockInfoFile);
                }

            }
        }).start();
    }

}
