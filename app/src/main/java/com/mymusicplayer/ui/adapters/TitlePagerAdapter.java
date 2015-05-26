package com.mymusicplayer.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/5/22 0022.
 */
public class TitlePagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

    private String[] TITLES;
    private int[] icons;

    private List<Fragment> pages = new ArrayList<Fragment>();

    public TitlePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment){
        if(fragment==null){
            Log.e("PagerAdapter","fragment is null");
            return;
        }
        pages.add(fragment);
        notifyDataSetChanged();
    }

    public void setTitles(String[] titles){
        this.TITLES = titles;
    }

    public void setIcons(int[] icons){
        this.icons = icons;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public Fragment getItem(int position) {
        return pages.get(position);
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public int getPageIconResId(int position) {
        return icons[position];
    }
}
