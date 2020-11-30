package com.microservices.common.utils;

import java.security.MessageDigest;

/**
 * 文件名称： com.wdcloud.order.common.utils.wxpay.util.MD5Util.java</br>
 * 初始作者： YPHU</br>
 * 创建日期： 2018年1月16日</br>
 * 功能说明：MD5加密 <br/>
 * =================================================<br/>
 * 修改记录：<br/>
 * 修改作者 日期 修改内容<br/>
 * ================================================<br/>
 * Copyright (c) 2010-2011 .All rights reserved.<br/>
 */
public class MD5Util {

	private static String byteArrayToHexString(byte b[]) {

		StringBuffer resultSb = new StringBuffer();
		for (byte element : b) {
			resultSb.append(byteToHexString(element));
		}

		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {

		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin, String charsetname) {

		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			if (charsetname == null || "".equals(charsetname)) {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
			} else {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
			}
		} catch (Exception exception) {
		}
		return resultString;
	}

	private static final String	hexDigits[]	= { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
											"e", "f" };
}
