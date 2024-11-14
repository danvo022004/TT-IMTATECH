package jccom.example.appbantra.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.CartItem;
import jccom.example.appbantra.Model.CartResponse;
import jccom.example.appbantra.Model.CartRequest;
import jccom.example.appbantra.R;
import jccom.example.appbantra.adapter.CartAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CartFragment extends Fragment implements CartAdapter.CartItemListener {

    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private ApiService apiService;
    private List<String> selectedProductIds;
    private String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService = RetrofitClient.getClient().create(ApiService.class);
        selectedProductIds = new ArrayList<>();

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        totalPriceTextView = view.findViewById(R.id.total_price_text_view);
        checkoutButton = view.findViewById(R.id.checkout_button_cart);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(cartAdapter);

        checkoutButton.setOnClickListener(v -> performCheckout());

        // Retrieve token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AppPrefs", MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "");
        if (token.isEmpty()) {
            Log.e("TokenCheck", "Token is missing in CartFragment!");
            Toast.makeText(getContext(), "Token không hợp lệ", Toast.LENGTH_SHORT).show();
        } else {
            fetchCartItems();
        }
    }

    private void fetchCartItems() {
        Call<CartResponse> call = apiService.viewCart("Bearer " + token);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    if (cartResponse.getCart() != null && cartResponse.getCart().getProducts() != null) {
                        cartAdapter.updateItems(cartResponse.getCart().getProducts());
                        updateTotalPrice(cartResponse.getTotalAmount());
                    } else {
                        Toast.makeText(getContext(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "Không thể lấy thông tin giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice(double totalAmount) {
        totalPriceTextView.setText(String.format("Tổng tiền: %,.0fđ", totalAmount));
    }

    @Override
    public void onItemClick(CartItem item) {
        if (item.getProduct() != null) {
            Toast.makeText(getContext(), "Đã chọn: " + item.getProduct().getName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(CartItem item) {
        if (item.getProduct() != null) {
            String productId = item.getProduct().getId();
            if (selectedProductIds.contains(productId)) {
                selectedProductIds.remove(productId);
            } else {
                selectedProductIds.add(productId);
            }
            calculateSelectedTotal();
        } else {
            Log.e("CartFragment", "Product is null in CartItem");
        }
    }

    private void calculateSelectedTotal() {
        Call<CartResponse> call = apiService.calculateSelectedTotal("Bearer " + token, selectedProductIds);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    updateTotalPrice(cartResponse.getSelectedTotalAmount());
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "Không tính được tổng số đã chọn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRemoveItem(CartItem item) {
        if (item.getProduct() != null) {
            removeProductFromCart(item.getProduct().getId());
        } else {
            Log.e("CartFragment", "Cannot remove item: Product is null");
        }
    }

    @Override
    public void onUpdateQuantity(CartItem item, int newQuantity) {
        if (item.getProduct() != null) {
            updateProductQuantity(item.getProduct().getId(), newQuantity);
        } else {
            Log.e("CartFragment", "Cannot update quantity: Product is null");
        }
    }

    private void removeProductFromCart(String productId) {
        Call<CartResponse> call = apiService.removeProductFromCart("Bearer " + token, productId);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    cartAdapter.updateItems(cartResponse.getCart().getProducts());
                    updateTotalPrice(cartResponse.getTotalAmount());
                    Toast.makeText(getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProductQuantity(String productId, int newQuantity) {
        CartRequest cartRequest = new CartRequest(productId, newQuantity);
        Call<CartResponse> call = apiService.addToCart("Bearer " + token, cartRequest);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    cartAdapter.updateItems(cartResponse.getCart().getProducts());
                    updateTotalPrice(cartResponse.getTotalAmount());
                    Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API Error", "Error: " + response.code() + " " + response.message());
                    Toast.makeText(getContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API Failure", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void performCheckout() {
        // Implement checkout logic here
        Toast.makeText(getContext(), "Tiến hành thanh toán", Toast.LENGTH_SHORT).show();
    }
}