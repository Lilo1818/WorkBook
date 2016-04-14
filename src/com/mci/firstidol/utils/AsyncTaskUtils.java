package com.mci.firstidol.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import com.google.gson.JsonObject;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.model.BaseModel;
import com.mci.firstidol.model.BaseRequest;

import android.R.bool;
import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.nfc.FormatException;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

@SuppressLint("NewApi")
public class AsyncTaskUtils {

//	private static final String URL_IP = "http://app.ieasy.tv:8158";

	
	// --------公共服务--------------
	public static final String URL = Constant.RequestContstants.URL_IP + "/api";
	//文件地址
	public static final String File_URL = "http://qn.ieasy.tv/";


	private static final String MSG_NETWORKFAIL = "无网络";
	private static final String MSG_CANCEL = "取消操作";
	private static final String MSG_FAIL = "请求失败";
	private static final String MSG_TIMEOUT = "请求超时";
	private static final String MSG_INTENETEXCEPTION = "请求异常";

	private static final String MSG_TOKEN = "无效toke，请重新登录";
	private static final String MSG_RIGHT = "无效权限";
	private static final String MSG_BUSINESSEXCEPTION = "业务逻辑错误";
	private static final int TIME_OUT_DELAY = 60000;

	public static <T> void doAsync(final Context pContext,
			final int pTitleResID, final int pMessageResID,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final boolean isShowPorgress) {
		AsyncTaskUtils.doAsync(pContext, pTitleResID, pMessageResID, pCallable,
				pCallback, null, false, isShowPorgress);
	}

	public static <T> void doAsync(final Context pContext,
			final CharSequence pTitle, final CharSequence pMessage,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final boolean isShowPorgress) {
		AsyncTaskUtils.doAsync(pContext, pTitle, pMessage, pCallable,
				pCallback, null, false, isShowPorgress);
	}

	public static <T> void doAsync(final Context pContext,
			final int pTitleResID, final int pMessageResID,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final boolean pCancelable, final boolean isShowPorgress) {
		AsyncTaskUtils.doAsync(pContext, pTitleResID, pMessageResID, pCallable,
				pCallback, null, pCancelable, isShowPorgress);
	}

	public static <T> void doAsync(final Context pContext,
			final CharSequence pTitle, final CharSequence pMessage,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final boolean pCancelable, final boolean isShowPorgress) {
		AsyncTaskUtils.doAsync(pContext, pTitle, pMessage, pCallable,
				pCallback, null, pCancelable, isShowPorgress);
	}

	public static <T> void doAsync(final Context pContext,
			final int pTitleResID, final int pMessageResID,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback,
			final boolean isShowPorgress) {
		AsyncTaskUtils.doAsync(pContext, pTitleResID, pMessageResID, pCallable,
				pCallback, pExceptionCallback, false, isShowPorgress);
	}

	public static <T> void doAsync(final Context pContext,
			final CharSequence pTitle, final CharSequence pMessage,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback,
			final boolean isShowPorgress) {
		AsyncTaskUtils.doAsync(pContext, pTitle, pMessage, pCallable,
				pCallback, pExceptionCallback, false, isShowPorgress);
	}

	public static <T> void doAsync(final Context pContext,
			final int pTitleResID, final int pMessageResID,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback,
			final boolean pCancelable, final boolean isShowPorgress) {
		AsyncTaskUtils.doAsync(pContext, pContext.getString(pTitleResID),
				pContext.getString(pMessageResID), pCallable, pCallback,
				pExceptionCallback, pCancelable, isShowPorgress);
	}

