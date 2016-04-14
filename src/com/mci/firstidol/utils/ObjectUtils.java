package com.mci.firstidol.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ObjectUtils {
	public static Map convertBean(Object bean) {
		Class<?> clazz = bean.getClass();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// BeanInfo beanInfo = Introspector
		List<Class> clazzs = new ArrayList<Class>();
		do {
			clazzs.add(clazz);
			clazz = clazz.getSuperclass();
		} while (!clazz.equals(Object.class));

		for (Class iClazz : clazzs) {
			Field[] fileds = iClazz.getDeclaredFields();
			for (Field field : fileds) {
				Object objVal = null;
				field.setAccessible(true);
				try {
					objVal = field.get(bean);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				returnMap.put(field.getName(), objVal);
			}
		}
		return returnMap;
	}

	/**
	 * 在list匹配某个字符串
	 */
	// public static boolean matchCode(String
	// codeStr,ArrayList<TaskGoodItemSubItem> taskDetails){
	// boolean result = false;
	// if(taskDetails!=null){
	// TaskGoodItemSubItem taskDetail;
	// for (int i = 0; i < taskDetails.size(); i++) {
	// taskDetail = taskDetails.get(i);
	// String code = taskDetail.BarCode;
	// if (codeStr.equals(code)) {//匹配
	// taskDetail.setType("Y");
	// taskDetails.set(i, taskDetail);
	// result = true;
	// break;
	// }
	// }
	// }
	// return result;
	// }

}
