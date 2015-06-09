package com.mymusicplayer.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.contextmenu.ContextMenuDialogFragment;
import com.contextmenu.MenuObject;
import com.contextmenu.MenuParams;
import com.mymusicplayer.PullToRefreshView;
import com.mymusicplayer.R;
import com.mymusicplayer.helper.database.DBManager;
import com.mymusicplayer.helper.utils.DBThread;
import com.mymusicplayer.ui.adapters.PagerAdapter;
import com.mymusicplayer.ui.fragments.DiscoverFragment;
import com.mymusicplayer.ui.fragments.FirendsFragment;
import com.mymusicplayer.ui.fragments.MusicGuideFragment;

import java.util.ArrayList;
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

    private View discover_view, music_view, friends_view;

    private List<View> views;

    private LayoutInflater lf;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号


    private PullToRefreshView pullToRefreshView;

    private DialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        fragmentManager = getSupportFragmentManager();
        initToolbar();
        initComponet();
        initMenuFragment();
        initData();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        TextView mToolBarTextView = (TextView) findViewById(R.id.text_view_toolbar_title);
        setSupportActionBar(mToolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false); //使左上角图标可点击
        actionBar.setDisplayHomeAsUpEnabled(false);// 给左上角图标的左边加上一个返回的图标
        actionBar.setDisplayShowTitleEnabled(false);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionBar_discover:
                        item.setChecked(true);
                        break;
                    case R.id.actionBar_music:
                        item.setChecked(true);
                        break;
                    case R.id.actionBar_friends:
                        item.setChecked(true);
                        break;
                }
                return true;
            }
        });

        Toolbar toolbarBottom = (Toolbar) findViewById(R.id.toolbar_bottom);
        toolbarBottom.setLogo(R.drawable.default_disc_141);
        toolbarBottom.setTitle(R.string.action_title);
        toolbarBottom.setSubtitle(R.string.action_sub_title);

        toolbarBottom.inflateMenu(R.menu.menu_bottom);

        toolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_play:
                        // TODO
                        item.setChecked(true);
                        break;
                    // TODO: Other cases
                }
                return true;
            }
        });


        // Inflate a menu to be displayed in the toolbar


//        mToolbar.setNavigationIcon(R.drawable.btn_back);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//        mToolBarTextView.setText("Samantha");
    }

    private void initComponet() {

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        discover_bar = (ImageView) findViewById(R.id.actionBar_discover);
        music_bar = (ImageView) findViewById(R.id.actionBar_music);
        friends_bar = (ImageView) findViewById(R.id.actionBar_friends);
        search_bar = (ImageButton) findViewById(R.id.actionBar_search);

        PageChangListener pageChangeListener = new PageChangListener();
        mViewPager.setOnPageChangeListener(pageChangeListener);

    }


    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("Send message");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("Like profile");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Add to friends");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
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

    private void initData() {
        DBManager.init(this);
        if(DBManager.getAllLocalAudioMedioCount()==0){
            new DBThread().new InitThread(this).start();
        }

        PagerAdapter fragmentPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        fragmentPagerAdapter.addFragment(new DiscoverFragment());
        fragmentPagerAdapter.addFragment(new MusicGuideFragment());
        fragmentPagerAdapter.addFragment(new FirendsFragment());
        mViewPager.setAdapter(fragmentPagerAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    class PageChangListener implements ViewPager.OnPageChangeListener {

//        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
//        int two = one * 2;// 页卡1 -> 页卡3 偏移量

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            if (position==0) {
//                Log.e("----------------", "actionBar_discover");
//                music_bar.setBackgroundResource(R.drawable.actionbar_music_normal);
//                friends_bar.setBackgroundResource(R.drawable.actionbar_friends_normal);
//                discover_bar.setBackgroundResource(R.drawable.actionbar_discover_selected);
//            } else if (position==1) {
//                Log.e("----------------", "actionBar_music");
//                friends_bar.setBackgroundResource(R.drawable.actionbar_friends_normal);
//                discover_bar.setBackgroundResource(R.drawable.actionbar_discover_normal);
//                music_bar.setBackgroundResource(R.drawable.actionbar_music_selected);
//            } else if (position==2) {
//                Log.e("----------------", "actionBar_friends");
//                music_bar.setBackgroundResource(R.drawable.actionbar_music_normal);
//                discover_bar.setBackgroundResource(R.drawable.actionbar_discover_normal);
//                friends_bar.setBackgroundResource(R.drawable.actionbar_friends_selected);
//            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }

    }

}
