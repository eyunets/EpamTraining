package com.epam.training.db;

import java.util.ResourceBundle;

public class DBResourceManager {
	private static DBResourceManager instance = new DBResourceManager();
	private static String DB_PROPS_FILE = "database";
	public static ResourceBundle bundle = ResourceBundle.getBundle(DB_PROPS_FILE);
	private DBResourceManager() {
	}

	public static DBResourceManager getInstance() {
		return instance;
	}

	public String getValue(String key) {
		return bundle.getString(key);
	}
}
