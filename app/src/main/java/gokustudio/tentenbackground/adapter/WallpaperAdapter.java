package gokustudio.tentenbackground.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.model.Wallpaper;

/**
 * Created by son on 9/9/15.
 */
public class WallpaperAdapter  extends BaseAdapter{

    private List<Wallpaper> listWallpaper;
    private Context context;

    public WallpaperAdapter(List<Wallpaper> listWallpaper, Context context) {
        this.listWallpaper = listWallpaper;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listWallpaper.size();
    }

    @Override
    public Wallpaper getItem(int i) {
        return listWallpaper.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        ViewHolder viewHolder = null;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Wallpaper wallpaper = listWallpaper.get(i);

        Picasso.with(context).load(wallpaper.getUrl()).fit().into(viewHolder.ivContent);
        return view;
    }

    static class ViewHolder{
        @Bind(R.id.wallpaper_item_image_view_content)
        ImageView ivContent;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
    }
}
