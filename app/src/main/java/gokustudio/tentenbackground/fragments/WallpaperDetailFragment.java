package gokustudio.tentenbackground.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.wefika.flowlayout.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.activities.WallpaperByTagActivity;
import gokustudio.tentenbackground.activities.WallpaperPreviewActivity;
import gokustudio.tentenbackground.callbacks.DownloadedWallpaperListener;
import gokustudio.tentenbackground.constants.ActivityValues;
import gokustudio.tentenbackground.constants.Copyrights;
import gokustudio.tentenbackground.databases.WallpaperDatabase;
import gokustudio.tentenbackground.models.parse.Author;
import gokustudio.tentenbackground.models.parse.LocalWallpaper;
import gokustudio.tentenbackground.models.parse.Tag;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;
import gokustudio.tentenbackground.tasks.DownloadWallpaperAndShareTask;
import gokustudio.tentenbackground.tasks.DownloadWallpaperTask;
import gokustudio.tentenbackground.utils.Utils;
import gokustudio.tentenbackground.views.TagView;

/**
 * Created by son on 10/27/15.
 */
public class WallpaperDetailFragment extends Fragment implements TagView.OnTagClickListener, DownloadedWallpaperListener {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String EXTRA_CALLED_FROM_ACTIVITY = "extra_called_from_activity";
    public static final String EXTRA_WALLPAPAER = "extra_wallpaper";

    int position;
    int calledFromActivity;
    Wallpaper wallpaper;
    LocalWallpaper localWallpaper;
    Utils utils;

    private WallpaperEndpoint wallpaperEndpoint;
    private WallpaperDatabase wallpaperDatabase;

    @Bind(R.id.fragment_wallpaper_detail_iv_header)
    ImageView ivHeader;

    @Bind(R.id.fragment_wallpaper_detail_iv_author_avatar)
    CircleImageView ivAuthorAvatar;

    @Bind(R.id.fragment_wallpaper_detail_tv_author_name)
    TextView tvAuthorName;

    @Bind(R.id.fragment_wallpaper_detail_tv_file_size)
    TextView tvFileSize;

    @Bind(R.id.fragment_wallpaper_detail_tv_copright)
    TextView tvCopyright;

    @Bind(R.id.fragment_wallpaper_detail_group_tags)
    FlowLayout flGroupTags;

    @Bind(R.id.fragment_wallpaper_detail_group_info)
    LinearLayout llGroupInfo;

    @Bind(R.id.fragment_wallpaper_detail_pb_loader)
    ProgressBar pbLoader;

    @Bind(R.id.fragment_wallpaper_detail_btn_favorite)
    Button btnFavorite;

    @Bind(R.id.fragment_wallpaper_detail_btn_download)
    Button btnDownload;

    @Bind(R.id.fragment_wallpaper_detail_btn_share)
    Button btnShare;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wallpaperEndpoint = App.getWallpaperEndpoint();
        wallpaperDatabase = App.getWallpaperDatabase(getActivity());

        utils = new Utils(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_wallpaper_detail, null, false);
        ButterKnife.bind(this, layout);

        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        calledFromActivity = getActivity().getIntent().getIntExtra(ActivityValues.EXTRA_ACTIVITY_VALUE, 0);

        if (savedInstanceState == null) {
            position = getArguments().getInt(EXTRA_POSITION);
        } else {
            position = savedInstanceState.getInt(EXTRA_POSITION);
            calledFromActivity = savedInstanceState.getInt(EXTRA_CALLED_FROM_ACTIVITY);
            wallpaper = (Wallpaper) savedInstanceState.getSerializable(EXTRA_WALLPAPAER);
        }
        getWallPaper(calledFromActivity);

        if (wallpaper != null) {
            loadWallpaperInfo(wallpaper);
            loadAuthorInfo(wallpaper);
            loadWallpaperTags(wallpaper);
            loadLocalFavoriteInfo(wallpaper);
            updateUI(false);

            Log.d("Wallpaper Fragment", "not null");
        }

