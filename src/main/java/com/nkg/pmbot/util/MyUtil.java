package com.nkg.pmbot.util;

import java.util.Random;

public class MyUtil {

	public static String getRandomHexString() {
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while (sb.length() < AppConstants.HEX_STRING_SIZE) {
			sb.append(Integer.toHexString(r.nextInt()));
		}

		return sb.toString().substring(0, AppConstants.HEX_STRING_SIZE);
	}
}
