//package gokustudio.tentenbackground.tasks;
//
//import android.content.Context;
//import android.os.AsyncTask;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//
//import org.apache.commons.io.FilenameUtils;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import gokustudio.tentenbackground.callbacks.DownloadedWallpaperListener;
//import gokustudio.tentenbackground.model.parse.LocalWallpaper;
//import gokustudio.tentenbackground.parse.WallpaperEndpoint;
//import gokustudio.tentenbackground.utils.Utils;
//
///**
// * Created by son on 10/27/15.
// */
//public class LoadWallpaperInfoTask extends AsyncTask<String, Void, LocalWallpaper> {
//
//    private Context context;
//    private WallpaperEndpoint.OnGetDetailWallpaperListener mOnGetDetailWallpaperListener;
//
//    public LoadWallpaperInfoTask(Context context, DownloadedWallpaperListener listener){
//        this.context = context;
//        this.mOnGetDetailWallpaperListener = listener;
//      }
//
//    @Override
//    protected void onPreExecute() {
//    }
//
//    @Override
//    protected LocalWallpaper doInBackground(String... strings) {
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(LocalWallpaper wallpaper) {
//      mOnGetDetailWallpaperListener.onGet(wallpaper);
//    }
//}
