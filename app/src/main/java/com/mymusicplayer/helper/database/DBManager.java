package com.mymusicplayer.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mymusicplayer.helper.vo.MusicEntity;

import java.util.List;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class DBManager {

    private DBOpenHelper hepler;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        hepler = new DBOpenHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = hepler.getWritableDatabase();
    }

    public void adds(List<MusicEntity> musics){


    }



}
