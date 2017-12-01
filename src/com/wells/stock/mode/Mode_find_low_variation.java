package com.wells.stock.mode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.wells.stock.setting.HistoryStockInfo;

/**
 * 
 * @author Wells.Chen
 * 
 *         找出所有至少連續 mKeepDay 天內股價變化低於gap_variation 漲跌幅度(以日為單位)
 *         可以得到開始天、結束天、結束時的漲跌幅度(這是一組資料)，可能有很多組資料
 */
public class Mode_find_low_variation implements Serializable {
    class Mode_find_low_variation_result_item implements Serializable {
        HistoryStockInfo start_day;// 包含超過的那一天，開始天
        HistoryStockInfo end_day;// 包含超過的那一天，結束天
        int duration_day;// 多少交易日時間內，幅度不大
        double overVariation;// 結束超過那天(交易天)的變異數

    }

    ArrayList<Mode_find_low_variation_result_item> mResult;

    double gap_variation = 0.2; // 漲跌幅度，正負都算
    double mKeepDay = 15;
    boolean mExecuteCompleted = false;

    HashMap<String, HistoryStockInfo> mHistoryStockInfoMap;
    ArrayList<String> mSortedKey;

    ArrayList<Boolean> mBooleanResult = new ArrayList<Boolean>();

    public Mode_find_low_variation(HashMap<String, HistoryStockInfo> map, ArrayList<String> sortKey) {
        mHistoryStockInfoMap = map;
        mSortedKey = sortKey;
        mResult = new ArrayList<Mode_find_low_variation_result_item>();
    }

    public void execute() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                mBooleanResult.clear();
                // TODO Auto-generated method stub
                if (mSortedKey != null && mHistoryStockInfoMap != null) {
                    ArrayList<String> overGapNode = new ArrayList<String>();// 所有超過漲跌幅度的日
                    for (String eachKey : mSortedKey) {
                        HistoryStockInfo eachHistoryStockInfo = mHistoryStockInfoMap.get(eachKey);
                        if (eachHistoryStockInfo != null) {
                            double startPrice = Double.parseDouble(eachHistoryStockInfo.startPrice);
                            double endPrice = Double.parseDouble(eachHistoryStockInfo.endPrice);
                            double gap = (endPrice - startPrice) / startPrice;

                            // System.out.println("gap:" + gap);
                            // System.out.println("Math.abs(gap):" +
                            // Math.abs(gap));
                            // System.out.println("gap aa:" +
                            // eachHistoryStockInfo.gapPrice);
                            // System.out.println("gap aa each:" +
                            // eachHistoryStockInfo);
                            if (Math.abs(gap) > gap_variation) {
                                overGapNode.add(eachKey);
                            }

                        } else {
                            System.out.println("can't get value:"
                                    + Thread.currentThread().getStackTrace());
                        }

                    }

                    // 找出超過n 天duration的結果
                    for (int i = 0; i < overGapNode.size(); i++) {
                        String eachDay = overGapNode.get(i);
                        if (i >= 1) {
                            String eachPreDay = overGapNode.get(i - 1);
                            int preIndex = mSortedKey.indexOf(eachPreDay);
                            int nowIndex = mSortedKey.indexOf(eachDay);
                            if (nowIndex - preIndex >= mKeepDay) {
                                Mode_find_low_variation_result_item result_item = new Mode_find_low_variation_result_item();
                                result_item.start_day = mHistoryStockInfoMap.get(eachPreDay);
                                result_item.end_day = mHistoryStockInfoMap.get(eachDay);
                                result_item.duration_day = nowIndex - preIndex;
                                result_item.overVariation = Double.parseDouble(mHistoryStockInfoMap
                                        .get(eachDay).startPrice)
                                        - Double.parseDouble(mHistoryStockInfoMap.get(eachDay).endPrice);
                                mResult.add(result_item);
                            }
                        }

                        int nowIndex = mSortedKey.indexOf(eachDay);
                        int nextIndex = nowIndex + 50;
                        if (mSortedKey.size() > nextIndex) {
                            String nextStringKey = mSortedKey.get(nextIndex);
                            HistoryStockInfo next_historyStockInfo = mHistoryStockInfoMap
                                    .get(nextStringKey);

                            HistoryStockInfo current_historyStockInfo = mHistoryStockInfoMap
                                    .get(eachDay);

                            String targetPrice = next_historyStockInfo.endPrice;

                            double startPrice = Double
                                    .parseDouble(current_historyStockInfo.startPrice);
                            double endPrice = Double.parseDouble(current_historyStockInfo.endPrice);
                            double gap = (endPrice - startPrice) / startPrice;

                            double result_gap = (Double.parseDouble(targetPrice) - Double
                                    .parseDouble(current_historyStockInfo.endPrice))
                                    / Double.parseDouble(targetPrice);

                            boolean success = false;
                            if (gap > 0 && result_gap > 0) {
                                if (Math.abs(gap) < Math.abs(result_gap)) {
                                    success = true;
                                }
                            } else if (gap < 0 && result_gap < 0) {
                                if (Math.abs(gap) < Math.abs(result_gap)) {
                                    success = true;
                                }

                            } else {
                                success = false;
                            }
                            mBooleanResult.add(success);

                            String format = "percent(%.5f => %.5f) price(%s => %s) result(%b) %s";
                            String message = String.format(format, gap, result_gap,
                                    current_historyStockInfo.endPrice, targetPrice, success,
                                    current_historyStockInfo.date);

                            // String message = "percent(" + gap + ") => (" +
                            // result_gap + "), price("
                            // + current_historyStockInfo.endPrice + ") => (" +
                            // targetPrice
                            // + ")";

                            System.out.println(message);

                        }

                    }

                }

                int percentSuccessCount = 0;
                for (boolean each : mBooleanResult) {
                    if (each) {
                        percentSuccessCount++;
                    }
                }
                if (mBooleanResult.size() > 0) {
                    System.out.println("Boolean Result:"
                            + ((double) percentSuccessCount / mBooleanResult.size()));
                }

                mExecuteCompleted = true;

                // if (mResult.size() > 0) {
                // for (Mode_find_low_variation_result_item each : mResult) {
                // String message = each.start_day.date + " - " +
                // each.end_day.date
                // + " total day:" + each.duration_day + " finish by"
                // + each.overVariation;
                // System.out.println(message);
                //
                // }
                //
                // } else {
                // System.out.println("can't find");
                // }

            }
        }).start();

    }
}
