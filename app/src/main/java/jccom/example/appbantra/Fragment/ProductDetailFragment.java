package jccom.example.appbantra.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailFragment extends Fragment {

    private TextView productName, productDescription, productPrice;
    private ImageView productImage;
    private String productId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);

        // Khởi tạo các view
        productName = view.findViewById(R.id.product_name);
        productDescription = view.findViewById(R.id.product_description);
        productPrice = view.findViewById(R.id.product_price);
        productImage = view.findViewById(R.id.product_image);

        // Nhận ID sản phẩm từ Bundle
        if (getArguments() != null) {
            productId = getArguments().getString("product_id");
            fetchProductDetails(productId);
        }

        return view;
    }

    private void fetchProductDetails(String productId) {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<Product> call = apiService.getProductById(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();
                    displayProductDetails(product);
                } else {
                    Log.e("ProductDetailFragment", "Error: " + response.message());
                    Toast.makeText(getContext(), "Không thể tải thông tin sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("ProductDetailFragment", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductDetails(Product product) {
        productName.setText(product.getName());
        productDescription.setText(product.getDescription());
        productPrice.setText(String.format("%.2f", product.getPrice()));
        Glide.with(this).load(product.getImageUrl()).into(productImage);
    }
}