	public static <T> void doAsync(final Context pContext,
			final CharSequence pTitle, final CharSequence pMessage,
			final Callable<T> pCallable, final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback,
			final boolean pCancelable, final boolean isShowPorgress) {
		if (!HttpUtils.isNetworkAvailable(pContext)) {
			if (pExceptionCallback != null) {
				pExceptionCallback.onCallback(new CancelledException(
						MSG_NETWORKFAIL));
			}
			return;
		}

		new AsyncTask<Void, Void, T>() {
			private ProgressDialog mPD;
			private Exception mException = null;

			@Override
			public void onPreExecute() {
				/**
				 * 第一个参数代表你在哪个界面显示 第二个参数代表显示的标题 第三个参数代表显示的信息
				 * 第四个参数代表indeterminate（不定的）如果为true，左边会有一个圆形的标示
				 * 第五个参数代表cancelable（撤销）
				 */
				if (isShowPorgress) {
					this.mPD = ProgressDialog.show(pContext, pTitle, pMessage,
							true, pCancelable);
					if (pCancelable) {
						this.mPD.setOnCancelListener(new OnCancelListener() {
							public void onCancel(
									final DialogInterface pDialogInterface) {
								pExceptionCallback
										.onCallback(new CancelledException(
												MSG_CANCEL));// 取消操作时要处理doInBackground的方法：中断网络请求或者添加标志位中断循环
								pDialogInterface.dismiss(); // 关闭弹出框
								cancel(true);// 取消asyncTask的执行，此方法只是不执行onPostExecute，但是不能中断doInBackground、onProgressUpdate方法的执行
							}
						});
					}
				}

				super.onPreExecute();
			}

			@Override
			public T doInBackground(final Void... params) {
				T objT = null;
				try {
					objT = pCallable.call();
					// throw new Exception("aaa")
				} catch (Exception e) {
					// TODO Auto-generated catch block
					mException = e;
					e.printStackTrace();
				}
				return objT;
			}

			@Override
			public void onPostExecute(final T result) {
				try {
					if (mPD.isShowing()) {
						this.mPD.dismiss();
					}
				} catch (final Exception e) {
					Log.e("Error", e.toString());
				}
				if (this.isCancelled()) {
					this.mException = new CancelledException();
				}
				if (this.mException == null) {
					pCallback.onCallback(result);
				} else {
					if (pExceptionCallback == null) {
						if (this.mException != null)
							Log.e("Error", this.mException.toString());
					} else {
						pExceptionCallback.onCallback(this.mException);
					}
				}
				super.onPostExecute(result);
			}
		}.execute((Void[]) null);
	}

