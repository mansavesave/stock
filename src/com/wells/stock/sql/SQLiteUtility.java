package com.wells.stock.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;

public class SQLiteUtility {
    public static String DataBaseName = "stock";

    public Connection getConnection() throws SQLException {
        SQLiteConfig config = new SQLiteConfig();
        // config.setReadOnly(true);
        config.setSharedCache(true);
        config.enableRecursiveTriggers(true);

        SQLiteDataSource ds = new SQLiteDataSource(config);
        ds.setUrl("jdbc:sqlite:./save_data/" + DataBaseName + ".db");
        return ds.getConnection();
    }

    // create Table
    public void createTable(Connection con, String tableName, boolean removeOldTable)
            throws SQLException {
        /**
         * "fields":["日期","成交股數","成交金額","開盤價","最高價","最低價","收盤價","漲跌價差","成交筆數"]
         */

        String sql = "";
        if (removeOldTable) {
            sql = "DROP TABLE IF EXISTS " + tableName
                    + " ;create table test (id integer, name string); ";
        } else {
            sql = "create table IF NOT EXISTS "
                    + tableName
                    + " ("
                    + "date string primary key not null, transaction_num_stock_unit string, "
                    + "transaction_stock_money string, start_price string, high_price string, "
                    + "low_price string, end_price string, gap_price string, transaction_num_of_count string);";
        }
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);

    }

    // drop table
    public void dropTable(Connection con, String tableName) throws SQLException {
        String sql = "drop table " + tableName;
        Statement stat = null;
        stat = con.createStatement();
        stat.executeUpdate(sql);
    }

    // 新增
    public void insert(Connection con, String tableName, String date,
            String transaction_num_stock_unit, String transaction_stock_money, String start_price,
            String high_price, String low_price, String end_price, String gap_price,
            String transaction_num_of_count) throws SQLException {
        // String sql = "insert into "
        // + tableName
        // +
        // " (date,transaction_num_stock_unit,transaction_stock_money,start_price,high_price,low_price,end_price,gap_price,transaction_num_of_count) "
        // + "values(?,?,?,?,?,?,?,?,?) WHERE NOT EXISTS(select * from " +
        // tableName
        // + " where date = \'" + date + "\')";
        String sql = "insert OR IGNORE into "
                + tableName
                + " (date,transaction_num_stock_unit,transaction_stock_money,start_price,high_price,low_price,end_price,gap_price,transaction_num_of_count) "
                + "values(?,?,?,?,?,?,?,?,?)";
        // System.out.println("insert cmd:" + sql);
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1;
        pst.setString(idx++, date);
        pst.setString(idx++, transaction_num_stock_unit);
        pst.setString(idx++, transaction_stock_money);
        pst.setString(idx++, start_price);
        pst.setString(idx++, high_price);
        pst.setString(idx++, low_price);
        pst.setString(idx++, end_price);
        pst.setString(idx++, gap_price);
        pst.setString(idx++, transaction_num_of_count);
        pst.executeUpdate();

    }

    // // 修改
    // public void update(Connection con, String tableName, int id, String name)
    // throws SQLException {
    // String sql = "update " + tableName + " set name = ? where id = ?";
    // PreparedStatement pst = null;
    // pst = con.prepareStatement(sql);
    // int idx = 1;
    // pst.setString(idx++, name);
    // pst.setInt(idx++, id);
    // pst.executeUpdate();
    // }

    // 刪除
    public void delete(Connection con, String tableName, String date) throws SQLException {
        String sql = "delete from " + tableName + " where date = ?";
        PreparedStatement pst = null;
        pst = con.prepareStatement(sql);
        int idx = 1;
        pst.setString(idx++, date);
        pst.executeUpdate();
    }

    public void selectAll(Connection con, String tableName) throws SQLException {
        String sql = "select * from " + tableName;
        Statement stat = null;
        ResultSet rs = null;
        stat = con.createStatement();
        rs = stat.executeQuery(sql);
        while (rs.next()) {
            String message = rs.getString("date") + "\t"
                    + rs.getString("transaction_num_stock_unit")
                    + rs.getString("transaction_stock_money") + rs.getString("start_price")
                    + rs.getString("high_price") + rs.getString("low_price")
                    + rs.getString("end_price") + rs.getString("gap_price")
                    + rs.getString("transaction_num_of_count");
            System.out.println(message);
        }
    }

    public static void runSample() {
        SQLiteUtility sQLiteUtility = new SQLiteUtility();
        try {
            Connection con = sQLiteUtility.getConnection();
            String stockNum = "_2002";

            // 建立table
            sQLiteUtility.createTable(con, stockNum, false);

            // 新增資料
            sQLiteUtility.insert(con, stockNum, "20171212", "100", "3300", "10", "20", "5", "15",
                    "5", "200");
            // sQLiteUtility.insert(con, 2, "第二個");
            // 查詢顯示資料
            System.out.println("新增一筆資料後狀況:");
            sQLiteUtility.selectAll(con, stockNum);

            // 修改資料
            // System.out.println("修改第一筆資料後狀況:");
            // sQLiteUtility.update(con, 1, "這個值被改變了!");
            // 查詢顯示資料
            // sQLiteUtility.selectAll(con);

            // 刪除資料
            System.out.println("刪除第一筆資料後狀況:");
            sQLiteUtility.delete(con, stockNum, "20171212");
            // 查詢顯示資料
            sQLiteUtility.selectAll(con, stockNum);

            // 刪除table
            sQLiteUtility.dropTable(con, stockNum);

            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
