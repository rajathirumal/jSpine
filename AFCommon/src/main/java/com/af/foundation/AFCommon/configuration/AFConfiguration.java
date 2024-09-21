/* Decompiler 10ms, total 744ms, lines 42 */
package com.af.foundation.AFCommon.configuration;

import com.af.foundation.AFCommon.exceptions.AFCommonExceptions;
import java.util.Arrays;
import java.util.List;

public class AFConfiguration {
	public static String AF_HOME = null;
	public static String DB_URL = null;
	public static String DB_USER = null;
	public static String DB_PASS = null;
	public static String DB_TYPE = null;
	public static int DB_TIMEOUT = 10;
	static final List<String> DB_TYPES = Arrays.asList("mssql");
	public static String LOG_DIR = null;
	public static String LOG_LEVEL = null;
	public static int LOG_MAX_SIZE = 100;
	static final List<String> LOG_LEVELS = Arrays.asList("INFO", "DEBUG", "ERROR");
	public static String CUSTOM_LIB = null;
	public static String DEFAULT_PACKAGE = null;

	static {
//      ConfigReader configReader = new ConfigReader();
		ConfigReader configReader = ConfigReader.getInstance();
		AF_HOME = configReader.getStringProperty("fw.home", null);
		DB_URL = configReader.getStringProperty("fw.db.url", (String) null);
		DB_USER = configReader.getStringProperty("fw.db.user", (String) null);
		DB_PASS = configReader.getStringProperty("fw.db.password", (String) null);
		DB_TYPE = configReader.getStringProperty("fw.db.dbtype", (String) null).toLowerCase();
		DB_TIMEOUT = configReader.getIntProperty("fw.db.timeout", true);
		LOG_DIR = configReader.getStringProperty("fw.log.directory", AF_HOME + "/log");
		LOG_LEVEL = configReader.getStringProperty("fw.log.level","ALL" );
		LOG_MAX_SIZE = configReader.getIntProperty("fw.log.maxsize", true);
		CUSTOM_LIB = configReader.getStringProperty("fw.custom.libs", AF_HOME + "/custom_lib");
		DEFAULT_PACKAGE = configReader.getStringProperty("fw.default.package", (String) null);
		if (!DB_TYPES.contains(DB_TYPE)) {
			throw new AFCommonExceptions("Un supported DB type: " + DB_TYPE);
		} else if (!LOG_LEVEL.contains(LOG_LEVEL)) {
			throw new AFCommonExceptions("Un supported logger level: " + LOG_LEVEL);
		}
	}
}