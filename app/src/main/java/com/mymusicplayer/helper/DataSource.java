package com.mymusicplayer.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.contextmenu.MenuObject;
import com.mymusicplayer.R;
import com.mymusicplayer.helper.database.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/6/12 0012.
 */
public class DataSource {





    public static final List<MenuObject> getMenuObjects(Context context) {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("Send message");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("Like profile");
        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.drawable.icn_2);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Add to friends");
        BitmapDrawable bd = new BitmapDrawable(context.getResources(),
                BitmapFactory.decodeResource(context.getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("Add to favorites");
        addFav.setResource(R.drawable.icn_4);

        MenuObject block = new MenuObject("Block user");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }

    /**
     * 获取音乐列表分类
     * @param  context
     * @param isByDataBase 是否从数据库中获取
     * */
    public static List<? extends Map<String, ?>> getMusicGuides(Context context,boolean isByDataBase){

        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        String[] keys = new String []{"item_icon","item_title","item_second_title"};
        String[] values = context.getResources().getStringArray(R.array.music_guide_arr);
        //本地音乐
        HashMap<String,Object> item = new HashMap<String,Object>();
        item.put(keys[0],R.drawable.music_icn_local);
        item.put(keys[1], values[0]);
        if(isByDataBase){
            item.put(keys[2],"（"+DBManager.getAllLocalAudioMedioCount()+"）");
        }else{
            item.put(keys[2],"（0）");
        }
        list.add(item);

        //最近播放
        item = new HashMap<String,Object>();
        item.put(keys[0],R.drawable.music_icn_recent);
        item.put(keys[1],values[1]);
        if(isByDataBase){
            item.put(keys[2],"（"+DBManager.getLateLyMusicCount()+"）");
        }else{
            item.put(keys[2],"（0）");
        }
        list.add(item);

        item = new HashMap<String,Object>();
        item.put(keys[0],R.drawable.music_icn_dld);
        item.put(keys[1],values[2]);
        item.put(keys[2],"（0）");
        list.add(item);

        item = new HashMap<String,Object>();
        item.put(keys[0],R.drawable.music_icn_artist);
        item.put(keys[1],values[3]);
        item.put(keys[2],"（0）");
        list.add(item);

        item = new HashMap<String,Object>();
        item.put(keys[0],R.drawable.music_icn_dj);
        item.put(keys[1],values[4]);
        item.put(keys[2],"（0）");
        list.add(item);

        item = new HashMap<String,Object>();
        item.put(keys[0],R.drawable.music_icn_mv);
        item.put(keys[1],values[5]);
        item.put(keys[2],"（0）");
        list.add(item);
        return list;
    }



    /**
     *
     * 右侧抽屉菜单
     * */
    public static final List<? extends Map<String, ?>> getDrawerMenuObject(Context context){
        List<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        String[] menuitemTitles = context.getResources().getStringArray(R.array.drawer_menus);
        HashMap<String,Object> item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_msg);
        item.put("menu_item_title",menuitemTitles[0]);
        list.add(item);
        item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_level);
        item.put("menu_item_title",menuitemTitles[1]);
        list.add(item);
        item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_store);
        item.put("menu_item_title",menuitemTitles[2]);
        list.add(item);
        item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_cloud);
        item.put("menu_item_title",menuitemTitles[3]);
        list.add(item);
        item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_identify);
        item.put("menu_item_title",menuitemTitles[4]);
        list.add(item);
        item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_time);
        item.put("menu_item_title",menuitemTitles[5]);
        list.add(item);
        item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_set);
        item.put("menu_item_title",menuitemTitles[6]);
        list.add(item);
        item = new HashMap<String,Object>();
        item.put("menu_item_icon",R.drawable.topmenu_icn_exit);
        item.put("menu_item_title",menuitemTitles[7]);
        list.add(item);
        return list;
    }



}
