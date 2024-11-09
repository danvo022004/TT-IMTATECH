package jccom.example.appbantra;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

import jccom.example.appbantra.Fragment.CartFragment;
import jccom.example.appbantra.Fragment.ProfileFragment;
import jccom.example.appbantra.FragmentAd.Order_Ad_Fragment;
import jccom.example.appbantra.FragmentAd.Product_Ad_Fragment;
import jccom.example.appbantra.FragmentAd.RevennueFragment;
import jccom.example.appbantra.adapter.AdapterViewPager;

public class Main_AdActivity extends AppCompatActivity {

    ViewPager2 pagerMainAd;
    ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    BottomNavigationView bottomNavAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_ad);

        pagerMainAd = findViewById(R.id.pagerMainAd);
        bottomNavAd = findViewById(R.id.bottomNavAd);

        // Add fragments to the list
        fragmentArrayList.add(new Product_Ad_Fragment());
        fragmentArrayList.add(new Order_Ad_Fragment());
        fragmentArrayList.add(new RevennueFragment());
        fragmentArrayList.add(new ProfileFragment());

        // Create and set the adapter for ViewPager2
        AdapterViewPager adapterViewPager = new AdapterViewPager(this, fragmentArrayList);
        pagerMainAd.setAdapter(adapterViewPager);

        // Set a callback for page selection
        pagerMainAd.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    bottomNavAd.setSelectedItemId(R.id.homead);
                } else if (position == 1) {
                    bottomNavAd.setSelectedItemId(R.id.cartad);
                } else if (position == 2) {
                    bottomNavAd.setSelectedItemId(R.id.revennuead);
                } else if (position == 3) {
                    bottomNavAd.setSelectedItemId(R.id.profilead);
                }
                super.onPageSelected(position);
            }
        });

        // Set a listener for bottom navigation item selection
        bottomNavAd.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homead) {
                    pagerMainAd.setCurrentItem(0);
                } else if (item.getItemId() == R.id.cartad) {
                    pagerMainAd.setCurrentItem(1);
                } else if (item.getItemId() == R.id.revennuead) {
                    pagerMainAd.setCurrentItem(2);
                } else if (item.getItemId() == R.id.profilead) {
                    pagerMainAd.setCurrentItem(3);
                }
                return true;
            }
        });
    }
}