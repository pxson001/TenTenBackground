package gokustudio.tentenbackground.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.parse.ParseException;
import com.xgc1986.parallaxPagerTransformer.ParallaxPagerTransformer;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.constants.ActivityValues;
import gokustudio.tentenbackground.fragments.WallpaperDetailFragment;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;


/**
 * Created by son on 9/30/15.
 */
public class WallpaperDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_CALLED_FROM_ACTIVITY = "extra_called_from_activity";

    @Bind(R.id.activity_wallpaper_detail_viewpager)
    ViewPager viewPager;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    int calledFromActivity;

    WallpaperEndpoint wallpaperEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // wrap pager to provide infinite paging with wrap-around
        viewPager.setPageTransformer(false, new ParallaxPagerTransformer(R.id.fragment_wallpaper_detail_iv_header));
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        setCurrentItem(calledFromActivity);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CALLED_FROM_ACTIVITY, calledFromActivity);
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


    private PagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public int getCount() {
            return 10000;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new WallpaperDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(WallpaperDetailFragment.EXTRA_POSITION, position);
            fragment.setArguments(bundle);
            return fragment;
        }
    };
}
