package gokustudio.tentenbackground.tasks;

import android.os.AsyncTask;

import java.util.List;

import gokustudio.tentenbackground.models.parse.Wallpaper;

/**
 * Created by son on 11/12/15.
 */
public abstract  class LoadWallpaperTask extends AsyncTask<Void, Void, List<Wallpaper>>{
    public void execute(){
        super.execute();
    }
}
