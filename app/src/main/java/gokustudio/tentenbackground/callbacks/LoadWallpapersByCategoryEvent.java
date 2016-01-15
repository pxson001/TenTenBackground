package gokustudio.tentenbackground.callbacks;

import java.util.List;

import gokustudio.tentenbackground.models.parse.Wallpaper;

/**
 * Created by son on 12/29/15.
 */
public class LoadWallpapersByCategoryEvent {
    List<Wallpaper> wallpapers;
    boolean isUpdate;

    public LoadWallpapersByCategoryEvent(List<Wallpaper> wallpapers, boolean isUpdate){
        this.wallpapers = wallpapers;
    }

    public List<Wallpaper> getWallpapers() {
        return wallpapers;
    }

    public boolean isUpdate() {
        return isUpdate;
    }
}
