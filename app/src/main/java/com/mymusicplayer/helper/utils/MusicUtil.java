package com.mymusicplayer.helper.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.widget.Toast;

import com.mymusicplayer.R;
import com.mymusicplayer.helper.vo.MusicEntity;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class MusicUtil {

    private static MusicUtil instance = new MusicUtil();

    private MusicUtil() {

    }

    public static MusicUtil getInstance() {
        return instance;
    }

    public static List<MusicEntity> scanAllMusic(Context context) {
        List<MusicEntity> musics = new ArrayList<MusicEntity>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        bindMusic(musics, cursor);
        Toast.makeText(context, "内部储存：" + musics.size(), Toast.LENGTH_SHORT).show();
        if (isSDcardEnable()) {
            cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            bindMusic(musics, cursor);
            Toast.makeText(context, "外部部储存：" + musics.size(), Toast.LENGTH_SHORT).show();
        } else {

        }
        cursor.close();
        return musics;
    }

    public static boolean isSDcardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static List<MusicEntity> bindMusic(List<MusicEntity> musiclist, Cursor cursor) {
        if (musiclist == null || cursor == null) {
            return musiclist;
        }
        while (cursor.moveToNext()) {
            // 歌曲ID
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media._ID));
            // 歌曲名称
            String title = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.TITLE));
            // 歌曲专辑名
            String album = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ALBUM));
            // 歌曲歌手名
            String artist = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.ARTIST));
            // 歌曲文件路径
            String url = cursor.getString(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DATA));
            // 歌曲时长
            int duration = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.DURATION));
            // 歌曲大小
            int size = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.Audio.Media.SIZE));

            MusicEntity musicInfo = new MusicEntity();
            musicInfo.setId(id);
            musicInfo.setTitle(title);
            musicInfo.setAlbum(album);
            musicInfo.setArtist(artist);
            musicInfo.setUrl(url);
            musicInfo.setSize(size);
            musicInfo.setDuration(duration);
            musiclist.add(musicInfo);
        }
        return musiclist;
    }


    public static Bitmap getArtwork(Context context, int song_id, int album_id, String data/**,int defalutImage*/) {

        if(data!=null&&!"".equals(data)){
            if(data.toLowerCase().indexOf("/storage/sdcard")!=-1){
                defaultSrc = "external";
            }
        }
        
        if (album_id < 0) {
            // This is something that is not in the database, so get the album art directly
            // from the file.
            if (song_id >= 0) {
                Bitmap bm = getArtworkFromFile(context, song_id, -1, data);
                if (bm != null) {
                    return bm;
                }
            }
            //设置默认图片
//            if (defalutImage!=0) {
//                return getDefaultArtwork(context,defalutImage);
//            }
            return null;
        }
        ContentResolver res = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(sArtworkUri(defaultSrc), album_id);
        if (uri != null) {
            InputStream in = null;
            try {
                in = res.openInputStream(uri);
                return BitmapFactory.decodeStream(in, null, sBitmapOptions);
            } catch (FileNotFoundException ex) {
                // The album art thumbnail does not actually exist. Maybe the user deleted it, or
                // maybe it never existed to begin with.
                Bitmap bm = getArtworkFromFile(context, song_id, album_id, data);
                if (bm != null) {
                    if (bm.getConfig() == null) {
                        bm = bm.copy(Bitmap.Config.RGB_565, false);
                        //设置默认图片
//                        if (bm == null && defalutImage!=0) {
//                            return getDefaultArtwork(context,defalutImage);
//                        }
                    }
//                } else if (defalutImage!=0) {
//                    bm = getDefaultArtwork(context,defalutImage);
                }
                return bm;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException ex) {
                }
            }
        }

        return null;
    }

    private static Bitmap getArtworkFromFile(Context context, long songid, long albumid, String data) {
        Bitmap bm = null;
        byte[] art = null;
        String path = null;
        if (albumid < 0 && songid < 0) {
            throw new IllegalArgumentException("Must specify an album or a song id");
        }

        try {
            if (albumid < 0) {
                Uri uri = Uri.parse("content://media/"+defaultSrc+"/audio/media/" + songid + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            } else {
                Uri uri = ContentUris.withAppendedId(sArtworkUri(defaultSrc), albumid);
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }
        } catch (FileNotFoundException ex) {

        }
        if (bm != null) {
            mCachedBit = bm;
        }
        return bm;
    }


    private static Bitmap getDefaultArtwork(Context context,int defalutImage) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(
                context.getResources().openRawResource(defalutImage), null, opts);
    }

    private static Uri sArtworkUri(String src){
        return  Uri.parse("content://media/"+src+"/audio/albumart");
    }

    private static final BitmapFactory.Options sBitmapOptions = new BitmapFactory.Options();
    private static Bitmap mCachedBit = null;
    private static String defaultSrc = "internal";
//    private static Uri sArtworkUri = Uri.parse("content://media/"+defaultSrc+"/audio/albumart");


}
