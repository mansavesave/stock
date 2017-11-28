package com.wells.stock;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import com.wells.stock.setting.StockSetting;

public class Main {

	public static void main(String[] args) {
		StockSetting.init();

		MainUI mainUI = new MainUI();
		mainUI.show();
	}

}
