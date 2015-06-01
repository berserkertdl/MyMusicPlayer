package com.mymusicplayer.helper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class DBOpenHelper extends SQLiteOpenHelper {


    private static String DB_NAME = "test.db";
    private static int DEFAULT_DB_VERSION = 1;

    public DBOpenHelper(Context context) {
        //CursorFactory设置为null,使用默认值
        super(context, DB_NAME, null, DEFAULT_DB_VERSION);
    }

    //数据库第一次被创建时onCreate会被调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        //建张表  3个字段  ，_id为主键 自增长
        db.execSQL("create table tb_music (" +
                "_id Integer primary key autoincrement," +
                "title TEXT," +
                "artist TEXT," +
                "album TEXT," +
                "url TEXT," +
                "duration Integer," +
                "size Integer," +
                "album_artist TEXT" +
                "is_alarm Integer," +
                "is_music Integer," +
                "bookmark Integer," +
                "year Integer," +
                "track Integer" +
                "lrc TEXT" +
                "isDelete Integer" +
                "date_add Integer" +
                "date_modified Integer" +
                "display_name TEXT" +
                "mine_type TEXT" +
                ")");
    }

    //如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
    //数据库版本提升的时候调用,比如软件升级时，要在原本的数据库一个表里面新增加一个字段，或者增加一张表，就在这里面修改数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("ALTER TABLE tb_music ADD COLUMN other STRING");
    }


}
