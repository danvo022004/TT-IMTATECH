package jccom.example.appbantra;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Order;
import jccom.example.appbantra.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;

public class CheckoutActivity extends AppCompatActivity {

    private TextView totalAmountText;
    private RadioGroup paymentMethodGroup;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Khởi tạo các thành phần UI
        totalAmountText = findViewById(R.id.totalAmountText);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        checkoutButton = findViewById(R.id.checkoutButton);

        // Lấy dữ liệu từ Intent
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0.0);

        // Hiển thị tổng tiền
        totalAmountText.setText(String.format("Tổng tiền: %,.0fđ", totalAmount));

        // Lấy danh sách productIds từ Intent
        ArrayList<String> selectedProductIds = getIntent().getStringArrayListExtra("selectedProductIds");

        // Log danh sách productIds
        if (selectedProductIds != null) {
            Log.d("CheckoutActivity", "Product IDs: " + selectedProductIds.toString());
        } else {
            Log.d("CheckoutActivity", "No product IDs received");
        }

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", null);

        // Kiểm tra token có tồn tại hay không
        if (token == null) {
            Toast.makeText(this, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Xử lý sự kiện thanh toán
        checkoutButton.setOnClickListener(v -> {
            // Lấy phương thức thanh toán đã chọn
            int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
            String paymentMethod = "";

            if (selectedPaymentMethodId == R.id.paymentCreditCard) {
                paymentMethod = "credit_card";
            } else if (selectedPaymentMethodId == R.id.paymentCashOnDelivery) {
                paymentMethod = "cash_on_delivery";
            }

            // Log phương thức thanh toán
            Log.d("Payment Method", "Selected Payment Method: " + paymentMethod);

            if (paymentMethod.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo đối tượng Order
            Order order = new Order();
            order.setPaymentMethod(paymentMethod);
//            order.setTotal_price(totalAmount);
            order.setProductIds(selectedProductIds);  // Gán danh sách productIds vào Order

            // Gọi API để đặt hàng
            placeOrder(token, order);
        });
    }

    private void placeOrder(String token, Order order) {
        ApiService apiService = RetrofitClient.getApiService();

        // Log token và productIds
        Log.d("API Request", "Token: " + token);
        Log.d("API Request", "Product IDs: " + order.getProductIds());

        // Tạo request call với token và order
        Call<Order> call = apiService.placeOrder("Bearer " + token, order);

        // Log request URL
        Log.d("API Request", "Request URL: " + call.request().url());

        // Gửi request
        call.enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful()) {
                    // Đặt hàng thành công
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình thanh toán
                } else {
                    Log.e("API Request", "Code: " + response.code() + ", Message: " + response.message());
                    Toast.makeText(CheckoutActivity.this, "Đặt hàng thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Toast.makeText(CheckoutActivity.this, "Lỗi kết nối. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