        setRetainInstance(true);
    }

    public void getWallPaper(int activityId) {
        switch (activityId) {
            case ActivityValues.MAIN_ACTIVITY:
                if (position >= App.getLoadedWallpapers().size()) {
                    wallpaper = null;
                } else {
                    wallpaper = App.getLoadedWallpapers().get(position);
                }
                break;
            case ActivityValues.WALLPAPER_BY_CATEGORY_ACTIVITY:
                if (position >= App.getLoadedWallpapersByCategory().size()) {
                    wallpaper = null;
                } else {
                    wallpaper = App.getLoadedWallpapersByCategory().get(position);
                }
                break;
            case ActivityValues.WALLPAPER_BY_TAG_ACTIVITY:
                if (position >= App.getLoadedWallpapersByTag().size()) {
                    wallpaper = null;
                } else {
                    wallpaper = App.getLoadedWallpapersByTag().get(position);
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_POSITION, position);
        outState.putInt(EXTRA_CALLED_FROM_ACTIVITY, calledFromActivity);
        outState.putSerializable(EXTRA_WALLPAPAER, wallpaper);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.fragment_wallpaper_detail_iv_header)
    void loadFullScreenWallpaper() {
        Intent intent = new Intent(getActivity(), WallpaperPreviewActivity.class);
        intent.putExtra(WallpaperPreviewActivity.EXTRA_WALLPAPER, wallpaper);
        startActivity(intent);
    }


    @OnClick(R.id.fragment_wallpaper_detail_btn_favorite)
    public void favorite() {
        boolean isFavorite = !localWallpaper.isFavorite();
        if (!isFavorite) {
            btnFavorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        } else {
            btnFavorite.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
        }
        try {
            wallpaperDatabase.updateToLocal(localWallpaper, isFavorite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fragment_wallpaper_detail_btn_download)
    public void download() {
        String filePath = utils.getFileFromSDCard(wallpaper.getName());
        if (filePath == null) {
            new DownloadWallpaperTask(getActivity(), this).execute(wallpaper.getUrl_2());
        } else {
            Toast.makeText(getActivity(), "File is existed in SD Card", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.fragment_wallpaper_detail_btn_share)
    public void share() {
        LocalWallpaper localWallpaper = wallpaperDatabase.getFromLocal(wallpaper.getObjectId());

        if (localWallpaper == null) {
            new DownloadWallpaperAndShareTask(getActivity(), this).execute(wallpaper.getUrl_2());
        } else {
            String filePath =localWallpaper.getDownloadPath();
            Utils.shareImage(getActivity(),filePath);
        }
    }

    @Override
    public void onDownloaded(String path) {
        wallpaperDatabase.updateToLocal(localWallpaper, path, false);
        Toast.makeText(getActivity(), "Download successfully", Toast.LENGTH_SHORT).show();
    }

    public void loadWallpaperInfo(Wallpaper wallpaper) {
        Glide.with(getActivity()).load(wallpaper.getUrl_2()).into(ivHeader);
        tvFileSize.setText(wallpaper.getFileSize() + " KB");
        tvCopyright.setText(Copyrights.COPYRIGHTS[Integer.parseInt(wallpaper.getCopyrightId())]);
    }

    public void loadAuthorInfo(Wallpaper wallpaper) {
        Author author = wallpaper.getAuthor();
        Glide.with(getActivity()).load(author.getThumbnail()).into(ivAuthorAvatar);
        tvAuthorName.setText(author.getName());
    }

    public void loadWallpaperTags(Wallpaper wallpaper) {
        List<Tag> tags = wallpaper.getTags();
        for (Tag tag : tags) {
            TagView tvTag = new TagView(getActivity(), tag);
            tvTag.setOnTagClickListener(this);
            flGroupTags.addView(tvTag);
        }
    }

    public void loadLocalFavoriteInfo(Wallpaper wallpaper) {
        localWallpaper = wallpaperDatabase.getFromLocal(wallpaper.getObjectId());
        if (localWallpaper == null) {
            localWallpaper = new LocalWallpaper(wallpaper);
        }
        boolean isFavorite = localWallpaper.isFavorite();
        if (isFavorite) {
            btnFavorite.setBackgroundResource(R.drawable.ic_favorite_white_24dp);
        } else {
            btnFavorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
        }
    }

    public void updateUI(boolean isUpdate) {
        if (isUpdate) {
            pbLoader.setVisibility(View.VISIBLE);
            llGroupInfo.setVisibility(View.GONE);
        } else {
            pbLoader.setVisibility(View.GONE);
            llGroupInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(Tag tag) {
        App.clearLoadedWallpaperByTag();
        App.setCurrentTagObjectId(tag.getObjectId());

        Intent intent = new Intent(getActivity(), WallpaperByTagActivity.class);
        intent.putExtra(WallpaperByTagActivity.EXTRA_TAG, tag);
        startActivity(intent);
    }
}
