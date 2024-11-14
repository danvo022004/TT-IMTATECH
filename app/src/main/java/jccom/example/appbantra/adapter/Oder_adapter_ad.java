package jccom.example.appbantra.adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Order;
import jccom.example.appbantra.Model.StatusUpdate;
import jccom.example.appbantra.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Oder_adapter_ad extends RecyclerView.Adapter<Oder_adapter_ad.ViewHolder> {
    private List<Order> orderList;
    private String[] statuses = {"pending", "confirmed", "completed", "cancelled"};

    public Oder_adapter_ad(List<Order> orderList) {
        this.orderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Hiển thị mã đơn hàng
        holder.orderIdTextView.setText("Mã đơn hàng : " + order.getId());

        // Hiển thị các thuộc tính khác nếu cần
        holder.totalPriceTextView.setText("Tổng tiền : " + order.getTotal_price());
        holder.statusTextView.setText("Trạng thái : " + order.getStatus());
        holder.paymentTextView.setText("Phương thức thanh toán : " + order.getPaymentMethod());

        // Đổi màu cho trạng thái và ẩn các nút nếu cần
        if (order.getStatus().equals("completed")) {
            holder.statusTextView.setTextColor(Color.GREEN); // Màu xanh lá cho trạng thái hoàn thành
            holder.changeStatusButton.setVisibility(View.GONE);
            holder.deleteStatusButton.setVisibility(View.GONE);
        } else if (order.getStatus().equals("cancelled")) {
            holder.statusTextView.setTextColor(Color.RED); // Màu đỏ cho trạng thái hủy
            holder.changeStatusButton.setVisibility(View.GONE);
            holder.deleteStatusButton.setVisibility(View.GONE);
        } else {
            holder.statusTextView.setTextColor(Color.BLACK); // Màu mặc định cho các trạng thái khác
            holder.changeStatusButton.setVisibility(View.VISIBLE);
            holder.deleteStatusButton.setVisibility(View.VISIBLE);
        }

        // Xử lý sự kiện cho nút thay đổi trạng thái
        holder.changeStatusButton.setOnClickListener(v -> changeOrderStatus(order, holder.statusTextView, holder));

        // Xử lý sự kiện cho nút hủy đơn hàng
        holder.deleteStatusButton.setOnClickListener(v -> cancelOrder(order, holder));
    }

    private void changeOrderStatus(Order order, TextView statusTextView, ViewHolder holder) {
        int currentIndex = java.util.Arrays.asList(statuses).indexOf(order.getStatus());
        String newStatus = null;

        // Xác định trạng thái tiếp theo, bỏ qua "cancelled"
        if (currentIndex < statuses.length - 2) { // -2 để bỏ qua "cancelled" (trạng thái cuối cùng)
            newStatus = statuses[currentIndex + 1];
        } else if (statuses[currentIndex].equals("completed")) {
            // Nếu trạng thái là "completed", ẩn các nút
            holder.changeStatusButton.setVisibility(View.GONE);
            holder.deleteStatusButton.setVisibility(View.GONE);
            return;
        }

        if (newStatus != null) {
            StatusUpdate statusUpdate = new StatusUpdate(newStatus);

            ApiService apiService = RetrofitClient.getApiService();
            String finalNewStatus = newStatus;

            // Log dữ liệu trước khi gửi lên API
            Log.d("Oder_adapter_ad", "Request to update order status: orderId=" + order.getId() + ", newStatus=" + finalNewStatus);

            apiService.updateOrderStatus(order.getId(), statusUpdate).enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("Oder_adapter_ad", "Successfully updated order status to: " + finalNewStatus);
                        order.setStatus(finalNewStatus);
                        statusTextView.setText("Trạng thái : " + finalNewStatus);

                        // Đổi màu và ẩn các nút nếu trạng thái là "completed"
                        if (finalNewStatus.equals("completed")) {
                            statusTextView.setTextColor(Color.GREEN);
                            holder.changeStatusButton.setVisibility(View.GONE);
                            holder.deleteStatusButton.setVisibility(View.GONE);
                        }
                    } else {
                        Log.e("Oder_adapter_ad", "Cập nhật trạng thái thất bại: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Log.e("Oder_adapter_ad", "Lỗi kết nối: " + t.getMessage());
                }
            });
        }
    }

    private void cancelOrder(Order order, ViewHolder holder) {
        String cancelledStatus = "cancelled";
        StatusUpdate statusUpdate = new StatusUpdate(cancelledStatus);

        ApiService apiService = RetrofitClient.getApiService();

        // Log dữ liệu trước khi gửi yêu cầu hủy đơn hàng lên API
        Log.d("Oder_adapter_ad", "Request to cancel order: orderId=" + order.getId() + ", status=" + cancelledStatus);

        apiService.updateOrderStatus(order.getId(), statusUpdate).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(Call<Order> call, Response<Order> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Oder_adapter_ad", "Order cancelled successfully.");
                    order.setStatus(cancelledStatus);
                    holder.statusTextView.setText("Trạng thái : " + cancelledStatus);
                    holder.statusTextView.setTextColor(Color.RED);

                    // Ẩn cả hai nút khi đơn hàng được hủy
                    holder.changeStatusButton.setVisibility(View.GONE);
                    holder.deleteStatusButton.setVisibility(View.GONE);
                } else {
                    Log.e("Oder_adapter_ad", "Hủy đơn hàng thất bại: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Order> call, Throwable t) {
                Log.e("Oder_adapter_ad", "Lỗi kết nối khi hủy đơn hàng: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView, totalPriceTextView, statusTextView, paymentTextView;
        Button changeStatusButton, deleteStatusButton;

        public ViewHolder(View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            paymentTextView = itemView.findViewById(R.id.PaymentTextView);
            changeStatusButton = itemView.findViewById(R.id.changeStatusButton);
            deleteStatusButton = itemView.findViewById(R.id.deleteStatusButton);
        }
    }
}
