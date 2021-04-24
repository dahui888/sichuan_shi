package com.yykj.mob.share.share;

import android.content.Context;
import android.text.TextUtils;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;
/**
 *    author : i小灰
 *    blog : https://www.jianshu.com/u/37d88b909f3b
 *    time   : 2020/01/11
 *    desc   : 分享到指定平台
 */
public class SharePlatformUtils {

    /**
     * 分享内容说明
     *
     * @param context
     * @param platformToShare //指定直接分享平台名称
     * @param title           //标题
     * @param titleUrl        //标题网址
     * @param imgUrl          //图片网址
     * @param info            //分享文本内容
     */
    public static void showShare(Context context, String platformToShare, final String title, final String titleUrl,
                                 final String imgUrl, final String imgPath, final String info) {
        OnekeyShare oks = new OnekeyShare();
        oks.setPlatform(platformToShare);
        oks.setTheme(OnekeyShareTheme.CLASSIC);
        //关闭SSO授权
        oks.disableSSOWhenAuthorize();
        //title标题：在邮箱、人人网、微信、QQ空间、信息中使用
        oks.setTitle(title);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(titleUrl);
        // URL仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(titleUrl);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(titleUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(info);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(info);
        // imagePath是图片的路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(imgUrl);
//        oks.setImagePath(imgPath);
        // 启动分享GUI
        oks.show(context);
    }
}
