package org.test.chapter05.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.IBinder;

import java.io.IOException;

/**
 * 定义播放音乐的服务
 * 
 * @author BladeStone
 * 
 */
public class MusicService extends Service {
	private MyReceiver serviceReceiver;
	private AssetManager assetManager;
	private String[] musics = new String[] { "for-love.mp3", "inmysong.mp3",
			"living-there.mp3", "when-I-thinking.mp3" };
	private MediaPlayer mediaPlayer;

	private int status = MusicBoxConstant.IDLE;
	private int current = 0;

	//绑定一个Activity
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	//当创建一个Service对象之后，会首先调用这个函数
	@Override
	public void onCreate() {
		assetManager = this.getAssets();
		serviceReceiver = new MyReceiver();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(MusicBoxConstant.ACTION_CTL);
		registerReceiver(serviceReceiver, filter);
		
		mediaPlayer = new MediaPlayer();
 
		super.onCreate();
	}

	//222-----接收Activity的传值，更新操作，主要功能
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("Music Serivce is started!");
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				//++i表示 i 的值先+1，再取i的值，i++表示先取i的值，在给i+1
				current++;
				if (current > 3) {
					current = 0;
				}

				//去掉下面4行，不接着播放第二首歌，MusicBox Activity里面的歌曲名称和作者未更改过来，界面上显示暂停按钮和停止按钮
				//current代表正在播放的歌曲
				Intent sendIntent = new Intent(MusicBoxConstant.ACTION_UPDATE);
				sendIntent.putExtra(MusicBoxConstant.TOKEN_CURRENT, current);
				sendBroadcast(sendIntent);
				//去掉此行，不接着播放第二首歌，MusicBox Activity里面的歌曲名称和作者已更改过来，界面上显示暂停按钮和停止按钮
				prepareAndPlay(musics[current]);
			}
		});
		
		return super.onStartCommand(intent, flags, startId);
	}

    
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		System.out.println("Music Service is destoryed!");
	}

	
	//4444444-------接收来自于MusicBox.java,点击播放/暂停/停止按钮时发送的广播对象
	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {	
			int control = intent.getIntExtra("control", -1);
			System.out.println("onReceive:" + control);
			//control=1，播放按钮 2,stop按钮
			switch (control) {
			//点击播放或暂停按钮时 MusicBoxConstant.PLAY_OR_PAUSE=1
			case MusicBoxConstant.PLAY_OR_PAUSE:
				//如果没有播放，全局变量
				if (status == MusicBoxConstant.IDLE) {
					prepareAndPlay(musics[current]);
					status = MusicBoxConstant.PLAYING;
				//正在播放
				} else if (status == MusicBoxConstant.PLAYING) {
					mediaPlayer.pause();
					status = MusicBoxConstant.PAUSING;
				//暂停播放
				} else if (status == MusicBoxConstant.PAUSING) {
					mediaPlayer.start();
					status = MusicBoxConstant.PLAYING;
				}
				break;
			//停止播放，MusicBoxConstant.STOP=2
			case MusicBoxConstant.STOP:
				if (status == MusicBoxConstant.PLAYING
						|| status == MusicBoxConstant.PAUSING) {
					mediaPlayer.stop();
					status = MusicBoxConstant.IDLE;
				}
				break;
			}

			Intent sendIntent = new Intent(MusicBoxConstant.ACTION_UPDATE);
			sendIntent.putExtra(MusicBoxConstant.TOKEN_UPDATE, status);
			sendIntent.putExtra(MusicBoxConstant.TOKEN_CURRENT, current);
			MusicService.this.sendBroadcast(sendIntent);
		}
	}
    
	private void prepareAndPlay(String music) {
		try {
			AssetFileDescriptor afd = assetManager.openFd(music);
			mediaPlayer.reset();
			mediaPlayer.setDataSource(afd.getFileDescriptor(),
					afd.getStartOffset(), afd.getLength());

			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}

//ending-------------------