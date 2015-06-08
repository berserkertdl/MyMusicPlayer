package com.mymusicplayer.ui.fragments.localmusic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mymusicplayer.R;
import com.mymusicplayer.activities.LocalMusicListActivity;
import com.mymusicplayer.helper.database.DBManager;
import com.mymusicplayer.helper.database.SortCursor;
import com.mymusicplayer.sliderbar.SideBar;
import com.mymusicplayer.ui.adapters.SortCursorAdpter;
import com.mymusicplayer.ui.fragments.BaseFragment;
import com.mymusicplayer.ui.fragments.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class MusicAlbumFragment extends BaseFragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static MusicAlbumFragment newInstance(String param1, String param2) {
        MusicAlbumFragment fragment = new MusicAlbumFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MusicAlbumFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    private ListView localMusicSpecialList;
    private SideBar sideBar;
    private SortCursorAdpter cursorAdapter;

    private Handler handler;

    private View mFragmentView;

    private int mCurItem = -1;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("MusicAlbumFragMent", "onCreateView");

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.local_music_album, container, false);
            localMusicSpecialList = (ListView)mFragmentView.findViewById(R.id.local_music_special);
            sideBar = (SideBar) mFragmentView.findViewById(R.id.sideBar);
            handler = new AlbumHandler();
            localMusicSpecialList.setOnItemClickListener(this);

            sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
                @Override
                public void onTouchingLetterChanged(String s) {
                    //该字母首次出现的位置
                    int position = cursorAdapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        localMusicSpecialList.setSelection(position);
                    }
                }

            });
            isPrepared = true;
            lazyLoad();
        }
        return mFragmentView;

    }

    @Override
    protected void lazyLoad() {
        Log.e("MusicAlbumFragMent", "lazyLoading");

        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        Log.e("MusicAlbumFragMent", "lazyLoad");

        mHasLoadedOnce = true;

        new Thread(){
            @Override
            public void run() {
                SortCursor cursor = new SortCursor(DBManager.getLocalMusicAlbums(), MediaStore.Audio.AudioColumns.ALBUM);
                Message message = handler.obtainMessage();
                message.obj = cursor;
                handler.sendMessage(message);
            }
        }.start();



    }

    class AlbumHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SortCursor cursor = (SortCursor)msg.obj;
            cursorAdapter = new SortCursorAdpter(getActivity(),R.layout.local_music_list_item,cursor,
                    new String[]{MediaStore.Audio.AlbumColumns.ALBUM, MediaStore.Audio.AlbumColumns.NUMBER_OF_SONGS, MediaStore.Audio.AlbumColumns.ARTIST}, new int [] {R.id.icon,R.id.title,R.id.subTitle,R.id.midTitle});
            getActivity().startManagingCursor(cursor);
            localMusicSpecialList.setAdapter(cursorAdapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }

        SortCursor cursor = (SortCursor) parent.getItemAtPosition(position);
        int album_id = cursor.getInt(cursor.getColumnIndexOrThrow("album_id"));
        Intent intent = new Intent(getActivity(), LocalMusicListActivity.class);
        intent.putExtra("album_id", album_id);
        startActivity(intent);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
