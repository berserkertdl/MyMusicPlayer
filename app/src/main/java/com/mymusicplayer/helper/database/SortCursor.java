package com.mymusicplayer.helper.database;

import android.database.Cursor;
import android.database.CursorWrapper;


import com.mymusicplayer.helper.utils.CharacterParser;
import com.mymusicplayer.helper.utils.PinyinComparator;
import com.mymusicplayer.helper.vo.SortEntity;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/5/28 0028.
 */
public class SortCursor extends CursorWrapper {

    private Cursor mCursor;
    private List<SortEntity> sortList = new ArrayList<SortEntity>();
    private int mPos = 0;
    //直接初始化,加快比较速度,在G3上从3s->0.2s
    @SuppressWarnings("rawtypes")
    private Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);

    @SuppressWarnings("unchecked")
    private PinyinComparator comparator = new PinyinComparator();
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;

    public List<SortEntity> getSortList() {
        return sortList;
    }




    public SortCursor(Cursor cursor, String columnName) {
        super(cursor);
        // TODO Auto-generated constructor stub
        mCursor = cursor;
        characterParser = CharacterParser.getInstance();
        if (mCursor != null && mCursor.getCount() > 0) {
            int i = 0;
            int column = cursor.getColumnIndexOrThrow(columnName);
            for (mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext(), i++) {
                SortEntity sortKey = new SortEntity();
                String name = cursor.getString(column);
                sortKey.setName(name);
                //汉字转换成拼音
                String pinyin = characterParser.getSelling(name);
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if(sortString.matches("[A-Z]")){
                    sortKey.setSortLetters(sortString.toUpperCase());
                }else{
                    sortKey.setSortLetters("#");
                }
                sortKey.setOrder(i);
                sortList.add(sortKey);
            }
        }
        //排序
        Collections.sort(sortList, comparator);
    }

    public boolean moveToPosition(int position) {
        if (position >= 0 && position < sortList.size()) {
            mPos = position;
            int order = sortList.get(position).getOrder();
            return mCursor.moveToPosition(order);
        }
        if (position < 0) {
            mPos = -1;
        }
        if (position >= sortList.size()) {
            mPos = sortList.size();
        }
        return mCursor.moveToPosition(position);
    }

    public boolean moveToFirst() {
        return moveToPosition(0);
    }

    public boolean moveToLast() {
        return moveToPosition(getCount() - 1);
    }

    public boolean moveToNext() {
        return moveToPosition(mPos + 1);
    }

    public boolean moveToPrevious() {
        return moveToPosition(mPos - 1);
    }

    public boolean move(int offset) {
        return moveToPosition(mPos + offset);
    }

    public int getPosition() {
        return mPos;
    }


}
