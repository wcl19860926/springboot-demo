package com.study.util;

import java.util.UUID;

/**
 * CheckDataUtils <br>
 * 
 * @author xiquee.com<br>
 * @date 2018-11-09 10:16:00
 */
public class PasswordUtils {

	private static final String LOWERCASE = ".*[a-z]{1,}.*";
	private static final String UPPERCASE = ".*[A-Z]{1,}.*";
	private static final String NUMBER = ".*\\d{1,}.*";
	private static final String SPECIAL = ".*[~!@#$%^+,()&*/_\\\\.?]{1,}.*";
	private static final String COMPLEX = ".*((?=[\\x21-\\x7e]+)[^A-Za-z0-9]).*";
	private static final int MIN_LEN = 6;
	private static final int MAX_LEN = 18;
	private static final int MIN_COMPLEX = 3;

	/**
	 * 验证密码是否满足要求长度6-18 特殊字符等
	 */
	public static boolean checkPassword(String password) {
		return checkPwd(password, true);
	}

	public static boolean checkPasswordAnySpecCharacter(String password) {
		return checkPwd(password, false);
	}

	private static boolean checkPwd(String password, boolean isSpecial) {
		if (StringUtils.isEmpty(password)) {
			return false;
		}
		if (password.length() < MIN_LEN || password.length() > MAX_LEN) {
			return false;
		}
		int checkTypes = 0;
		// 小写字母检测
		if (password.matches(LOWERCASE)) {
			checkTypes++;
		}
		// 大写字母检测
		if (password.matches(UPPERCASE)) {
			checkTypes++;
		}
		// 数字检测
		if (password.matches(NUMBER)) {
			checkTypes++;
		}

		if (isSpecial) {
			// 特殊字符检测
			if (password.matches(SPECIAL)) {
				checkTypes++;
			}
		} else {
			if (password.matches(COMPLEX)) {
				checkTypes++;
			}
		}

		// 符合项检测
		return checkTypes >= MIN_COMPLEX;
	}

	public static String generateSalt() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		return uuid.substring(5, 11);
	}

	public static String randomPassword() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "");
		return uuid.substring(5, 13);
	}

	/**
	 * @param plainText
	 * @param salt
	 * @return
	 */
	public static String encryptPassword(String plainText, String salt) {
		// 算法参考org.apache.shiro.crypto.hash.Md5Hash
		return MD5HashUtil.getMD5Hash(plainText, salt, 1);
	}

}
