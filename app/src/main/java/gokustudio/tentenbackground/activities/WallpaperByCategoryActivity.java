package gokustudio.tentenbackground.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.adapters.WallpaperAdapter;
import gokustudio.tentenbackground.callbacks.EndlessScrollListener;
import gokustudio.tentenbackground.callbacks.LoadWallpapersByCategoryEvent;
import gokustudio.tentenbackground.constants.ActivityValues;
import gokustudio.tentenbackground.models.parse.Category;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;
import gokustudio.tentenbackground.tasks.LoadWallpapersByCategoryTask;

/**
 * Created by son on 11/2/15.
 */
public class WallpaperByCategoryActivity extends BaseActivity implements ObservableScrollViewCallbacks, AdapterView.OnItemClickListener {

    public static final String EXTRA_CATEGORY = "extra_category";

    private List<Wallpaper> listWallpapers;
    private WallpaperAdapter wallpaperAdapter;
    private LoadWallpapersByCategoryTask loadWallpapersByCategoryTask;
    private WallpaperEndpoint wallpaperEndpoint;

    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_wallpaper_by_category);
        ButterKnife.bind(this);

        wallpaperEndpoint = App.getWallpaperEndpoint();
        listWallpapers = App.getLoadedWallpapersByCategory();

        if (savedInstanceState == null) {
            category = (Category) getIntent().getSerializableExtra(EXTRA_CATEGORY);
        } else {
            category = (Category) savedInstanceState.getSerializable(EXTRA_CATEGORY);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(category.getName());

        footerLoadMore = LayoutInflater.from(this).inflate(R.layout.footer_load_more, null);
        gvWallpapers.addFooterView(footerLoadMore);

        wallpaperAdapter = new WallpaperAdapter(this, listWallpapers);
        gvWallpapers.setAdapter(wallpaperAdapter);
        gvWallpapers.setScrollViewCallbacks(this);
        gvWallpapers.setOnItemClickListener(this);


        if (listWallpapers.isEmpty()) {
            updateUI(false, false);

            loadWallpapersByCategoryTask = new LoadWallpapersByCategoryTask(category.getObjectId(),App.getLoadedWallpapersByCategory().size(), false);
            loadWallpapersByCategoryTask.execute();
        } else {
            updateUI(false, true);
        }

        gvWallpapers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                updateUI(true, false);

                loadWallpapersByCategoryTask = new LoadWallpapersByCategoryTask(category.getObjectId(),App.getLoadedWallpapersByCategory().size(), true);
                loadWallpapersByCategoryTask.execute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (loadWallpapersByCategoryTask != null) {
            loadWallpapersByCategoryTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_CATEGORY, category);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(this, WallpaperDetailActivity.class);
        intent.putExtra(ActivityValues.EXTRA_ACTIVITY_VALUE, ActivityValues.WALLPAPER_BY_CATEGORY_ACTIVITY);
        App.setCurrentWallpaperByCategoryPosition(position);
        startActivity(intent);
    }

    public void onEvent(LoadWallpapersByCategoryEvent event){
        listWallpapers = App.getLoadedWallpapersByCategory();
        wallpaperAdapter.update(listWallpapers);

        updateUI(event.isUpdate(), true);
    }
}