	// ***************************************************************************************************************
	// http请求
	public synchronized static <T> AsyncTask doAsync(final Context pContext,
			final CharSequence pTitle, final CharSequence pMessage,
			final CharSequence requestID, final Object requestObj,
			final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback,
			final boolean pCancelable, final boolean isShowPorgress) {

		AsyncTask asyncTask = new AsyncTask<Void, Void, T>() {
			private ProgressDialog mPD;
			private Exception mException = null;
			private HttpClient client;
			private HttpPost post;
			private HttpGet httpGet;
			private boolean isCancel;
			private int mark;

			@Override
			public void onPreExecute() {
				/**
				 * 第一个参数代表你在哪个界面显示 第二个参数代表显示的标题 第三个参数代表显示的信息
				 * 第四个参数代表indeterminate（不定的）如果为true，左边会有一个圆形的标示
				 * 第五个参数代表cancelable（撤销）
				 */
				if (isShowPorgress) {
					this.mPD = ProgressDialog.show(pContext, pTitle, pMessage,
							true, pCancelable);
					if (pCancelable) {
						this.mPD.setOnCancelListener(new OnCancelListener() {
							public void onCancel(
									final DialogInterface pDialogInterface) {
								pExceptionCallback
										.onCallback(new CancelledException(
												MSG_CANCEL));// 取消操作时要处理doInBackground的方法：中断网络请求或者添加标志位中断循环
								pDialogInterface.dismiss(); // 关闭弹出框
								cancel(true);// 取消asyncTask的执行，此方法只是不执行onPostExecute，但是不能中断doInBackground、onProgressUpdate方法的执行
								cancelRequest();// 关闭网络请求，效果待验证
							}
						});
					}
				}

				super.onPreExecute();
			}

			@SuppressWarnings({ "unchecked", "deprecation", "static-access" })
			@Override
			public T doInBackground(final Void... params) {
				String result = null;

				if (!HttpUtils.isNetworkAvailable(pContext)) {
					mark = 5;
				} else {
					try {
						client = new DefaultHttpClient();
						// client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
						// 5000);
						client.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,TIME_OUT_DELAY); // 请求超时设置
						client.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT,TIME_OUT_DELAY);// 连接超时
						String requestMothod = ((BaseRequest)requestObj).getRequestMethod();
						String jsonStr = GsonUtils.objectToJson(requestObj);
						
						
						HttpResponse response = null;
//						String requestMothod = (String) GsonUtils.getJsonValue(jsonStr, "requestMethod");
						if (TextUtils.isEmpty(requestMothod)) {
							requestMothod = "get";
						}
						if (requestMothod.equals("post")) {//post 请求
							
							 if (requestID!=null) {
							 post = new HttpPost(URL+"/"+requestID);
							 } else {
							 post = new HttpPost(URL);
							 }

//							 post.addHeader("content-type","application/json");
							// 设置HTTP POST请求参数必须用NameValuePair对象
//		                		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//		                		if (requestObj!=null) {
//		        					Map<String, String> stuMap = ObjectUtils.convertBean(requestObj);
//		        					
//		                    		Set<String> set = stuMap.keySet();
//		            		        Iterator<String> iterator = set.iterator();
//		            		        while (iterator.hasNext()) {
//		            					String key = iterator.next();
//		            					nameValuePairs.add(new BasicNameValuePair(key, stuMap.get(key).toString()));
//		            				}
//		        				}
//		                		
//		    					post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));

		    					
//							MultipartEntity reqEntity = new MultipartEntity(
//									HttpMultipartMode.BROWSER_COMPATIBLE);
//							
//							reqEntity.addPart("JsonData", new StringBody(jsonStr,Charset.forName("utf-8")));
							 
							 
							    DataManager.getInstance();
								post.addHeader("token", DataManager.userModel!=null ? DataManager.getInstance().userModel.Token:"");
							    post.addHeader("packagename", pContext.getPackageName());
							    if (((String)requestID).indexOf(Constant.RequestContstants.Request_User_Login)!=-1 || ((String)requestID).indexOf(Constant.RequestContstants.Request_User_Social_Login)!=-1) {
							    	DataManager.getInstance();
									post.addHeader("Bd_UserId",DataManager.bd_userId);
								    post.addHeader("Bd_ChannelId",DataManager.getInstance().bd_channelId);
								}
							    
							    
//							    jsonStr.replace(",\"requestMethod\": \"post\"", "");
				                StringEntity reqEntity = new StringEntity(jsonStr.toString(),"utf-8");//解决中文乱码问题    
				                reqEntity.setContentEncoding("UTF-8");    
				                reqEntity.setContentType("application/json");    
//				                post.setEntity(entity); 
							
//							if (requestObj!=null) {
//	        					HashMap<String, Object> stuMap = (HashMap<String, Object>) ObjectUtils.convertBean(requestObj);
//	        					stuMap.remove("requestMethod");
//	                    		Set<String> set = stuMap.keySet();
//	            		        Iterator<String> iterator = set.iterator();
//	            		        while (iterator.hasNext()) {
//	            					String key = iterator.next();
//	            					
//	            					
//	            					if (key.equals("fileUrl")) {
//	            						File file = new File(GsonUtils.getJsonValue(
//	    										jsonStr, "fileUrl").toString());
//	    								ContentBody contentBody = new FileBody(file,
//	    										"image/jpg");
//	    								reqEntity.addPart("PNG", contentBody);
//									} else if(key.equals("fileUrls")){
//										ArrayList<String> files = (ArrayList<String>) stuMap.get(key);
//										for (int i = 0; i < files.size(); i++) {
//											File file = new File(files.get(i));
//		    								ContentBody contentBody = new FileBody(file,
//		    										"image/jpg");
//		    								reqEntity.addPart("PNG"+i, contentBody);
//										}
//									} else {
//										StringBody stringBody = new StringBody(stuMap.get(key)==null?"":stuMap.get(key).toString());
//										reqEntity.addPart(key, stringBody);
//									}
//	            				}
//	        				} 
							
							
//							if (GsonUtils.getJsonValue(jsonStr, "fileUrl") != null) {
//								File file = new File(GsonUtils.getJsonValue(
//										jsonStr, "fileUrl").toString());
//								ContentBody contentBody = new FileBody(file,
//										"image/jpg");
//								reqEntity.addPart("PNG", contentBody);
//							}
//							System.out.println(jsonStr);
//							reqEntity.addPart("JsonData", new StringBody(jsonStr,
//									Charset.forName("utf-8")));
//
							post.setEntity(reqEntity);

							response = client.execute(post);
						} else if (requestMothod.equals("get")) {//get 请求{
							
							
							if (requestID!=null) {
								httpGet = new HttpGet(URL+"/"+requestID);
							} else {
								httpGet = new HttpGet(URL);
							}
							httpGet.addHeader("token", DataManager.getInstance().userModel!=null ? DataManager.getInstance().userModel.Token:"");
							httpGet.addHeader("packagename", pContext.getPackageName());
//							httpGet.setHeader("MagazineId", "37");
							response = client.execute(httpGet);
						}
						
						result = EntityUtils.toString(response.getEntity(),HTTP.UTF_8);
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {// 请求成功
							
							 Object obj = null;
							 try {
								 obj = GsonUtils.getJsonValue(result, "result");
								 
								 boolean isSuc = Boolean.parseBoolean(GsonUtils.objectToJson(GsonUtils.getJsonValue(result,"isSuc")));
								 if (isSuc) {
									 result = GsonUtils.objectToJson(GsonUtils.getJsonValue(result,"result"));
									 mark = 1;
								} else {
									result = (String) GsonUtils.getJsonValue(result, "message");
									mark = 2;
								}
								 
								 
							 } catch (Exception e) {
								 // TODO: handle exception
							 }
							
						} else {// 请求失败
							
							result = (String) GsonUtils.getJsonValue(result, "message");
							mark = 2;
						}
					} catch (ConnectTimeoutException e) {
						mark = 3;
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						mark = 4;
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						// 释放网络连接资源
						client.getConnectionManager().shutdown();
					}
				}

				return (T) result;
			}

