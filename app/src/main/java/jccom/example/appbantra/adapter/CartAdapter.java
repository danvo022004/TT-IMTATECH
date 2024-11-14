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
import java.util.List;
import jccom.example.appbantra.Model.CartItem;
import jccom.example.appbantra.R;

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
            if (item.getProduct() != null) {
                String imageUrl = item.getProduct().getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(imageUrl)
                            .apply(new RequestOptions()
                                    .placeholder(R.drawable.error_image)
                                    .error(R.drawable.error_image)
                                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                            .into(productImage);
                } else {
                    Log.e("CartAdapter", "Image URL is null or empty for product: " + item.getProduct().getName());
                    productImage.setImageResource(R.drawable.error_image);
                }
                productName.setText(item.getProduct().getName());
                productDescription.setText(item.getProduct().getDescription());
                productPrice.setText(String.format("%,.0fđ", item.getProduct().getPrice() * item.getQuantity()));
            } else {
                Log.e("CartAdapter", "Product is null in CartItem");
            }

            quantityText.setText(String.valueOf(item.getQuantity()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });

            decreaseQuantity.setOnClickListener(v -> {
                if (listener != null && item.getQuantity() > 1) {
                    listener.onUpdateQuantity(item, item.getQuantity() - 1);
                }
            });

            increaseQuantity.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUpdateQuantity(item, item.getQuantity() + 1);
                }
            });

            removeItem.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onRemoveItem(item);
                }
            });
        }
    }

    public interface CartItemListener {
        void onItemClick(CartItem item);
        void onItemSelected(CartItem item);
        void onRemoveItem(CartItem item);
        void onUpdateQuantity(CartItem item, int newQuantity);
    }
}