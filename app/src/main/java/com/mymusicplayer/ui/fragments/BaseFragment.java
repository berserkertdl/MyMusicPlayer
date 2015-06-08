package com.mymusicplayer.ui.fragments;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2015/6/8 0008.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Fragment ��ǰ״̬�Ƿ�ɼ�
     */
    protected boolean isVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * �ɼ�
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * ���ɼ�
     */
    protected void onInvisible() {

    }

    protected abstract void lazyLoad();

}
