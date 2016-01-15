package gokustudio.tentenbackground.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.activities.WallpaperDetailActivity;
import gokustudio.tentenbackground.adapters.WallpaperAdapter;
import gokustudio.tentenbackground.callbacks.EndlessScrollListener;
import gokustudio.tentenbackground.callbacks.LoadWallpapersByRecentEvent;
import gokustudio.tentenbackground.constants.ActivityValues;
import gokustudio.tentenbackground.models.parse.Wallpaper;
import gokustudio.tentenbackground.parse.WallpaperEndpoint;
import gokustudio.tentenbackground.tasks.LoadWallpapersByRecentTask;
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
    private LoadWallpapersByRecentTask loadWallpapersByRecentTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

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
        wallpaperAdapter = new WallpaperAdapter(getActivity(), listWallpapers);
        gvWallpapers.setAdapter(wallpaperAdapter);
        gvWallpapers.setOnItemClickListener(this);

        wallpaperEndpoint = App.getWallpaperEndpoint();
        if (listWallpapers.isEmpty()) {
            updateUI(false, false);
            loadWallpapersByRecentTask = new LoadWallpapersByRecentTask(0, false);
            loadWallpapersByRecentTask.execute();
        } else {
            updateUI(false, true);
        }

        gvWallpapers.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                updateUI(true, false);
                loadWallpapersByRecentTask = new LoadWallpapersByRecentTask(App.getLoadedWallpapers().size(), true);
                loadWallpapersByRecentTask.execute();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (loadWallpapersByRecentTask != null)
            loadWallpapersByRecentTask.cancel(true);
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
            if (isComplete) {
                footerLoadMore.setVisibility(View.GONE);
            } else {
                footerLoadMore.setVisibility(View.VISIBLE);
            }
        } else {
            footerLoadMore.setVisibility(View.GONE);
            if (isComplete) {
                pbLoader.setVisibility(View.GONE);
                gvWallpapers.setVisibility(View.VISIBLE);
            } else {
                pbLoader.setVisibility(View.VISIBLE);
                gvWallpapers.setVisibility(View.GONE);
            }
        }
    }


    private void refreshGridView() {
        int gridViewEntrySize = DimensionUtils.dpToPx(getActivity(), 160);
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

    public void onEvent(LoadWallpapersByRecentEvent event){
        List<Wallpaper> wallpapers = App.getLoadedWallpapers();
        wallpaperAdapter.update(wallpapers);
        updateUI(event.isUpdate(), true);
    }

}