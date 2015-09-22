package gokustudio.tentenbackground.model;

import java.io.Serializable;

/**
 * Created by son on 9/9/15.
 */
public class Wallpaper implements Serializable {
    private String title;
    private int width;
    private int height;
    private String url;
    private String originalUrl;

    public Wallpaper(String title, int width, int height, String url, String originalUrl){
        this.title = title;
        this.width = width;
        this.height = height;
        this.url = url;
        this.originalUrl = originalUrl;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
