package gokustudio.tentenbackground.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import gokustudio.tentenbackground.R;
import gokustudio.tentenbackground.fragments.MainFragment;
import gokustudio.tentenbackground.fragments.WallpaperDownloadFragment;
import gokustudio.tentenbackground.fragments.WallpaperFavoriteFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    Fragment f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.drawer_item_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName(R.string.drawer_item_settings);
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(true)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar).withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_home).withName("Home").withIdentifier(1),
                        new SecondaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_shuffle).withName("Shuffle").withIdentifier(2),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_favorite).withName("Favorite").withIdentifier(3),
                        new SecondaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_download).withName("Download").withIdentifier(4),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withIcon(GoogleMaterial.Icon.gmd_settings).withName("Settings").withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D

                        Fragment f = null;

                        switch (drawerItem.getIdentifier()) {
                            case 3:
                                f = WallpaperFavoriteFragment.getInstance();
                                break;
                            case 4:
                                f = WallpaperDownloadFragment.getInstance();
                                break;
                            default:
                                f = MainFragment.getInstance();
                                break;
                        }

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_container, f).addToBackStack(f.getTag());
                        fragmentTransaction.commit();


                        return false;
                    }
                })
                .build();

        f = MainFragment.getInstance();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, f).addToBackStack(f.getTag()).commit();

    }

}
