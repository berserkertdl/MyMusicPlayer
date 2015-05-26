package com.mymusicplayer.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

/**
 * Created by Administrator on 2015/5/26 0026.
 */
public class ListCursorAdpter extends SimpleCursorAdapter {

    public ListCursorAdpter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);

    }

}
