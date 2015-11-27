package gokustudio.tentenbackground.tasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.afollestad.materialdialogs.MaterialDialog;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import gokustudio.tentenbackground.callbacks.DownloadedWallpaperListener;
import gokustudio.tentenbackground.utils.Utils;

/**
 * Created by son on 9/29/15.
 */
public class DownloadWallpaperTask extends AsyncTask<String, Integer, String>{

    private MaterialDialog progressDialog;
    private Context context;
    private DownloadedWallpaperListener downloadedWallpaperListener;

    public DownloadWallpaperTask(Context context, DownloadedWallpaperListener listener){
        this.context = context;
        this.progressDialog = new MaterialDialog.Builder(context).title("Download").progress(false, 100, true).build();
        this.downloadedWallpaperListener = listener;
    }

    @Override
    protected void onPreExecute() {
        this.progressDialog.show();
    }

    @Override
    protected String doInBackground(String... urls) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String downloadedPath = null;

        try {
            URL url = new URL(urls[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();
            this.progressDialog.setMaxProgress(fileLength / 1024);

            // download the file
            input = connection.getInputStream();

            downloadedPath = Utils.getDownloadPath(context) + "/" + FilenameUtils.getName(urls[0]);
            System.out.println(downloadedPath);
            output = new FileOutputStream(downloadedPath);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress(count / 1024);
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null)
                    output.close();
                   if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return downloadedPath;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        this.progressDialog.incrementProgress(values[0]);
        this.progressDialog.setProgressNumberFormat("%d/%d KB");
    }

    @Override
    protected void onPostExecute(String wallpaperPath) {
        this.progressDialog.setContent("Done");
        this.progressDialog.dismiss();
        downloadedWallpaperListener.onDownloaded(wallpaperPath);
    }
}
