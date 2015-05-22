package com.mymusicplayer.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymusicplayer.R;

import java.util.List;

/**
 * Created by Administrator on 2015/5/22 0022.
 */
public class MusicGudieAdpter extends BaseAdapter{

    private List<Object[]> musicTypes;

    private LayoutInflater lf;
    private Context context;

    public MusicGudieAdpter(Context context) {
        this.context = context;
        if(this.context==null){
            return;
        }
        lf = LayoutInflater.from(context);

    }

    public MusicGudieAdpter(Context context, List<Object[]> musicTypes) {
        this.context = context;
        this.musicTypes = musicTypes;
        if(this.context==null){
            return;
        }
        lf = LayoutInflater.from(context);

    }


    public void addItem(Object[] item){
        musicTypes.add(item);
    }

    @Override
    public int getCount() {
        return musicTypes.size();
    }
    @Override
    public Object getItem(int position) {
        return musicTypes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Object[] obj = musicTypes.get(position);
        convertView = lf.inflate(R.layout.index_music_list_item, parent, false);
        ImageView img = (ImageView) convertView.findViewById(R.id.item_icon);
        img.setBackgroundResource(Integer.parseInt(obj[0] + ""));
        TextView titleView = (TextView) convertView.findViewById(R.id.item_title);
        titleView.setText(obj[1] + "");
        TextView countView = (TextView) convertView.findViewById(R.id.item_count);
        countView.setText(obj[2] + "");
        return convertView;
    }
}
