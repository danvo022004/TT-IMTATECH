package jccom.example.appbantra;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {

    // Các trường nhập liệu
    private TextInputEditText editTextName, editTextEmail, editTextPhone;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Lấy tham chiếu đến các view
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSave = findViewById(R.id.buttonSave);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", null);

        // Kiểm tra nếu có userId, tải thông tin người dùng
        if (userId != null) {
            getUserInfo(userId);
        }

        // Xử lý sự kiện click Save
        buttonSave.setOnClickListener(v -> updateUserInfo(userId));
    }

    // Lấy thông tin người dùng từ API
    private void getUserInfo(String userId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = apiService.getUser(userId);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Hiển thị thông tin người dùng vào các trường nhập liệu
                    User user = response.body();
                    editTextName.setText(user.getName());
                    editTextEmail.setText(user.getEmail());
                    editTextPhone.setText(user.getPhone());
                } else {
                    Toast.makeText(UserActivity.this, "Failed to load user info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Cập nhật thông tin người dùng
    private void updateUserInfo(String userId) {
        // Lấy thông tin từ các trường nhập liệu
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();

        // Kiểm tra số điện thoại
        if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || phone.length() < 10) {
            Toast.makeText(UserActivity.this, "SĐT không đúng định dạng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra định dạng email nếu có nhập
        if (!TextUtils.isEmpty(email) && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(UserActivity.this, "Email không đúng định dang", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng User với thông tin mới
        // Chỉ gửi tên và email nếu người dùng nhập vào
        User user = new User(phone, name, email.isEmpty() ? "" : email);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<User> call = apiService.updateUser(userId, user);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    // Cập nhật thành công
                    Toast.makeText(UserActivity.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserActivity.this, LoginPage.class);
                    startActivity(intent);
                } else {
                    // Nếu có lỗi
                    Toast.makeText(UserActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(UserActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
