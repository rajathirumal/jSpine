package com.custom.cetl;

import com.af.foundation.AFCommon.logging.AFLogger;

public class CustomExtractor {
	public void processor() {
		AFLogger logger = AFLogger.getLogger("customextractor");
		logger.info("testtttt");
	}

	public static void main(String[] args) {
		System.out.println("hello");
	}
}
