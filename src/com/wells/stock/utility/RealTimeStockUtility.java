package com.wells.stock.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wells.stock.setting.StockInfo;

public class RealTimeStockUtility implements Serializable{
    static final String COOKIES_HEADER = "Set-Cookie";
    // static java.net.CookieManager msCookieManager = new
    // java.net.CookieManager();

    public static String Login_URL_Template = "http://mis.twse.com.tw/stock/fibest.jsp?lang=zh_tw&stock=%s";

    // http://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=otc_5371.tw&json=1&delay=0&_=1510819244458
    public static String Fetch_Stock_URL_Template = "http://mis.twse.com.tw/stock/api/getStockInfo.jsp?ex_ch=%s_%s.tw&json=1&delay=0&_=%d";

    private ArrayList<String> mCookieList;
    // public String mtockNum; // 2002
    public StockInfo mStockInfo;

    // public RealTimeStock(String stockNum) {
    // stockInfo.numName = stockNum;
    //
    // mStockInfo = stockInfo;
    // mtockNum = stockNum;
    // mCookieList = new ArrayList<String>();
    // }

    public RealTimeStockUtility(StockInfo stockInfo) {
        mStockInfo = stockInfo;
        // mtockNum = stockInfo.numName;
        mCookieList = new ArrayList<String>();
    }

    public void init() {
        login();
    }

    public StockInfo getData() {
        String jsonRawData = getStockDataFromWeb();
        return parserStockJson(jsonRawData);
    }

    public void login() {
        // URL obj = new
        // URL("http://mis.tse.com.tw/stock/api/getStockInfo.jsp?ex_ch=tse_2353.tw&json=1&delay=0&_=1510716318196");
        String urlString = String.format(Login_URL_Template, mStockInfo.numName);
        // System.out.println("login urlString: " + urlString);
        URL url;
        try {
            url = new URL(urlString);
            HttpURLConnection httpLoginURLConnection = (HttpURLConnection) url.openConnection();
            httpLoginURLConnection.setRequestMethod("GET");
            httpLoginURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
            httpLoginURLConnection.setRequestProperty("Content-type", "text/html;charset=UTF-8");
            httpLoginURLConnection.setConnectTimeout(5000);
            httpLoginURLConnection.setReadTimeout(5000);

            int responseCode = httpLoginURLConnection.getResponseCode();
            System.out.println(mStockInfo.numName + " login: " + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(httpLoginURLConnection.getInputStream(), "UTF-8"));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                // print result
                // System.out.println(response.toString());
            } else {
                System.out.println("Login request not worked");
            }

            String headerName = null;
            for (int i = 1; (headerName = httpLoginURLConnection.getHeaderFieldKey(i)) != null; i++) {
                if (headerName.equals(COOKIES_HEADER)) {
                    String cookie = httpLoginURLConnection.getHeaderField(i);
                    mCookieList.add(cookie);
                    // System.out.println("aa cookie:" + cookie);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Login Exception:" + e.toString());
        }

    }

    public String getStockDataFromWeb() {
        String result = null;
        int cookieSize = mCookieList.size();
        // System.out.println("cookieSize:" + cookieSize);

        if (cookieSize > 0) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the
            // servers are using ';'
            StringBuilder stringBuilderCookie = new StringBuilder();
            for (int i = 0; i < cookieSize; i++) {
                stringBuilderCookie.append(mCookieList.get(i));
                if (i != cookieSize - 1) { // not be last item
                    stringBuilderCookie.append(";");
                }

            }
            // System.out.println("companyType:: " + companyType);
            // System.out.println("mtockNum:: " + mtockNum);
            String urlString = String.format(Fetch_Stock_URL_Template, mStockInfo.companyType, mStockInfo.numName, Utility.getCurrentTimeStamp());
            // System.out.println("get data, urlString:: " + urlString);
            URL url;
            try {
                url = new URL(urlString);
                HttpURLConnection httpFetchStockURLConnection = (HttpURLConnection) url.openConnection();
                httpFetchStockURLConnection.setRequestMethod("GET");
                httpFetchStockURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
                httpFetchStockURLConnection.setRequestProperty("Content-type", "text/html;charset=UTF-8");
                httpFetchStockURLConnection.setConnectTimeout(5000);
                httpFetchStockURLConnection.setReadTimeout(5000);

                httpFetchStockURLConnection.setRequestProperty("Cookie", stringBuilderCookie.toString());

                int responseCode = httpFetchStockURLConnection.getResponseCode();
                // System.out.println("GET Response Code :: " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(httpFetchStockURLConnection.getInputStream(), "UTF-8"));
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
                    System.out.println("GET stock, request not worked");
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        return result;
    }

    public StockInfo parserStockJson(String jsonString) {
        // System.out.println("+parserStockJson:" + jsonString);
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        StringBuilder stringbuilder = new StringBuilder();
        ArrayList<String> tagList = new ArrayList<String>();
        tagList.add("nf");// 全名
        tagList.add("n");// 簡稱
        tagList.add("c");// 股票代號, ex 2002

        tagList.add("z");// 價格
        tagList.add("o");// 開盤
        tagList.add("h");// 最高
        tagList.add("l");// 最低

        // tagList.add("y");
        tagList.add("tv"); // 單量
        tagList.add("v"); // 總量
        tagList.add("tlong"); // Timestamp

        try {
            JSONObject jSONObject = new JSONObject(jsonString);
            if (!jSONObject.isNull("msgArray")) {
                JSONArray msgArray = jSONObject.getJSONArray("msgArray");
                // System.out.println("msgArray:" + msgArray.length());
                if (msgArray != null) {
                    for (int i = 0; i < msgArray.length(); i++) {
                        JSONObject jSONObjectEach = msgArray.getJSONObject(i);

                        for (int j = 0; j < tagList.size(); j++) {
                            String tag = tagList.get(j);
                            if (!jSONObjectEach.isNull(tag)) {
                                String value = jSONObjectEach.getString(tag);
                                stringbuilder.append(tag + ":" + value + "\n");
                                // System.out.println("each:" + i + " " + tag
                                // + ":" + value);
                                if (tag.equals("nf")) {
                                    mStockInfo.fullName = value;
                                } else if (tag.equals("n")) {
                                    mStockInfo.shortName = value;
                                } else if (tag.equals("c")) {
                                    mStockInfo.numName = value;
                                } else if (tag.equals("z")) {
                                    mStockInfo.currentPrice = value;
                                } else if (tag.equals("o")) {
                                    mStockInfo.startPrice = value;
                                } else if (tag.equals("h")) {
                                    mStockInfo.highPrice = value;
                                } else if (tag.equals("l")) {
                                    mStockInfo.lowPrice = value;
                                } else if (tag.equals("tv")) {
                                    mStockInfo.quantum_temporal = value;
                                } else if (tag.equals("v")) {
                                    mStockInfo.quantum_total = value;
                                } else if (tag.equals("tlong")) {
                                    mStockInfo.timeStamp = value;
                                }
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mStockInfo;
    }
}
