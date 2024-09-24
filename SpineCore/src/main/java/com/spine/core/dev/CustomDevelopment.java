package com.spine.core.dev;

import java.util.List;

import com.spine.common.logging.SpineLogger;

/**
 * An interface of {@link Spine} functions. Create an object for
 * {@link SpineCustomCode} and use it as,</br>
 * 
 * <pre>
 * CustomDevelopment funs = new SpineCustomCode();
 * funs.setLogFile("myLogFile");
 * </pre>
 * 
 * @author rajathirumal
 * 
 */
public interface CustomDevelopment {

	/**
	 * This sets the log file name for the current context
	 * 
	 * @param logFile - name of the log file
	 * @return - {@code SpineLogger} type object, which can be used as standard java
	 *         logger
	 */
	SpineLogger setLogFile(String logFile);

	/**
	 * Performs a database select with the given condition
	 * 
	 * @param tableName - Name of the table that you want to select
	 * @param condition - Selection, Example: {@code RECID='YOUR_RECID',COL1='TEST'}
	 * @return - List of record id's for the selection.
	 */
	List<String> selectRecordIds(String tableName, String condition);

	/**
	 * Performs a full select on the given table name
	 * 
	 * @param tableName - Name of the table that you want to select
	 * @return - List of record id's on the table
	 */
	List<String> selectRecordIds(String tableName);

}
