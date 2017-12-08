package com.wells.stock.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Properties;

import com.wells.stock.utility.Utility;

public class StockSetting implements Serializable {
    public static final String Field_name = "名稱(股票號碼)";// 15
    public static final String Field_price = "現在價格";// 12
    public static final String Field_start_price = "開盤價格";// 12
    public static final String Field_hight_price = "最高價格";// 12
    public static final String Field_low_price = "最低價格";// 12
    public static final String Field_quantum_total = "總量";// 12
    public static final String Field_current_time = "時間";// 25

    public static final int Field_name_lengh = 15;
    public static final int Field_price_lengh = 20;
    public static final int Field_start_price_lengh = 20;
    public static final int Field_hight_price_lengh = 20;
    public static final int Field_low_price_lengh = 20;
    public static final int Field_quantum_total_lengh = 20;
    public static final int Field_current_time_lengh = 20;

    public static String DisplayFormat;

    public static String[][] StockList = { { "2330", "tse" }, { "2353", "tse" }, { "2451", "tse" },
            { "5371", "otc" }, { "2420", "tse" } };
    public static String Property_Ready_Key = "Property_Ready_Key";
    public static File PropertyFile = new File(Utility.getWorkFolderPath(), "myProp.properties");

    public static SimpleDateFormat QueryDateFormat = new SimpleDateFormat("yyyyMMdd");

    public static String HISTORY_QueryDate_END = "20171130";
    public static String HISTORY_QueryDate_START = "20170101";

    public static void init() {
        DisplayFormat = "%-" + StockSetting.Field_name_lengh + "s\t";
        DisplayFormat += "%-" + StockSetting.Field_price_lengh + "s\t";
        DisplayFormat += "%-" + StockSetting.Field_start_price_lengh + "s\t";
        DisplayFormat += "%-" + StockSetting.Field_hight_price_lengh + "s\t";
        DisplayFormat += "%-" + StockSetting.Field_low_price_lengh + "s\t";
        DisplayFormat += "%-" + StockSetting.Field_quantum_total_lengh + "s\t";
        DisplayFormat += "%-" + StockSetting.Field_current_time_lengh + "s\t";
        // System.out.println("DisplayFormat: " + DisplayFormat);

        initProperty();
    }

    private static void initProperty() {
        if (!PropertyFile.exists()) {
            Properties properties = new Properties();
            for (String[] stockEach : StockList) {
                properties.setProperty(stockEach[0], stockEach[1]);
            }

            properties.setProperty(Property_Ready_Key, "false");

            FileOutputStream output = null;
            try {
                output = new FileOutputStream(PropertyFile.getAbsolutePath());
                properties.store(output, null);
            } catch (IOException io) {
                io.printStackTrace();
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }

    public static Properties getProperty() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PropertyFile));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return properties;
    }

    public static String getProperty(String key) {
        String result = null;
        Properties properties = getProperty();
        if (properties != null) {
            result = properties.getProperty(key, null);
        }
        return result;
    }

    public static void writeProperty(String key, String value) {
        Properties props = new Properties();
        FileOutputStream out;
        try {
            out = new FileOutputStream(PropertyFile.getAbsolutePath());
            props.setProperty(key, value);
            props.store(out, null);
            out.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
