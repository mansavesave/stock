package com.wells.stock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import com.wells.stock.crawl.HistoryStockUtility;
import com.wells.stock.mode.Mode_find_low_variation;
import com.wells.stock.setting.StockInfo;
import com.wells.stock.setting.StockSetting;
import com.wells.stock.utility.RealTimeStockUtility;
import com.wells.stock.utility.RealTimeStockController;
import com.wells.stock.utility.Utility;

public class MainUI {
    public static int mFrameWidth = 800;
    public static int mFrameHeight = 600;
    public static double mScreenWidth;
    public static double mScreenHeight;

    ArrayList<JButton> mJButtonList = new ArrayList<JButton>();
    JFrame mMainJFrame;
    // JTextArea mJTextArea;
    JTextPane mJTextPane;
    DefaultStyledDocument mDefaultStyledDocument;
    JPanel mJPanel_1;
    JPanel mJPanel_2;

    Style mainStyle;
    Style mStyple_green;
    Style mStyple_read;

    public MainUI() {
        // initView();
        initView2();
        addListener();
    }

    public void initView2() {
        mMainJFrame = new JFrame("UI");
        mMainJFrame.setSize(mFrameWidth, mFrameHeight);
        mMainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GridBagLayout gridBagLayout = new GridBagLayout();
        mMainJFrame.getContentPane().setLayout(gridBagLayout);

        mJPanel_1 = new JPanel();
        // mJPanel_1.setBackground(Color.blue);
        mJPanel_1.setLayout(new FlowLayout());
        mJPanel_2 = new JPanel();
        // mJPanel_2.setBackground(Color.red);
        mJPanel_2.setLayout(new GridBagLayout());

        GridBagConstraints gbc_1 = new GridBagConstraints();
        gbc_1.fill = GridBagConstraints.BOTH;
        gbc_1.insets = new Insets(13, 13, 13, 13);
        gbc_1.gridx = 0;
        gbc_1.gridy = 0;
        gbc_1.gridwidth = 10;
        gbc_1.gridheight = 10;
        gbc_1.weightx = 1;
        gbc_1.weighty = 1;
        gridBagLayout.setConstraints(mJPanel_1, gbc_1);
        mMainJFrame.add(mJPanel_1);

        GridBagConstraints gbc_2 = new GridBagConstraints();
        gbc_2.fill = GridBagConstraints.BOTH;
        gbc_2.insets = new Insets(3, 3, 3, 3);
        gbc_2.gridx = 0;
        gbc_2.gridy = 10;
        gbc_2.gridwidth = 10;
        gbc_2.gridheight = 10;
        gbc_2.weightx = 1;
        gbc_2.weighty = 1;
        gridBagLayout.setConstraints(mJPanel_2, gbc_2);
        mMainJFrame.add(mJPanel_2);

        // �[Button
        for (int i = 0; i < 15; i++) {
            JButton button = new JButton("" + i);
            button.setPreferredSize(new Dimension(100, 40));
            mJPanel_1.add(button);
            mJButtonList.add(button);
        }

        StyleContext sc = new StyleContext();
        mDefaultStyledDocument = new DefaultStyledDocument(sc);
        mJTextPane = new JTextPane(mDefaultStyledDocument);

        JScrollPane jScrollPane = new JScrollPane(mJTextPane);
        jScrollPane.setPreferredSize(new Dimension(mFrameWidth - 40, 100));
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        GridBagConstraints gbc_textview = new GridBagConstraints();
        gbc_textview.fill = GridBagConstraints.BOTH;
        gbc_textview.insets = new Insets(3, 3, 3, 3);
        gbc_textview.gridx = 0;
        gbc_textview.gridy = 0;
        gbc_textview.gridwidth = 10;
        gbc_textview.gridheight = 10;
        gbc_textview.weightx = 1;
        gbc_textview.weighty = 1;
        mJPanel_2.add(jScrollPane, gbc_textview);
        // mMainJFrame.add(jScrollPane, gbc_1);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        mScreenWidth = dimension.getWidth();
        mScreenHeight = dimension.getHeight();
        int x = (int) ((dimension.getWidth() - mMainJFrame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - mMainJFrame.getHeight()) / 2);
        mMainJFrame.setLocation(x, y);

        Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
        mainStyle = sc.addStyle("MainStyle", defaultStyle);
        StyleConstants.setLeftIndent(mainStyle, 16);
        StyleConstants.setRightIndent(mainStyle, 16);
        StyleConstants.setFirstLineIndent(mainStyle, 16);
        StyleConstants.setFontFamily(mainStyle, "serif");
        StyleConstants.setFontSize(mainStyle, 12);

        mStyple_green = sc.addStyle("ConstantWidth", null);
        StyleConstants.setFontFamily(mStyple_green, "monospaced");
        StyleConstants.setForeground(mStyple_green, Color.green);

        mStyple_read = sc.addStyle("ConstantWidth", null);
        StyleConstants.setFontFamily(mStyple_read, "serif");
        StyleConstants.setForeground(mStyple_read, Color.red);
    }

