package com.mymusicplayer.activities;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.mymusicplayer.R;
import com.mymusicplayer.ui.adapters.TitlePagerAdapter;
import com.mymusicplayer.ui.fragments.localmusic.MusicFolderFragment;
import com.mymusicplayer.ui.fragments.localmusic.MusicSingerFragment;
import com.mymusicplayer.ui.fragments.localmusic.MusicSpecialFragment;
import com.mymusicplayer.ui.fragments.localmusic.SingleMusicFragment;

public class LocalMusicActivity extends ActionBarActivity {

    private final Handler handler = new Handler();

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.local_music);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

        initPages();
        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
//        pager.setPageMargin(pageMargin);
        tabs.setIndicatorColorResource(R.color.pagerTabIndicatorColor);
        tabs.setUnderlineColorResource(R.color.pagerTabUnderlineColorResource);
        tabs.setIndicatorHeight(6);
        tabs.setViewPager(pager);


    }

    private void initPages(){
        TitlePagerAdapter titlePagerAdapter = new TitlePagerAdapter(getSupportFragmentManager());
        final int icons [] = {R.drawable.tab_icn_song,R.drawable.tab_icn_artist,R.drawable.tab_icn_alb,R.drawable.tab_icn_folder};
        titlePagerAdapter.setTitles(getResources().getStringArray(R.array.local_music_titles));
        titlePagerAdapter.setIcons(icons);
        titlePagerAdapter.addFragment(new SingleMusicFragment());
        titlePagerAdapter.addFragment(new MusicSingerFragment());
        titlePagerAdapter.addFragment(new MusicSpecialFragment());
        titlePagerAdapter.addFragment(new MusicFolderFragment());
        pager.setAdapter(titlePagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_music, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
