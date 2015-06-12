package com.mymusicplayer.helper.database;

import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.mymusicplayer.helper.utils.L;
import com.mymusicplayer.helper.vo.MusicEntity;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class DBManager {

    private static final String TAG = DBManager.class.getSimpleName();

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
                db.execSQL("insert into " + table_name + " values( " + stringBuffer + ",0,null )", objs);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            L.e(TAG, e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    public static Cursor getAllAudioMedio() {
        return db.rawQuery("select * from " + TABLE_NAME + " where is_delete = 0", null);
    }

    public static int getAllLocalAudioMedioCount() {
        Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME+ " where is_delete = 0", null);
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

    /**
     * 更新最近播放的歌曲时间
     * */
    public static void updateMusicLastPlayTimeById(int song_id){
        db.beginTransaction();
        try{
            db.execSQL("update " + TABLE_NAME + " set last_play_time = datetime('now','localtime') where _id=?",new Object[]{song_id});
            db.setTransactionSuccessful();
        }catch  (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * 最近播放的100首
     * */
    public static Cursor lateLyMusicPlayList(){
        return db.rawQuery("select * from " + TABLE_NAME + " where is_delete = 0 and last_play_time<>0 order by last_play_time desc limit 100",null);
    }

    /**
     * 最近播放的歌曲数
     * */
    public static int getLateLyMusicCount(){
        Cursor cursor = db.rawQuery("select count(*) from " + TABLE_NAME + " where is_delete = 0 and last_play_time<>0", null);
        while (cursor.moveToNext()) {
            return cursor.getInt(0);
        }
        return 0;
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
