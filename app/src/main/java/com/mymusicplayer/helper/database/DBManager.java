package com.mymusicplayer.helper.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.mymusicplayer.helper.vo.MusicEntity;

import java.util.List;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class DBManager {

    private static DBOpenHelper hepler;
    private static SQLiteDatabase db;

    private static boolean initialized = false;

    private final static String TABLE_NAME = "tb_music";

    private Context mContext;


    public DBManager(Context context) {
        this.mContext = context;
        hepler = new DBOpenHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = hepler.getWritableDatabase();
    }

    private static void isInit() throws Exception {
        if (!initialized) {
            throw new Exception("BDManager must be init first");
        }
    }

    public static void init(Context context) {
        hepler = new DBOpenHelper(context);
        // 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
        // mFactory);
        // 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = hepler.getWritableDatabase();
        initialized = true;
    }

//    public static void adds(List<MusicEntity> musics) {
//        isInit();
//        db.beginTransaction();
//        for (MusicEntity music : musics) {
//            db.execSQL("insert into " + TABLE_NAME, new Object[]{music.getTitle(),
//                    music.getArtist(),
//                    music.getAlbum(),
//                    music.getUrl(),
//                    music.getDuration(),
//                    music.getSize(),
//                    music.getAlbum_artist(),
//                    music.getIs_alarm(),
//                    music.getIs_music(),
//                    music.getDisplay_name(),
//                    music.getBookmark(),
//                    music.getYear(),
//                    music.getTrack(),
//                    music.getDate_add(),
//                    music.getDate_modified(),
//                    music.getMine_type(),
//                    music.getIs_delete(),
//            });
//        }
//
//    }

    public static void adds(Context context) {

        ContentResolver contentResolver = context.getContentResolver();
        insert(contentResolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, "is_music=1 and duration > 60000", null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER), TABLE_NAME);
        insert(contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, "is_music=1 and duration > 60000", null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER), TABLE_NAME);
    }

    private static void insert(Cursor cursor, String table_name) {
        if (cursor == null || cursor.getColumnCount() == 0) {
            return;
        }
        Object[] objs;
        db.beginTransaction();
        int columCount = cursor.getColumnCount();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < columCount; i++) {
            if (i == columCount - 1) {
                stringBuffer.append("?");
                break;
            }
            stringBuffer.append("?,");
        }
        try {
            while (cursor.moveToNext()) {
                objs = new Object[columCount];
                for (int i = 0; i < columCount; i++) {
                    int type = cursor.getType(i);
                    if (type == Cursor.FIELD_TYPE_INTEGER) {
                        objs[i] = cursor.getInt(i);
                    } else if (type == Cursor.FIELD_TYPE_STRING) {
                        objs[i] = cursor.getString(i);
                    }
                }
                db.execSQL("insert into " + table_name + " values( " + stringBuffer + ",0 )", objs);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }


    }

    public static Cursor getAllAudioMedio() {
        return db.rawQuery("select * from " + TABLE_NAME + " where is_delete = 0", null);
    }

    public static int getAllLocalAudioMedioCount() {
        Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            return cursor.getInt(0);
        }
        return 0;
    }

    public static Cursor getAllLocalArtist() {
        return db.rawQuery("SELECT count(*) number_of_tracks  ,* FROM " + TABLE_NAME + " where is_delete = 0 group by artist_id ", null);
    }

    public static Cursor getLocalMusicByArtist(int artist_id) {
        return db.rawQuery("select * from " + TABLE_NAME + " where is_delete = 0 and artist_id = ?", new String[]{artist_id + ""});
    }

    public static Cursor getLocalMusicByAlbum(int album_id) {
        return db.rawQuery("select * from " + TABLE_NAME + " where is_delete = 0 and album_id = ?", new String[]{album_id + ""});
    }

    public static Cursor getLocalMusicAlbums() {
        return db.rawQuery("SELECT count(*) numsongs  ,* FROM " + TABLE_NAME + " where is_delete = 0 group by album_id ", null);
    }


    public static void destory() {
        if (db != null) {
            db.close();
            db = null;
        }

        if (hepler != null) {
            hepler.close();
            hepler = null;
        }
        initialized = false;

    }


}
