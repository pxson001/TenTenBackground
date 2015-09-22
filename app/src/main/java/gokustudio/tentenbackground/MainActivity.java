package gokustudio.tentenbackground;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;
import com.nineoldandroids.view.ViewHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gokustudio.tentenbackground.adapter.WallpaperAdapter;
import gokustudio.tentenbackground.callbacks.EndlessScrollListener;
import gokustudio.tentenbackground.model.Wallpaper;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ObservableScrollViewCallbacks{

    private PicasawebService myService;

    @Bind(R.id.activity_main_grid_view_wallpapers)
    ObservableGridView gridViewImage;

    @Bind(R.id.activitiy_main_toolbar)
    Toolbar mToolbar;

    private List<Wallpaper> listWallpapers;
    private WallpaperAdapter wallpaperAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listWallpapers = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(listWallpapers, this);
        gridViewImage.setAdapter(wallpaperAdapter);

        gridViewImage.setOnItemClickListener(this);
        gridViewImage.setScrollViewCallbacks(this);
        gridViewImage.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Toast.makeText(MainActivity.this, "On loadmore", Toast.LENGTH_SHORT).show();
            }
        });

        //Test merge change

        new LoadingWallPaperTask("https://picasaweb.google.com/data/feed/api/user/103790232873821665119/albumid/6192548855768281841?imgmax=2048").execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Wallpaper wallpaper = wallpaperAdapter.getItem(i);
        Intent intent = new Intent(this, WallpaperPreviewActivity.class);
        intent.putExtra("wallpaper", wallpaper);
        startActivity(intent);
    }

    class LoadingWallPaperTask extends AsyncTask<Void, Void, List<Wallpaper>> {

        private String url;

        LoadingWallPaperTask(String url) {
            this.url = url;
        }

        @Override
        protected List<Wallpaper> doInBackground(Void... voids) {
            List<Wallpaper> listWallpapers = new ArrayList<>();
            try {
                myService = new PicasawebService("Hello-acsc231");
                URL feedURl = new URL(url);

                AlbumFeed albumFeed = myService.getFeed(feedURl, AlbumFeed.class);

                for (PhotoEntry photoEntry : albumFeed.getPhotoEntries()) {
                    System.out.println(photoEntry.getTitle().getPlainText() + " " + photoEntry.getMediaGroup().getContents().get(0).getUrl());

                    int thumbnailSize = photoEntry.getMediaGroup().getThumbnails().size();
                    MediaThumbnail mediaThumbnail = photoEntry.getMediaGroup().getThumbnails().get(thumbnailSize - 1);
                    String title = photoEntry.getTitle().getPlainText();
                    int width = mediaThumbnail.getWidth();
                    int height = mediaThumbnail.getHeight();
                    String url = mediaThumbnail.getUrl();
                    String orginalUrl = photoEntry.getMediaGroup().getContents().get(0).getUrl();
                    listWallpapers.add(new Wallpaper(title, width, height, url, orginalUrl));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listWallpapers;
        }

        @Override
        protected void onPostExecute(List<Wallpaper> wallpapers) {
            super.onPostExecute(wallpapers);

            System.out.println("Results : " + wallpapers.size());
            listWallpapers.addAll(wallpapers);
            wallpaperAdapter.notifyDataSetChanged();
        }
    }

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
                ViewHelper.setTranslationY((View) gridViewImage, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) ((View) gridViewImage).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                ((View) gridViewImage).requestLayout();
            }
        });
        animator.start();
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

}
