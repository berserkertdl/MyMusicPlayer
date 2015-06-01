package com.mymusicplayer.helper.vo;

/**
 * Created by Administrator on 2015/5/28 0028.
 */
public class SortEntity {
    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母
    private int order;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}