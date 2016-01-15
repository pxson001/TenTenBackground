package gokustudio.tentenbackground.demos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import gokustudio.tentenbackground.R;

/**
 * Created by son on 1/11/16.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.wallpaper_item_image_view_content)
    ImageView ivContent;
    public ImageViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}