package com.study.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author 050355
 *
 */
public final class CollectionUtils {

	private CollectionUtils() {

	}

	/**
	 * 将数组中字符串元素按指定元素拼接成字符串
	 * 
	 * @param array 奖要拼接的数组
	 * @param split 分隔符，如为null默认为;
	 * @return 拼接后的字符串。
	 */
	public static String join(Collection<String> collection, String split) {
		return CollectionUtils._join(collection, split, true);
	}

	/**
	 * 将数组中字符串元素按指定元素拼接成字符串
	 * 
	 * @param array 奖要拼接的数组
	 * @param split 分隔符，如为null默认为;
	 * @return 拼接后的字符串。
	 */
	public static String joinNotEmpty(Collection<String> collection, String split) {
		return CollectionUtils._join(collection, split, false);
	}

	/**
	 * 将数组中字符串元素按指定元素拼接成字符串
	 * 
	 * @param array     奖要拼接的数组
	 * @param split     分隔符，如为null默认为;
	 * @param joinEmpty 是否拼接空的元素
	 * @return 拼接后的字符串。
	 */
	private static String _join(Collection<String> collection, String split, boolean joinEmpty) {
		if (collection == null || collection.isEmpty()) {
			return null;
		}
		String tempSplit = null;
		if (StringUtils.isBlank(split)) {
			tempSplit = ";";
		} else {
			tempSplit = split;
		}
		StringBuilder buf = new StringBuilder();
		for (String str : collection) {
			if (joinEmpty) {
				buf.append(str);
				buf.append(tempSplit);
			} else {
				if (StringUtils.isNotBlank(str)) {
					buf.append(str);
					buf.append(tempSplit);
				}
			}
		}
		String result = buf.toString();
		return result.substring(0, result.length() - tempSplit.length());

	}

	/**
	 * 将字符串按特定字符分隔并，放入集合。
	 * 
	 * @param array 奖要拼接的数组
	 * @param split 分隔符，如为null默认为;
	 * @return 拼接后的字符串。
	 */
	public static Set<String> splitToSet(String str, String split) {
		Set<String> collection = new HashSet<String>();
		if (str != null) {
			String[] tempArray = str.split(split);
			for (String s : tempArray) {
				collection.add(s);
			}
		}
		return collection;
	}

	/**
	 * 将字符串按特定字符分隔并，放入集合。
	 * 
	 * @param array 奖要拼接的数组
	 * @param split 分隔符，如为null默认为;
	 * @return 拼接后的字符串。
	 */
	public static List<String> splitToList(String str, String split) {
		List<String> collection = new ArrayList<String>();
		if (str != null) {
			String[] tempArray = str.split(split);
			for (String s : tempArray) {
				collection.add(s);
			}
		}
		return collection;
	}

	/**
	 * Null-safe check if the specified collection is empty.
	 * <p>
	 * Null returns true.
	 * 
	 * @param coll the collection to check, may be null
	 * @return true if empty or null
	 * @since Commons Collections 3.2
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return (coll == null || coll.isEmpty());
	}

	/**
	 * Null-safe check if the specified collection is not empty.
	 * <p>
	 * Null returns false.
	 * 
	 * @param coll the collection to check, may be null
	 * @return true if non-null and non-empty
	 * @since Commons Collections 3.2
	 */
	public static boolean isNotEmpty(Collection<?> coll) {
		return !CollectionUtils.isEmpty(coll);
	}

	/**
	 * @param excelHeader
	 * @param exportFields
	 * @return
	 */
	public static Map<String, String> selectEntryInMap(Map<String, String> excelHeader, String selectedFields) {
		Map<String, String> excelHeaderResult = new LinkedHashMap<>();
		if (StringUtils.isBlank(selectedFields)) {
			return excelHeader;
		} else {
			List<String> fields = Arrays.asList(selectedFields.split(","));
			for (String key : excelHeader.keySet()) {
				if (fields.contains(key)) {
					excelHeaderResult.put(key, excelHeader.get(key));
				}
			}
			return excelHeaderResult;
		}
	}

	/**
	 * 把一个列表切片为多个列表
	 * 
	 * @param list      --列表
	 * @param sliceSize --切片大小
	 * @param <E>       --泛型类型
	 * @return 切片列表 Notnull
	 */
	public static <E> List<List<E>> slice(List<E> list, int sliceSize) {
		List<List<E>> slicedLists = new ArrayList<>();
		if (list != null && list.size() > 0 && sliceSize > 0) {
			for (int i = 0; i < list.size(); i++) {
				int sliceIndex = i / sliceSize;
				if (slicedLists.size() == sliceIndex) {
					slicedLists.add(new ArrayList<>());
				}
				List<E> innerList = slicedLists.get(sliceIndex);
				innerList.add(list.get(i));
			}
		}
		return slicedLists;
	}

