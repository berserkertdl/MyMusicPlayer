package com.mymusicplayer.ui.fragments.localmusic;

import android.app.Activity;
import android.content.ContentResolver;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.mymusicplayer.helper.database.DBManager;
import com.mymusicplayer.helper.database.SortCursor;
import com.mymusicplayer.sliderbar.SideBar;
import com.mymusicplayer.ui.adapters.SortCursorAdpter;
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
public class MusicSingerFragment extends Fragment implements AbsListView.OnItemClickListener {

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

    private SideBar sideBar;

    // TODO: Rename and change types of parameters
    public static MusicSingerFragment newInstance(String param1, String param2) {
        MusicSingerFragment fragment = new MusicSingerFragment();
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
    public MusicSingerFragment() {
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

    private ListView localMusicSingerList;
    private SortCursorAdpter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.local_music_singer, container, false);

        localMusicSingerList = (ListView) view.findViewById(R.id.local_music_singer);
        sideBar = (SideBar) view.findViewById(R.id.sideBar);

        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                Log.e("OnTouchingLetterChanged", s);
                //该字母首次出现的位置
                int position = cursorAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    localMusicSingerList.setSelection(position);
                }
            }

        });
        localMusicSingerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager =  getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.layout.fragment_artist_music,new ArtistMusicFragment());
//                transaction.addToBackStack("right");
                transaction.commit();

            }

        });

        Log.e("MusicSingerFragMent", "onCreate-process");
//        ContentResolver contentResolver = getActivity().getContentResolver();
//        SortCursor cursor = new SortCursor(contentResolver.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, new String[]{BaseColumns._ID, MediaStore.Audio.ArtistColumns.ARTIST, MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS}, null, null, null),MediaStore.Audio.AudioColumns.ARTIST);
        SortCursor cursor = new SortCursor(DBManager.getAllLocalArtist(), MediaStore.Audio.AudioColumns.ARTIST);
        cursorAdapter = new SortCursorAdpter(getActivity(), R.layout.local_music_list_item, cursor,
                new String[]{MediaStore.Audio.AudioColumns.ARTIST, MediaStore.Audio.ArtistColumns.NUMBER_OF_TRACKS}, new int[]{R.id.title, R.id.subTitle});
        getActivity().startManagingCursor(cursor);
        localMusicSingerList.setAdapter(cursorAdapter);
        Log.e("MusicSingerFragMent", "onCreate-OK");
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
//            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
