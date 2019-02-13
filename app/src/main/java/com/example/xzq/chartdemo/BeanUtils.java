package com.example.xzq.chartdemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteClosable;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * FileName    : BeanUtils.java
 * Description : 通用的对象操作帮助类
 * @author     : xieziqi
 * @version    : 1.0
 * Create Date : 2015-8-19 上午10:28:36
 **/
public class BeanUtils {

    /** 判断对象为null  */
    public static boolean isNull(Object obj) {
        return obj == null;
    }

    /** 判断对象不为null  */
    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

	/** 判断对象不为null或为空  */
	public static boolean isNotAllEmpty(Object obj) {
		return !isNEmpty(obj);
	}

	/** 判断对象为null或为空  */
	public static boolean isNEmpty(Object obj) {
		return obj == null || isEmpty(obj);
	}

	/** 判断字符串,集合,数组不为空  */
	public static boolean isNotEmpty(Object obj) {
		return !isEmpty(obj);
	}

	/** 判断字符串为空，集合为空，数组为空(后续可以拓展hashSet,hashMap ...) */
	public static boolean isEmpty(Object obj) {
		boolean isEmpty = true;
		if (obj != null) {
			if (obj instanceof String) {
				// 字符串
				String tmp = obj.toString();
				isEmpty = tmp.trim().equals("");
			} else if (obj instanceof Collection<?>) {
				// 集合
				Collection<?> collections = (Collection<?>) obj;
				isEmpty = collections.size() == 0;
			} else if (obj instanceof Map<?, ?>) {
				// Map
				Map<?, ?> map = (Map<?, ?>) obj;
				isEmpty = map.size() == 0;
			} else if (obj instanceof Object[]) {
				// 数组
				Object[] objarray = (Object[]) obj;
				isEmpty = objarray.length == 0;
			} else {
				// 其它类型对象统一为非空
				isEmpty = false;
			}
		}
		return isEmpty;
	}



}