	/**
	 * 把单个对象转换为对象列表
	 * 
	 * @param e   对象
	 * @param <E> 泛型类型
	 * @return 对象列表
	 */
	public static <E> List<E> asList(E e) {
		List<E> list = new ArrayList<>();
		list.add(e);
		return list;
	}

	/**
	 * 列表转换
	 * 
	 * @param list
	 * @param apply
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	public static <T, R> List<R> asList(List<T> list, Function<T, R> apply) {
		List<R> result = new ArrayList<>();
		if (list != null) {
			list.forEach(item -> result.add(apply.apply(item)));
		}
		return result;
	}

	/**
	 * 抽取list属性构建map
	 * 
	 * @param list
	 * @param applyKey
	 * @param applyValue
	 * @param <T>
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <T, K, V> Map<K, V> asMap(List<T> list, Function<T, K> applyKey, Function<T, V> applyValue) {
		Map<K, V> map = new HashMap<>(8);
		if (list != null) {
			list.forEach(item -> map.put(applyKey.apply(item), applyValue.apply(item)));
		}
		return map;
	}

	/**
	 * 抽取list属性构建map
	 * 
	 * @param list
	 * @param applyKey
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	public static <T, K> Map<K, T> asMap(List<T> list, Function<T, K> applyKey) {
		Map<K, T> map = new HashMap<>(8);
		if (list != null) {
			list.forEach(item -> map.put(applyKey.apply(item), item));
		}
		return map;
	}

	/**
	 * 把对象数组转换为对象列表
	 * 
	 * @param es  对象
	 * @param <E> 泛型类型
	 * @return 对象列表
	 */
	public static <E> List<E> arrayToList(@SuppressWarnings("unchecked") E... e) {
		List<E> list = new ArrayList<>();
		if (e != null && e.length > 0) {
			for (E i : e) {
				list.add(i);
			}
		}
		return list;
	}

	/**
	 * 获取list1 减去 list2的差集
	 * 
	 * @param list1 --集合1
	 * @param list2 --结合2
	 * @param <E>   泛型类型
	 * @return list1 减去 list2的差集
	 */
	public static <E> List<E> getSubtraction(List<E> list1, List<E> list2) {
		List<E> result = new ArrayList<>();
		if (list1 != null && list1.size() > 0) {
			if (list2 != null && list2.size() > 0) {
				for (E item : list1) {
					if (!list2.contains(item)) {
						result.add(item);
					}
				}
			} else {
				result.addAll(list1);
			}
		}
		return result;
	}

	/**
	 * 获取list1 与 list2的交集
	 * 
	 * @param list1 --集合1
	 * @param list2 --结合2
	 * @param <E>   泛型类型
	 * @return list1 与 list2的交集
	 */
	public static <E> List<E> getIntersection(List<E> list1, List<E> list2) {
		List<E> result = new ArrayList<>();
		if (list1 != null && list1.size() > 0 && list2 != null && list2.size() > 0) {
			for (E item : list1) {
				if (list2.contains(item)) {
					result.add(item);
				}
			}
		}
		return result;
	}

	/**
	 * 获取list1 与 list2的并集
	 * 
	 * @param list1 --集合1
	 * @param list2 --结合2
	 * @param <E>   泛型类型
	 * @return list1 与 list2的并集
	 */
	public static <E> List<E> getUnionSet(List<E> list1, List<E> list2) {
		List<E> result = new ArrayList<>();
		if (list1 != null && list1.size() > 0) {
			result.addAll(list1);
		}
		if (list2 != null && list2.size() > 0) {
			for (E item : list2) {
				if (!result.contains(item)) {
					result.add(item);
				}
			}
		}
		return result;
	}

