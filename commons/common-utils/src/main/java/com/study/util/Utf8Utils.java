package com.study.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utf8Utils {

	private static final Pattern UTFPTRN = Pattern.compile("_x([0-9A-F]{4})_");

	private Utf8Utils() {

	}

	/**
	 * 
	 */
	static String utfDecode(String value) {
		if (value == null) {
			return null;
		}

		StringBuffer buf = new StringBuffer();
		Matcher m = UTFPTRN.matcher(value);
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
	
	
	
	 /**
     * 全角转半角
     *
     * @param obj 需转换的数据
     * @return 转成半角后的数据
     * @author linjie
     * @data 2017-04-24
     */
    public static String toSBCS(Object obj) {
        String str = (String) obj;
        if (str == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, n = str.length(); i < n; i++) {
            int c = str.charAt(i);
            if ((c >= 'Ａ') && (c <= 'Ｚ')) {
                c = (c + 'A') - 'Ａ';
            } else if ((c >= '０') && (c <= '９')) {
                c = (c + '0') - '０';
            } else if ((c >= 'ａ') && (c <= 'ｚ')) {
                c = (c + 'a') - 'ａ';
            } else if (c == '（') {
                c = (c + '(') - '（';
            } else if (c == '）') {
                c = (c + ')') - '）';
            }
            sb.append((char) c);
        }
        return sb.toString();
    }


}
