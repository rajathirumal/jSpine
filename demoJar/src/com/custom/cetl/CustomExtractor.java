package com.custom.cetl;

import com.spine.common.logging.SpineLogger;

public class CustomExtractor {
	public void processor() {
		SpineLogger logger = SpineLogger.getLogger("customextractor");
		logger.info("testtttt");
	}

	public static void main(String[] args) {
		System.out.println("hello");
	}
}
