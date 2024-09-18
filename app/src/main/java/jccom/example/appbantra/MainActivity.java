package jccom.example.appbantra;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import jccom.example.appbantra.Fragment.CartFragment;
import jccom.example.appbantra.Fragment.HomeFragment;
import jccom.example.appbantra.Fragment.NotificationFragment;
import jccom.example.appbantra.Fragment.ProfileFragment;
import jccom.example.appbantra.adapter.AdapterViewPager;

public class MainActivity extends AppCompatActivity {

    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the correct layout before accessing any UI elements
        setContentView(R.layout.activity_main);

        pagerMain = findViewById(R.id.pagerMain);
        bottomNav = findViewById(R.id.bottomNav);

        // Add fragments to the list
        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new CartFragment());
        fragmentArrayList.add(new NotificationFragment());
        fragmentArrayList.add(new ProfileFragment());

        // Create and set the adapter for ViewPager2
        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        pagerMain.setAdapter(adapterViewPager);

        // Set a callback for page selection
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    bottomNav.setSelectedItemId(R.id.home);
                } else if (position == 1) {
                    bottomNav.setSelectedItemId(R.id.cart);
                } else if (position == 2) {
                    bottomNav.setSelectedItemId(R.id.notification);
                } else if (position == 3) {
                    bottomNav.setSelectedItemId(R.id.profile);
                }
                super.onPageSelected(position);
            }
        });

        // Set a listener for bottom navigation item selection
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    pagerMain.setCurrentItem(0);
                } else if (item.getItemId() == R.id.cart) {
                    pagerMain.setCurrentItem(1);
                } else if (item.getItemId() == R.id.notification) {
                    pagerMain.setCurrentItem(2);
                } else if (item.getItemId() == R.id.profile) {
                    pagerMain.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}
