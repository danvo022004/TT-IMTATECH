package jccom.example.appbantra.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jccom.example.appbantra.Model.Order;
import jccom.example.appbantra.R;

public class Order_adapter_user extends RecyclerView.Adapter<Order_adapter_user.ViewHolder> {

    private List<Order> orderList;

    public Order_adapter_user(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.tvOrderId.setText("Mã đơn hàng: " + order.getId());
        holder.tvProductName.setText("Tên sản phẩm: " + order.getProduct_name());
        holder.tvQuantity.setText("Số lượng: " + order.getQuantity());
        holder.tvTotalPrice.setText("Tổng giá: " + order.getTotal_price());
        holder.tvStatus.setText("Trạng thái: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvProductName, tvQuantity, tvTotalPrice, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
