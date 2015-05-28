package com.mymusicplayer.helper.utils;

import com.mymusicplayer.helper.vo.SortEntry;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/5/28 0028.
 */
public class PinyinComparator implements Comparator<SortEntry> {

    public int compare(SortEntry o1, SortEntry o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}