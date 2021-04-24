package com.campuscard.app.pay;

import android.app.Activity;
import android.util.Log;

import com.base.frame.utils.XToastUtil;
import com.ccb.ccbnetpay.message.CcbPayResultListener;
import com.ccb.ccbnetpay.platform.CcbPayAliPlatform;
import com.ccb.ccbnetpay.platform.CcbPayPlatform;
import com.ccb.ccbnetpay.platform.CcbPayWechatPlatform;
import com.ccb.ccbnetpay.platform.Platform;

import java.util.Map;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 支付工具类
 */
public class PayUtils {

    //商户参数
    private static final String MERCHANTID = "105651000011961";//商户编号
    private static final String POSID = "007931578";//柜台号
    private static final String BRANCHID = "510000000";//分行代码
    private static final String THIRDAPPINFO = "comccbpay" + MERCHANTID + "androidPay";//客户端标识
    private static final String PUB32 = "118efdd8893ae5b9a3b35b89020111";//公钥后30位

    //支付方式名称
    public static final String JIAN_HANG_PAY = "JIAN_HANG_PAY";
    public static final String WX = "WX";
    public static final String ALIPAY = "ALIPAY";
    public static final String JIAN_HANG_PAY_NAME = "建行支付";
    public static final String WX_NAME = "微信支付";
    public static final String ALIPAY_NAME = "支付宝支付";


    /**
     * 总支付
     *
     * @param context
     * @param params
     * @param ccbPayResultListener
     */
    public static void startPay(final Activity context, String params, String payType, final CcbPayResultListener ccbPayResultListener) {
        switch (payType) {
            case JIAN_HANG_PAY:
                setBankPay(context, params, ccbPayResultListener);
                break;
            case WX:
                wxPay(context, params, ccbPayResultListener);
                break;
            case ALIPAY:
                aliPay(context, params, ccbPayResultListener);
                break;
        }
    }


    /**
     * 银行卡支付--建行App或者建行网页支付
     *
     * @param context
     */
    public static void setBankPay(final Activity context, String params, final CcbPayResultListener ccbPayResultListener) {
        Platform ccbPayPlatform = new CcbPayPlatform
                .Builder()
                .setActivity(context)
                // 商户串  格式见 （3.1）
                .setParams(params)
                // 支付模式  建行APP或H5 支付
                .setPayStyle(Platform.PayStyle.APP_OR_H5_PAY)
                //  支付回调
                .setListener(new CcbPayResultListener() {
                    @Override
                    public void onSuccess(Map<String, String> map) {
                        XToastUtil.showToast(context, "支付成功");
                        ccbPayResultListener.onSuccess(map);
                        for (Map.Entry entry : map.entrySet()) {
                            Log.d("ooo", "key --" + entry.getKey() + "  value --" + entry.getValue());
                        }
                    }

                    @Override
                    public void onFailed(String s) {
                        XToastUtil.showToast(context, s);
                        ccbPayResultListener.onFailed(s);
                    }
                })
                .build();
        ccbPayPlatform.pay();
    }

    /**
     * 微信--支付包支付
     *
     * @param context
     * @param params  商户参数
     */
    public static void wxPay(final Activity context, String params, final CcbPayResultListener ccbPayResultListener) {
        Platform ccbPayWechatPlatform = new CcbPayWechatPlatform
                .Builder()
                .setActivity(context)
                .setListener(new CcbPayResultListener() {
                    @Override
                    public void onSuccess(Map<String, String> map) {
                        XToastUtil.showToast(context, "支付成功");
                        ccbPayResultListener.onSuccess(map);
                        for (Map.Entry entry : map.entrySet()) {
                            Log.d("ooo", "key --" + entry.getKey() + "  value --" + entry.getValue());
                        }
                    }

                    @Override
                    public void onFailed(String s) {
                        XToastUtil.showToast(context, s);
                        ccbPayResultListener.onFailed(s);
                    }
                })
                .setParams(params)
                .build();
        ccbPayWechatPlatform.pay();
    }

    /**
     * 支付包支付
     *
     * @param context
     * @param params  商户参数
     */
    public static void aliPay(final Activity context, String params, final CcbPayResultListener ccbPayResultListener) {
        Platform ccbPayAliPlatform = new CcbPayAliPlatform
                .Builder()
                .setActivity(context)
                .setListener(new CcbPayResultListener() {
                    @Override
                    public void onSuccess(Map<String, String> map) {
                        XToastUtil.showToast(context, "支付成功");
                        ccbPayResultListener.onSuccess(map);
                        for (Map.Entry entry : map.entrySet()) {
                            Log.d("ooo", "key --" + entry.getKey() + "  value --" + entry.getValue());
                        }
                    }

                    @Override
                    public void onFailed(String s) {
                        XToastUtil.showToast(context, s);
                        ccbPayResultListener.onFailed(s);
                    }
                })
                .setParams(params)
                .build();
        ccbPayAliPlatform.pay();
    }

    /**
     * 支付参数
     *
     * @return
     */
    public static String payParams(String oriderId, String money, String des, String IP) {

        //签名时候用
        String paramsMD5 = "MERCHANTID=" + MERCHANTID + "&POSID=" + POSID + "&BRANCHID=" + BRANCHID + "&ORDERID=" + oriderId + "&PAYMENT=" + money + "&CURCODE=01&TXCODE=520100&REMARK1=&REMARK2=&TYPE=1" +
                "&PUB=" + PUB32 + "&GATEWAY=0&CLIENTIP=" + IP + "&REGINFO=&PROINFO=" + Util.getInstance().escape(des) + "&REFERER=&THIRDAPPINFO=" + THIRDAPPINFO;
        //参数--PUB不能作为参数要去掉
        String params = "MERCHANTID=" + MERCHANTID + "&POSID=" + POSID + "&BRANCHID=" + BRANCHID + "&ORDERID=" + oriderId + "&PAYMENT=" + money + "&CURCODE=01&TXCODE=520100&REMARK1=&REMARK2=&TYPE=1" +
                "&GATEWAY=0&CLIENTIP=" + IP + "&REGINFO=&PROINFO=" + Util.getInstance().escape(des) + "&REFERER=&THIRDAPPINFO=" + THIRDAPPINFO;
        //全部参数--有的空值参数不参与MAC校验
        String paramsAll = params + "&TIMEOUT=&NoCredit=&NoDebit=";
        String paramsNew = paramsAll + "&MAC=" + Util.getInstance().hex_md5(paramsMD5);
        return paramsNew;
    }

}