			public void cancelRequest() {
				if (post!=null) {
					post.abort();
				}
				if (httpGet!=null) {
					httpGet.abort();
				}
				try {
					client.getConnectionManager().shutdown();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}

			@Override
			public void onPostExecute(final T result) {

				if (isShowPorgress && mPD.isShowing()) {
					this.mPD.dismiss();
				}

				switch (mark) {
				case 1:
					pCallback.onCallback(result);
					break;
				case 2:// 请求失败
					if (pExceptionCallback != null) {
//						pExceptionCallback.onCallback(new CancelledException(
//								MSG_FAIL));
						pExceptionCallback.onCallback(new CancelledException(
								(String)result));
					}
					break;
				case 3:// 请求超时
					if (pExceptionCallback != null) {
						pExceptionCallback.onCallback(new CancelledException(
								MSG_TIMEOUT));
					}
					break;
				case 4:// 其它异常
					if (pExceptionCallback != null) {
						pExceptionCallback.onCallback(new CancelledException(
								MSG_INTENETEXCEPTION));
					}
					break;
				case 5:// 网络未连接
					if (pExceptionCallback != null) {
						pExceptionCallback.onCallback(new CancelledException(
								MSG_NETWORKFAIL));
					}
					break;

				default:
					break;
				}

				super.onPostExecute(result);
			}

			@Override
			protected void onCancelled() {
				// TODO Auto-generated method stub
				super.onCancelled();
				isCancel = true;// 或者通过isCancelled();方法获取
			}

			@SuppressLint("NewApi")
			@Override
			protected void onCancelled(T result) {
				// TODO Auto-generated method stub
				super.onCancelled(result);
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}

		}.execute((Void[]) null);
		
