package com.mymusicplayer.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mymusicplayer.R;
import com.mymusicplayer.helper.database.SortCursor;
import com.mymusicplayer.helper.utils.MusicUtil;
import com.mymusicplayer.helper.vo.SortEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/26 0026.
 */
public class SortCursorAdpter extends SimpleCursorAdapter {

    private SortCursor mSortCursor;

    private int mLayout;

    public SortCursorAdpter(Context context, int layout, SortCursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.mSortCursor = c;
        this.mLayout = layout;
    }

    public SortCursor getmSortCursor() {
        return mSortCursor;
    }

    public void setmSortCursor(SortCursor mSortCursor) {
        this.mSortCursor = mSortCursor;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        ViewHolder viewHolder = null;
        final SortEntity mContent = mSortCursor.getSortList().get(position);
        TextView catalog,title,subTitle,midTitle;
        if (view == null) {
//            view = LayoutInflater.from(mContext).inflate(R.layout.local_music_list_item, null);
            view = LayoutInflater.from(mContext).inflate(mLayout, null);
            viewHolder = new ViewHolder();
            bindView(view,viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.view.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        }else{
            viewHolder.view.setVisibility(View.GONE);
        }

        bindData(viewHolder,mCursor);
        return view;
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
        final int[] from = mFrom;
        int i = 0;
        for(View v : viewHolder.views){
            if (v instanceof TextView) {
                setViewText((TextView) v, cursor.getString(from[i++]));
            } else if (v instanceof ImageView) {
                setViewImage((ImageView) v, cursor);
            } else {
                throw new IllegalStateException(v.getClass().getName() + " is not a " +
                        " view that can be bounds by this SimpleCursorAdapter");
            }
        }
    }

    public void setViewImage(ImageView v, Cursor cursor) {
        int song_id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        int album_id = cursor.getInt(cursor.getColumnIndexOrThrow("album_id"));
        String data = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
        Bitmap bm =  MusicUtil.getArtwork(mContext, song_id, album_id, data, R.drawable.default_artist_160);
        if(bm != null){
            Log.d("setViewImage", "bm is not null==========================");
            v.setImageBitmap(bm);
        }else{
            Log.d("setViewImage","bm is null============================");
        }

    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position){
        return mSortCursor.getSortList().get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section){
        for (int i = 0,len=getCount(); i < len; i++) {
            String sortStr =  mSortCursor.getSortList().get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    final static class ViewHolder {
        TextView tvLetter;
        View view;
        List<View> views = new ArrayList<View>();
    }


}
