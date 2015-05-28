package com.mymusicplayer.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.mymusicplayer.R;
import com.mymusicplayer.helper.database.SortCursor;
import com.mymusicplayer.helper.vo.SortEntry;

/**
 * Created by Administrator on 2015/5/26 0026.
 */
public class SortCursorAdpter extends SimpleCursorAdapter {

    private SortCursor mSortCursor;

    public SortCursorAdpter(Context context, int layout, SortCursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.mSortCursor = c;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final SortEntry mContent = mSortCursor.getSortList().get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.local_music_list_item, null);
//           view = newView(mContext, mCursor, parent);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(position == getPositionForSection(section)){
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(mContent.getSortLetters());
        }else{
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tvTitle.setText(mContent.getName());
        return view;
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
        TextView tvTitle;
    }


}
