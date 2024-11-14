package jccom.example.appbantra.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Category;
import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.R;
import jccom.example.appbantra.adapter.CategoryAdapter;
import jccom.example.appbantra.adapter.ProductAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ViewFlipper viewFlipper;
    private RecyclerView recyclerViewCategory, recycler_view;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    private List<Product> productList;
    private List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo các view
        AnhXa(view);

        // Thiết lập RecyclerView cho danh mục
        setupRecyclerView();

        // Fetch danh sách sản phẩm đầu tiên
        fetchProducts();

        // Fetch danh mục từ API
        fetchCategories();

        // Cấu hình ViewFlipper cho quảng cáo
        ActionViewFlipper();

        return view;
    }

    private void fetchProducts() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getListProduct();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Error: " + response.message());
                    Toast.makeText(getContext(), "Không thể tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("HomeFragment", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchCategories() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Category>> call = apiService.getListCategory();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryList.clear();
                    categoryList.addAll(response.body());
                    for (Category category : categoryList) {
                        Log.d("Categories", "ID: " + category.getId() + ", Name: " + category.getName());
                    }
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Error: " + response.message());
                    Toast.makeText(getContext(), "Không thể tải danh mục", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e("HomeFragment", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchProductsByCategoryId(String categoryId) {
        Log.d("HomeFragment", "Fetching products for Category ID: " + categoryId);
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Product>> call = apiService.getProductsByCategoryId(categoryId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    productAdapter.notifyDataSetChanged();
                } else {
                    Log.e("HomeFragment", "Error: " + response.message());
                    Toast.makeText(getContext(), "Không thể tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("HomeFragment", "Failure: " + t.getMessage());
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView() {
        categoryList = new ArrayList<>();
        productList = new ArrayList<>();

        // Thiết lập Adapter cho danh mục với sự kiện nhấp vào danh mục
        categoryAdapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCategoryClickListener() {
            @Override
            public void onCategoryClick(String categoryId) {
                Log.d("HomeFragment", "Category ID clicked: " + categoryId);
                if (categoryId != null) {
                    fetchProductsByCategoryId(categoryId); // Lấy sản phẩm khi nhấp vào danh mục
                } else {
                    Log.e("HomeFragment", "Category ID is null");
                }
            }
        });

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setAdapter(categoryAdapter);

        // Thiết lập Adapter cho danh sách sản phẩm với listener
        productAdapter = new ProductAdapter(productList, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(String productId) {
                Log.d("HomeFragment", "Product clicked: " + productId);
                // Mở ProductDetailActivity thay vì ProductDetailFragment
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                // Lấy sản phẩm từ productList theo productId
                Product clickedProduct = getProductById(productId); // Thay thế bằng cách lấy sản phẩm tương ứng
                if (clickedProduct != null) {
                    intent.putExtra("product_image", clickedProduct.getImageUrl());
                    intent.putExtra("product_name", clickedProduct.getName());
                    intent.putExtra("product_price", String.valueOf(clickedProduct.getPrice()));
                    intent.putExtra("product_description", clickedProduct.getDescription());
                    intent.putExtra("product_id", clickedProduct.getId());
                    startActivity(intent);
                }
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2); // 2 cột
        recycler_view.setLayoutManager(gridLayoutManager);
        recycler_view.setAdapter(productAdapter);
    }

    private Product getProductById(String productId) {
        for (Product product : productList) {
            if (product.getId().equals(productId)) {
                return product;
            }
        }
        return null; // Nếu không tìm thấy sản phẩm
    }

    private void ActionViewFlipper() {
        List<String> manquangcao = new ArrayList<>();
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/01.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/05.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/06.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/07.jpg");
        manquangcao.add("https://tienthientra.vn/wp-content/uploads/2022/09/03.jpg");

        for (String url : manquangcao) {
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            Glide.with(getActivity().getApplicationContext()).load(url).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }

        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void AnhXa(View view) {
        viewFlipper = view.findViewById(R.id.viewFlipper);
        recyclerViewCategory = view.findViewById(R.id.categoryRecyclerView);
        recycler_view = view.findViewById(R.id.recycler_view);
    }
}