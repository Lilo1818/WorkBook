package com.mci.firstidol.utils;

import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.mci.firstidol.model.UserModel;

public class ParseJson<T> {
	
	private Class<T> mJavaBean;
	
	public boolean isCommon(JSONObject jsonObject) {
		boolean result = false;
		try {
			if (jsonObject != null) {
				if (jsonObject.has("isSuc")) {
					result = jsonObject.getBoolean("isSuc");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	/**
	 * 得到错误信息
	 * @param jsonObject
	 * @return 错误信息
	 */
	public String getErrorMessage(JSONObject jsonObject){
		String result = null;
		try {
			if(jsonObject!=null){
				if(jsonObject.has("message")){
					result = jsonObject.get("message").toString();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public List<Object> getModelList(JSONObject jsonObject, Class<?> targetClass) {
		List<Object> list = null;
		try {
			if (jsonObject.has("result")) {
//				JSONObject object = new JSONObject(jsonObject.get("result")
//						.toString().trim());
//				if (object.has("Items")) {
					list = (List<Object>) JSON.parseArray(
							jsonObject.get("result").toString(), targetClass);
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Object getModelObject(JSONObject jsonObject,Class<?> targetClass) {
		Object object = null;
		try{
			if (jsonObject.has("result")) {
				String jsonStr = jsonObject.get("result").toString().trim();
				object = JSON.parseObject(jsonStr, targetClass);
			}
		}catch(Exception e){
			Log.e("easy", e.toString());
			e.printStackTrace();
		}
		return object;
	}
	
	/**
	 * 将对象转化成json字符串
	 * @param object
	 * @return
	 */
	public String ObjectToJsonStr(Object object){
		String params = null;
		try {
			params = JSON.toJSONString(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}
	
	/**
	 * result是true或者false
	 * @param jsonObject：jsonObject
	 * @return
	 */
	public boolean isResultTrue(JSONObject jsonObject){
		boolean result = false;
		try {
			if(jsonObject!=null&&jsonObject.has("result")){
				result = jsonObject.getBoolean("result");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
