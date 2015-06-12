package com.mymusicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.Menu;

import com.mymusicplayer.R;
import com.mymusicplayer.helper.utils.DBThread;
import com.mymusicplayer.helper.utils.L;

import java.util.ArrayList;
import java.util.List;

public class PlayerService extends Service {

    private static final String TAG = PlayerService.class.getSimpleName();
    private static MediaPlayer player;

    private static List<String> musicUrls = new ArrayList<String>();

    private static String currentUrl = null;

    //0 未初始化 1 准备 2 播放 3暂停
    private static int player_state = 0;

    private int position = 0;

    public PlayerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.e(TAG,"onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.e(TAG,"onStartCommand");
        String tempUrl = intent.getStringExtra("url");
        int playFlag = intent.getIntExtra("flag", 2);
        int id = intent.getIntExtra("id", 0);

        switch (playFlag) {
            //stop
            case 0:
                stop();
                break;
            //play
            case 2:
                //要播放的歌曲与当前播放的歌曲相同
                if (currentUrl != null && tempUrl.equals(currentUrl)) {
                    //当前播放器的状态为暂停
                    if (player_state == 3) {
                        play();
                    }
                } else {
                    initPlayer();
                    preparePlayer(tempUrl);
                    play();
                    musicUrls.add(tempUrl);
                    currentUrl = tempUrl;
                    new DBThread().new UpdateLateLyPlayListThread(id).start();  //更新歌曲最后播放时间线程
                }
                break;
            //pause
            case 3:
                pause();
                break;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void initPlayer() {
        if (player == null) {
            player = new MediaPlayer();
        } else {
            player.reset();
            position = 0;
        }

    }

    private void preparePlayer(String url) {
        if (player == null) {
            L.e("MediaPlayer is null");
            return;
        }
        try {
            player.setDataSource(url);
            player.prepare();
            player_state = 1;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void play() {
        if (player != null) {
            player.seekTo(position);
            player.start();
            player_state = 2;
        }

    }

    private void stop() {
        if (player != null) {
            position = 0;
            player.stop();
            player.release();
            player = null;
            player_state = 0;
        }

    }

    private void pause() {
        if (player != null) {
            position = player.getCurrentPosition();
            player.pause();
            player_state = 3;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        L.e(TAG,"onBind");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
     public void onStart(Intent intent, int startId) {
        L.e(TAG,"onStart");
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        L.e(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        L.e(TAG,"onUnbind");
        return super.onUnbind(intent);
    }
}
