package jccom.example.appbantra;

import android.content.Intent;
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
import jccom.example.appbantra.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterPage extends AppCompatActivity {

    private TextInputEditText editTextName, editTextSDT, editTextPassword, editTextConfirmPassword;
    private Button signUp;
    private TextView signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        // Lấy tham chiếu các View
        editTextName = findViewById(R.id.username);
        editTextSDT = findViewById(R.id.sdt);
        editTextPassword = findViewById(R.id.password);
        editTextConfirmPassword = findViewById(R.id.cofirmpassword);

        signUp = findViewById(R.id.sign_up);
        signIn = findViewById(R.id.sign_in);

        // Chuyển đến màn hình đăng nhập nếu đã có tài khoản
        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterPage.this, LoginPage.class);
            startActivity(intent);
            finish();
        });

        // Xử lý khi nhấn đăng ký
        signUp.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        // Lấy giá trị từ các input'
        String name = editTextName.getText().toString().trim();
        String phone = editTextSDT.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();
        // Kiểm tra xem các trường có rỗng không
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ) {
            Toast.makeText(RegisterPage.this, "Không được để rỗng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra số điện thoại
        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            Toast.makeText(RegisterPage.this, "SĐT không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu: ít nhất 6 ký tự
        if (password.length() < 6) {
            Toast.makeText(RegisterPage.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu có khớp không
        if (!password.equals(confirmPassword)) {
            Toast.makeText(RegisterPage.this, "Mật khẩu khống khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng User để gửi lên API
        User user = new User(name, phone, password);

        // Gọi API đăng ký người dùng
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Void> call = apiService.register(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Đăng ký thành công
                    Toast.makeText(RegisterPage.this, "Đăng ký Thành Công", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình đăng nhập
                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                    startActivity(intent);
                } else {
                    // Nếu đăng ký thất bại
                    if (response.code() == 400) {
                        Toast.makeText(RegisterPage.this, "Số điện thoại đã tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterPage.this, "Đăng Ký Thất Bại: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterPage.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
