package com.mymusicplayer.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

public class PlayerService extends Service {


    private static MediaPlayer player;

    private static List<String> musicUrls;

    private String currentUrl;

    //0 未初始化 1 准备 2 播放 3暂停
    private static int player_state = 0;

    private int position = 0;

    public PlayerService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        currentUrl = intent.getStringExtra("url");

        if (musicUrls == null) {
            musicUrls = new ArrayList<String>();
        }
        if(musicUrls.size()>0){
            if( musicUrls.get(musicUrls.size()-1).equals(currentUrl)){

            }else{
                musicUrls.add(currentUrl);
                initPlayer(currentUrl);
                play();
            }
        }else{
            musicUrls.add(currentUrl);
            initPlayer(currentUrl);
            play();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void initPlayer(String url) {
        player = new MediaPlayer();
        try {
            player.setDataSource(url);
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
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
