package jccom.example.appbantra.FragmentAd;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.R;
import jccom.example.appbantra.adapter.ProductAdapter_Ad;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Product_Ad_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter_Ad adapter;
    private Uri imageUri;
    private ImageView imageView; // ImageView để hiển thị ảnh đã chọn
    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ImageView btn_addProAd = view.findViewById(R.id.Iv_addProAd);
        recyclerView = view.findViewById(R.id.rcvProAd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        btn_addProAd.setOnClickListener(v -> showAddProductDialog());
        fetchProductList();
        return view;
    }

    private void fetchProductList() {
        Call<List<Product>> call = RetrofitClient.getApiService().getListProduct();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> productList = response.body();
                    Log.d("ProductList", "Số lượng sản phẩm: " + productList.size());
                    if (adapter == null) {
                        adapter = new ProductAdapter_Ad(productList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.updateProductList(productList);
                    }
                } else {
                    Toast.makeText(getContext(), "Không lấy được dữ liệu sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddProductDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_add_product, null);
        builder.setView(dialogView);

        EditText editCategoryId = dialogView.findViewById(R.id.editCategoryId);
        EditText editName = dialogView.findViewById(R.id.editProductName);
        EditText editPrice = dialogView.findViewById(R.id.editProductPrice);
        EditText editDescription = dialogView.findViewById(R.id.editProductDescription);
        imageView = dialogView.findViewById(R.id.imageView); // Khởi tạo imageView từ dialogView
        Button btnSelectImage = dialogView.findViewById(R.id.btnSelectImage);

        // Yêu cầu quyền truy cập bộ nhớ ngoài nếu chưa được cấp
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        // Mở thư viện ảnh khi người dùng nhấn vào Button
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        builder.setTitle("Thêm sản phẩm mới");
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String categoryId = editCategoryId.getText().toString();
            String name = editName.getText().toString();
            double price = Double.parseDouble(editPrice.getText().toString());
            String description = editDescription.getText().toString();

            if (imageUri != null) {
                String imagePath = copyFileToTempDir(imageUri);
                if (imagePath != null) {
                    uploadProductToApi(categoryId, name, price, description, imagePath);
                } else {
                    Toast.makeText(getContext(), "Lỗi trong quá trình chuẩn bị ảnh", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (imageView != null && imageUri != null) {
                // Hiển thị ảnh đã chọn vào imageView bằng Glide
                Glide.with(this).load(imageUri).into(imageView);
            } else {
                Toast.makeText(getContext(), "Không thể tải ảnh", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String copyFileToTempDir(Uri uri) {
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            File tempFile = new File(getContext().getCacheDir(), "temp_image.jpg");
            OutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
            return tempFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Không thể sao chép tệp", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void uploadProductToApi(String categoryId, String name, double price, String description, String imagePath) {
        File file = new File(imagePath);
        if (!file.exists()) {
            Toast.makeText(getContext(), "Ảnh không tồn tại hoặc không thể truy cập", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imageUrl", file.getName(), requestFile);
        RequestBody categoryIdBody = RequestBody.create(MediaType.parse("multipart/form-data"), categoryId);
        RequestBody nameBody = RequestBody.create(MediaType.parse("multipart/form-data"), name);
        RequestBody priceBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(price));
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("multipart/form-data"), description);

        ApiService apiService = RetrofitClient.getApiService();
        Call<Product> call = apiService.addProduct(categoryIdBody, nameBody, priceBody, descriptionBody, imagePart);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    fetchProductList();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("API Error", "Error response: " + errorBody);
                        Toast.makeText(getContext(), "Thêm sản phẩm thất bại: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e("API Error", "Không thể đọc phản hồi lỗi", e);
                        Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
