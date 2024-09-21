package com.spine.communicator.dbops;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.spine.common.exceptions.SpineCommonExceptions;

public class TableDataExtract {
	private ResultSet resultSet = null;
	/**
	 * Default maximum number of rows to query and print.
	 */
	@SuppressWarnings("unused")
	private static final int DEFAULT_MAX_ROWS = 10;

	/**
	 * Default maximum width for text columns (like a <code>VARCHAR</code>) column.
	 */
	@SuppressWarnings("unused")
	private static final int DEFAULT_MAX_TEXT_COL_WIDTH = 150;

	/**
	 * Column type category for <code>CHAR</code>, <code>VARCHAR</code> and similar
	 * text columns.
	 */
	public static final int CATEGORY_STRING = 1;

	/**
	 * Column type category for <code>TINYINT</code>, <code>SMALLINT</code>,
	 * <code>INT</code> and <code>BIGINT</code> columns.
	 */
	public static final int CATEGORY_INTEGER = 2;

	/**
	 * Column type category for <code>REAL</code>, <code>DOUBLE</code>, and
	 * <code>DECIMAL</code> columns.
	 */
	public static final int CATEGORY_DOUBLE = 3;

	/**
	 * Column type category for date and time related columns like
	 * <code>DATE</code>, <code>TIME</code>, <code>TIMESTAMP</code> etc.
	 */
	public static final int CATEGORY_DATETIME = 4;

	/**
	 * Column type category for <code>BOOLEAN</code> columns.
	 */
	public static final int CATEGORY_BOOLEAN = 5;

	/**
	 * Column type category for types for which the type name will be printed
	 * instead of the content, like <code>BLOB</code>, <code>BINARY</code>,
	 * <code>ARRAY</code> etc.
	 */
	public static final int CATEGORY_OTHER = 0;

	public ResultSet getResultSet() {
		return this.resultSet;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}

	private static int whichCategory(int type) {
		switch (type) {
		case Types.BIGINT:
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
			return CATEGORY_INTEGER;

		case Types.REAL:
		case Types.DOUBLE:
		case Types.DECIMAL:
			return CATEGORY_DOUBLE;

		case Types.DATE:
		case Types.TIME:
			// case Types.TIME_WITH_TIMEZONE:
		case Types.TIMESTAMP:
			// case Types.TIMESTAMP_WITH_TIMEZONE:
			// return CATEGORY_DATETIME;

		case Types.BOOLEAN:
			return CATEGORY_BOOLEAN;

		case Types.VARCHAR:
		case Types.NVARCHAR:
		case Types.LONGVARCHAR:
		case Types.LONGNVARCHAR:
		case Types.CHAR:
		case Types.NCHAR:
			return CATEGORY_STRING;
		default:
			throw new SpineCommonExceptions("Unable to determint the Data type of :" + type);
		}
	}

	public List<JSONObject> extractDataFromResultSet() {

		if (this.getResultSet() == null) {
			throw new SpineCommonExceptions("TableDataExtract Error: Result set is null!");
		} else {
			try {
				List<JSONObject> resultList = new ArrayList<JSONObject>();
				ResultSetMetaData rsmd = this.resultSet.getMetaData();
				int columnCount = rsmd.getColumnCount();
				List<Column> columns = new ArrayList<Column>(columnCount);
				List<String> tableNames = new ArrayList<String>(columnCount);

				for (int i = 1; i <= columnCount; ++i) {
					Column c = new Column(rsmd.getColumnLabel(i), rsmd.getColumnType(i), rsmd.getColumnTypeName(i));
					c.setTypeCategory(whichCategory(c.getType()));
					columns.add(c);
					if (!tableNames.contains(rsmd.getTableName(i))) {
						tableNames.add(rsmd.getTableName(i));
					}
				}
				while (this.getResultSet().next()) {
					JSONObject innerResultJson = new JSONObject();
					int rowCount = 1;

					for (int i = 0; i < columnCount; ++i) {
						Column c = (Column) columns.get(i);
						int category = c.getTypeCategory();
						String value;
						if (category == 0) {
							value = "(" + c.getTypeName() + ")";
						}

						switch (category) {
						case 1:
							value = this.resultSet.getString(i + 1) == null ? "NULL" : this.resultSet.getString(i + 1);
							innerResultJson.put(rsmd.getColumnName(rowCount), value);
							break;
						case 2:
							int iValue = this.resultSet.getInt(i + 1);
							innerResultJson.put(rsmd.getColumnName(rowCount), iValue);
							value = Integer.toString(iValue);
							break;
						case 3:
							Double dValue = this.resultSet.getDouble(i + 1);
							innerResultJson.put(rsmd.getColumnName(rowCount), dValue);
							value = String.format("%.3f", dValue);
							break;
						case 4:
							value = this.resultSet.getTimestamp(i + 1) == null ? "NULL"
									: this.resultSet.getTimestamp(i + 1).toString();
							innerResultJson.put(rsmd.getColumnName(rowCount), value);
							break;
						case 5:
							Boolean iBoolean = this.resultSet.getBoolean(i + 1);
							innerResultJson.put(rsmd.getColumnName(rowCount), iBoolean);
							value = String.format("", iBoolean);
						}

						++rowCount;
					}

					resultList.add(innerResultJson);
				}

				return resultList;
			} catch (Exception var15) {
				throw new SpineCommonExceptions(var15.getMessage());
			}
		}
	}
}