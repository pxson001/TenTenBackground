package gokustudio.tentenbackground;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.flaviofaria.kenburnsview.Transition;
import com.flaviofaria.kenburnsview.TransitionGenerator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gokustudio.tentenbackground.model.Wallpaper;
import uk.co.senab.photoview.PhotoViewAttacher;

public class WallpaperPreviewActivity extends AppCompatActivity {

    @Bind(R.id.wallpaper_activity_iv_preview)
    KenBurnsView ivWallpaperPreview;

    @Bind(R.id.wallpaper_activity_btn_set_wallpaper)
    Button btnSetWallpaper;

    @Bind(R.id.wallpaper_activity_btn_download_wallpapaer)
    Button btnDownloadWallpaper;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private Utils utils;
    private Wallpaper wallpaper;
    PhotoViewAttacher mAttacher;

    private boolean isKenburnsViewPaused = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_preview);
        ButterKnife.bind(this);
        utils = new Utils(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra("wallpaper")) {
                wallpaper = (Wallpaper) intent.getSerializableExtra("wallpaper");
            }
        } else {
            wallpaper = (Wallpaper) savedInstanceState.getSerializable("wallpaper");
        }


        toolbar.setTitle(wallpaper.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        Picasso.with(this).load(wallpaper.getOriginalUrl()).into(ivWallpaperPreview, new Callback() {
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
        outState.putSerializable("wallpaper", wallpaper);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_wallpaper_preview, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.wallpaper_activity_btn_set_wallpaper)
    public void setWallpaper() {
        final Bitmap bitmap = ((BitmapDrawable) ivWallpaperPreview.getDrawable()).getBitmap();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message").setMessage("Do you want to set this image as wallpaper?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                utils.setAsWallpaper(bitmap);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }

    @OnClick(R.id.wallpaper_activity_btn_download_wallpapaer)
    public void downloadWallpaper() {
        final Bitmap bitmap = ((BitmapDrawable) ivWallpaperPreview.getDrawable()).getBitmap();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Message").setMessage("Do you want to set this image as wallpaper?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                utils.saveImageToSDCard(bitmap);
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
    }
}
