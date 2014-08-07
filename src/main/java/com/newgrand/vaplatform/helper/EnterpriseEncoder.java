package com.newgrand.vaplatform.helper;

import java.math.BigInteger;
import java.security.MessageDigest;

public class EnterpriseEncoder {
	/**
	 * md5加密
	 * @param input
	 * @return
	 */
	public static String MD5Encode(String input) {
		MessageDigest md;
		try {
			// 生成一个MD5加密计算摘要
			md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(input.getBytes());
			// digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
			// BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
			String pwd = new BigInteger(1, md.digest()).toString(16);
			//System.err.println(pwd);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
			return input;
		}
	}
}
