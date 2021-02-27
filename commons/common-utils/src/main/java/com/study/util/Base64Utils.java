package com.study.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Base64Utils {

	private static final Pattern utfPtrn = Pattern.compile("_x([0-9A-F]{4})_");

	private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Base64Utils.class);

	/**
	 * @param input
	 * @return
	 * @author 050355 将输入流转成BASE64编码的字符串
	 */
	public static String toBase64String(InputStream input) {
		if (input != null) {
			try {
				return Base64Utils.encode(Base64Utils.toByteArray(input));
			} catch (Exception e) {
				LOGGER.error("", e);
			}
		}
		return "";
	}

	/**
	 * @param input
	 * @return
	 * @author 050355 将输入流转为byte[]
	 */
	public static byte[] toByteArray(InputStream input) {
		if (input != null) {
			try {
				return IOUtils.toByteArray(input);
			} catch (IOException e) {
				LOGGER.error("inputStream to  byteArray", e);
			}
		}
		return null;

	}

	/**
	 * <p>
	 * BASE64字符串解码为二进制数据
	 * </p>
	 *
	 * @param base64
	 * @return
	 * @throws Exception
	 */
	public static byte[] toByteArrayFromString(String base64) throws Exception {
		return Base64.decodeBase64(base64.getBytes());
	}

	/**
	 * <p>
	 * 二进制数据编码为BASE64字符串
	 * </p>
	 *
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static String encode(byte[] bytes) throws Exception {
		return new String(Base64.encodeBase64(bytes));
	}

	/**
	 *
	 */
	static String utfDecode(String value) {
		if (value == null) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		Matcher m = utfPtrn.matcher(value);
		int idx = 0;
		while (m.find()) {
			int pos = m.start();
			if (pos > idx) {
				buf.append(value.substring(idx, pos));
			}

			String code = m.group(1);
			int icode = Integer.decode("0x" + code);
			buf.append((char) icode);

			idx = m.end();
		}
		buf.append(value.substring(idx));
		return buf.toString();
	}

}
