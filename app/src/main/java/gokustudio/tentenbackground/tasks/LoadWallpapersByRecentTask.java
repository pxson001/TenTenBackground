package gokustudio.tentenbackground.tasks;

import android.os.AsyncTask;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.callbacks.LoadWallpapersByRecentEvent;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;

/**
 * Created by son on 12/29/15.
 */
public class LoadWallpapersByRecentTask extends AsyncTask<Void, Void, List<Wallpaper>> {

    private WallpaperEndpoint wallpaperEndpoint;
    private int skip;
    private boolean isUpdate;

    public LoadWallpapersByRecentTask(int skip, boolean isUpdate) {
        this.wallpaperEndpoint = App.getWallpaperEndpoint();
        this.skip = skip;
        this.isUpdate = isUpdate;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected List<Wallpaper> doInBackground(Void... voids) {
        List<Wallpaper> wallpapers = new ArrayList<>();
        try {
            wallpapers = wallpaperEndpoint.getAllRecent(skip, App.LIMIT_LOAD_WALLPAPERS);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return wallpapers;
    }

    @Override
    protected void onPostExecute(List<Wallpaper> wallpapers) {
        App.addLoadedWallpaper(wallpapers, isUpdate);
        EventBus.getDefault().post(new LoadWallpapersByRecentEvent(wallpapers,isUpdate));
    }
}
