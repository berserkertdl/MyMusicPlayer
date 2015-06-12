package com.mymusicplayer.ui.fragments.localmusic;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mymusicplayer.R;
import com.mymusicplayer.helper.database.DBManager;
import com.mymusicplayer.helper.database.SortCursor;
import com.mymusicplayer.helper.utils.MusicUtil;
import com.mymusicplayer.services.PlayerService;
import com.mymusicplayer.sliderbar.SideBar;
import com.mymusicplayer.ui.adapters.SortCursorAdpter;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocalMusicActivityFragment extends Fragment {

    public LocalMusicActivityFragment() {
    }

    private ListView localMusicList;
    private SideBar sideBar;
    private SortCursorAdpter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_artist_music, container, false);

        Intent intent = getActivity().getIntent();
        int artist_id = intent.getIntExtra("artist_id", 0);
        int album_id = intent.getIntExtra("album_id", 0);
        localMusicList = (ListView) view.findViewById(R.id.local_artist_music);
        sideBar = (SideBar) view.findViewById(R.id.sideBar);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.e("OnTouchingLetterChanged", s);
                //该字母首次出现的位置
                int position = cursorAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    localMusicList.setSelection(position);
                }
            }
        });

        SortCursor cursor = null;
        if (artist_id != 0) {
            cursor = new SortCursor(DBManager.getLocalMusicByArtist(artist_id), MediaStore.Audio.AudioColumns.TITLE);
        } else if (album_id != 0) {
            cursor = new SortCursor(DBManager.getLocalMusicByAlbum(album_id), MediaStore.Audio.AudioColumns.TITLE);
        }else{
            cursor = new SortCursor(DBManager.lateLyMusicPlayList(), MediaStore.Audio.AudioColumns.TITLE);
        }
        cursorAdapter = new SortCursorAdpter(getActivity(), R.layout.local_music_list_item, cursor,
                new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ALBUM}, new int[]{R.id.title, R.id.subTitle, R.id.midTitle});
        getActivity().startManagingCursor(cursor);
        localMusicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicUtil.playMusic(getActivity(), (SortCursor) parent.getItemAtPosition(position));
            }
        });
        localMusicList.setAdapter(cursorAdapter);
        return view;
    }
}
