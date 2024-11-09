package jccom.example.appbantra;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import jccom.example.appbantra.Fragment.CartFragment;
import jccom.example.appbantra.Fragment.HomeFragment;
import jccom.example.appbantra.Fragment.NotificationFragment;
import jccom.example.appbantra.Fragment.ProfileFragment;
import jccom.example.appbantra.Fragment.RevennueFragment;
import jccom.example.appbantra.adapter.AdapterViewPager;

public class Main_AdActivity extends AppCompatActivity {

    ViewPager2 pagerMain;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView bottomNav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_ad);

        pagerMain = findViewById(R.id.pagerMainAd);
        bottomNav = findViewById(R.id.bottomNavAd);

        // Add fragments to the list
        fragmentArrayList.add(new HomeFragment());
        fragmentArrayList.add(new CartFragment());
        fragmentArrayList.add(new RevennueFragment());
        fragmentArrayList.add(new ProfileFragment());

        // Create and set the adapter for ViewPager2
        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        pagerMain.setAdapter(adapterViewPager);

        // Set a callback for page selection
        pagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    bottomNav.setSelectedItemId(R.id.homead);
                } else if (position == 1) {
                    bottomNav.setSelectedItemId(R.id.cartad);
                } else if (position == 2) {
                    bottomNav.setSelectedItemId(R.id.revennuead);
                } else if (position == 3) {
                    bottomNav.setSelectedItemId(R.id.profilead);
                }
                super.onPageSelected(position);
            }
        });

        // Set a listener for bottom navigation item selection
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homead) {
                    pagerMain.setCurrentItem(0);
                } else if (item.getItemId() == R.id.cartad) {
                    pagerMain.setCurrentItem(1);
                } else if (item.getItemId() == R.id.revennuead) {
                    pagerMain.setCurrentItem(2);
                } else if (item.getItemId() == R.id.profilead) {
                    pagerMain.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}