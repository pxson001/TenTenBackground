package gokustudio.tentenbackground.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wefika.flowlayout.FlowLayout;

import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.models.parse.Tag;

/**
 * Created by son on 10/27/15.
 */
public class TagView extends Button implements View.OnClickListener {

    private OnTagClickListener mOnTagClickListener;

    private Tag tag;

    public TagView(Context context) {
        super(context);
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TagView(Context context, Tag tag) {
        super(context);
        setBackgroundResource(R.drawable.button_tag);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        setPadding(5, 2, 5, 2);
        setLayoutParams(layoutParams);
        setTextSize(12);
        setText("#" + tag.getName());

        this.tag = tag;
        this.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(mOnTagClickListener!=null){
            mOnTagClickListener.onClick(tag);
        }
    }

    public void setOnTagClickListener(OnTagClickListener mOnTagClickListener) {
        this.mOnTagClickListener = mOnTagClickListener;
    }

    public interface OnTagClickListener{
        void onClick(Tag tag);
    }
}
