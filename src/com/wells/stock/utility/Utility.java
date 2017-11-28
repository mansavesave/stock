package com.wells.stock.utility;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
	public static long getCurrentTimeStamp() {
		Date date = new Date();
		return date.getTime();
	}

	public static String formatTime(long timeStamp) {
		String result = "";
		Date date = new Date();
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			result = sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String getWorkFolderPath() {
		// String workFolderPath = System.getProperty("user.dir");
		Path currentRelativePath = Paths.get("");
		String path = currentRelativePath.toAbsolutePath().toString();
		return path;
	}

}
