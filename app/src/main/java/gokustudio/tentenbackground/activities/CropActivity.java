package gokustudio.tentenbackground.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.isseiaoki.simplecropview.CropImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.constants.CropMode;
import gokustudio.tentenbackground.helpers.CropBitmapHelper;
import gokustudio.tentenbackground.utils.DimensionUtils;
import gokustudio.tentenbackground.utils.Utils;

public class CropActivity extends AppCompatActivity {

    @Bind(R.id.cropImageView)
    com.isseiaoki.simplecropview.CropImageView cropImageView;
    @Bind(R.id.activity_crop_btn_crop_mode_fixed)
    Button btnCropModeFixed;
    @Bind(R.id.activity_crop_btn_crop_mode_normal)
    Button btnCropModeNormal;
    @Bind(R.id.activity_crop_btn_crop_mode_no_crop)
    Button btnCropModeNoCrop;
    @Bind(R.id.activity_crop_btn_crop_mode_scrollable)
    Button btnCropModeScrollable;
    @Bind(R.id.activity_crop_btn_cancel)
    Button btnCancel;
    @Bind(R.id.activity_crop_btn_set)
    Button btnSet;

    private int screenWidth;
    private int screenHeight;

    private String wallpaperPath;
    private Bitmap wallpaperBitmap;

    private Utils utilsWallpaper;

    private CropMode cropMode = CropMode.DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenSize();
        utilsWallpaper = new Utils(this);

        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra("IMG_PATH")) {
                wallpaperPath = intent.getExtras().getString("IMG_PATH");
            }
        } else {
            wallpaperPath = savedInstanceState.getString("IMG_PATH");
        }

        Bitmap bitmap = BitmapFactory.decodeFile(wallpaperPath);
        cropImageView.setImageBitmap(bitmap);
        cropImageView.setGuideShowMode(com.isseiaoki.simplecropview.CropImageView.ShowMode.NOT_SHOW);
        cropImageView.setHandleShowMode(com.isseiaoki.simplecropview.CropImageView.ShowMode.NOT_SHOW);

        setNormalMode();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("IMG_PATH", wallpaperPath);
    }

    @OnClick(R.id.activity_crop_btn_crop_mode_normal)
    public void setNormalMode() {
        float initialFrameScaleRatio = (float) screenHeight / (float) cropImageView.getImageBitmap().getHeight();
        int heightRatio = screenHeight / DimensionUtils.getUCLN(screenHeight, screenWidth);
        int widthRatio = screenWidth / DimensionUtils.getUCLN(screenHeight, screenWidth);

        cropImageView.setCropEnabled(true);
        cropImageView.setInitialFrameScale(initialFrameScaleRatio);
        cropImageView.setMinFrameSizeInDp(102);
        cropImageView.setCustomRatio(widthRatio, heightRatio);

        cropMode = CropMode.DEFAULT;
    }

    @OnClick(R.id.activity_crop_btn_crop_mode_fixed)
    public void setFixedMode() {
        int heightRatio = screenHeight / DimensionUtils.getUCLN(screenHeight, screenWidth);
        int widthRatio = screenWidth / DimensionUtils.getUCLN(screenHeight, screenWidth);

        cropImageView.setInitialFrameScale(1);
        cropImageView.setMinFrameSizeInDp(screenHeight);
        cropImageView.setCustomRatio(widthRatio, heightRatio);

        cropMode = CropMode.FIXED;
    }

    @OnClick(R.id.activity_crop_btn_crop_mode_no_crop)
    public void setNoCropMode() {
        cropImageView.setCropEnabled(false);
        cropImageView.setInitialFrameScale(1);
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);

        cropMode = CropMode.NO_CROP;
    }

    @OnClick(R.id.activity_crop_btn_crop_mode_scrollable)
    public void setScrollableMode() {
        cropImageView.setCropEnabled(false);
        cropImageView.setInitialFrameScale(1);
        cropImageView.setCropMode(CropImageView.CropMode.RATIO_FIT_IMAGE);

        cropMode = CropMode.SCROLLABLE;
    }

    @OnClick(R.id.activity_crop_btn_set)
    public void setWallpaper() {
        Bitmap croppedImage = cropImageView.getCroppedBitmap();

        switch (cropMode) {
            case DEFAULT:
                wallpaperBitmap = CropBitmapHelper.getFixed(croppedImage, screenWidth, screenHeight);
                break;
            case FIXED:
                wallpaperBitmap = CropBitmapHelper.getFixed(croppedImage, screenWidth, screenHeight);
                break;
            case NO_CROP:
                wallpaperBitmap = CropBitmapHelper.getNoCrop(croppedImage, screenWidth, screenHeight);
                break;
            case SCROLLABLE:
                wallpaperBitmap = croppedImage;
            default:
                break;
        }

        System.out.println("Screen height  : " + screenHeight);
        System.out.println("Screen width   : " + screenWidth);

        System.out.println("Raw Image height  : " + cropImageView.getImageBitmap().getHeight());
        System.out.println("Raw Image width   : " + cropImageView.getImageBitmap().getWidth());

        System.out.println("Cropped Image height  : " + croppedImage.getHeight());
        System.out.println("Cropped Image width   : " + croppedImage.getWidth());

        btnSet.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("LocalWallpaper Image height  : " + wallpaperBitmap.getHeight());
                System.out.println("LocalWallpaper Image width   : " + wallpaperBitmap.getWidth());
                utilsWallpaper.setAsWallpaper(wallpaperBitmap);

                finish();
            }
        });
    }

    @OnClick(R.id.activity_crop_btn_cancel)
    public void cancel() {
        finish();
    }

    protected void getScreenSize() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_crop, menu);
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


}
