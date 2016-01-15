package gokustudio.tentenbackground.tasks;

import android.os.AsyncTask;

import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.callbacks.LoadWallpapersByCategoryEvent;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;

/**
 * Created by son on 12/29/15.
 */
public class LoadWallpapersByCategoryTask extends AsyncTask<Void, Void, List<Wallpaper>> {

    private WallpaperEndpoint wallpaperEndpoint;
    private String categoryObjectId;
    private int skip;
    private boolean isUpdate;

    public LoadWallpapersByCategoryTask(String categoryObjectId, int skip, boolean isUpdate) {
        this.wallpaperEndpoint = App.getWallpaperEndpoint();
        this.categoryObjectId = categoryObjectId;
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
            wallpapers = wallpaperEndpoint.getByCategory(categoryObjectId, skip, App.LIMIT_LOAD_WALLPAPERS);
            System.out.println("LocalWallpaper by category : " + wallpapers.size());
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return wallpapers;
    }

    @Override
    protected void onPostExecute(List<Wallpaper> wallpapers) {
        App.addLoadedWallpaperByCategory(wallpapers, isUpdate);
        EventBus.getDefault().post(new LoadWallpapersByCategoryEvent(wallpapers, isUpdate));
    }
}