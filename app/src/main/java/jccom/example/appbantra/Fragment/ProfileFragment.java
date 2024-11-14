package jccom.example.appbantra.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.FragmentAd.RevennueFragment;
import jccom.example.appbantra.LoginPage;
import jccom.example.appbantra.Model.User;
import jccom.example.appbantra.OrderActivity;
import jccom.example.appbantra.R;
import jccom.example.appbantra.RegisterPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private TextView tvUserName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //anh xa
        tvUserName = view.findViewById(R.id.tvUserName);
        TextView tvOderUser = view.findViewById(R.id.tvoderuser);
        tvOderUser.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), OrderActivity.class);
            startActivity(intent);
        });

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        // Kiểm tra nếu userId tồn tại
        if (userId != null) {
            // Gọi API để lấy thông tin người dùng
            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<User> call = apiService.getUser(userId);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        User user = response.body();
                        // Cập nhật UI với thông tin người dùng
                        tvUserName.setText("Hello: " + user.getName());
                    } else {
                        // Xử lý khi không nhận được dữ liệu hoặc có lỗi
                        tvUserName.setText("User not found");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // Xử lý khi gọi API thất bại
                    tvUserName.setText("Error: " + t.getMessage());
                }
            });
        } else {
            // Xử lý khi không có userId trong SharedPreferences
            tvUserName.setText("User not logged in");
        }

    }
}