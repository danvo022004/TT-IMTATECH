package jccom.example.appbantra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

        initializeViews();

        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(LoginPage.this, RegisterPage.class);
            startActivity(intent);
            finish();
        });

        signIn.setOnClickListener(v -> loginUser());
    }

    private void initializeViews() {
        editTextSDT = findViewById(R.id.sdt);
        editTextPassword = findViewById(R.id.password);
        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
    }

    private void loginUser() {
        String phone = editTextSDT.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!isInputValid(phone, password)) return;

        LoginRequest loginRequest = new LoginRequest(phone, password);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<AuthResponse> call = apiService.login(loginRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                handleLoginResponse(response);
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginPage.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isInputValid(String phone, String password) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginPage.this, "Không được để rỗng", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            Toast.makeText(LoginPage.this, "SĐT không đúng định dạng", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(LoginPage.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void handleLoginResponse(Response<AuthResponse> response) {
        if (response.isSuccessful() && response.body() != null) {
            AuthResponse authResponse = response.body();
            saveUserData(authResponse);
            navigateToMainActivity();
            finish();
        } else {
            Toast.makeText(LoginPage.this, "Đăng nhập thất bại! Vui lòng kiểm tra thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserData(AuthResponse authResponse) {
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TOKEN", authResponse.getToken());
        editor.putString("USER_ID", authResponse.getUser().getId());
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginPage.this, OrderActivity.class);
        startActivity(intent);
    }
}
