package gokustudio.tentenbackground;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;

import java.util.ArrayList;
import java.util.List;

import gokustudio.tentenbackground.databases.WallpaperDatabase;
import gokustudio.tentenbackground.models.parse.Category;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;

/**
 * Created by son on 10/27/15.
 */
public class App extends Application {

    public static final int LIMIT_LOAD_WALLPAPERS = 30;

    private static WallpaperEndpoint wallpaperEndpoint;
    private static WallpaperDatabase wallpaperDatabase;

    private static List<Category> listCategories;

    private static List<Wallpaper> listLoadedWallpapers;
    private static List<Wallpaper> listLoadedWallpapersByCategory;
    private static List<Wallpaper> listLoadedWallpapersByTag;

    private static int currentWallpaperPosition;
    private static int currentWallpaperByCategoryPosition;
    private static int currentWallpaperByTagPosition;

    private static String currentCategoryObjectId;
    private static String currentTagObjectId;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "iBWHI4092YQkGgaQOfjj3KnZp5NvK1tQqvisBXBj", "BXaOhj7C7jcEKotfxEy8HXk2SAPe5iW6Z59x6Qx4");
    }

    public static WallpaperEndpoint getWallpaperEndpoint() {
        if (wallpaperEndpoint == null) {
            wallpaperEndpoint = new WallpaperEndpoint();
        }
        return wallpaperEndpoint;
    }

    public static WallpaperDatabase getWallpaperDatabase(Context context) {
        if (wallpaperDatabase == null) {
            wallpaperDatabase = new WallpaperDatabase(context);
        }
        return wallpaperDatabase;
    }

    public static void setListCategories(List<Category> listCategories) {
        App.listCategories = listCategories;
    }

    public static List<Category> getListCategories() {
        if (listCategories == null) {
            return new ArrayList<>();
        }
        return listCategories;
    }

    public static void setCurrentWallpaperPosition(int position) {
        App.currentWallpaperPosition = position;
    }

    public static void setCurrentTagObjectId(String currentTagObjectId) {
        App.currentTagObjectId = currentTagObjectId;
    }

    public static void setCurrentCategoryObjectId(String currentCategoryObjectId) {
        App.currentCategoryObjectId = currentCategoryObjectId;
    }

    public static void setCurrentWallpaperByCategoryPosition(int currentWallpaperByCategoryPosition) {
        App.currentWallpaperByCategoryPosition = currentWallpaperByCategoryPosition;
    }

    public static void setCurrentWallpaperByTagPosition(int currentWallpaperByTagPosition) {
        App.currentWallpaperByTagPosition = currentWallpaperByTagPosition;
    }

    public static int getCurrentWallpaperPosition() {
        return currentWallpaperPosition;
    }

    public static String getCurrentTagObjectId() {
        return currentTagObjectId;
    }

    public static String getCurrentCategoryObjectId() {
        return currentCategoryObjectId;
    }

    public static int getCurrentWallpaperByCategoryPosition() {
        return currentWallpaperByCategoryPosition;
    }

    public static int getCurrentWallpaperByTagPosition() {
        return currentWallpaperByTagPosition;
    }


    public static void addLoadedWallpaper(List<Wallpaper> wallpapers, boolean isUpdate) {
        if (isUpdate) {
            listLoadedWallpapers.addAll(wallpapers);
        } else {
            listLoadedWallpapers.clear();
            listLoadedWallpapers = wallpapers;
        }
    }

    public static void addLoadedWallpaperByCategory(List<Wallpaper> wallpapers, boolean isUpdate) {
        if (isUpdate) {
            listLoadedWallpapersByCategory.addAll(wallpapers);
        } else {
            listLoadedWallpapersByCategory.clear();
            listLoadedWallpapersByCategory = wallpapers;
        }
    }

    public static void addLoadedWallpaperByTag(List<Wallpaper> wallpapers, boolean isUpdate) {
        if (isUpdate) {
            listLoadedWallpapersByTag.addAll(wallpapers);
        } else {
            listLoadedWallpapersByTag.clear();
            listLoadedWallpapersByTag = wallpapers;
        }
    }

    public static List<Wallpaper> getLoadedWallpapers() {
        if (listLoadedWallpapers == null) {
            listLoadedWallpapers = new ArrayList<>();
        }
        return listLoadedWallpapers;
    }

    public static List<Wallpaper> getLoadedWallpapersByCategory() {
        if (listLoadedWallpapersByCategory == null) {
            listLoadedWallpapersByCategory = new ArrayList<>();
        }
        return listLoadedWallpapersByCategory;
    }

    public static List<Wallpaper> getLoadedWallpapersByTag() {
        if (listLoadedWallpapersByTag == null) {
            listLoadedWallpapersByTag = new ArrayList<>();
        }
        return listLoadedWallpapersByTag;
    }

    public static void clearLoadedWallpaper() {
        if (listLoadedWallpapers != null)
            listLoadedWallpapers.clear();
    }

    public static void clearLoadedWallpaperByCategory() {
        if (listLoadedWallpapersByCategory != null)
            listLoadedWallpapersByCategory.clear();
    }


    public static void clearLoadedWallpaperByTag() {
        if (listLoadedWallpapersByTag != null)
            listLoadedWallpapersByTag.clear();
    }


}