package com.mymusicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.contextmenu.ContextMenuDialogFragment;
import com.contextmenu.MenuObject;
import com.contextmenu.MenuParams;
import com.contextmenu.interfaces.OnMenuItemClickListener;
import com.contextmenu.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;


public class IndexActivity extends ActionBarActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ImageButton discover_bar, music_bar, friends_bar, search_bar, menu_bar;

    private View discover_view, music_view, friends_view;

    private List<View> views;

    private LayoutInflater lf;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号

    private ListView index_music_list;

    private List<Object[]> musicTypes;

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
        discover_bar = (ImageButton) findViewById(R.id.actionBar_discover);
        music_bar = (ImageButton) findViewById(R.id.actionBar_music);
        friends_bar = (ImageButton) findViewById(R.id.actionBar_friends);
        search_bar = (ImageButton) findViewById(R.id.actionBar_search);
//        menu_bar = (ImageButton) findViewById(R.id.actionBar_menu);
        ActionBarClickListener actionBarClickListener = new ActionBarClickListener();
//        discover_bar.setOnClickListener(actionBarClickListener);
//        music_bar.setOnClickListener(actionBarClickListener);
//        friends_bar.setOnClickListener(actionBarClickListener);

        lf = getLayoutInflater().from(this);
        discover_view = (View) lf.inflate(R.layout.index_discover, null);
        music_view = (View) lf.inflate(R.layout.index_music, null);
        friends_view = (View) lf.inflate(R.layout.index_firends, null);

        PageChangListener pageChangeListener = new PageChangListener();
        mViewPager.setOnPageChangeListener(pageChangeListener);
        index_music_list = (ListView) music_view.findViewById(R.id.local_music_list);
        pullToRefreshView = (PullToRefreshView) music_view.findViewById(R.id.pull_refresh_view);


    }


    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

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
        views = new ArrayList<View>();
        views.add(discover_view);
        views.add(music_view);
        views.add(friends_view);

        PagerAdapter pageAdapter = new PagerAdapter() {

            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }
        };
        mViewPager.setAdapter(pageAdapter);
        musicTypes = new ArrayList<Object[]>();
        Object[] obj1 = {R.drawable.music_icn_local, "本地音乐", "（479）"};
        musicTypes.add(obj1);
        Object[] obj2 = {R.drawable.music_icn_recent, "最近播放", "（100）"};
        musicTypes.add(obj2);
        Object[] obj3 = {R.drawable.music_icn_dld, "下载管理", "（704）"};
        musicTypes.add(obj3);
        Object[] obj4 = {R.drawable.music_icn_artist, "我的歌手", "（0）"};
        musicTypes.add(obj4);
        Object[] obj5 = {R.drawable.music_icn_dj, "我的电台", "（2）"};
        musicTypes.add(obj5);
        Object[] obj6 = {R.drawable.music_icn_mv, "我的MV", "（6）"};
        musicTypes.add(obj6);
        IndexMusicListAdpter indexMusicAdpter = new IndexMusicListAdpter();
        index_music_list.setAdapter(indexMusicAdpter);

        pullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });

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

    class ActionBarClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
//            if (v.getId() == R.id.actionBar_discover) {
//                mViewPager.setCurrentItem(0);
//                Log.e("----------------", "actionBar_discover");
//                music_bar.setBackgroundResource(R.drawable.actionbar_music_normal);
//                friends_bar.setBackgroundResource(R.drawable.actionbar_friends_normal);
//                discover_bar.setBackgroundResource(R.drawable.actionbar_discover_selected);
//            } else if (v.getId() == R.id.actionBar_music) {
//                mViewPager.setCurrentItem(1);
//                Log.e("----------------", "actionBar_music");
//                friends_bar.setBackgroundResource(R.drawable.actionbar_friends_normal);
//                discover_bar.setBackgroundResource(R.drawable.actionbar_discover_normal);
//                music_bar.setBackgroundResource(R.drawable.actionbar_music_selected);
//            } else if (v.getId() == R.id.actionBar_friends) {
//                mViewPager.setCurrentItem(2);
//                Log.e("----------------", "actionBar_friends");
//                music_bar.setBackgroundResource(R.drawable.actionbar_music_normal);
//                discover_bar.setBackgroundResource(R.drawable.actionbar_discover_normal);
//                friends_bar.setBackgroundResource(R.drawable.actionbar_friends_selected);
//            }
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


    class IndexMusicListAdpter extends BaseAdapter {


        @Override
        public int getCount() {
            return musicTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return musicTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Object[] obj = musicTypes.get(position);
            convertView = lf.inflate(R.layout.index_music_list_item, parent, false);
            ImageView img = (ImageView) convertView.findViewById(R.id.item_icon);
            img.setBackgroundResource(Integer.parseInt(obj[0] + ""));
            TextView titleView = (TextView) convertView.findViewById(R.id.item_title);
            titleView.setText(obj[1] + "");
            TextView countView = (TextView) convertView.findViewById(R.id.item_count);
            countView.setText(obj[2] + "");
            return convertView;
        }
    }


}
