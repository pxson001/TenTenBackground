package gokustudio.tentenbackground.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import gokustudio.tentenbackground.App;
import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.activities.WallpaperByCategoryActivity;
import gokustudio.tentenbackground.adapters.CategoryAdapter;
import gokustudio.tentenbackground.models.parse.Category;
import gokustudio.tentenbackground.parse.CategoryEndpoint;

/**
 * Created by son on 11/2/15.
 */
public class CategoriesFragment extends Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.scroll)
    ObservableListView lvCategories;

    @Bind(R.id.fragment_categoríes_pb_loader)
    ProgressBar pbLoader;

    private Context context;

    List<Category> listCategories;

    private CategoryAdapter categoryAdapter;
    private LoadCategoriesTask loadCategoriesTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        ButterKnife.bind(this, view);

        Fragment parentFragment = getParentFragment();
        ViewGroup viewGroup = (ViewGroup) parentFragment.getView();
        if (viewGroup != null) {
            lvCategories.setTouchInterceptionViewGroup((ViewGroup) viewGroup.findViewById(R.id.container));
            if (parentFragment instanceof ObservableScrollViewCallbacks) {
                lvCategories.setScrollViewCallbacks((ObservableScrollViewCallbacks) parentFragment);
            }
        }
        listCategories = App.getListCategories();
        categoryAdapter = new CategoryAdapter(context, listCategories);
        lvCategories.setAdapter(categoryAdapter);
        lvCategories.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CategoryEndpoint categoryEndpoint = App.getWallpaperEndpoint().getCategoryEndpoint();

        if (listCategories == null || listCategories.isEmpty()) {
            loadCategoriesTask = new LoadCategoriesTask(categoryEndpoint);
            loadCategoriesTask.execute();
        } else {
            updateUI(false);
        }
    }

    @Override
    public void onDestroyView() {
        if (loadCategoriesTask != null) {
            loadCategoriesTask.cancel(true);
        }
        super.onDestroyView();
    }

    private void updateUI(boolean isUpdate) {
        if (isUpdate) {
            pbLoader.setVisibility(View.VISIBLE);
            lvCategories.setVisibility(View.GONE);
        } else {
            pbLoader.setVisibility(View.GONE);
            lvCategories.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Category category = categoryAdapter.getItem(i);
        App.clearLoadedWallpaperByCategory();
        App.setCurrentCategoryObjectId(category.getObjectId());

        Intent intent = new Intent(context, WallpaperByCategoryActivity.class);
        intent.putExtra(WallpaperByCategoryActivity.EXTRA_CATEGORY, category);
        startActivity(intent);
    }

    class LoadCategoriesTask extends AsyncTask<Void, Void, List<Category>> {

        private CategoryEndpoint categoryEndpoint;

        public LoadCategoriesTask(CategoryEndpoint categoryEndpoint) {
            this.categoryEndpoint = categoryEndpoint;
        }

        @Override
        protected void onPreExecute() {
            updateUI(true);
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            List<Category> categories = categoryEndpoint.getAll();
            return categories;
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            System.out.println("Categories size: " + categories.size());
            if (App.getListCategories().isEmpty())
                App.setListCategories(categories);
            listCategories = App.getListCategories();
            categoryAdapter.update(listCategories);
            updateUI(false);
        }
    }
}
