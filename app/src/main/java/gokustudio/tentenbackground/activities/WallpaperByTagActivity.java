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
import gokustudio.tentenbackground.callbacks.LoadWallpapersByTagEvent;
import gokustudio.tentenbackground.constants.ActivityValues;
import gokustudio.tentenbackground.models.parse.Tag;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;
import gokustudio.tentenbackground.tasks.LoadWallpapersByTagTask;

/**
 * Created by son on 11/2/15.
 */
public class WallpaperByTagActivity extends BaseActivity implements ObservableScrollViewCallbacks, AdapterView.OnItemClickListener {

    public static final String EXTRA_TAG = "extra_tag";

    private List<Wallpaper> listWallpapers;
    private WallpaperAdapter wallpaperAdapter;
    private LoadWallpapersByTagTask loadWallpapersByTagTask;

    Tag tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_wallpaper_by_tag);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            tag = (Tag) getIntent().getSerializableExtra(EXTRA_TAG);
        } else {
            tag = (Tag) savedInstanceState.getSerializable(EXTRA_TAG);
        }

        listWallpapers = App.getLoadedWallpapersByTag();
        App.setCurrentTagObjectId(tag.getObjectId());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(tag.getName());

        footerLoadMore = LayoutInflater.from(this).inflate(R.layout.footer_load_more, null);
        gvWallpapers.addFooterView(footerLoadMore);

        wallpaperAdapter = new WallpaperAdapter(this, listWallpapers);
        gvWallpapers.setAdapter(wallpaperAdapter);
        gvWallpapers.setScrollViewCallbacks(this);
        gvWallpapers.setOnItemClickListener(this);


        if (listWallpapers.isEmpty()) {
            updateUI(false, false);
            loadWallpapersByTagTask = new LoadWallpapersByTagTask(tag.getObjectId(), App.getLoadedWallpapersByTag().size() ,false);
            loadWallpapersByTagTask.execute();
        } else {
            updateUI(false, true);
        }

        gvWallpapers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                updateUI(true, false);
                loadWallpapersByTagTask = new LoadWallpapersByTagTask(tag.getObjectId(),App.getLoadedWallpapersByTag().size() ,true);
                loadWallpapersByTagTask.execute();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (loadWallpapersByTagTask != null) {
            loadWallpapersByTagTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_TAG, tag);
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
        intent.putExtra(ActivityValues.EXTRA_ACTIVITY_VALUE, ActivityValues.WALLPAPER_BY_TAG_ACTIVITY);

        App.setCurrentWallpaperByTagPosition(position);
        startActivity(intent);
    }

    public void onEvent(LoadWallpapersByTagEvent event){
        listWallpapers = App.getLoadedWallpapersByTag();
        wallpaperAdapter.update(listWallpapers);
        updateUI(event.isUpdate(), true);
    }
}
