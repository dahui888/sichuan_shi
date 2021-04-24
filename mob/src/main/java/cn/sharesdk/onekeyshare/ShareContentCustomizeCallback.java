
package cn.sharesdk.onekeyshare;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;

/** 自定义不同平台分享不同内容的接口 */
public interface ShareContentCustomizeCallback {

	public void onShare(Platform platform, ShareParams paramsToShare);

}
