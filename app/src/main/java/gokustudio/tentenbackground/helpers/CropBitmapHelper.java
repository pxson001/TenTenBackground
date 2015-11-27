package gokustudio.tentenbackground.helpers;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * Created by son on 9/22/15.
 */
public class CropBitmapHelper {

    public static Bitmap getCenter(Bitmap bitmap, int screenWidth, int screenHeight) {
        float screenRatio = (float) screenHeight / (float) screenWidth;

        if (bitmap.getWidth() >= bitmap.getHeight()) {
            int centeredScaleBitmapWidth = (int) (bitmap.getHeight() / screenRatio);

            Bitmap centeredScaleBitmap = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth() / 2 - centeredScaleBitmapWidth / 2,
                    0,
                    centeredScaleBitmapWidth,
                    bitmap.getHeight()
            );
            return Bitmap.createScaledBitmap(centeredScaleBitmap, screenWidth, screenHeight, false);

        } else {
            int centeredScaleBitmapHeight = (int) (bitmap.getWidth() * screenRatio);
            Bitmap centeredScaleBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight() / 2 - centeredScaleBitmapHeight / 2,
                    bitmap.getWidth(),
                    centeredScaleBitmapHeight);
            return Bitmap.createScaledBitmap(centeredScaleBitmap, screenWidth, screenHeight, false);

        }

    }

    public static Bitmap getFixed(Bitmap bitmap, int screenWidth, int screenHeight){
        return Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, false);
    }

    public static Bitmap getNoCrop(Bitmap bitmap, int screenWidth, int screenHeight){
        Bitmap background = Bitmap.createBitmap((int)screenWidth, (int)screenHeight, Bitmap.Config.ARGB_8888);
        float originalWidth = bitmap.getWidth(), originalHeight = bitmap.getHeight();
        Canvas canvas = new Canvas(background);
        float scale = screenWidth/originalWidth;
        float xTranslation = 0.0f, yTranslation = (screenHeight - originalHeight * scale)/2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, transformation, paint);
        return background;
    }
}
