
package cn.sharesdk.onekeyshare;

import cn.sharesdk.onekeyshare.themes.classic.ClassicTheme;

/** 快捷分享的主题样式  */
public enum OnekeyShareTheme {
	/** 九格宫的主题样式 ,对应的实现类ClassicTheme */
	CLASSIC(0, new ClassicTheme());

	private final int value;
	private final OnekeyShareThemeImpl impl;

	private OnekeyShareTheme(int value, OnekeyShareThemeImpl impl) {
		this.value = value;
		this.impl = impl;
	}

	public int getValue() {
		return value;
	}

	public OnekeyShareThemeImpl getImpl() {
		return impl;
	}

	public static OnekeyShareTheme fromValue(int value) {
		for (OnekeyShareTheme theme : OnekeyShareTheme.values()) {
			if (theme.value == value) {
				return theme;
			}
		}
		return CLASSIC;
	}

}
