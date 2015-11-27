package gokustudio.tentenbackground.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.activities.WallpaperDetailActivity;
import gokustudio.tentenbackground.adapters.WallpaperAdapter;
import gokustudio.tentenbackground.callbacks.EndlessScrollListener;
import gokustudio.tentenbackground.constants.ActivityValues;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;
import gokustudio.tentenbackground.utils.DimensionUtils;

/**
 * Created by son on 9/15/15.
 */
public class ViewPagerTabFragmentGridViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.scroll)
    ObservableGridView gvWallpapers;

    @Bind(R.id.fragment_recent_loader)
    ProgressBar pbLoader;

    View footerLoadMore;

    private List<Wallpaper> listWallpapers;
    private WallpaperAdapter wallpaperAdapter;
    private WallpaperEndpoint wallpaperEndpoint;
    private LoadRecentWallpapersTask loadRecentWallpapersTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ButterKnife.bind(this, view);

        footerLoadMore = LayoutInflater.from(getActivity()).inflate(R.layout.footer_load_more, null);
        gvWallpapers.addFooterView(footerLoadMore);

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            gvWallpapers.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                gvWallpapers.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listWallpapers = App.getLoadedWallpapers();
        wallpaperAdapter = new WallpaperAdapter(getActivity(),listWallpapers);
        gvWallpapers.setAdapter(wallpaperAdapter);
        gvWallpapers.setOnItemClickListener(this);

        wallpaperEndpoint = App.getWallpaperEndpoint();
        if(listWallpapers.isEmpty()) {
            loadRecentWallpapersTask = new LoadRecentWallpapersTask(wallpaperEndpoint, false);
            loadRecentWallpapersTask.execute();
        } else {
            updateUI(false, true);
        }

        gvWallpapers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                loadRecentWallpapersTask = new LoadRecentWallpapersTask(wallpaperEndpoint, true);
                loadRecentWallpapersTask.execute();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (loadRecentWallpapersTask != null)
            loadRecentWallpapersTask.cancel(true);
        super.onDestroyView();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Intent intent = new Intent(getActivity(), WallpaperDetailActivity.class);
        intent.putExtra(ActivityValues.EXTRA_ACTIVITY_VALUE, ActivityValues.MAIN_ACTIVITY);

        App.setCurrentWallpaperPosition(position);
        startActivity(intent);
    }

    public void updateUI(boolean isUpdate, boolean isComplete) {
        if (isUpdate) {
            pbLoader.setVisibility(View.GONE);
            gvWallpapers.setVisibility(View.VISIBLE);
            if(isComplete){
                footerLoadMore.setVisibility(View.GONE);
            } else {
                footerLoadMore.setVisibility(View.VISIBLE);
            }
        } else {
            footerLoadMore.setVisibility(View.GONE);
            if(isComplete){
                pbLoader.setVisibility(View.GONE);
                gvWallpapers.setVisibility(View.VISIBLE);
            } else {
                pbLoader.setVisibility(View.VISIBLE);
                gvWallpapers.setVisibility(View.GONE);
            }
        }
    }


    private void refreshGridView() {
        int gridViewEntrySize = DimensionUtils.dpToPx(getActivity(),160);
        int gridViewSpacing = 0;

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int numColumns = (display.getWidth() - gridViewSpacing) / (gridViewEntrySize + gridViewSpacing);

        gvWallpapers.setNumColumns(numColumns);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshGridView();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshGridView();
    }


    class LoadRecentWallpapersTask extends AsyncTask<Void, Void, List<Wallpaper>> {

        private WallpaperEndpoint wallpaperEndpoint;
        private boolean isUpdate;

        public LoadRecentWallpapersTask(WallpaperEndpoint wallpaperEndpoint, boolean isUpdate) {
            this.wallpaperEndpoint = wallpaperEndpoint;
            this.isUpdate = isUpdate;
        }

        @Override
        protected void onPreExecute() {
            updateUI(isUpdate,false);
        }

        @Override
        protected List<Wallpaper> doInBackground(Void... voids) {
            List<Wallpaper> wallpapers = new ArrayList<>();
            try {
                wallpapers = wallpaperEndpoint.getAllRecent(App.getLoadedWallpapers().size(), App.LIMIT_LOAD_WALLPAPERS);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return wallpapers;
        }

        @Override
        protected void onPostExecute(List<Wallpaper> wallpapers) {
            App.addLoadedWallpaper(wallpapers, isUpdate);
            listWallpapers = App.getLoadedWallpapers();
            wallpaperAdapter.update(listWallpapers);
            updateUI(isUpdate,true);
        }
    }
}