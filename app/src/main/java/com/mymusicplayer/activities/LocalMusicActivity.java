package com.mymusicplayer.activities;

import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.astuetz.PagerSlidingTabStrip;
import com.mymusicplayer.R;
import com.mymusicplayer.helper.utils.L;
import com.mymusicplayer.ui.adapters.TitlePagerAdapter;
import com.mymusicplayer.ui.fragments.localmusic.MusicFolderFragment;
import com.mymusicplayer.ui.fragments.localmusic.MusicArtistFragment;
import com.mymusicplayer.ui.fragments.localmusic.MusicAlbumFragment;
import com.mymusicplayer.ui.fragments.localmusic.SingleMusicFragment;

import java.lang.reflect.Method;

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
        initToolBar();

    }


    private void initToolBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);// 给左上角图标的左边加上一个返回的图标
        actionBar.setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.drawable.actionbar_check);
    }

    private void initPages() {
        TitlePagerAdapter titlePagerAdapter = new TitlePagerAdapter(getSupportFragmentManager());
        final int icons[] = {R.drawable.tab_icn_song, R.drawable.tab_icn_artist, R.drawable.tab_icn_alb, R.drawable.tab_icn_folder};
        titlePagerAdapter.setTitles(getResources().getStringArray(R.array.local_music_titles));
        titlePagerAdapter.setIcons(icons);
        titlePagerAdapter.addFragment(new SingleMusicFragment());
        titlePagerAdapter.addFragment(new MusicArtistFragment());
        titlePagerAdapter.addFragment(new MusicAlbumFragment());
        titlePagerAdapter.addFragment(new MusicFolderFragment());
        pager.setAdapter(titlePagerAdapter);
        //设置ViewPager初始化时哪个Fragment页面
        pager.setCurrentItem(0);
        //设置ViewPager初始化加载的Fragment页面数,如果此处不指定，默认只加载相邻页
        pager.setOffscreenPageLimit(1);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_music, menu);
        return true;
    }

    /**
     * 利用反射让隐藏在Overflow中的MenuItem显示Icon图标
     * @param featureId
     * @param menu
     */
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        L.e("onMenuOpened : " + featureId);
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                finish();
                return true;

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
