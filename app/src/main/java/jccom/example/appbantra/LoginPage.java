package jccom.example.appbantra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.LoginRequest;
import jccom.example.appbantra.Model.AuthResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {

    private TextInputEditText editTextSDT, editTextPassword;
    private Button signIn;
    private TextView signUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        editTextSDT = findViewById(R.id.sdt);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);

        // Chuyển hướng đến trang đăng ký
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(intent);
            finish();
        });

        signIn.setOnClickListener(v -> loginUser());
    }

    private  void loginUser() {
        String phone = editTextSDT.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Kiểm tra xem các trường nhập liệu có hợp lệ không
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginPage.this, "Không được để rỗng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại
        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            Toast.makeText(LoginPage.this, "SĐT không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu: ít nhất 6 ký tự
        if (password.length() < 6) {
            Toast.makeText(LoginPage.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng LoginRequest
        LoginRequest loginRequest = new LoginRequest(phone, password);

        // Gửi yêu cầu đăng nhập
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<AuthResponse> call = apiService.login(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Nhận token và thông tin người dùng
                    String token = response.body().getToken();
                    String userId = response.body().getUser().getId();
                    String role = response.body().getUser().getRole();

                    // Lưu token vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TOKEN", token);
                    editor.putString("USER_ID", userId);
                    editor.putString("ROLE", role);
                    editor.apply();

                    // Kiểm tra role và điều hướng
                    if (role.equals("admin")) {
                        // Nếu là admin, chuyển đến màn hình admin
                        Intent intent = new Intent(LoginPage.this, AdminActivity.class);
                        startActivity(intent);
                    } else {
                        // Nếu là user, chuyển đến màn hình chính của người dùng
                        Intent intent = new Intent(LoginPage.this, UserActivity.class);
                        startActivity(intent);
                    }

                    finish(); // Đóng màn hình đăng nhập
                } else {
                    Toast.makeText(LoginPage.this, "Login failed! Please check your credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginPage.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Phương thức kiểm tra token có hợp lệ không
    private void checkToken() {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);
        String userId = sharedPreferences.getString("USER_ID", null);
        String role = sharedPreferences.getString("ROLE", null);

        if (token != null) {
            // Token có hợp lệ, kiểm tra quyền và chuyển hướng
            if (role != null) {
                if (role.equals("admin")) {
                    // Chuyển đến màn hình quản trị
                    Intent intent = new Intent(LoginPage.this, AdminActivity.class);
                    startActivity(intent);
                } else {
                    // Chuyển đến màn hình người dùng
                    Intent intent = new Intent(LoginPage.this, UserActivity.class);
                    startActivity(intent);
                }
            }
        }
    }
}