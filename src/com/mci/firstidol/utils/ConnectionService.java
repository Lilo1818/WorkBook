package com.mci.firstidol.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.mci.firstidol.callbacks_and_listeners.DownLoadCallback;
import com.mci.firstidol.callbacks_and_listeners.StringCallBack;
import com.mci.firstidol.constant.Constant;
import com.mci.firstidol.constant.DataManager;
import com.mci.firstidol.view.ToastUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class ConnectionService {
	
	private ExecutorService executorService = null;
	public static ConnectionService connectionService;
	private Handler handler = new Handler();

	private ConnectionService() {
	}

	public static synchronized ConnectionService getInstance() {
		if (connectionService == null) {
			connectionService = new ConnectionService();
		}
		return connectionService;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public void serviceConn(final Context context,  final String processURL,
			final String params, final StringCallBack callBack) {

		if (!isNetworkAvailable(context)) {
			ToastUtils.showCustomToast(context, "网络环境差，请检查网络");
			if (callBack != null) {
				callBack.getError();
			}

			return;
		}

		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					String urlString;
					if (processURL!=null) {
						urlString = AsyncTaskUtils.URL+"/"+processURL;
					} else {
						urlString = AsyncTaskUtils.URL;
					}
					HttpPost request = new HttpPost(urlString);
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 25000);
					HttpConnectionParams.setSoTimeout(httpParams, 25000);
					//List<NameValuePair> nameValuePair = getPramsFromMap(params);
					StringEntity reqEntity = new StringEntity(params,"utf-8");//解决中文乱码问题    
	                reqEntity.setContentEncoding("UTF-8");    
	                reqEntity.setContentType("application/json");  
					request.setEntity(reqEntity);
					request.setParams(httpParams);
					String token = getToken();
					request.addHeader(new BasicHeader("token", token));
					request.addHeader(new BasicHeader("packagename","com.mci.firstidol"));
					if(processURL.contains(Constant.RequestContstants.Request_article_add)){
						request.addHeader(new BasicHeader("MagazineId", "37"));
					}
					
					HttpResponse response = httpClient.execute(request);
					HttpEntity entity = response.getEntity();

					final String result = EntityUtils.toString(entity);
					/**
					 * 成功走这里
					 */
					if (callBack != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								callBack.getSuccessString(result);
							}
						});
					}
				} catch (Exception e) {
					/**
					 * 失败走这里
					 */
					handler.post(new Runnable() {
						@Override
						public void run() {
							callBack.getError();
						}
					});
				}
			}
		});
	}

	public void serviceConnUseGet(final Context context,
			final String processURL, final HashMap<String, String> params,
			final StringCallBack callBack) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					String url = getUrlPath(processURL, params);
					HttpGet request = new HttpGet(url);
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 25000);
					HttpConnectionParams.setSoTimeout(httpParams, 25000);
					List<NameValuePair> nameValuePair = getPramsFromMap(params);
					request.setParams(httpParams);
					String token = getToken();
					request.addHeader(new BasicHeader("token", token));
					request.addHeader(new BasicHeader("packagename","com.mci.firstidol"));
					HttpResponse response = httpClient.execute(request);
					HttpEntity entity = response.getEntity();
					final String result = EntityUtils.toString(entity);
					/**
					 * 成功走这里
					 */
					if (callBack != null) {
						handler.post(new Runnable() {     
							@Override
							public void run() {
								callBack.getSuccessString(result);
							}
						});
					}
				} catch (Exception e) {
					/**
					 * 失败走这里
					 */
					handler.post(new Runnable() {
						@Override
						public void run() {
							callBack.getError();
						}
					});
				}
			}
		});
	}

	/**
	 * 得到get请求地址参数
	 * 
	 * @param processURL
	 * @param params
	 * @return
	 */
	public String getUrlPath(String processURL, HashMap<String, String> params) {
		String Url = processURL;
		if (params != null && params.size() > 0) {
			Url += "?";
			for (String key : params.keySet()) {
				if (params.get(key) != null) {
					String value = params.get(key);
					Url += key + "=" + value + "&";
				}
			}
			Url = Url.substring(0, Url.length() - 1);
		}
		return Url;
	}

	/**
	 * 封装参数
	 * @param params
	 * @return
	 */
	public List<NameValuePair> getPramsFromMap(HashMap<String, String> params) {
		List<NameValuePair> result = new ArrayList<NameValuePair>();
		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				if (params.get(key) != null) {
					String value = params.get(key);
					result.add(new BasicNameValuePair(key, value));
				}
			}
		}
		return result;
	}
	/**
	 * 下载apk
	 * 
	 * @author 王海啸
	 */
	public void downloading(final String URL, final String name,
			final DownLoadCallback downloadBack) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(URL);
					HttpResponse response;
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					File file = new File(name);
					fileOutputStream = new FileOutputStream(file);
					if (!file.exists()) {
						file.mkdirs();
					}
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						final int progress = (int) (((float) count / length) * 100);
						// 更新进度
						handler.post(new Runnable() {
							@Override
							public void run() {
								downloadBack.getProcess(progress);
							}
						});
						if (numread <= 0) {
							// 下载完成通知
							handler.post(new Runnable() {
								@Override
								public void run() {
									downloadBack.Success();
								}
							});
							break;
						}
						fileOutputStream.write(buf, 0, numread);
					} while (true);// 点击取消就停止下载.

					fileOutputStream.close();
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
					handler.post(new Runnable() {
						@Override
						public void run() {
							downloadBack.Fail();
						}
					});
					Log.e("pnl", e.toString());
				}

			}
		});
	}

	/**
	 * 带附件上传（带图片）
	 * 
	 * @param context
	 * @param processURL
	 * @param params
	 * @param files
	 */
	public void serviceConnWithFileArray(final Context context,
			final String processURL, final HashMap<String, String> params,
			final String fileName, final List<File> files,
			final StringCallBack callBack) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost request = new HttpPost(processURL);
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 60000);
					HttpConnectionParams.setSoTimeout(httpParams, 60000);
					String token = getToken();
					request.addHeader(new BasicHeader("token", token));
					MultipartEntity reqEntity = new MultipartEntity();
					if (files != null && files.size() > 0) {
						int length = files.size();
						for (int i = 0; i < length; i++) {
							ContentBody cbFile = new FileBody(files.get(i));
							reqEntity.addPart(fileName + "[]", cbFile);
						}

					}
					if (params != null && params.size() > 0) {
						for (String key : params.keySet()) {
							if (params.get(key) != null) {
								String value = params.get(key);
								reqEntity.addPart(key, new StringBody(value,
										Charset.forName("utf-8")));
							}
						}
					}
					request.setEntity(reqEntity);
					request.setParams(httpParams);
					HttpResponse response = httpClient.execute(request);
					HttpEntity entity = response.getEntity();
					final String result = EntityUtils.toString(entity);
					if (callBack != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								callBack.getSuccessString(result);
							}
						});
					}
				} catch (Exception e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							callBack.getError();
						}
					});
				}
			}
		});
	}

	/**
	 * 带附件上传（带图片）
	 * <p/>
	 * file[1] file[2] file[3] 这种形式的上传用这个
	 * 
	 * @param context
	 * @param processURL
	 * @param params
	 * @param files
	 */
	public void serviceConnWithCustomFileArray(final Context context,
			final String processURL, final HashMap<String, String> params,
			final String fileName, final List<File> files,
			final StringCallBack callBack) {

		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient httpClient = new DefaultHttpClient();
					HttpPost request = new HttpPost(processURL);
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 60000);
					HttpConnectionParams.setSoTimeout(httpParams, 60000);
					String token = getToken();
					request.addHeader(new BasicHeader("token", token));
					MultipartEntity reqEntity = new MultipartEntity();
					if (files != null && files.size() > 0) {
						int length = files.size();
						for (int i = 0; i < length; i++) {
							ContentBody cbFile = new FileBody(files.get(i));
							reqEntity.addPart(fileName + "[" + i + "]", cbFile);
						}
					}
					if (params != null && params.size() > 0) {
						for (String key : params.keySet()) {
							if (params.get(key) != null) {
								String value = params.get(key);
								reqEntity.addPart(key, new StringBody(value,
										Charset.forName("utf-8")));
							}
						}
					}
					request.setEntity(reqEntity);
					request.setParams(httpParams);
					HttpResponse response = httpClient.execute(request);
					HttpEntity entity = response.getEntity();
					final String result = EntityUtils.toString(entity);
					if (callBack != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								callBack.getSuccessString(result);
							}
						});
					}
				} catch (Exception e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							callBack.getError();
						}
					});
				}
			}
		});
	}

	/**
	 * 带附件上传（带图片） 单文件
	 * 
	 * @param context
	 * @param processURL
	 * @param params
	 */
	public void serviceConnWithSingleFile(final Context context,
			final String processURL, final HashMap<String, String> params,
			final String fileName, final File file,
			final StringCallBack callBack) {
		getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpPost request = new HttpPost(processURL);
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams
							.setConnectionTimeout(httpParams, 60000);
					HttpConnectionParams.setSoTimeout(httpParams, 60000);
					String token = getToken();
					request.addHeader(new BasicHeader("token", token));
					MultipartEntity reqEntity = new MultipartEntity();
					if (file != null) {
						ContentBody cbFile = new FileBody(file);
						reqEntity.addPart(fileName, cbFile);
					}
					if (params != null && params.size() > 0) {
						for (String key : params.keySet()) {
							if (params.get(key) != null) {
								String value = params.get(key);
								reqEntity.addPart(key, new StringBody(value,
										Charset.forName("utf-8")));
							}
						}
					}
					request.setEntity(reqEntity);
					request.setParams(httpParams);
					HttpResponse response = httpClient.execute(request);
					HttpEntity entity = response.getEntity();
					final String result = EntityUtils.toString(entity);
					if (callBack != null) {
						handler.post(new Runnable() {
							@Override
							public void run() {
								callBack.getSuccessString(result);
							}
						});
					}
				} catch (Exception e) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							callBack.getError();
						}
					});
				}
			}
		});
	}

	/**
	 * 得到token
	 * 
	 * @return
	 */
	public String getToken() {
		String token = DataManager.getInstance().userModel != null ? DataManager
				.getInstance().userModel.Token : "";
		return token;
	}

	private ExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newFixedThreadPool(10);
		}
		return executorService;
	}
}
