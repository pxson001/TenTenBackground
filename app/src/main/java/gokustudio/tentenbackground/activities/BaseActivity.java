package gokustudio.tentenbackground.activities;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import butterknife.Bind;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.utils.DimensionUtils;

/**
 * Created by son on 11/12/15.
 */
public class BaseActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.gv_wallpaper)
    ObservableGridView gvWallpapers;

    @Bind(R.id.pb_loader)
    ProgressBar pbLoader;

    View footerLoadMore;

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }


    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Log.e("DEBUG", "onUpOrCancelMotionEvent: " + scrollState);
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                ViewHelper.setTranslationY((View) gvWallpapers, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) gvWallpapers).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) gvWallpapers).requestLayout();
            }
        });
        animator.start();
    }

    public void updateUI(boolean isUpdate, boolean isComplete) {
        if (isUpdate) {
            pbLoader.setVisibility(View.GONE);
            gvWallpapers.setVisibility(View.VISIBLE);
            if(isComplete){
                footerLoadMore.setVisibility(View.GONE);
            } else {
                footerLoadMore.setVisibility(View.VISIBLE);
            }
        } else {
            footerLoadMore.setVisibility(View.GONE);
            if(isComplete){
                pbLoader.setVisibility(View.GONE);
                gvWallpapers.setVisibility(View.VISIBLE);
            } else {
                pbLoader.setVisibility(View.VISIBLE);
                gvWallpapers.setVisibility(View.GONE);
            }
        }
    }

    private void refreshGridView() {
        int gridViewEntrySize = DimensionUtils.dpToPx(this, 160);
        int gridViewSpacing = 0;

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int numColumns = (display.getWidth() - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);

        gvWallpapers.setNumColumns(numColumns);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshGridView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshGridView();
    }

}
