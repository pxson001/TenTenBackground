package gokustudio.tentenbackground.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.callbacks.DownloadedWallpaperListener;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.tasks.DownloadWallpaperTask;
import gokustudio.tentenbackground.utils.Utils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class WallpaperPreviewActivity extends AppCompatActivity implements DownloadedWallpaperListener {

    public static final String EXTRA_WALLPAPER = "extra_wallpaper";
    public static final String EXTRA_IS_KENBURN_VIEW_PAUSED = "extra_is_kenburn_view_paused";
    public static final String EXTRA_ACTION_DOWNLOAD = "extra_action_download";

    @Bind(R.id.wallpaper_activity_iv_preview)
    KenBurnsView ivWallpaperPreview;

    @Bind(R.id.wallpaper_activity_btn_set_wallpaper)
    Button btnSetWallpaper;

    @Bind(R.id.wallpaper_activity_btn_download_wallpapaer)
    Button btnDownloadWallpaper;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    Utils utils;
    PhotoViewAttacher mAttacher;

    Wallpaper wallpaper;
    boolean isKenburnsViewPaused = true;
    boolean actionDownload = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_preview);
        ButterKnife.bind(this);

        utils = new Utils(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_WALLPAPER)) {
                wallpaper = (Wallpaper) intent.getSerializableExtra(EXTRA_WALLPAPER);
            }
        } else {
            wallpaper = (Wallpaper) savedInstanceState.getSerializable(EXTRA_WALLPAPER);
            isKenburnsViewPaused = savedInstanceState.getBoolean(EXTRA_IS_KENBURN_VIEW_PAUSED);
            actionDownload = savedInstanceState.getBoolean(EXTRA_ACTION_DOWNLOAD);
        }


        toolbar.setBackgroundColor(Color.TRANSPARENT);
        toolbar.setTitle(wallpaper.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        Picasso.with(this).load(wallpaper.getUrl_2()).into(ivWallpaperPreview, new Callback() {
            @Override
            public void onSuccess() {
                isKenburnsViewPaused = false;
                mAttacher = new PhotoViewAttacher(ivWallpaperPreview);

                ivWallpaperPreview.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (!isKenburnsViewPaused) {
                            isKenburnsViewPaused = true;
                            ivWallpaperPreview.pause();
                            System.out.println("onTouch pause");
                        } else {
                            isKenburnsViewPaused = false;
                            ivWallpaperPreview.resume();

                            System.out.println("onTouch play");
                        }
                        return false;
                    }
                });

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_WALLPAPER, wallpaper);
        outState.putBoolean(EXTRA_IS_KENBURN_VIEW_PAUSED, isKenburnsViewPaused);
        outState.putBoolean(EXTRA_ACTION_DOWNLOAD, actionDownload);
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

    @OnClick(R.id.wallpaper_activity_btn_set_wallpaper)
    public void setWallpaper() {
        actionDownload = false;
        String filePath = utils.getFileFromSDCard(wallpaper.getName());
        if(filePath == null) {
            new DownloadWallpaperTask(this, this).execute(wallpaper.getUrl_2());
        } else {
            startCropActivity(filePath);
        }
    }

    @OnClick(R.id.wallpaper_activity_btn_download_wallpapaer)
    public void downloadWallpaper() {
        actionDownload = true;
        String filePath = utils.getFileFromSDCard(wallpaper.getName());
        if(filePath == null) {
            new DownloadWallpaperTask(this, this).execute(wallpaper.getUrl_2());
        } else {
            Toast.makeText(this, "File is existed in SD Card", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDownloaded(String path) {
        if(actionDownload){
            Toast.makeText(this,"Download successfully", Toast.LENGTH_SHORT).show();
        } else {
            startCropActivity(path);
        }
    }

    public void startCropActivity(String path){
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra("IMG_PATH", path);
        startActivity(intent);
    }
}
