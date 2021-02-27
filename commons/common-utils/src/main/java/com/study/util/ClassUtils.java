package com.study.util;

import java.util.Collection;

public final class ClassUtils {

	private ClassUtils() {

	}

	/**
	 * 是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str) {
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch == '+' || ch == '-') {
				if (i != 0) {
					return false;
				} else {
					continue;
				}
			} else if (ch < '0' || ch > '9') {
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isArray(Class<?> clazz) {
		return clazz != null && (clazz.isArray() || Collection.class.isAssignableFrom(clazz));
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isArray(Object obj) {
		if ((obj != null && obj.getClass().isArray()) || (obj instanceof Collection)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isBoolean(Class<?> clazz) {
		return clazz != null && (Boolean.TYPE.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz));
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isBoolean(Object obj) {
		if ((obj instanceof Boolean) || (obj != null && obj.getClass() == Boolean.TYPE)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isDouble(Class<?> clazz) {
		return clazz != null && (Double.TYPE.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz));
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isNumber(Class<?> clazz) {
		return clazz != null && (Byte.TYPE.isAssignableFrom(clazz) || Short.TYPE.isAssignableFrom(clazz)
				|| Integer.TYPE.isAssignableFrom(clazz) || Long.TYPE.isAssignableFrom(clazz)
				|| Float.TYPE.isAssignableFrom(clazz) || Double.TYPE.isAssignableFrom(clazz)
				|| Number.class.isAssignableFrom(clazz));
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isNumber(Object obj) {
		if ((obj != null && obj.getClass() == Byte.TYPE) || (obj != null && obj.getClass() == Short.TYPE)
				|| (obj != null && obj.getClass() == Integer.TYPE) || (obj != null && obj.getClass() == Long.TYPE)
				|| (obj != null && obj.getClass() == Float.TYPE) || (obj != null && obj.getClass() == Double.TYPE)) {
			return true;
		}

		return obj instanceof Number;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isObject(Object obj) {
		return !isNumber(obj) && !isString(obj) && !isBoolean(obj) && !isArray(obj);
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isString(Class<?> clazz) {
		return clazz != null && (String.class.isAssignableFrom(clazz)
				|| (Character.TYPE.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz)));
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean isString(Object obj) {
		if ((obj instanceof String) || (obj instanceof Character) || (obj != null
				&& (obj.getClass() == Character.TYPE || String.class.isAssignableFrom(obj.getClass())))) {
			return true;
		}
		return false;
	}

}
