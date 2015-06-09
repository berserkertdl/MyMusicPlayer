package com.mymusicplayer.ui.fragments.localmusic;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mymusicplayer.R;

import com.mymusicplayer.helper.database.DBManager;
import com.mymusicplayer.helper.database.SortCursor;
import com.mymusicplayer.helper.utils.MusicUtil;
import com.mymusicplayer.helper.vo.MusicEntity;
import com.mymusicplayer.services.PlayerService;
import com.mymusicplayer.sliderbar.SideBar;
import com.mymusicplayer.ui.adapters.SortCursorAdpter;
import com.mymusicplayer.ui.fragments.BaseFragment;
import com.mymusicplayer.ui.fragments.dummy.DummyContent;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class SingleMusicFragment extends BaseFragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAGMENT_INDEX = "fragment_index";

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


    // TODO: Rename and change types of parameters
    public static SingleMusicFragment newInstance(int index) {
        SingleMusicFragment fragment = new SingleMusicFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SingleMusicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurItem = getArguments().getInt(FRAGMENT_INDEX);
        }
    }


    private View mFragmentView;
    private SortCursorAdpter cursorAdapter = null;
    private SortCursor cursor = null;
    private Handler handler;
    private ListAdapter mAdapter;
    private ListView localMusicList;
    private int mCurItem = -1;
    /**
     *
     */
    private boolean isPrepared;
    /**
     *
     */
    private boolean mHasLoadedOnce;
    private SideBar sideBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("SingMusicFragMent", "onCreateView");

        if (mFragmentView == null) {
            mFragmentView = inflater.inflate(R.layout.local_single_music, container, false);
            localMusicList = (ListView) mFragmentView.findViewById(R.id.local_music_list);
            sideBar = (SideBar) mFragmentView.findViewById(R.id.sideBar);
            handler = new SingleMusicHandler();
            localMusicList.setOnItemClickListener(this);
            sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
                @Override
                public void onTouchingLetterChanged(String s) {
                    Log.e("OnTouchingLetterChanged", s);
                    //
                    int position = cursorAdapter.getPositionForSection(s.charAt(0));
                    if (position != -1) {
                        localMusicList.setSelection(position);
                    }
                }

            });
            isPrepared = true;
            lazyLoad();
        }
        //
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null) {
            parent.removeView(mFragmentView);
        }
        return mFragmentView;

    }

    @Override
    protected void lazyLoad() {

        Log.e("SingMusicFragMent", "lazyLoading");
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        Log.e("SingMusicFragMent", "lazyLoad");
        mHasLoadedOnce = true;
        new Thread() {
            @Override
            public void run() {
                SortCursor cursor = new SortCursor(DBManager.getAllAudioMedio(), MediaStore.Audio.AudioColumns.TITLE);
                Message message = handler.obtainMessage();
                message.obj = cursor;
                handler.sendMessage(message);
            }

        }.start();

    }


    class SingleMusicHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            SortCursor cursor = (SortCursor) msg.obj;
            cursorAdapter = new SortCursorAdpter(getActivity(), R.layout.local_music_list_item, cursor,
                    new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.AudioColumns.ALBUM}, new int[]{R.id.title, R.id.subTitle, R.id.midTitle});
            getActivity().startManagingCursor(cursor);
            localMusicList.setAdapter(cursorAdapter);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }

        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        String url = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        Intent intent = new Intent(getActivity(), PlayerService.class);
        intent.putExtra("url", url);
        getActivity().startService(intent);
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
