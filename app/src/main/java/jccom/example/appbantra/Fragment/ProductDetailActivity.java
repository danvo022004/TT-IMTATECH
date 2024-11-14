package jccom.example.appbantra.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.MainActivity;
import jccom.example.appbantra.Model.CartRequest;
import jccom.example.appbantra.Model.CartResponse;
import jccom.example.appbantra.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage, btn_exit;
    private TextView productName, productPrice, productDescription, totalPrice, quantity, decreaseQuantity, increaseQuantity;
    private Button orderButton;

    private int quantityValue = 1;
    private String productId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Khởi tạo ApiService từ RetrofitClient
        apiService = RetrofitClient.getClient().create(ApiService.class);

        // Ánh xạ các view
        productImage = findViewById(R.id.product_image);
        productName = findViewById(R.id.product_name);
        productPrice = findViewById(R.id.product_price);
        productDescription = findViewById(R.id.product_description);
        totalPrice = findViewById(R.id.total_price);
        quantity = findViewById(R.id.quantity);
        decreaseQuantity = findViewById(R.id.decrease_quantity);
        increaseQuantity = findViewById(R.id.increase_quantity);
        orderButton = findViewById(R.id.order_button);
        btn_exit = findViewById(R.id.btn_exit);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("product_image");
        String name = intent.getStringExtra("product_name");
        String price = intent.getStringExtra("product_price");
        String description = intent.getStringExtra("product_description");
        productId = intent.getStringExtra("product_id");

        // Hiển thị dữ liệu
        Glide.with(this).load(imageUrl).into(productImage);
        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText(price);
        updateTotalPrice();

        // Thiết lập sự kiện cho nút tăng/giảm số lượng
        decreaseQuantity.setOnClickListener(v -> {
            if (quantityValue > 1) {
                quantityValue--;
                updateQuantity();
            }
        });
        increaseQuantity.setOnClickListener(v -> {
            quantityValue++;
            updateQuantity();
        });

        // Thiết lập sự kiện nhấn cho nút thoát
        btn_exit.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProductDetailActivity.this, MainActivity.class);
            startActivity(intent1);
        });

        // Thiết lập sự kiện nhấn cho nút đặt hàng
        orderButton.setOnClickListener(v -> addToCart());
    }

    private void updateQuantity() {
        quantity.setText(String.valueOf(quantityValue));
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        String priceText = productPrice.getText().toString().replace(" đ", "");
        if (priceText.isEmpty()) {
            priceText = "0";
        }
        try {
            double price = Double.parseDouble(priceText);
            double total = price * quantityValue;
            totalPrice.setText("Tổng tiền: " + total + " đ");
        } catch (NumberFormatException e) {
            totalPrice.setText("Tổng tiền: 0 đ");
        }
    }

    private void addToCart() {
        // Kiểm tra nếu productId là null
        if (productId == null || productId.isEmpty()) {
            Log.e("ProductDetail", "Product ID is null or empty");
            Toast.makeText(ProductDetailActivity.this, "Không có sản phẩm để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            return; // Dừng lại nếu không có productId
        }
        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        Log.d("TokenCheck", "Token: " + token); // Log token để kiểm tra

        // Kiểm tra xem token có tồn tại không
        if (token.isEmpty()) {
            Log.e("TokenCheck", "Token is missing!");
            Toast.makeText(ProductDetailActivity.this, "Token không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng CartRequest
        CartRequest cartRequest = new CartRequest(productId, quantityValue);

        // Log thông tin sản phẩm và số lượng
        Log.d("CartRequest", "Product ID: " + productId + ", Quantity: " + quantityValue);

        // Gọi API thêm vào giỏ hàng
        Call<CartResponse> call = apiService.addToCart("Bearer " + token, cartRequest);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()) {
                    // Log kết quả thành công
                    Log.d("API Response", "Success: " + response.body());
                    Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ProductDetailActivity.this, MainActivity.class);
                    startActivity(i);
                } else {
                    // Log chi tiết lỗi khi không thành công
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(ProductDetailActivity.this, "Lỗi khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                // Log chi tiết lỗi khi gặp sự cố kết nối
                Log.e("API Failure", "Error: " + t.getMessage());
                Toast.makeText(ProductDetailActivity.this, "thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(ProductDetailActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}