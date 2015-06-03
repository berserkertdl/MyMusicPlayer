package com.mymusicplayer.helper.utils;

import android.content.Context;

import com.mymusicplayer.helper.database.DBManager;

/**
 * Created by Administrator on 2015/6/3 0003.
 */
public class DBThread {

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


}
