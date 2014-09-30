package com.dreamforce.demo.util;

import org.apache.commons.lang3.StringUtils;

public class Validator {

	public static boolean isBlank(String... input) {
		for(String str : input) {
			if(StringUtils.isBlank(str))
				return true;
			else
				continue;
		}

		return false;
	}
}
