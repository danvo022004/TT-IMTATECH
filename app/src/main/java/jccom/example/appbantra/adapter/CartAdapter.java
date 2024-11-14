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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.CartItem;
import jccom.example.appbantra.Model.CartResponse;
import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.R;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> items;
    private CartItemListener listener;

    public CartAdapter(List<CartItem> items, CartItemListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        if (item != null) {
            holder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void updateItems(List<CartItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    // Thêm phương thức getter cho items
    public List<CartItem> getItems() {
        return items;
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productDescription, productPrice, quantityText;
        ImageView decreaseQuantity, increaseQuantity, removeItem;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image_cart);
            productName = itemView.findViewById(R.id.product_name_cart);
            productDescription = itemView.findViewById(R.id.product_description_cart);
            productPrice = itemView.findViewById(R.id.product_price_cart);
            quantityText = itemView.findViewById(R.id.quantity_text_cart);
            decreaseQuantity = itemView.findViewById(R.id.decrease_quantity_cart);
            increaseQuantity = itemView.findViewById(R.id.increase_quantity_cart);
            removeItem = itemView.findViewById(R.id.remove_item_cart);
        }

        void bind(final CartItem item) {
            Product product = item.getProductId();

            if (product != null) {
                String imageUrl = product.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.error_image)
                                    .error(R.drawable.error_image)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                            .into(productImage);
                } else {
                    productImage.setImageResource(R.drawable.error_image);
                }

                productName.setText(product.getName());
                productDescription.setText(product.getDescription());
                updatePrice(item);
            } else {
                productName.setText("No product");
                productDescription.setText("No description");
                productPrice.setText("0đ");
                productImage.setImageResource(R.drawable.error_image);
            }

            quantityText.setText(String.valueOf(item.getQuantity()));

            // Xử lý sự kiện giảm số lượng
            decreaseQuantity.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    int newQuantity = item.getQuantity() - 1;
                    updateQuantity(item, newQuantity);
                }
            });

            // Xử lý sự kiện tăng số lượng
            increaseQuantity.setOnClickListener(v -> {
                int newQuantity = item.getQuantity() + 1;
                updateQuantity(item, newQuantity);
            });

            // Xử lý sự kiện xóa sản phẩm
            removeItem.setOnClickListener(v -> removeProduct(item));
        }

        private void updateQuantity(CartItem item, int newQuantity) {
            item.setQuantity(newQuantity);
            quantityText.setText(String.valueOf(newQuantity));
            updatePrice(item);
            listener.onUpdateQuantity(item, newQuantity);
        }

        private void updatePrice(CartItem item) {
            Product product = item.getProductId();
            if (product != null) {
                double totalPrice = product.getPrice() * item.getQuantity();
                productPrice.setText(String.format("%,.0fđ", totalPrice));
            }
        }

        private void removeProduct(CartItem item) {
            String token = "Bearer " + "your_token_here"; // Replace with actual token retrieval logic
            String productId = item.getProductId().getId();

            RetrofitClient.getApiService().removeProductFromCart(token, productId)
                    .enqueue(new retrofit2.Callback<CartResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<CartResponse> call, retrofit2.Response<CartResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("CartAdapter", "Product removed successfully.");
                                int position = getAdapterPosition();
                                if (position != RecyclerView.NO_POSITION) {
                                    double removedItemPrice = item.getProductId().getPrice() * item.getQuantity();
                                    items.remove(position);
                                    notifyItemRemoved(position);
                                    updateTotalPrice(-removedItemPrice);
                                    listener.onRemoveItem(item);
                                }
                            } else {
                                Log.d("CartAdapter", "Failed to remove product: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<CartResponse> call, Throwable t) {
                            Log.d("CartAdapter", "Error: " + t.getMessage());
                        }
                    });
        }
    }

    private void updateTotalPrice(double priceChange) {
        listener.onUpdateTotalPrice(priceChange);
    }

    public interface CartItemListener {
        void onItemClick(CartItem item);
        void onItemSelected(CartItem item);
        void onRemoveItem(CartItem item);
        void onUpdateQuantity(CartItem item, int newQuantity);
        void onUpdateTotalPrice(double priceChange);
    }
}