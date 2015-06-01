package com.mymusicplayer.helper.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.mymusicplayer.helper.vo.MusicEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class MusicUtil {

    private static MusicUtil instance = new MusicUtil();

    private MusicUtil() {

    }

    public static MusicUtil getInstance(){
        return instance;
    }

    public static List<MusicEntity> scanAllMusic(Context context) {
        List<MusicEntity> musics = new ArrayList<MusicEntity>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        bindMusic(musics,cursor);
        Toast.makeText(context,"内部储存："+musics.size(),Toast.LENGTH_SHORT).show();
        if(isSDcardEnable()){
            cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
            bindMusic(musics,cursor);
            Toast.makeText(context,"外部部储存："+musics.size(),Toast.LENGTH_SHORT).show();
        }else{

        }
        cursor.close();
        return musics;
    }

    public static boolean isSDcardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private static List<MusicEntity> bindMusic(List<MusicEntity> musiclist,Cursor cursor){
        if(musiclist==null || cursor ==null){
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



}