	/**
	 * 合并list到已有的list，过滤掉已有的元素
	 * 
	 * @param source
	 * @param willAccept
	 * @param comparator
	 * @param <E>
	 * @return
	 */
	public static <E> List<E> getUnionSet(List<E> source, List<E> willAccept, Comparator<E> comparator) {
		final List<E> list = (source == null ? new ArrayList<>() : source);
		if (willAccept != null && willAccept.size() > 0) {
			willAccept.forEach(item -> {
				if (item != null) {
					boolean contain = false;
					if (comparator != null) {
						for (E i1 : list) {
							if (comparator.compare(i1, item) == 0) {
								contain = true;
								break;
							}
						}
					} else {
						contain = list.contains(item);
					}
					if (!contain) {
						list.add(item);
					}
				}
			});
		}
		return list;
	}

	/**
	 * 移除重复的元素
	 * 
	 * @param list --集合
	 * @param <E>  泛型类型
	 */
	public static <E> List<E> removeDuplicate(final List<E> list) {
		List<E> newList = new ArrayList<>();
		if (list != null && list.size() > 0) {
			list.forEach(item -> {
				if (!newList.contains(item)) {
					newList.add(item);
				}
			});
		}
		return newList;
	}

	/**
	 * 获取子列表
	 * 
	 * @param list     --列表
	 * @param start    --开始索引
	 * @param endIndex --结束索引，不包含结束元素
	 * @param <E>      --泛型
	 * @return
	 */
	public static <E> List<E> subList(List<E> list, int start, int endIndex) {
		List<E> newList = new ArrayList<>();
		if (list != null && list.size() > 0 && endIndex > 0) {
			for (int i = start; i >= 0 && i < endIndex && i < list.size(); i++) {
				newList.add(list.get(i));
			}
		}
		return newList;
	}

	/**
	 * 获取子数组
	 * 
	 * @param array    --字符数组
	 * @param start    --开始索引
	 * @param endIndex --结束索引，不包含结束元素
	 * @return
	 */
	public static String[] subArray(String[] array, int start, int endIndex) {
		List<String> subList = subList(arrayToList(array), start, endIndex);
		return subList.toArray(new String[0]);
	}

	/**
	 * 用分隔符连接列表
	 * 
	 * @param list      --列表
	 * @param separator --分隔符
	 * @param <E>
	 * @return
	 */
	public static <E> String join(List<E> list, String separator) {
		String result = "";
		if (list != null && list.size() > 0) {
			StringBuilder text = new StringBuilder();
			list.forEach(item -> {
				if (item != null) {
					text.append(item.toString()).append(separator);
				}
			});
			if (text.length() > 0) {
				result = text.substring(0, text.length() - 1);
			}
		}
		return result;
	}

	/**
	 * 用连接列表
	 * 
	 * @param list --列表
	 * @param <E>
	 * @return
	 */
	public static <E> String join(List<E> list) {
		String result = "";
		if (list != null && list.size() > 0) {
			StringBuilder text = new StringBuilder();
			list.forEach(item -> {
				if (item != null) {
					text.append(item.toString());
				}
			});
			result = text.toString();
		}
		return result;
	}

	/**
	 * 用分隔符连接数组
	 * 
	 * @param list      --数组
	 * @param separator --分隔符
	 * @param <E>
	 * @return
	 */
	public static <E> String join(E[] list, String separator) {
		return join(arrayToList(list), separator);
	}

	/**
	 * map的Value转换为List列表
	 * 
	 * @param map --map集合
	 * @param <K> --泛型类型
	 * @param <V> --泛型类型
	 * @return
	 */
	public static <K, V> List<V> convertMapValueToList(Map<K, V> map) {
		List<V> list = new ArrayList<>();
		if (map != null && map.size() > 0) {
			map.forEach((k, v) -> list.add(v));
		}
		return list;
	}

	/**
	 * map的Key转换为List列表
	 * 
	 * @param map --map集合
	 * @param <K> --泛型类型
	 * @param <V> --泛型类型
	 * @return
	 */
	public static <K, V> List<K> convertMapKeyToList(Map<K, V> map) {
		List<K> list = new ArrayList<>();
		if (map != null && map.size() > 0) {
			map.forEach((k, v) -> list.add(k));
		}
		return list;
	}

	/**
	 * 对象List 按照sortList的顺序进行排序
	 * 
	 * @param list
	 * @param applyKey
	 * @param sortList
	 * @param <T>
	 * @param <K>
	 * @return
	 */
	public static <T, K> List<T> sorted(List<T> list, Function<T, K> applyKey, List<K> sortList) {
		Map<K, T> map = asMap(list, applyKey);
		List<T> returnList = new ArrayList<>();
		sortList.forEach(item -> {
			returnList.add(map.get(item));
		});
		return returnList;
	}

}
