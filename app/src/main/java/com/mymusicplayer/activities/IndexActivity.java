package com.mymusicplayer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.contextmenu.ContextMenuDialogFragment;
import com.contextmenu.MenuParams;
import com.mymusicplayer.PullToRefreshView;
import com.mymusicplayer.R;
import com.mymusicplayer.helper.DataSource;
import com.mymusicplayer.helper.database.DBManager;
import com.mymusicplayer.helper.utils.AppUtils;
import com.mymusicplayer.helper.utils.DBThread;
import com.mymusicplayer.helper.utils.L;
import com.mymusicplayer.services.PlayerService;
import com.mymusicplayer.ui.adapters.PagerAdapter;
import com.mymusicplayer.ui.fragments.DiscoverFragment;
import com.mymusicplayer.ui.fragments.FirendsFragment;
import com.mymusicplayer.ui.fragments.MusicGuideFragment;

import java.util.List;


public class IndexActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link PagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ImageView discover_bar, music_bar, friends_bar, search_bar, menu_bar;

    private View discover_view, music_view, friends_view, rightDrawerLayout;

    private ListView drwaerMenuListView;

    private PullToRefreshView pullToRefreshView;

    private DialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final String TAG = IndexActivity.class.getSimpleName();

    private List<View> views;

    private LayoutInflater lf;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.add(this);
        setContentView(R.layout.activity_index);
        initToolbar();
        initData();
        initDrawer();
        initComponet();
//        initMenuFragment();

    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // 實作 drawer toggle 並放入 toolbar
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        rightDrawerLayout = findViewById(R.id.drawer_view);
        drwaerMenuListView = (ListView) findViewById(R.id.drawer_menu_list);
        drwaerMenuListView.setAdapter(new SimpleAdapter(this,DataSource.getDrawerMenuObject(this),R.layout.drawer_menu_list_item,getResources().getStringArray(R.array.list_item_key),new int[]{R.id.menu_item_icon,R.id.menu_item_title}));
        drwaerMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    //exit
                    case 7:
                        AppUtils.finishProgram();
                        break;

                }

            }
        });
    }

    private void initToolbar() {

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false); //使左上角图标可点击
        actionBar.setDisplayHomeAsUpEnabled(false);// 给左上角图标的左边加上一个返回的图标
        actionBar.setDisplayShowTitleEnabled(false);

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        toolbarBottom.setNavigationIcon(R.drawable.default_disc_141);
        toolbarBottom.setTitle(R.string.action_title);
        toolbarBottom.setSubtitle(R.string.action_sub_title);

        toolbarBottom.inflateMenu(R.menu.menu_bottom);

        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                L.e("OnMenuItemClickListener: " + item.getItemId());
                switch (item.getItemId()) {
                    case R.id.action_play:
                        L.e("onMenuItemClick : " + item.isChecked());
                        item.setChecked(false);
                        item.setIcon(R.drawable.note_btn_pause_white);
                        Intent intent = new Intent(IndexActivity.this, PlayerService.class);
                        intent.putExtra("flag", 3);
                        startService(intent);
                        break;
                    // TODO: Other cases
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return true;
    }

    private void initComponet() {

        discover_bar = (ImageView) findViewById(R.id.actionBar_discover);
        music_bar = (ImageView) findViewById(R.id.actionBar_music);
        friends_bar = (ImageView) findViewById(R.id.actionBar_friends);
//        search_bar = (ImageButton) findViewById(R.id.actionBar_search);

        ActionBarButtonClickListener clickListener = new ActionBarButtonClickListener();
        discover_bar.setOnClickListener(clickListener);
        music_bar.setOnClickListener(clickListener);
        friends_bar.setOnClickListener(clickListener);

        PageChangListener pageChangeListener = new PageChangListener();
        mViewPager.setOnPageChangeListener(pageChangeListener);

    }


    /**
     * 初始化菜单
     */
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(DataSource.getMenuObjects(this));
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }


    private void initData() {
        DBManager.init(this);
        if (DBManager.getAllLocalAudioMedioCount() == 0) {
            new DBThread().new InitThread(this).start();
        }
        PagerAdapter fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(new DiscoverFragment());
        fragmentPagerAdapter.addFragment(new MusicGuideFragment());
        fragmentPagerAdapter.addFragment(new FirendsFragment());
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(fragmentPagerAdapter);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        L.e(TAG, "onOptionsItemSelected : item id-----" + item.getItemId());
        switch (item.getItemId()) {

            case R.id.context_menu:
                openRightLayout();
                /*if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 打开右侧抽屉
     */
    public void openRightLayout() {
        if (mDrawerLayout.isDrawerOpen(rightDrawerLayout)) {
            mDrawerLayout.closeDrawer(rightDrawerLayout);
        } else {
            mDrawerLayout.openDrawer(rightDrawerLayout);
        }
    }


    class PageChangListener implements ViewPager.OnPageChangeListener {

//        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                music_bar.setImageResource(R.drawable.actionbar_music_normal);
                friends_bar.setImageResource(R.drawable.actionbar_friends_normal);
                discover_bar.setImageResource(R.drawable.actionbar_discover_selected);
            } else if (position == 1) {
                friends_bar.setImageResource(R.drawable.actionbar_friends_normal);
                discover_bar.setImageResource(R.drawable.actionbar_discover_normal);
                music_bar.setImageResource(R.drawable.actionbar_music_selected);
            } else if (position == 2) {
                music_bar.setImageResource(R.drawable.actionbar_music_normal);
                discover_bar.setImageResource(R.drawable.actionbar_discover_normal);
                friends_bar.setImageResource(R.drawable.actionbar_friends_selected);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

    class ActionBarButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.actionBar_discover:
                    music_bar.setImageResource(R.drawable.actionbar_music_normal);
                    friends_bar.setImageResource(R.drawable.actionbar_friends_normal);
                    discover_bar.setImageResource(R.drawable.actionbar_discover_selected);
                    mViewPager.setCurrentItem(0);
                    break;

                case R.id.actionBar_music:
                    friends_bar.setImageResource(R.drawable.actionbar_friends_normal);
                    discover_bar.setImageResource(R.drawable.actionbar_discover_normal);
                    music_bar.setImageResource(R.drawable.actionbar_music_selected);
                    mViewPager.setCurrentItem(1);
                    break;

                case R.id.actionBar_friends:
                    music_bar.setImageResource(R.drawable.actionbar_music_normal);
                    discover_bar.setImageResource(R.drawable.actionbar_discover_normal);
                    friends_bar.setImageResource(R.drawable.actionbar_friends_selected);
                    mViewPager.setCurrentItem(2);
                    break;
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移出管理栈
        AppUtils.remove(this);

    }
}
