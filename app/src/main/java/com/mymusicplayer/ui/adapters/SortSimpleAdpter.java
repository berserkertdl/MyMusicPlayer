package com.mymusicplayer.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mymusicplayer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/6/1 0001.
 */
public class SortSimpleAdpter extends BaseAdapter {

    private List list;
    private Context mContext;
    private int mLayout;
    private String[] mFrom;
    private int[] mTo;

    public SortSimpleAdpter(List list, Context mContext, int mLayout, String[] from, int[] to) {
        this.list = list;
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.mFrom = from;
        this.mTo = to;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(list==null||list.size()<=0){
            return convertView;
        }

        if(convertView ==null){
            convertView = LayoutInflater.from(mContext).inflate(mLayout,parent);

        }


        return convertView;
    }

    public void bindView(View view,ViewHolder viewHolder) {
        final int count = mTo.length;
        final int[] to = mTo;
        viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
        viewHolder.view = view.findViewById(R.id.local_music_list_catalog_frame);
        for (int i = 0; i < count; i++) {
            final View v = view.findViewById(to[i]);
            if (v != null) {
                viewHolder.views.add(v);
            }
        }

        view.setTag(viewHolder);
    }

    public void bindData(ViewHolder viewHolder, Cursor cursor){
        if(viewHolder==null||viewHolder.views==null){
            return;
        }
        final String[] from = mFrom;
        int i = 0;
        for(View v : viewHolder.views){
            String text = cursor.getString(0);
//            String text = cursor.getString(from[i++]);
            if (v instanceof TextView) {
                setViewText((TextView) v, text);
            } else if (v instanceof ImageView) {
                setViewImage((ImageView) v, text);
            } else {
                throw new IllegalStateException(v.getClass().getName() + " is not a " +
                        " view that can be bounds by this SimpleCursorAdapter");
            }
        }
    }

    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    /**
     * Called by bindView() to set the text for a TextView but only if
     * there is no existing ViewBinder or if the existing ViewBinder cannot
     * handle binding to an TextView.
     *
     * Intended to be overridden by Adapters that need to filter strings
     * retrieved from the database.
     *
     * @param v TextView to receive text
     * @param text the text to be set for the TextView
     */
    public void setViewText(TextView v, String text) {
        v.setText(text);
    }



    final static class ViewHolder {
        TextView tvLetter;
        View view;
        List<View> views = new ArrayList<View>();
    }




}
