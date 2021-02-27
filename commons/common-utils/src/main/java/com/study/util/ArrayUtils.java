package com.study.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author 050355
 *
 */
public final class ArrayUtils {

	private ArrayUtils() {

	}

	/**
	 * 判断一个数组是否为空
	 * 
	 * @param t
	 *            数组
	 * @return boolean
	 */

	public static <T> boolean isEmpty(T[] t) {

		return t == null || t.length == 0;
	}

	/**
	 * 判断一个数组不为空
	 * 
	 * @param t
	 *            数组
	 * @return boolean
	 */
	public static <T> boolean isNotEmpty(T[] t) {

		return !ArrayUtils.isEmpty(t);
	}

	/**
	 * 将数组中字符串元素按指定元素拼接成字符串
	 * 
	 * @param array
	 *            奖要拼接的数组
	 * @param split
	 *            分隔符，如为null默认为;
	 * @return 拼接后的字符串。
	 */
	public static <T> String join(T[] array, String split) {
		return ArrayUtils._join(array, split, true);
	}

	/**
	 * 将数组中字符串元素按指定元素拼接成字符串
	 * 
	 * @param array
	 *            奖要拼接的数组
	 * @param split
	 *            分隔符，如为null默认为;
	 * @return 拼接后的字符串。
	 */
	public static <T> String joinNotEmpty(T[] array, String split) {
		return ArrayUtils._join(array, split, false);
	}

	/**
	 * 将数组中字符串元素按指定元素拼接成字符串
	 * 
	 * @param array
	 *            奖要拼接的数组
	 * @param split
	 *            分隔符，如为null默认为;
	 * @param joinEmpty
	 *            是否拼接空的元素
	 * @return 拼接后的字符串。
	 */
	private static <T> String _join(T[] array, String split, boolean joinEmpty) {
		if (array == null || array.length == 0) {
			return null;
		}
		String tempSplit = null;
		if (StringUtils.isBlank(split)) {
			tempSplit = ";";
		} else {
			tempSplit = split;
		}
		StringBuilder buf = new StringBuilder();
		for (T str : array) {
			if(ObjectUtils.isBlank(str)){
				if(joinEmpty){
					buf.append("");
					buf.append(tempSplit);
				}else{
					continue;
				}
			}else{
				buf.append(str);
				buf.append(tempSplit);
			}
		}
		String result = buf.toString();
		return result.substring(0, result.length() - tempSplit.length());

	}


}
