package gokustudio.tentenbackground.demos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import gokustudio.tentenbackground.R;

/**
 * Created by son on 1/11/16.
 */
public class TextViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;
    public TextViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.text);
    }
}
