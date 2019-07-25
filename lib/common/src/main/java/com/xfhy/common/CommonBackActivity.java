package com.xfhy.common;

import android.os.Bundle;

import com.blankj.swipepanel.SwipePanel;
import com.blankj.utilcode.util.SizeUtils;
import com.xfhy.base.BaseActivity;


public abstract class CommonBackActivity extends BaseActivity {

    public abstract boolean isSwipeBack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeBack();
    }

    private void initSwipeBack() {
        if (isSwipeBack()) {
            final SwipePanel swipeLayout = new SwipePanel(this);
            swipeLayout.setLeftDrawable(R.drawable.base_back);
            swipeLayout.setLeftEdgeSize(SizeUtils.dp2px(100));
            swipeLayout.wrapView(findViewById(android.R.id.content));
            swipeLayout.setOnFullSwipeListener(new SwipePanel.OnFullSwipeListener() {
                @Override
                public void onFullSwipe(int direction) {
                    swipeLayout.close(direction);
                    finish();
                }
            });
        }
    }
}