		return asyncTask;
	}

	// http请求
	public static <T> void doAsync1(final Context pContext,
			final CharSequence pTitle, final CharSequence pMessage,
			final CharSequence requestID, final Object requestObj,
			final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback,
			final boolean pCancelable, final boolean isShowPorgress) {

		new AsyncTask<Void, Void, T>() {
			private ProgressDialog mPD;
			private Exception mException = null;
			private HttpClient client;
			private HttpPost post;
			private boolean isCancel;
			private int mark;

			@Override
			public void onPreExecute() {
				/**
				 * 第一个参数代表你在哪个界面显示 第二个参数代表显示的标题 第三个参数代表显示的信息
				 * 第四个参数代表indeterminate（不定的）如果为true，左边会有一个圆形的标示
				 * 第五个参数代表cancelable（撤销）
				 */
				if (isShowPorgress) {
					this.mPD = ProgressDialog.show(pContext, pTitle, pMessage,
							true, pCancelable);
					if (pCancelable) {
						this.mPD.setOnCancelListener(new OnCancelListener() {
							public void onCancel(
									final DialogInterface pDialogInterface) {
								pExceptionCallback
										.onCallback(new CancelledException(
												MSG_CANCEL));// 取消操作时要处理doInBackground的方法：中断网络请求或者添加标志位中断循环
								pDialogInterface.dismiss(); // 关闭弹出框
								cancel(true);// 取消asyncTask的执行，此方法只是不执行onPostExecute，但是不能中断doInBackground、onProgressUpdate方法的执行
								cancelRequest();// 关闭网络请求，效果待验证
							}
						});
					}
				}

				super.onPreExecute();
			}

			@SuppressWarnings("unchecked")
			@Override
			public T doInBackground(final Void... params) {
				String result = null;

				if (!HttpUtils.isNetworkAvailable(pContext)) {
					mark = 5;
				} else {
					if (requestObj == null) {
						return (T) result;
					}

					try {

						client = new DefaultHttpClient();
						// client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT,
						// 5000);
						client.getParams()
								.setIntParameter(
										HttpConnectionParams.SO_TIMEOUT,
										TIME_OUT_DELAY); // 请求超时设置
						client.getParams().setIntParameter(
								HttpConnectionParams.CONNECTION_TIMEOUT,
								TIME_OUT_DELAY);// 连接超时
						String jsonStr = GsonUtils.objectToJson(requestObj);
						String jym = (String) GsonUtils.getJsonValue(jsonStr,
								"jym");
						if (jym.startsWith("R")) {// 仁济医院接口服务
							post = new HttpPost(URL);
						} else {// 公共接口服务
							post = new HttpPost(URL);
						}
						// if (requestID!=null) {
						// post = new HttpPost(URL+"/"+requestID);
						// } else {
						// post = new HttpPost(URL);
						// }

						MultipartEntity reqEntity = new MultipartEntity(
								HttpMultipartMode.BROWSER_COMPATIBLE);
						if (GsonUtils.getJsonValue(jsonStr, "fileUrl") != null) {
							File file = new File(GsonUtils.getJsonValue(
									jsonStr, "fileUrl").toString());
							if ("C006".equals(jym)) {// 发送的图片文件
								ContentBody contentBody = new FileBody(file,
										"image/jpg");
								reqEntity.addPart("PNG", contentBody);
							} else if ("S009".equals(jym)) {//
								if ("2".equals(GsonUtils.getJsonValue(jsonStr,
										"ptype"))) {// 发送spx音频文件
									ContentBody contentBody = new FileBody(
											file, "imultipart/form-data");
									reqEntity.addPart("SPX", contentBody);
								} else {// 发送图片
									ContentBody contentBody = new FileBody(
											file, "image/jpg");
									reqEntity.addPart("PNG", contentBody);
								}
							}
							reqEntity.addPart("JsonData", new StringBody(
									jsonStr, Charset.forName("utf-8")));
							post.setEntity(reqEntity);
						} else {
							// 设置HTTP POST请求参数必须用NameValuePair对象
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
							nameValuePairs.add(new BasicNameValuePair(
									"JsonData", GsonUtils
											.objectToJson(requestObj)));
							post.setEntity(new UrlEncodedFormEntity(
									nameValuePairs, HTTP.UTF_8));
						}

						// FileEntity fileEntity = new FileEntity(null, "");
						// post.setEntity(fileEntity);

						HttpResponse response = client.execute(post);
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {// 请求成功
							result = EntityUtils.toString(response.getEntity(),
									HTTP.UTF_8);
							LogUtils.i(result);
							BaseModel baseModel = (BaseModel) GsonUtils
									.jsonToBean(result, BaseModel.class);
							String status = baseModel.status;

							if (status.equals("0")) {// 成功
								mark = 1;
								result = baseModel.result;
							} else {// 失败
								mark = 2;
								result = (String) GsonUtils.getJsonValue(
										result, "result");
							}

						} else {// 请求失败
							mark = 2;
						}
					} catch (ConnectTimeoutException e) {
						mark = 3;
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						mark = 4;
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						// 释放网络连接资源
						client.getConnectionManager().shutdown();
					}
				}

				return (T) result;
			}

			public void cancelRequest() {
				post.abort();
				client.getConnectionManager().shutdown();
			}

			@Override
			public void onPostExecute(final T result) {

				if (isShowPorgress && mPD.isShowing()) {
					this.mPD.dismiss();
				}

				switch (mark) {
				case 1:
					pCallback.onCallback(result);
					break;
				case 2:// 请求失败
					if (pExceptionCallback != null) {
						pExceptionCallback.onCallback(new CancelledException(
								MSG_FAIL));
					}
					break;
				case 3:// 请求超时
					if (pExceptionCallback != null) {
						pExceptionCallback.onCallback(new CancelledException(
								MSG_TIMEOUT));
					}
					break;
				case 4:// 其它异常
					if (pExceptionCallback != null) {
						pExceptionCallback.onCallback(new CancelledException(
								MSG_INTENETEXCEPTION));
					}
					break;
				case 5:// 网络未连接
					if (pExceptionCallback != null) {
						pExceptionCallback.onCallback(new CancelledException(
								MSG_NETWORKFAIL));
					}
					break;

				default:
					break;
				}

				super.onPostExecute(result);
			}

			@Override
			protected void onCancelled() {
				// TODO Auto-generated method stub
				super.onCancelled();
				isCancel = true;// 或者通过isCancelled();方法获取
			}

			@SuppressLint("NewApi")
			@Override
			protected void onCancelled(T result) {
				// TODO Auto-generated method stub
				super.onCancelled(result);
			}

			@Override
			protected void onProgressUpdate(Void... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
			}

		}.execute((Void[]) null);
	}

	

	// ***************************************************************************************************************
	public static <T> void doProgressAsync(final Context pContext,
			final int pTitleResID, final ProgressCallable<T> pCallable,
			final Callback<T> pCallback) {
		AsyncTaskUtils.doProgressAsync(pContext, pTitleResID, pCallable,
				pCallback, null);
	}

	public static <T> void doProgressAsync(final Context pContext,
			final int pTitleResID, final ProgressCallable<T> pCallable,
			final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback) {
		if (!HttpUtils.isNetworkAvailable(pContext)) {
			if (pExceptionCallback != null) {
				pExceptionCallback.onCallback(new CancelledException(
						MSG_NETWORKFAIL));
			}
			return;
		}

		new AsyncTask<Void, Integer, T>() {
			private ProgressDialog mPD;
			private Exception mException = null;

			@Override
			public void onPreExecute() {
				this.mPD = new ProgressDialog(pContext);
				this.mPD.setTitle(pTitleResID);
				this.mPD.setIcon(android.R.drawable.ic_menu_send);
				this.mPD.setIndeterminate(false);
				this.mPD.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				this.mPD.setCanceledOnTouchOutside(false);
				this.mPD.show();
				super.onPreExecute();
			}

			@Override
			public T doInBackground(final Void... params) {
				try { // 回调用于处理业务中上传进度和错误处理
					return pCallable.call(new IProgressListener() {
						@Override
						public void onProgressChanged(final int pProgress) {
							// onProgressUpdate(pProgress);
							publishProgress(pProgress);
						}

						@Override
						public void onSendFail(Exception e) {
							// TODO Auto-generated method stub
							mException = e;
						}

					});
				} catch (final Exception e) {
					this.mException = e;
				}
				return null;
			}

			@Override
			public void onProgressUpdate(final Integer... values) {
				this.mPD.setProgress(values[0]);
			}

			@Override
			public void onPostExecute(final T result) {
				try {
					this.mPD.dismiss();
				} catch (final Exception e) {
					Log.e("Error", e.getLocalizedMessage());
					/* Nothing. */
				}
				if (this.isCancelled()) {
					this.mException = new CancelledException();
				}
				if (this.mException == null) {
					pCallback.onCallback(result);
				} else {
					if (pExceptionCallback == null) {
						// Log.e("Error",
						// this.mException.getLocalizedMessage());
					} else {
						pExceptionCallback.onCallback(this.mException);
					}
				}
				super.onPostExecute(result);
			}
		}.execute((Void[]) null);
	}

	public static <T> void doAsync(final Context pContext,
			final int pTitleResID, final int pMessageResID,
			final AsyncCallable<T> pAsyncCallable, final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback) {
		final ProgressDialog pd = ProgressDialog.show(pContext,
				pContext.getString(pTitleResID),
				pContext.getString(pMessageResID));
		pAsyncCallable.call(new Callback<T>() {
			@Override
			public void onCallback(final T result) {
				try {
					pd.dismiss();
				} catch (final Exception e) {
					Log.e("Error", e.getLocalizedMessage());
					/* Nothing. */
				}
				pCallback.onCallback(result);
			}
		}, pExceptionCallback);
	}

	public static <T> void doAsync(final Context pContext,
			final ResultBack<T> pResultback, final Callback<T> pCallback,
			final Callback<Exception> pExceptionCallback) {
		if (!HttpUtils.isNetworkAvailable(pContext)) {
			if (pExceptionCallback != null) {
				pExceptionCallback.onCallback(new CancelledException(
						MSG_NETWORKFAIL));
			}
			return;
		}

		new AsyncTask<Void, Integer, T>() {
			private ProgressDialog mPD;
			private Exception mException = null;

			@Override
			public void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			public T doInBackground(final Void... params) {
				try { // 回调用于处理业务中上传进度和错误处理
					return pResultback.onResultback();
				} catch (final Exception e) {
					this.mException = e;
				}
				return null;
			}

			@Override
			public void onProgressUpdate(final Integer... values) {
				this.mPD.setProgress(values[0]);
			}

			@Override
			public void onPostExecute(final T result) {

				if (this.isCancelled()) {
					this.mException = new CancelledException();
				}
				if (this.mException == null) {
					pCallback.onCallback(result);
				} else {
					if (pExceptionCallback == null) {
						// Log.e("Error",
						// this.mException.getLocalizedMessage());
					} else {
						pExceptionCallback.onCallback(this.mException);
					}
				}
				super.onPostExecute(result);
			}
		}.execute((Void[]) null);
	}

	public static class CancelledException extends Exception {
		private static final long serialVersionUID = -78123211381435595L;

		public CancelledException() {
			super();
			// TODO Auto-generated constructor stub
		}

		public CancelledException(String detailMessage, Throwable throwable) {
			super(detailMessage, throwable);
			// TODO Auto-generated constructor stub
		}

		public CancelledException(String detailMessage) {
			super(detailMessage);
			// TODO Auto-generated constructor stub
		}

		public CancelledException(Throwable throwable) {
			super(throwable);
			// TODO Auto-generated constructor stub
		}

	}

	public interface AsyncCallable<T> {
		// ===========================================================
		// Final Fields
		// ===========================================================
		// ===========================================================
		// Methods
		// ===========================================================
		/**
		 * Computes a result asynchronously, return values and exceptions are to
		 * be handled through the callbacks. This method is expected to return
		 * almost immediately, after starting a {@link Thread} or similar.
		 * 
		 * @return computed result
		 * @throws Exception
		 *             if unable to compute a result
		 */
		public void call(final Callback<T> pCallback,
				final Callback<Exception> pExceptionCallback);
	}

	public interface Callback<T> {
		// ===========================================================
		// Final Fields
		// ===========================================================
		// ===========================================================
		// Methods
		// ===========================================================
		/**
		 * 当加载完成后回调，加载完毕的事后处理
		 */
		public void onCallback(final T pCallbackValue);
		// public T onCallback();
	}

	public interface ResultBack<T> {
		// ===========================================================
		// Final Fields
		// ===========================================================
		// ===========================================================
		// Methods
		// ===========================================================
		/**
		 * 当加载完成后回调，加载完毕的事后处理
		 */
		public T onResultback();
	}

	public interface ProgressCallable<T> {
		// ===========================================================
		// Final Fields
		// ===========================================================
		// ===========================================================
		// Methods
		// ===========================================================
		/**
		 * 当加载完成后回调，加载完毕的事后处理
		 */
		// IProgressListener iProgressListener = new IProgressListener();
		public T call(final IProgressListener iProgressListener);
	}

	public interface IProgressListener<T> {
		// ===========================================================
		// Constants
		// ===========================================================
		// ===========================================================
		// Methods
		// ===========================================================
		/**
		 * @param pProgress
		 *            between 0 and 100.
		 */
		public void onProgressChanged(final int pProgress);

		/**
		 * 数据请求时发生异常
		 */
		public void onSendFail(final Exception e);
	}

}
