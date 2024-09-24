package com.spine.core.dev;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.spine.common.logging.SpineLogger;
import com.spine.communicator.RequestHandeller;
import com.spine.communicator.dbops.QueryOperation;
import com.spine.communicator.utils.SpineHttpStatusCodes;

public class SpineCustomCode implements CustomDevelopment {
	final RequestHandeller requestHandeller = new RequestHandeller();

	@Override
	public SpineLogger setLogFile(String nameOfTheLogFile) {
		nameOfTheLogFile = nameOfTheLogFile.replace(".", "_");
		return SpineLogger.getLogger(nameOfTheLogFile);
	}

	@Override
	public List<String> selectRecordIds(String tableName, String condition) {
		String q = String.format("S/RECID/%s/%s", tableName, condition);
		return selectRec(q);
	}

	@Override
	public List<String> selectRecordIds(String tableName) {
		String q = String.format("S/RECID/%s", tableName);
		return selectRec(q);
	}

	private List<String> selectRec(String q) {
		JSONObject resp = requestHandeller.process(QueryOperation.EXECUTE_QUERY, q);
		if (resp.getJSONObject("response").getInt("status") == SpineHttpStatusCodes.OK.getValue()) {
			List<String> ids = new ArrayList<>();
			// Get the "data" array from the JSON object
			JSONArray dataArray = resp.getJSONObject("response").getJSONArray("data");
			// Iterate through the array and extract the id values
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject userObject = dataArray.getJSONObject(i);
				String id = userObject.getString("RECID");
				ids.add(id);
			}
			return ids;
		} else {
			return Collections.emptyList();
		}
	}

}
