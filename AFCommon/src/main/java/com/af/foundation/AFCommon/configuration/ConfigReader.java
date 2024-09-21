/* Decompiler 46ms, total 783ms, lines 93 */
package com.af.foundation.AFCommon.configuration;

import com.af.foundation.AFCommon.exceptions.AFCommonExceptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class ConfigReader {
	static String location = System.getenv("AF_HOME") == null ? "/Users/rajathirumal/Raja/code/java/CETL/dev/framework"
			: System.getenv("AF_HOME");
	public static final String FW_HOME;
	private static final String CONFIG_FOLDER;
	private static final String DEFAULT_FILE;
	public static String config_file;
	private static Properties properties;

	public static ConfigReader getInstance() {
		return ConfigReader.ConfigReaderHolder.INSTANCE;
	}

	public String getStringProperty(String key, String defaultValue) {
		String value = properties.getProperty(key).trim();
		if (value == null) {
			if (defaultValue == null) {
				throw new AFCommonExceptions("Invalid property: " + key);
			}

			value = defaultValue;
		}

		return value;
	}

	public int getIntProperty(String key, boolean getAbsValue) {
		String value = properties.getProperty(key).trim();

		try {
			int x = Integer.parseInt(value);
			return getAbsValue ? Math.abs(x) : x;
		} catch (NumberFormatException var5) {
			throw new AFCommonExceptions("'" + value + "' is not an integer value, check the property " + key);
		}
	}

	static {
		FW_HOME = location.endsWith("/") ? location : location + "/";
		CONFIG_FOLDER = FW_HOME + "conf/";
		DEFAULT_FILE = CONFIG_FOLDER + "prop.default";
		config_file = "";

		try {
			Scanner myReader = null;

			try {
				File myObj = new File(DEFAULT_FILE);

				String data;
				for (myReader = new Scanner(myObj); myReader.hasNextLine(); config_file = CONFIG_FOLDER + data) {
					data = myReader.nextLine();
				}
			} catch (FileNotFoundException var9) {
				var9.printStackTrace();
			} finally {
				myReader.close();
			}
		} catch (Exception var11) {
			var11.printStackTrace();
		}

		try {
			InputStream in = new FileInputStream(new File(config_file));
			properties = new Properties();
			properties.load(in);
		} catch (Exception var8) {
			var8.printStackTrace();
		}

	}

	private static class ConfigReaderHolder {
		private static final ConfigReader INSTANCE = new ConfigReader();
	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		ConfigReader configReader = getInstance();
		System.out.println(configReader.getStringProperty("fw.db.dbtype", (String) null));
		System.out.println(configReader.getStringProperty("fw.home", (String) null));
		System.out.println(configReader.getIntProperty("fw.db.timeout", true));
	}
}