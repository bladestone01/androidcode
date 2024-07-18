package org.test.chapter05.music;

import static org.test.chapter05.music.R.*;
import static org.test.chapter05.music.R.id.play;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MusicBox extends Activity {
    private TextView title, author;
    private ImageButton play, stop;
    private ActivityReceiver actReceiver;
   
    private int status = MusicBoxConstant.IDLE;
    private Intent actionIntent;
    
	//1----
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MusicListener musicListener = new MusicListener();
		
		play = (ImageButton)this.findViewById(id.play);
		stop = (ImageButton)this.findViewById(R.id.stop);
		title = (TextView)this.findViewById(R.id.title);
		author = (TextView)this.findViewById(R.id.author);
		
		play.setOnClickListener(musicListener);
		stop.setOnClickListener(musicListener);

		/*
		`注册广播有两种方式：
			（1）静态注册：在AndroidManifest.xml里通过<receive>标签声明
			（2）动态注册：在代码中调用Context.registerReceiver（）方法
		 */
		// 1. 实例化BroadcastReceiver子类 &  IntentFilter
		actReceiver = new ActivityReceiver();
		IntentFilter filter = new IntentFilter();
		//2. 设置接收广播的类型
		filter.addAction(MusicBoxConstant.ACTION_UPDATE);
		//3. 动态注册：调用Context的registerReceiver（）方法
		this.registerReceiver(actReceiver, filter);
		//Intent actionIntent = new Intent(this, MusicService.class);


		Intent actionIntent = new Intent(this,MusicService.class);
		//android不能采用隐式启动service，只能采用显式调用
//		actionIntent.setAction(MusicBoxConstant.MUSIC_SERVICE);
		this.startService(actionIntent);
	}

	/**
	 * 自定义的BroadcastReceiver,负责监听从Service传回来的广播。
	 *
	 */
	public class ActivityReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			//获取Intent中的update信息，update代表播放状态
			 int update = intent.getIntExtra("update", -1);
			//获取Intent中的current消息, current代表当前正在播放的歌曲
			 int current = intent.getIntExtra("current", -1);
			 
			 if (current >= 0) {
				 System.out.println("Title:" + MusicBoxConstant.titles[current]);
				 title.setText(MusicBoxConstant.titles[current]);
				 author.setText(MusicBoxConstant.authors[current]);
			 }

			 ////获取Intent中的update信息，update代表播放状态
			 switch(update) {
			 //	IDLE没有播放
			 case MusicBoxConstant.IDLE:
				 play.setImageResource(R.drawable.play);
				 status = MusicBoxConstant.IDLE;
				 stopService(actionIntent);
				 break;
				 //控制系统进入播放状态
			 case MusicBoxConstant.PLAYING:
				 //设置播放状态下的暂停图标
				 play.setImageResource(R.drawable.pause);
				 //设置当前状态
				 status = MusicBoxConstant.PLAYING;
				 break;
				 //控制系统进入暂停状态
			 case MusicBoxConstant.PAUSING:
				 //暂停状态下设置使用播放图标
				 play.setImageResource(R.drawable.play);
				 //设置当前状态
				 status = MusicBoxConstant.PAUSING;
				 break;
			 }
		}		
	}

	/**
	 * 3333----音乐播放器按钮事件监听器
	 *
	 * @author BladeStone
	 *
	 */
	class MusicListener implements OnClickListener {

		@Override
		public void onClick(View source) {
			// 创建Intent
			Intent intent = new Intent(MusicBoxConstant.ACTION_CTL);
			if (source.getId() == id.play) {
				intent.putExtra("control", 1);
			} else if (source.getId() == id.stop) {
				intent.putExtra("control", 2);
			}

            //发送广播, 将被Service组件中的BroadcastReceiver接收到
			System.out.println("OnClick and send Broadcast....");
			MusicBox.this.sendBroadcast(intent);
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
