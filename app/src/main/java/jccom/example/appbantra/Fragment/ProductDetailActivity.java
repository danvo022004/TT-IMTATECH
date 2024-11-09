package jccom.example.appbantra.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import jccom.example.appbantra.MainActivity;
import jccom.example.appbantra.R;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView productImage, btn_exit;
    private TextView productName, productPrice, productDescription, totalPrice, quantity, decreaseQuantity, increaseQuantity;
    private Button orderButton;

    private int quantityValue = 1; // Số lượng mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

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

        // Hiển thị dữ liệu
        Glide.with(this).load(imageUrl).into(productImage);
        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText(price); // Hiển thị giá tiền
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

        // Thiết lập sự kiện nhấn cho nút
        btn_exit.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProductDetailActivity.this, MainActivity.class);
            startActivity(intent1); // Bắt đầu SecondActivity
        });

        orderButton.setOnClickListener(v -> {
            // Xử lý đặt hàng (có thể thêm logic ở đây)
        });
    }

    private void updateQuantity() {
        quantity.setText(String.valueOf(quantityValue));
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        String priceText = productPrice.getText().toString().replace(" đ", ""); // Loại bỏ ký tự "đ" nếu có
        if (priceText.isEmpty()) {
            priceText = "0"; // Gán giá mặc định nếu chuỗi rỗng
        }
        try {
            double price = Double.parseDouble(priceText); // Chuyển đổi chuỗi thành số
            double total = price * quantityValue;
            totalPrice.setText("Tổng tiền: " + total + " đ");
        } catch (NumberFormatException e) {
            totalPrice.setText("Tổng tiền: 0 đ"); // Xử lý lỗi nếu không thể chuyển đổi
        }
    }
}