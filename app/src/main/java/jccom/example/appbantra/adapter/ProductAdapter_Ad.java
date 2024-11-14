package jccom.example.appbantra.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.R;
import jccom.example.appbantra.API.ApiService;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter_Ad extends RecyclerView.Adapter<ProductAdapter_Ad.ViewHolder> {

    private List<Product> productList;

    public ProductAdapter_Ad(List<Product> productList) {
        this.productList = productList;
    }

    public void updateProductList(List<Product> products) {
        productList.clear();
        productList.addAll(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_management, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.nameTextView.setText(product.getName());
        holder.priceTextView.setText(String.format("%.0fđ", product.getPrice()));
        holder.descriptionTextView.setText(product.getDescription());

        // Xây dựng URL đầy đủ nếu cần thiết
        String imageUrl = product.getImageUrl();
        if (!imageUrl.startsWith("http")) {
            // Giả sử `http://<ip-address>:<port>` là URL gốc của máy chủ
            imageUrl = "http://<ip-address>:<port>/" + imageUrl;
        }

        // Sử dụng Glide để tải ảnh từ URL đầy đủ
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder) // Đặt ảnh mặc định trong khi tải
                .into(holder.productImageView);

        holder.editButton.setOnClickListener(view -> {
            showEditDialog(holder.itemView.getContext(), product);
        });
    }


    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView, descriptionTextView;
        ImageView productImageView, editButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

    private void showEditDialog(Context context, Product product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_product, null);
        builder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.editProductName);
        EditText editDescription = dialogView.findViewById(R.id.editProductDescription);
        EditText editPrice = dialogView.findViewById(R.id.editProductPrice);
        EditText editStatus = dialogView.findViewById(R.id.editProductStatus);

        // Đặt dữ liệu hiện tại vào các trường
        editName.setText(product.getName());
        editDescription.setText(product.getDescription());
        editPrice.setText(String.valueOf(product.getPrice()));
        editStatus.setText(String.valueOf(product.isStatus()));

        builder.setTitle("Chỉnh sửa sản phẩm");
        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            String newName = editName.getText().toString();
            String newDescription = editDescription.getText().toString();
            double newPrice = Double.parseDouble(editPrice.getText().toString());
            boolean newStatus = Boolean.parseBoolean(editStatus.getText().toString());

            updateProductOnApi(context, product.getId(), newName, newDescription, newPrice, newStatus);
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateProductOnApi(Context context, String productId, String name, String description, double price, boolean status) {
        ApiService apiService = RetrofitClient.getApiService();

        RequestBody nameBody = RequestBody.create(okhttp3.MultipartBody.FORM, name);
        RequestBody descriptionBody = RequestBody.create(okhttp3.MultipartBody.FORM, description);
        RequestBody priceBody = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(price));
        RequestBody statusBody = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(status));

        Call<Product> call = apiService.updateProduct(productId, null, nameBody, priceBody, descriptionBody, statusBody, null);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    refreshProductList(context);
                } else {
                    Toast.makeText(context, "Cập nhật sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshProductList(Context context) {
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Product>> call = apiService.getListProduct();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body());
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(context, "Không thể tải lại danh sách sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
