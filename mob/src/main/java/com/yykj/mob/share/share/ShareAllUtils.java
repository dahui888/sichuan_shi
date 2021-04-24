package com.yykj.mob.share.share;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 一键分享到多个平台（供选择）
 */
public class ShareAllUtils {

    /**
     * 分享内容说明
     *
     * @param context
     * @param title
     * @param titleUrl
     * @param imgUrl
     * @param info
     */
    public static void showShare(Context context, final String title, final String titleUrl,
                                 final String imgUrl, final String info) {
        OnekeyShare oks = new OnekeyShare();
        //关闭SSO授权
        oks.disableSSOWhenAuthorize();
        //titile标题：在邮箱、人人网、微信、QQ空间、信息中使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(info);
        // imagePath是图片的路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(imgUrl);
        // URL仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(titleUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(info);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(titleUrl);
        // 启动分享GUI
        oks.show(context);
    }
}
