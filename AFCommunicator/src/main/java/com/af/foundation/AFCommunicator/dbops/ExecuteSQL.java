package com.af.foundation.AFCommunicator.dbops;

import com.af.foundation.AFCommon.converters.Translate;
import com.af.foundation.AFCommon.exceptions.AFCommonExceptions;
import com.af.foundation.AFCommon.logging.AFLogger;
import com.af.foundation.AFCommunicator.connection.ConnectionFactory;
import com.af.foundation.AFCommunicator.exceptions.AFCommunicatorExceptions;
import com.af.foundation.AFCommunicator.utils.AFHttpStatusCodes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

public class ExecuteSQL {
	AFLogger logger = AFLogger.getLogger("database");
	private static final List<String> QUERY_TYPES = Arrays.asList("S", "I");
	private Connection connnection = null;
	private final Translate translate = new Translate();
	private Statement statement = null;
	private TableDataExtract tableDataExtract = new TableDataExtract();
	private String browserQuery = null;

	/**
	 * Writes the given `message` to `responseJsonObject`
	 * 
	 * @param message            - message to append
	 * @param responseJsonObject - object to add the content
	 * @param statusCode         -
	 *                           {@link com.af.foundation.AFCommunicator.utils.AFHttpStatusCodes}
	 */
	private void errorMessageBulder(String message, JSONObject responseJsonObject, AFHttpStatusCodes statusCode) {
		JSONObject error_details = new JSONObject();
		error_details.put("status", statusCode.getValue());
		error_details.put("data", JSONObject.NULL);
		String statusCodeDescription = statusCode.getDescription();
		error_details.put("message", statusCodeDescription + " :" + message.trim());
		responseJsonObject.put("response", error_details);
	}

	public JSONObject executeQueryStringRequest(String browserRequest) throws AFCommunicatorExceptions {
		this.logger.debug(browserRequest);
		JSONObject responseJsonObject = new JSONObject();
		this.browserQuery = this.translate.queryStringToSQL(browserRequest);

		try {
			this.connnection = ConnectionFactory.getConnection();
		} catch (AFCommunicatorExceptions connectionException) {
			this.logger.error("Connection erroe :" + connectionException.getMessage());
			this.errorMessageBulder(connectionException.getMessage(), responseJsonObject,
					AFHttpStatusCodes.INTERNAL_SERVER_ERROR);
		}

		if (this.connnection != null) {
			responseJsonObject.put("request", browserRequest);

			try {
				this.statement = this.connnection.createStatement();
			} catch (SQLException statementException) {
				this.errorMessageBulder(statementException.getMessage(), responseJsonObject,
						AFHttpStatusCodes.EXPECTATION_FAILED);
			}

			try {
				String parts = browserRequest.split("/")[0].toUpperCase();
				switch (parts.toUpperCase()) {
				case "I":
					this.insertExecution(responseJsonObject);
					break;
				case "S":
					this.selectExecution(responseJsonObject);
					break;
				default:
					throw new AFCommonExceptions(
							"Invalid operation " + parts + ", Must be one of " + String.valueOf(QUERY_TYPES));
				}
			} catch (Exception querryParsingException) {
				this.logger.error(querryParsingException.getMessage());
				throw new AFCommonExceptions(querryParsingException.getMessage(), querryParsingException);
			}
		}

		return responseJsonObject;
	}

	private void insertExecution(JSONObject responseJsonObject) {
		JSONObject insertExecution = new JSONObject();
		List<JSONObject> metaData = this.tableMetaInfo();

		try {
			if (this.statement.executeUpdate(this.browserQuery) == 1) {
				insertExecution.put("status", AFHttpStatusCodes.CREATED.getValue());
				insertExecution.put("data", (new JSONObject()).put("updateCount", this.statement.getUpdateCount()));
				insertExecution.put("meta", metaData);
				responseJsonObject.put("response", insertExecution);
			}
		} catch (SQLException var5) {
			if (var5.getMessage().contains("Cannot insert duplicate key")) {
				this.errorMessageBulder("Duplicate data", responseJsonObject, AFHttpStatusCodes.BAD_REQUEST);
			}
		}

	}

	private void selectExecution(JSONObject responseJsonObject) {
		JSONObject selectExecution = new JSONObject();

		try {
			ResultSet browserResultSet = this.statement.executeQuery(this.browserQuery);

			this.tableDataExtract.setResultSet(browserResultSet);
			List<JSONObject> userData = this.tableDataExtract.extractDataFromResultSet();
			List<JSONObject> metaData = this.tableMetaInfo();
			if (!userData.isEmpty() && !metaData.isEmpty()) {
				if (!userData.isEmpty() && !metaData.isEmpty()) {
					selectExecution.put("status", AFHttpStatusCodes.OK.getValue());
					selectExecution.put("data", userData);
					selectExecution.put("meta", metaData);
					responseJsonObject.put("response", selectExecution);
				}
			} else {
				this.errorMessageBulder("No data to show for the given selection", responseJsonObject,
						AFHttpStatusCodes.NO_CONTENT);
			}
		} catch (SQLException var6) {
			if (!var6.getMessage().contains("Invalid object name")) {
				throw new AFCommunicatorExceptions("SELECT failed", var6);
			}

			this.errorMessageBulder("No such table", responseJsonObject, AFHttpStatusCodes.BAD_REQUEST);
		}

	}

	public List<JSONObject> tableMetaInfo() {
		String metaQuery = this.translate.getDisplayOrderQuerry();

		ResultSet metaResultSet;
		try {
			metaResultSet = this.statement.executeQuery(metaQuery);
		} catch (SQLException var4) {
			throw new AFCommunicatorExceptions(metaQuery, var4);
		}

		this.tableDataExtract.setResultSet(metaResultSet);
		return this.tableDataExtract.extractDataFromResultSet();
	}

	public static void main(String[] args) {
		ExecuteSQL executeSQL = new ExecuteSQL();
		System.out.println(executeSQL.executeQueryStringRequest("s//CETL_WORKER/WORKER_ID='WRK1724290065'"));
		System.out.println(executeSQL.executeQueryStringRequest("s//CETL_WORKER/WORKER_ID='WRK172453wced'"));
		System.out.println(executeSQL.executeQueryStringRequest("s//CETL_WORKER/WORKER_ID='WRK1724533317'"));

	}
}