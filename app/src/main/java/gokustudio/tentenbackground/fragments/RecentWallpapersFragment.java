package gokustudio.tentenbackground.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
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

/**
 * Created by son on 11/2/15.
 */
public class RecentWallpapersFragment extends Fragment implements AdapterView.OnItemClickListener{

    public static final String ARG_INITIAL_POSITION = "ARG_INITIAL_POSITION";

    @Bind(R.id.scroll)
    ObservableGridView gvWallpapers;

    @Bind(R.id.fragment_recent_loader)
    ProgressBar pbLoader;

    private List<Wallpaper> listWallpapers;
    private WallpaperAdapter wallpaperAdapter;
    private WallpaperEndpoint wallpaperEndpoint;
    private LoadRecentWallpapersTask loadRecentWallpapersTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        ButterKnife.bind(this, view);

        Activity parentActivity = getActivity();

        if (parentActivity instanceof ObservableScrollViewCallbacks) {
            Bundle args = getArguments();
            if (args != null && args.containsKey(ARG_INITIAL_POSITION)) {
                final int initialPosition = args.getInt(ARG_INITIAL_POSITION, 0);
                ScrollUtils.addOnGlobalLayoutListener(gvWallpapers, new Runnable() {
                    @Override
                    public void run() {
                        gvWallpapers.setSelection(initialPosition);
                    }
                });
            }
            gvWallpapers.setTouchInterceptionViewGroup((ViewGroup) parentActivity.findViewById(R.id.root));
            gvWallpapers.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentActivity);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listWallpapers = App.getLoadedWallpapers();
        wallpaperAdapter = new WallpaperAdapter(getActivity(), listWallpapers);
        gvWallpapers.setAdapter(wallpaperAdapter);
        gvWallpapers.setOnItemClickListener(this);

        wallpaperEndpoint = App.getWallpaperEndpoint();
        loadRecentWallpapersTask = new LoadRecentWallpapersTask(wallpaperEndpoint);
        loadRecentWallpapersTask.execute();

        gvWallpapers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                updateUI(true);
                loadRecentWallpapersTask = new LoadRecentWallpapersTask(wallpaperEndpoint);
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

    public void updateUI(boolean isUpdate) {
        if (isUpdate) {
            pbLoader.setVisibility(View.VISIBLE);
            gvWallpapers.setVisibility(View.GONE);
        } else {
            pbLoader.setVisibility(View.GONE);
            gvWallpapers.setVisibility(View.VISIBLE);
        }
    }


    class LoadRecentWallpapersTask extends AsyncTask<Void, Void, List<Wallpaper>> {

        private WallpaperEndpoint wallpaperEndpoint;

        public LoadRecentWallpapersTask(WallpaperEndpoint wallpaperEndpoint) {
            this.wallpaperEndpoint = wallpaperEndpoint;
        }

        @Override
        protected void onPreExecute() {
            updateUI(true);
        }

        @Override
        protected List<Wallpaper> doInBackground(Void... voids) {
            List<Wallpaper> wallpapers = new ArrayList<>();
            try {
                wallpapers = wallpaperEndpoint.getAllRecent(listWallpapers.size(), App.LIMIT_LOAD_WALLPAPERS);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return wallpapers;
        }

        @Override
        protected void onPostExecute(List<Wallpaper> wallpapers) {
            App.addLoadedWallpaper(wallpapers, true);
            wallpaperAdapter.notifyDataSetChanged();
            updateUI(false);
        }
    }
}
