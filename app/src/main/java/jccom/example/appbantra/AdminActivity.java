package jccom.example.appbantra;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity {

    private TextView tvWelcome, tvUserName, tvUserEmail, tvUserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);

        // Ánh xạ các TextView
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvUserPhone = findViewById(R.id.tvUserPhone);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
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
                        tvUserName.setText("Name: " + user.getName());
                        tvUserEmail.setText("Email: " + user.getEmail());
                        tvUserPhone.setText("Phone: " + user.getPhone());
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
