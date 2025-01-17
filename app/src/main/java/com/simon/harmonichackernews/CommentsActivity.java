package com.simon.harmonichackernews;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.gw.swipeback.SwipeBackLayout;
import com.simon.harmonichackernews.utils.ThemeUtils;
import com.simon.harmonichackernews.utils.Utils;

public class CommentsActivity extends AppCompatActivity implements CommentsFragment.BottomSheetFragmentCallback {

    private boolean disableSwipeAtWeb;
    private boolean disableSwipeAtComments;
    private SwipeBackLayout swipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThemeUtils.setupTheme(this, true);

        setContentView(R.layout.activity_comments);

        CommentsFragment fragment = new CommentsFragment();
        fragment.setArguments(getIntent().getExtras());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.comment_fragment_container_view, fragment);
        transaction.commit();

        swipeBackLayout = findViewById(R.id.swipeBackLayout);
        FragmentContainerView fragmentContainerView = findViewById(R.id.comment_fragment_container_view);
        fragmentContainerView.setPadding(0, Utils.getStatusBarHeight(getResources()), 0, 0);

        swipeBackLayout.setSwipeBackListener(new SwipeBackLayout.OnSwipeBackListener() {
            @Override
            public void onViewPositionChanged(View mView, float swipeBackFraction, float swipeBackFactor) {
                mView.invalidate();
            }

            @Override
            public void onViewSwipeFinished(View mView, boolean isEnd) {
                if (isEnd) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });

        disableSwipeAtWeb = Utils.shouldDisableWebviewSwipeBack(getApplicationContext());
        disableSwipeAtComments = Utils.shouldDisableCommentsSwipeBack(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.activity_out_animation);
    }

    @Override
    public void onSwitchView(boolean isAtWebView) {
        if (isAtWebView) {
            swipeBackLayout.setActive(!disableSwipeAtWeb);
        } else {
            swipeBackLayout.setActive(!disableSwipeAtComments);
        }
    }
}