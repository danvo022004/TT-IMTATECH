package jccom.example.appbantra.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jccom.example.appbantra.Model.Category;
import jccom.example.appbantra.R;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    private int selectedPosition = -1; // Biến để lưu vị trí đã chọn

    // Interface để lắng nghe sự kiện nhấp vào danh mục
    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryId);
    }

    // Constructor nhận vào danh sách category và listener
    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.onCategoryClickListener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_category.xml
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ten_category, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        // Lấy danh mục tại vị trí này
        Category category = categoryList.get(position);

        // Hiển thị tên category trong TextView
        holder.categoryName.setText(category.getName());

        // Thiết lập màu nền theo trạng thái
        if (position == selectedPosition) {
            holder.categoryName.setBackgroundResource(R.drawable.khung); // Giữ drawable
            holder.categoryName.setBackgroundColor(Color.RED); // Màu đỏ khi được chọn
        } else {
            holder.categoryName.setBackgroundResource(R.drawable.khung); // Giữ drawable
            holder.categoryName.setBackgroundColor(Color.parseColor("#D3D3D3")); // Màu xám nhạt khi không được chọn
        }

        // Thiết lập sự kiện nhấp vào danh mục
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position; // Cập nhật vị trí đã chọn
            notifyDataSetChanged(); // Cập nhật lại RecyclerView
            if (onCategoryClickListener != null) {
                onCategoryClickListener.onCategoryClick(category.getId());
                Log.d("CategoryAdapter", "Clicked Category ID: " + category.getId()); // Ghi log ID khi nhấp
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    // ViewHolder cho mỗi item
    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ TextView từ layout item_category.xml
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}