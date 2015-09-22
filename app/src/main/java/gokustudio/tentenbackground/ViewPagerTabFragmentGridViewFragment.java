package gokustudio.tentenbackground;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.PhotoEntry;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gokustudio.tentenbackground.adapter.WallpaperAdapter;
import gokustudio.tentenbackground.callbacks.EndlessScrollListener;
import gokustudio.tentenbackground.model.Wallpaper;

/**
 * Created by son on 9/15/15.
 */
public class ViewPagerTabFragmentGridViewFragment extends Fragment implements AdapterView.OnItemClickListener {


    private PicasawebService myService;

    @Bind(R.id.fragment_gridview)
    ObservableGridView gridViewImage;

    private List<Wallpaper> listWallpapers;
    private WallpaperAdapter wallpaperAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gridview, container, false);
        ButterKnife.bind(this, view);

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            gridViewImage.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                gridViewImage.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        listWallpapers = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(listWallpapers, getActivity());
        gridViewImage.setAdapter(wallpaperAdapter);

        gridViewImage.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Toast.makeText(getActivity(), "On loadmore", Toast.LENGTH_SHORT).show();
            }
        });

        new LoadingWallPaperTask("https://picasaweb.google.com/data/feed/api/user/103790232873821665119/albumid/6192548855768281841?imgmax=2048").execute();


        gridViewImage.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Wallpaper wallpaper = wallpaperAdapter.getItem(i);
        Intent intent = new Intent(getActivity(), WallpaperPreviewActivity.class);
        intent.putExtra("wallpaper", wallpaper);
        startActivity(intent);
    }

    class LoadingWallPaperTask extends AsyncTask<Void, Void, List<Wallpaper>> {

        private String url;

        LoadingWallPaperTask(String url) {
            this.url = url;
        }

        @Override
        protected List<Wallpaper> doInBackground(Void... voids) {
            List<Wallpaper> listWallpapers = new ArrayList<>();
            try {
                myService = new PicasawebService("Hello-acsc231");
                URL feedURl = new URL(url);

                AlbumFeed albumFeed = myService.getFeed(feedURl, AlbumFeed.class);

                for (PhotoEntry photoEntry : albumFeed.getPhotoEntries()) {
                    System.out.println(photoEntry.getTitle().getPlainText() + " " + photoEntry.getMediaGroup().getContents().get(0).getUrl());

                    int thumbnailSize = photoEntry.getMediaGroup().getThumbnails().size();
                    MediaThumbnail mediaThumbnail = photoEntry.getMediaGroup().getThumbnails().get(thumbnailSize - 1);
                    String title = photoEntry.getTitle().getPlainText();
                    int width = mediaThumbnail.getWidth();
                    int height = mediaThumbnail.getHeight();
                    String url = mediaThumbnail.getUrl();
                    String orginalUrl = photoEntry.getMediaGroup().getContents().get(0).getUrl();
                    listWallpapers.add(new Wallpaper(title, width, height, url, orginalUrl));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listWallpapers;
        }

        @Override
        protected void onPostExecute(List<Wallpaper> wallpapers) {
            super.onPostExecute(wallpapers);

            System.out.println("Results : " + wallpapers.size());
            listWallpapers.addAll(wallpapers);
            wallpaperAdapter.notifyDataSetChanged();
        }
    }
}