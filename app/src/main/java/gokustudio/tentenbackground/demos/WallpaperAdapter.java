package gokustudio.tentenbackground.demos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.models.parse.Wallpaper;

/**
 * Created by son on 9/9/15.
 */
public class WallpaperAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<Wallpaper> listWallpaper;
    private Context context;

    public WallpaperAdapter(Context context, List<Wallpaper> listWallpaper) {
        this.listWallpaper = listWallpaper;
        this.context = context;
    }

    public void update(List<Wallpaper> listWallpaper) {
        this.listWallpaper = listWallpaper;
        notifyDataSetChanged();
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        Wallpaper wallpaper = listWallpaper.get(position);
        Glide.with(context).load(wallpaper.getUrl_1()).into(holder.ivContent);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return listWallpaper.size();
    }
}
