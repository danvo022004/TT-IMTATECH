package jccom.example.appbantra.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnProductClickListener onProductClickListener;

    // Interface để lắng nghe sự kiện nhấp vào sản phẩm
    public interface OnProductClickListener {
        void onProductClick(String productId);
    }

    // Constructor nhận vào danh sách sản phẩm và listener
    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList != null ? productList : new ArrayList<>(); // Kiểm tra null
        this.onProductClickListener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Sử dụng Glide để tải ảnh từ URL
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .into(holder.productImage);

        holder.productName.setText(product.getName());
        holder.productDescription.setText(product.getDescription());
        holder.productPrice.setText(String.format("%.2f", product.getPrice()));

        // Thiết lập sự kiện nhấp vào sản phẩm
        holder.itemView.setOnClickListener(v -> {
            if (onProductClickListener != null) {
                String productId = product.getId();
                if (productId != null) {
                    onProductClickListener.onProductClick(productId);
                    Log.d("ProductAdapter", "Clicked Product ID: " + productId);
                } else {
                    Log.e("ProductAdapter", "Product ID is null");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder cho mỗi item
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDescription, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productDescription = itemView.findViewById(R.id.product_description);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }
}