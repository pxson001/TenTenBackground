package gokustudio.tentenbackground.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Created by son on 9/10/15.
 */
public class Utils {
    private String TAG = Utils.class.getSimpleName();
    private Context _context;

    // constructor
    public Utils(Context context) {
        this._context = context;
    }

    /*
     * getting screen width
     */
    @SuppressWarnings("deprecation")
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) _context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) {
            // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }

    public String saveImageToSDCard(Bitmap bitmap) {
        File myDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                _context.getApplicationInfo().loadLabel(_context.getPackageManager()).toString());

        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "LocalWallpaper-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            Log.d(TAG, "LocalWallpaper saved to: " + file.getAbsolutePath());
            Toast.makeText(_context, "Saved " + file.getName() + " successfully", Toast.LENGTH_SHORT).show();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context, "Error when saving " + file.getName() + " successfully", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public String getFileFromSDCard(String fileName){
        File folder = new File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                _context.getApplicationInfo().loadLabel(_context.getPackageManager()).toString());
        if(folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for(File file : files){
                if(file.getName().equals(fileName)){
                    return file.getAbsolutePath();
                }
            }
        }
        return null;
    }

    public Bitmap getImageFromSDCard(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(path, options);
    }


    public void setAsWallpaper(Bitmap bitmap) {
        try {
            WallpaperManager wm = WallpaperManager.getInstance(_context);
            wm.setBitmap(bitmap);
            Toast.makeText(_context, "Set wallpaper successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(_context, "Can't set wallpaper", Toast.LENGTH_SHORT).show();
        }
    }

    public void setAsWallpaper(String filePath){
        Bitmap bitmap = getImageFromSDCard(filePath);
        setAsWallpaper(bitmap);
    }

    public static String getDownloadPath(Context context) {
        String path = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        } else {
            if (!folder.isDirectory()) {
                folder.mkdir();
            }
        }
        return path;
    }
}

