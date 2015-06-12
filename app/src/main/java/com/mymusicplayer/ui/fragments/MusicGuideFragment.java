package com.mymusicplayer.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mymusicplayer.PullToRefreshView;
import com.mymusicplayer.R;
import com.mymusicplayer.activities.LocalMusicActivity;
import com.mymusicplayer.helper.DataSource;
import com.mymusicplayer.helper.database.DBManager;
import com.mymusicplayer.helper.utils.DBThread;
import com.mymusicplayer.ui.adapters.MusicGudieAdpter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MusicGuideFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class MusicGuideFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private List<Object[]> musicTypes;
    private ListView index_music_list;
    private PullToRefreshView pullToRefreshView;
    private DBManager dbManager;


    public MusicGuideFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    private SimpleAdapter gudieAdpter;

    private List<HashMap<String,Object>> guideList;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        guideList = (List<HashMap<String,Object>>) DataSource.getMusicGuides(getActivity(),false);
        gudieAdpter = new SimpleAdapter(getActivity(), guideList,R.layout.index_music_list_item,new String []{"item_icon","item_title","item_second_title"},new int []{R.id.item_icon,R.id.item_title,R.id.item_second_title,});
        if(index_music_list!=null){
            index_music_list.setAdapter(gudieAdpter);
        }
        index_music_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(), LocalMusicActivity.class));
            }
        });


        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        guideList = (List<HashMap<String,Object>>) DataSource.getMusicGuides(getActivity(),true);
                        gudieAdpter.notifyDataSetChanged();
                        pullToRefreshView.setRefreshing(false);
                    }
                },1000);
            }
        });
        pullToRefreshView.setRefreshing(true,true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.index_music, container, false);
        index_music_list =(ListView) root.findViewById(R.id.local_music_list);
        pullToRefreshView = (PullToRefreshView) root.findViewById(R.id.pull_refresh_view);
        return root;

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
        public void onFragmentInteraction(Uri uri);
    }

}