    // public void initView() {
    // mMainJFrame = new JFrame();
    // mMainJFrame.setSize(mFrameWidth, mFrameHeight);
    // mMainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // mMainJFrame.getContentPane().setLayout(new FlowLayout());
    //
    // // �[Button
    // for (int i = 0; i < 15; i++) {
    // JButton button = new JButton("" + i);
    // button.setPreferredSize(new Dimension(100, 40));
    // mMainJFrame.add(button);
    // mJButtonList.add(button);
    // }
    //
    // mJTextArea = new JTextArea(5, 20);
    // mJTextArea.setEditable(true);
    //
    // JScrollPane jScrollPane = new JScrollPane(mJTextArea);
    // jScrollPane.setPreferredSize(new Dimension(mFrameWidth - 40, 100));
    // jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    //
    // mMainJFrame.add(jScrollPane, BorderLayout.CENTER);
    //
    // Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    // mScreenWidth = dimension.getWidth();
    // mScreenHeight = dimension.getHeight();
    // int x = (int) ((dimension.getWidth() - mMainJFrame.getWidth()) / 2);
    // int y = (int) ((dimension.getHeight() - mMainJFrame.getHeight()) / 2);
    // mMainJFrame.setLocation(x, y);
    //
    // }

    public void addListener() {
        mJButtonList.get(0).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                appendText("click 0");
                RealTimeStockController realTimeStockController = new RealTimeStockController();
                appendText(realTimeStockController.updateCurrentStatus());

            }
        });

        mJButtonList.get(12).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                appendText("click 12");

                Controller controller = new Controller();
                controller.click_12();

                // final HistoryStockUtility historyStockUtility = new
                // HistoryStockUtility("2002");
            }
        });

        mJButtonList.get(13).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                appendText("click 13");
                appendText("current timestamp:" + Utility.getCurrentTimeStamp());
                appendText("time foramt:" + Utility.formatTime(Utility.getCurrentTimeStamp()));

                appendText("work path:" + Utility.getWorkFolderPath());
                appendText("2330:" + StockSetting.getProperty("2330"));
            }
        });

        mJButtonList.get(14).addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // TODO Auto-generated method stub
                appendText("click 14");
                // RealTimeStock realTimeStock = new RealTimeStock("5371");
                RealTimeStockUtility realTimeStock = new RealTimeStockUtility(new StockInfo("2353", "tse"));
                realTimeStock.init();
                StockInfo stockInfo = realTimeStock.getData();
                if (stockInfo != null) {
                    appendText("stockInfo name:" + stockInfo.fullName);
                } else {
                    appendText("stockInfo name: null");
                }

            }
        });
    }

    public void show() {
        mMainJFrame.setVisible(true);
    }

    private void showText(String text) {
        // mJTextArea.setText(text);

        // mDefaultStyledDocument.setLogicalStyle(0, mainStyle);
        // mJTextPane.setText(text);
        // mDefaultStyledDocument.setCharacterAttributes(0, 10, mStyple_read,
        // false);

        try {
            // mDefaultStyledDocument.insertString(0, text, null);
            mDefaultStyledDocument.replace(0, mDefaultStyledDocument.getLength(), text, null);
            mDefaultStyledDocument.setCharacterAttributes(0, 20, mStyple_green, false);
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    StringBuilder stringBuilder = new StringBuilder();

    private void appendText(String text) {
        stringBuilder.append(text + "\n");
        showText(stringBuilder.toString());

    }

}
