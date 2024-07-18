package org.test.chapter05.music;

public class MusicBoxConstant {
	//music box status flag:
	// 0x01: 没有播放， 0x02: 正在播放, 0x03: 暂停
	public static final int IDLE = 0x01;
	public static final int PLAYING = 0x02;
	public static final int PAUSING = 0x03;
	
	public static String[] titles = new String[]{
			"月亮在哭泣",
			"我的歌声里",
			"存在",
			"当我想起你的时候"
    };
    
	public static String[] authors = new String[]{
			"过亚弥乃，つじあやの",
			"曲婉婷",
			"汪峰",
			"汪峰1"
    };
	
	 public static final String ACTION_CTL = "com.quick.android.action.ACTION_CTL";
	 public static final String ACTION_UPDATE = "com.quick.android.action.ACTION_UPDATE";
	 
	 public static final String TOKEN_CONTROL = "control";
	 public static final String TOKEN_UPDATE = "update";
	 public static final String TOKEN_CURRENT = "current";
	 
	 public static final int STOP = 2;
	 public static final int PLAY_OR_PAUSE = 1;
	 
	 public static final String MUSIC_SERVICE = "com.quick.android.MUSIC_SERVICE";
}
