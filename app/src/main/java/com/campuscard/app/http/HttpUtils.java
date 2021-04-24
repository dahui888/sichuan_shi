package com.campuscard.app.http;

import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.base.frame.utils.XToastUtil;
import com.campuscard.app.utils.UserInfoUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.task.PriorityExecutor;
import org.xutils.ex.HttpException;
import org.xutils.x;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 网络配置
 */
public class HttpUtils {

    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;

    /**
     * get请求
     *
     * @param context
     * @param params
     * @param callBack
     */
    public static void get(Context context, HttpRequestParams params, final HttpResponseCallBack callBack) {
        setDefaultHeader(params);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", "get: " + result);
                callBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {
                    httpExceptionHandler(ex, callBack);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * Post请求
     *
     * @param context
     * @param callBack
     */
    public static void post(Context context, HttpRequestParams params, final HttpResponseCallBack callBack) {
        setDefaultHeader(params);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", "post: " + result);
                callBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {
                    httpExceptionHandler(ex, callBack);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * Json数据提交
     *
     * @param context
     * @param params
     * @param jsonString json的String--需要将json数据格式转为String
     * @param callBack
     */
    public static void postJson(Context context, HttpRequestParams params, String jsonString, final HttpResponseCallBack callBack) {
        setDefaultJsonHeader(params, jsonString);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", "post: " + result);
                callBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {
                    httpExceptionHandler(ex, callBack);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * Json数据表单格式的提交
     *
     * @param context
     * @param url
     * @param paramsMap
     * @param callBack
     */
    public static void post(Context context, String url, ParamsMap paramsMap, final HttpResponseCallBack callBack) {
        HttpRequestParams params = new HttpRequestParams(url);
        setDefaultHeader(params, paramsMap);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", "post: " + result);
                callBack.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {
                    httpExceptionHandler(ex, callBack);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * Json数据表单格式的提交
     *
     * @param context
     * @param url
     * @param paramsMap
     * @param callBack
     */
    public static void post(Context context, String url, ParamsMap paramsMap, final Dialog dialog, final HttpResponseCallBack callBack) {
        HttpRequestParams params = new HttpRequestParams(url);
        setDefaultHeader(params, paramsMap);
        if (dialog != null) {
            dialog.show();
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.d("result", "post: " + result);
                callBack.onSuccess(result);
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (ex instanceof HttpException) {
                    httpExceptionHandler(ex, callBack);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFinished() {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
    }


    /**
     * 上传图片（单张）
     *
     * @param context
     * @param url
     * @param uploadeFilePath
     * @param callBack
     */
    public static void uploadPicture(Context context, String url, String uploadeFilePath, HttpResponseCallBack callBack) {
        HttpRequestParams params = new HttpRequestParams(url);
        params.setMultipart(true);
        params.addBodyParameter("file", new File(uploadeFilePath));
        params.addParameter("serviceName", "end-user-service");
        params.addParameter("fileType", "PICTURE");
        post(context, params, callBack);

    }

    /**
     * 设置默认请求header
     *
     * @param params
     */
    private static void setDefaultHeader(HttpRequestParams params) {
        if (!TextUtils.isEmpty(UserInfoUtils.getToken())) {
            params.addHeader("Authorization", UserInfoUtils.getToken());
        }
        params.addHeader("api-version", "1.0");
        params.setConnectTimeout(60 * 1000);
        params.setReadTimeout(60 * 1000);
    }

    /**
     * 设置默认请求header
     *
     * @param params
     */
    private static void setDefaultJsonHeader(HttpRequestParams params, String contentJson) {
        if (!TextUtils.isEmpty(UserInfoUtils.getToken())) {
            params.addHeader("Authorization", UserInfoUtils.getToken());
        }
        params.addHeader("api-version", "1.0");
        params.addHeader("Content-Type", "application/json");
        params.setAsJsonContent(true);
        params.setBodyContent(contentJson);
        params.setConnectTimeout(60 * 1000);
        params.setReadTimeout(60 * 1000);
    }


    /**
     * 设置默认请求header
     *
     * @param params
     */
    private static void setDefaultHeader(HttpRequestParams params, ParamsMap paramsMap) {
        if (!TextUtils.isEmpty(UserInfoUtils.getToken())) {
            params.addHeader("Authorization", UserInfoUtils.getToken());
        }
        params.addHeader("api-version", "1.0");
        params.addHeader("Content-Type", "application/json");
        if (paramsMap != null) {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
                try {
                    jsonObject.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            params.setAsJsonContent(true);
            params.setBodyContent(jsonObject.toString());
            params.setConnectTimeout(60 * 1000);
            params.setReadTimeout(60 * 1000);
        }
    }

    private static void httpExceptionHandler(Throwable ex, HttpResponseCallBack callBack) {
        HttpException httpEx = (HttpException) ex;
        int responseCode = httpEx.getCode();
        String responseMsg = httpEx.getMessage();
        String errorResult = httpEx.getResult();
        callBack.onFailed(responseCode, responseMsg);
    }
}
