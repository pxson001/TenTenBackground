package gokustudio.tentenbackground;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.edmodo.cropper.CropImageView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private Bitmap croppedImage;

    private Utils utilsWallpaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getScreenSize();
        utilsWallpaper = new Utils(this);

        setContentView(R.layout.activity_crop);
        ButterKnife.bind(this);

        cropImageView.setImageResource(R.drawable.cat2);
        cropImageView.setInitialFrameScale(1);
        cropImageView.setGuideShowMode(com.isseiaoki.simplecropview.CropImageView.ShowMode.NOT_SHOW);
        cropImageView.setHandleShowMode(com.isseiaoki.simplecropview.CropImageView.ShowMode.NOT_SHOW);
    }

    protected void getScreenSize() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    @OnClick(R.id.activity_crop_btn_crop_mode_fixed)
    public void setFixedMode() {
        int rawImageWidth = cropImageView.getImageBitmap().getWidth();
        int rawImageHeight = cropImageView.getImageBitmap().getHeight();

        float rawImageRation = (float) rawImageWidth/ (float) rawImageHeight;

        float screenRatio = (float) screenHeight / (float) screenWidth;

        System.out.println("Screen height  : " + screenHeight);
        System.out.println("Screen width   : " + screenWidth);
        System.out.println("Screen ratio          : " + screenRatio);

        cropImageView.setMinFrameSizeInDp(screenHeight);
        cropImageView.setCustomRatio(100, (int) (100 * screenRatio));
     }

    @OnClick(R.id.activity_crop_btn_crop_mode_normal)
    public void setNormalMode() {
        cropImageView.setCustomRatio(1, 1);
    }

    @OnClick(R.id.activity_crop_btn_crop_mode_no_crop)
    public void setNoCropMode() {
        cropImageView.setInitialFrameScale(1);
        cropImageView.setCropMode(com.isseiaoki.simplecropview.CropImageView.CropMode.RATIO_FIT_IMAGE);
    }

    @OnClick(R.id.activity_crop_btn_crop_mode_scrollable)
    public void setScrollableMode() {
        int rawImageWidth = cropImageView.getImageBitmap().getWidth();
        int rawImageHeight = cropImageView.getImageBitmap().getHeight();

        float ratioWidth = (float) screenWidth / (float) rawImageWidth;
        int noCropHeight = (int) (rawImageHeight * ratioWidth);

        cropImageView.setInitialFrameScale(1);
        cropImageView.setCropMode(com.isseiaoki.simplecropview.CropImageView.CropMode.RATIO_FIT_IMAGE);
        cropImageView.setImageBitmap(Bitmap.createScaledBitmap(cropImageView.getImageBitmap(), screenWidth, noCropHeight, false));
    }

    @OnClick(R.id.activity_crop_btn_set)
    public void setWallpaper() {
        croppedImage = cropImageView.getCroppedBitmap();

        int croppedImageWidth = croppedImage.getWidth();
        int croppedImageHeight = croppedImage.getHeight();


        btnSet.post(new Runnable() {
            @Override
            public void run() {
                utilsWallpaper.setAsWallpaper(croppedImage);
            }
        });
    }

    @OnClick(R.id.activity_crop_btn_cancel)
    public void cancel() {
        cropImageView.setImageResource(R.drawable.cat2);
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
