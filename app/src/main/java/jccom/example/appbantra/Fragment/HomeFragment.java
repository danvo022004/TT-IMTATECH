package jccom.example.appbantra.Fragment;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jccom.example.appbantra.R;

public class HomeFragment extends Fragment {

    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManHinhChinh;
    DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views using the inflated view
        AnhXa(view);

        // Start the ViewFlipper action
        ActionViewFlipper();

        return view; // Return the inflated view, not null
    }

    private void ActionViewFlipper() {
        List<String> manquangcao = new ArrayList<>();
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/01.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/05.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/06.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/07.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/03.jpg");

        for (int i = 0; i < manquangcao.size(); i++) {
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            Glide.with(getActivity().getApplicationContext()).load(manquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void AnhXa(View view) {
        viewFlipper = view.findViewById(R.id.viewFlipper);
        recyclerViewManHinhChinh = view.findViewById(R.id.recycler_view);
        drawerLayout = view.findViewById(R.id.drawerlayout);
    }
}
