package com.study.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * 
 * @author wcl19
 *
 */
public final class XmlUtils {
	
	/**
	 * 构选方法
	 */
	private  XmlUtils(){
		
	}

	/**
	 * 转换特殊
	 * 
	 * @param str
	 * @return
	 */
	public static String encoderXML(String str) {
		if (str == null){
			return "";
		}
		//去除非utf8的字符
		String temp=str.replaceAll("[^\u0020-\u9FA5]", "");
		int len = temp.length();
		StringBuilder buffer = new StringBuilder();
		char c ;
		for (int i = 0; i < len; i++) {
			c = temp.charAt(i);
			switch (c) {
			case '<':
				buffer.append("&lt;");
				break;
			case '>':
				buffer.append("&gt;");
				break;
			case '&':
				buffer.append("&amp;");
				break;
			default:
				buffer.append(c);
				break;
			}
		}
		return buffer.toString();
	}
	
	public	static  String filterOffUtf8Mb4(String text) throws UnsupportedEncodingException {
		byte[] bytes = text.getBytes("UTF-8");
		ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
		int i = 0;
		while (i < bytes.length) {
			short b = bytes[i];
			if (b > 0) {
				buffer.put(bytes[i++]);
				continue;
			}
			b += 256;
			if ((b ^ 0xC0) >> 4 == 0) {
				buffer.put(bytes, i, 2);
				i += 2;
			}
			else if ((b ^ 0xE0) >> 4 == 0) {
				buffer.put(bytes, i, 3);
				i += 3;
			}
			else if ((b ^ 0xF0) >> 4 == 0) {
				i += 4;
			}
		}
		buffer.flip();
		return new String(buffer.array(), "utf-8");
	}

}
