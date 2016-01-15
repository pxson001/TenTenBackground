package gokustudio.tentenbackground.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseException;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.adapters.ViewDetailPagerAdapter;
import gokustudio.tentenbackground.callbacks.LoadWallpapersByCategoryEvent;
import gokustudio.tentenbackground.callbacks.LoadWallpapersByRecentEvent;
import gokustudio.tentenbackground.callbacks.LoadWallpapersByTagEvent;
import gokustudio.tentenbackground.constants.ActivityValues;
import gokustudio.tentenbackground.fragments.WallpaperDetailFragment;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;
import gokustudio.tentenbackground.tasks.LoadWallpapersByCategoryTask;
import gokustudio.tentenbackground.tasks.LoadWallpapersByRecentTask;
import gokustudio.tentenbackground.tasks.LoadWallpapersByTagTask;


/**
 * Created by son on 9/30/15.
 */
public class WallpaperDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_CALLED_FROM_ACTIVITY = "extra_called_from_activity";

    @Bind(R.id.activity_wallpaper_detail_viewpager)
    ViewPager viewPager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static int calledFromActivity;

    WallpaperEndpoint wallpaperEndpoint;

    private ViewDetailPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_wallpaper_detail);
        ButterKnife.bind(this);

        wallpaperEndpoint = App.getWallpaperEndpoint();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (savedInstanceState == null) {
            calledFromActivity = getIntent().getIntExtra(ActivityValues.EXTRA_ACTIVITY_VALUE, 0);
        } else {
            calledFromActivity = savedInstanceState.getInt(EXTRA_CALLED_FROM_ACTIVITY);
        }

        adapter = new ViewDetailPagerAdapter(getSupportFragmentManager(), viewPager);
        switch (calledFromActivity) {
            case ActivityValues.MAIN_ACTIVITY:
                updateViewPager(App.getLoadedWallpapers(),false);
                break;
            case ActivityValues.WALLPAPER_BY_CATEGORY_ACTIVITY:
                updateViewPager(App.getLoadedWallpapersByCategory(), false);
                break;
            case ActivityValues.WALLPAPER_BY_TAG_ACTIVITY:
                updateViewPager(App.getLoadedWallpapersByTag(), false);
                break;
        }


        // wrap pager to provide infinite paging with wrap-around
        viewPager.setPageTransformer(false, new ParallaxPagerTransformer(R.id.fragment_wallpaper_detail_iv_header));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        setCurrentItem(calledFromActivity);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CALLED_FROM_ACTIVITY, calledFromActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    public void setCurrentItem(int activityId) {
        switch (activityId) {
            case ActivityValues.MAIN_ACTIVITY:
                viewPager.setCurrentItem(App.getCurrentWallpaperPosition());
                break;
            case ActivityValues.WALLPAPER_BY_CATEGORY_ACTIVITY:
                viewPager.setCurrentItem(App.getCurrentWallpaperByCategoryPosition());
                break;
            case ActivityValues.WALLPAPER_BY_TAG_ACTIVITY:
                viewPager.setCurrentItem(App.getCurrentWallpaperByTagPosition());
                break;
        }
    }

    public Wallpaper getCurrentWallpaper(int activityId) {
        int position;
        switch (activityId) {
            case ActivityValues.WALLPAPER_BY_CATEGORY_ACTIVITY:
                position = App.getCurrentWallpaperByCategoryPosition();
                return App.getLoadedWallpapersByCategory().get(position);
            case ActivityValues.WALLPAPER_BY_TAG_ACTIVITY:
                position = App.getCurrentWallpaperByTagPosition();
                return App.getLoadedWallpapersByTag().get(position);

            default:
                position = App.getCurrentWallpaperPosition();
                return App.getLoadedWallpapers().get(position);
        }
    }

    public void updateViewPager(List<Wallpaper> listWallpapers, boolean isUpdate) {
        if (!isUpdate) {
            adapter.removePages();
        }
        int i = adapter.getCount();

        for (int j = 0; j < listWallpapers.size(); j++) {
            WallpaperDetailFragment fragment = new WallpaperDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(WallpaperDetailFragment.EXTRA_POSITION, (i + j));
            fragment.setArguments(bundle);
            adapter.addTab(fragment);
        }
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(i);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        System.out.println("Position: " + position + "/" + viewPager.getAdapter().getCount());
        if (position == viewPager.getAdapter().getCount() - 1) {
            System.out.println("Load new page");
            switch (calledFromActivity){
                case ActivityValues.MAIN_ACTIVITY:
                    new LoadWallpapersByRecentTask(App.getLoadedWallpapers().size(), true).execute();
                    break;
                case ActivityValues.WALLPAPER_BY_CATEGORY_ACTIVITY:
                    new LoadWallpapersByCategoryTask(App.getCurrentCategoryObjectId(), App.getLoadedWallpapersByCategory().size(), true).execute();
                    break;
                case ActivityValues.WALLPAPER_BY_TAG_ACTIVITY:
                    new LoadWallpapersByTagTask(App.getCurrentTagObjectId(), App.getLoadedWallpapersByTag().size(), true).execute();
                    break;
            }

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onEvent(LoadWallpapersByRecentEvent event){
        List<Wallpaper> wallpapers = event.getWallpapers();
        updateViewPager(wallpapers, true);
    }

    public void onEvent(LoadWallpapersByTagEvent event){
        List<Wallpaper> wallpapers = event.getWallpapers();
        updateViewPager(wallpapers, true);
    }

    public void onEvent(LoadWallpapersByCategoryEvent event){
        List<Wallpaper> wallpapers = event.getWallpapers();
        updateViewPager(wallpapers, true);
    }


}
