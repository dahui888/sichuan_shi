package com.campuscard.app.pay;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 支付MD5
 */
public class Util {

    private int hexcase = 0; /* hex output format. 0 - lowercase; 1 - uppercase */
    // private String b64pad = ""; /* base-64 pad character. "=" for strict RFC
    // compliance */
    private int chrsz = 8; /* bits per input character. 8 - ASCII; 16 - Unicode */

    // 使用volatile关键字保其可见性
    volatile private static Util util = null;

    private Util() {

    }

    public static Util getInstance() {
        try {
            if (util == null) {
                // 创建实例之前可能会有一些准备性的耗时工作
                Thread.sleep(300);
                synchronized (Util.class) {
                    if (util == null)
                        util = new Util();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return util;
    }

    public String hex_md5(String param) {
        return binl2hex(core_md5(str2binl(param), param.length() * chrsz));
    }

    public int[] str2binl(String str) {
        int index = ((((str.length() * chrsz) + 64) >>> 9) << 4) + 14;
        int[] bin = new int[index + 1];
        int mask = (1 << chrsz) - 1;
        for (int i = 0; i < str.length() * chrsz; i += chrsz)
            bin[i >> 5] |= (getUnicode(str, (i / chrsz)) & mask) << (i % 32);
        return bin;
    }

    public String binl2hex(int[] binarray) {
        String hex_tab = (hexcase == 1) ? "0123456789ABCDEF"
                : "0123456789abcdef";
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < binarray.length * 4; i++) {
            builder.append(hex_tab
                    .charAt((binarray[i >> 2] >> ((i % 4) * 8 + 4)) & 0xF)
                    + ""
                    + hex_tab.charAt((binarray[i >> 2] >> ((i % 4) * 8)) & 0xF));
        }
        return builder.toString();
    }

    public int[] core_md5(int[] x, int len) {
        /* append padding */
        x[len >> 5] |= 0x80 << ((len) % 32);
        x[(((len + 64) >>> 9) << 4) + 14] = len;
        int a = 1732584193;
        int b = -271733879;
        int c = -1732584194;
        int d = 271733878;
        int element = 0;

        for (int i = 0; i < x.length; i += 16) {
            int olda = a;
            int oldb = b;
            int oldc = c;
            int oldd = d;
            if (x.length == (i + 15)) {
                element = 0;
            } else {
                element = x[i + 15];
            }
            a = md5_ff(a, b, c, d, x[i + 0], 7, -680876936);
            d = md5_ff(d, a, b, c, x[i + 1], 12, -389564586);
            c = md5_ff(c, d, a, b, x[i + 2], 17, 606105819);
            b = md5_ff(b, c, d, a, x[i + 3], 22, -1044525330);
            a = md5_ff(a, b, c, d, x[i + 4], 7, -176418897);
            d = md5_ff(d, a, b, c, x[i + 5], 12, 1200080426);
            c = md5_ff(c, d, a, b, x[i + 6], 17, -1473231341);
            b = md5_ff(b, c, d, a, x[i + 7], 22, -45705983);
            a = md5_ff(a, b, c, d, x[i + 8], 7, 1770035416);
            d = md5_ff(d, a, b, c, x[i + 9], 12, -1958414417);
            c = md5_ff(c, d, a, b, x[i + 10], 17, -42063);
            b = md5_ff(b, c, d, a, x[i + 11], 22, -1990404162);
            a = md5_ff(a, b, c, d, x[i + 12], 7, 1804603682);
            d = md5_ff(d, a, b, c, x[i + 13], 12, -40341101);
            c = md5_ff(c, d, a, b, x[i + 14], 17, -1502002290);
            b = md5_ff(b, c, d, a, element, 22, 1236535329);

            a = md5_gg(a, b, c, d, x[i + 1], 5, -165796510);
            d = md5_gg(d, a, b, c, x[i + 6], 9, -1069501632);
            c = md5_gg(c, d, a, b, x[i + 11], 14, 643717713);
            b = md5_gg(b, c, d, a, x[i + 0], 20, -373897302);
            a = md5_gg(a, b, c, d, x[i + 5], 5, -701558691);
            d = md5_gg(d, a, b, c, x[i + 10], 9, 38016083);
            c = md5_gg(c, d, a, b, element, 14, -660478335);
            b = md5_gg(b, c, d, a, x[i + 4], 20, -405537848);
            a = md5_gg(a, b, c, d, x[i + 9], 5, 568446438);
            d = md5_gg(d, a, b, c, x[i + 14], 9, -1019803690);
            c = md5_gg(c, d, a, b, x[i + 3], 14, -187363961);
            b = md5_gg(b, c, d, a, x[i + 8], 20, 1163531501);
            a = md5_gg(a, b, c, d, x[i + 13], 5, -1444681467);
            d = md5_gg(d, a, b, c, x[i + 2], 9, -51403784);
            c = md5_gg(c, d, a, b, x[i + 7], 14, 1735328473);
            b = md5_gg(b, c, d, a, x[i + 12], 20, -1926607734);

            a = md5_hh(a, b, c, d, x[i + 5], 4, -378558);
            d = md5_hh(d, a, b, c, x[i + 8], 11, -2022574463);
            c = md5_hh(c, d, a, b, x[i + 11], 16, 1839030562);
            b = md5_hh(b, c, d, a, x[i + 14], 23, -35309556);
            a = md5_hh(a, b, c, d, x[i + 1], 4, -1530992060);
            d = md5_hh(d, a, b, c, x[i + 4], 11, 1272893353);
            c = md5_hh(c, d, a, b, x[i + 7], 16, -155497632);
            b = md5_hh(b, c, d, a, x[i + 10], 23, -1094730640);
            a = md5_hh(a, b, c, d, x[i + 13], 4, 681279174);
            d = md5_hh(d, a, b, c, x[i + 0], 11, -358537222);
            c = md5_hh(c, d, a, b, x[i + 3], 16, -722521979);
            b = md5_hh(b, c, d, a, x[i + 6], 23, 76029189);
            a = md5_hh(a, b, c, d, x[i + 9], 4, -640364487);
            d = md5_hh(d, a, b, c, x[i + 12], 11, -421815835);
            c = md5_hh(c, d, a, b, element, 16, 530742520);
            b = md5_hh(b, c, d, a, x[i + 2], 23, -995338651);

            a = md5_ii(a, b, c, d, x[i + 0], 6, -198630844);
            d = md5_ii(d, a, b, c, x[i + 7], 10, 1126891415);
            c = md5_ii(c, d, a, b, x[i + 14], 15, -1416354905);
            b = md5_ii(b, c, d, a, x[i + 5], 21, -57434055);
            a = md5_ii(a, b, c, d, x[i + 12], 6, 1700485571);
            d = md5_ii(d, a, b, c, x[i + 3], 10, -1894986606);
            c = md5_ii(c, d, a, b, x[i + 10], 15, -1051523);
            b = md5_ii(b, c, d, a, x[i + 1], 21, -2054922799);
            a = md5_ii(a, b, c, d, x[i + 8], 6, 1873313359);
            d = md5_ii(d, a, b, c, element, 10, -30611744);
            c = md5_ii(c, d, a, b, x[i + 6], 15, -1560198380);
            b = md5_ii(b, c, d, a, x[i + 13], 21, 1309151649);
            a = md5_ii(a, b, c, d, x[i + 4], 6, -145523070);
            d = md5_ii(d, a, b, c, x[i + 11], 10, -1120210379);
            c = md5_ii(c, d, a, b, x[i + 2], 15, 718787259);
            b = md5_ii(b, c, d, a, x[i + 9], 21, -343485551);

            a = safe_add(a, olda);
            b = safe_add(b, oldb);
            c = safe_add(c, oldc);
            d = safe_add(d, oldd);
        }
        int[] array = new int[4];
        array[0] = a;
        array[1] = b;
        array[2] = c;
        array[3] = d;
        return array;

    }

    public int md5_cmn(int q, int a, int b, int x, int s, int t) {
        return safe_add(bit_rol(safe_add(safe_add(a, q), safe_add(x, t)), s), b);
    }

    public int safe_add(int x, int y) {
        int lsw = (x & 0xFFFF) + (y & 0xFFFF);
        int msw = (x >> 16) + (y >> 16) + (lsw >> 16);
        return (msw << 16) | (lsw & 0xFFFF);
    }

    public int bit_rol(int num, int cnt) {
        return (num << cnt) | (num >>> (32 - cnt));
    }

    public int md5_ff(int a, int b, int c, int d, int x, int s, int t) {
        return md5_cmn((b & c) | ((~b) & d), a, b, x, s, t);
    }

    public int md5_gg(int a, int b, int c, int d, int x, int s, int t) {
        return md5_cmn((b & d) | (c & (~d)), a, b, x, s, t);
    }

    public int md5_hh(int a, int b, int c, int d, int x, int s, int t) {
        return md5_cmn(b ^ c ^ d, a, b, x, s, t);
    }

    public int md5_ii(int a, int b, int c, int d, int x, int s, int t) {
        return md5_cmn(c ^ (b | (~d)), a, b, x, s, t);
    }

    public int getUnicode(String str, int num) {
        char rs = str.charAt(num);
        return (int) rs;
    }

    public boolean isStringNullOrEmpty(String str) {
        return str == null || "".equals(str.trim())
                || TextUtils.isEmpty(str.trim());
    }

    public ProgressDialog showProgressDialog(Context c, int titleResId,
                                             int messageResId, boolean indeterminate, boolean cancelable,
                                             ProgressDialog progressDialog) {
        String title = "";
        if (titleResId != -1)
            title = getResourceString(c, titleResId);
        String message = "";

        if (-1 != messageResId) {
            message = getResourceString(c, messageResId);
        }
        return showProgressDialog(c, title, message, indeterminate, cancelable,
                progressDialog);

    }

    public String getResourceString(Context c, int textId) {
        return c == null ? null : c.getResources().getString(textId);
    }

    public ProgressDialog showProgressDialog(Context c, String title,
                                             String message, boolean indeterminate, boolean cancelable,
                                             ProgressDialog progressDialog) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(c);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //按返回键可dismiss
        progressDialog.setCancelable(cancelable);
        //false滚动条的当前值自动在最小到最大值之间来回移动，但不能提示工作进度
        progressDialog.setIndeterminate(indeterminate);
        if (!isStringNullOrEmpty(title))
            progressDialog.setTitle(title);
        if (!isStringNullOrEmpty(message))
            progressDialog.setMessage(message);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        return progressDialog;
    }

    public void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }


    public String escape(String str) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(str.length() * 6);
        for (i = 0; i < str.length(); i++) {
            j = str.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

}
