package com.custom.cetl;

import java.util.List;

import com.spine.common.logging.SpineLogger;
//import com.spine.common.logging.SpineLogger;
import com.spine.core.dev.CustomDevelopment;
import com.spine.core.dev.SpineCustomCode;

public class CustomExtractor {
	public void processor() {
		CustomDevelopment funs = new SpineCustomCode();
		SpineLogger logger = funs.setLogFile(this.getClass().getSimpleName());
		logger.info("cwdce");

		// get all the workers
		System.out.println((funs.selectRecordIds("CETL_WORKER", "recid='WRK1724533317'")));
		List<String> ids = funs.selectRecordIds("CETL_WORKER");
		logger.info(ids.get(0));
	}

	public static void main(String[] args) {
		System.out.println("hello");
		CustomExtractor c = new CustomExtractor();
		c.processor();
	}
}
