package com.mymusicplayer.helper.utils;

import android.content.Context;

import com.mymusicplayer.helper.database.DBManager;

/**
 * Created by Administrator on 2015/6/3 0003.
 */
public class DBThread {

    /**
     * 初始化数据库
     * */
    public class InitThread extends Thread {
        private Context context;

        public InitThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            DBManager.adds(context);
        }
    }

    /**
     * 更新歌曲最后播放时间线程
     * */
    public class UpdateLateLyPlayListThread extends Thread{
        private int songId;

        public UpdateLateLyPlayListThread(int songId) {
            this.songId = songId;
        }
        @Override
        public void run() {
            DBManager.updateMusicLastPlayTimeById(songId);
        }

    }


}
