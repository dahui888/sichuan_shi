package com.campuscard.app.http;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 回调
 */
public interface HttpResponseCallBack {
    void onSuccess(String result);

    void onFailed(int code, String failedMsg);

    void onFinished();
}
