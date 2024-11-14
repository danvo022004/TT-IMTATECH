package jccom.example.appbantra.FragmentAd;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.OrderResponse;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Order;
import jccom.example.appbantra.R;
import jccom.example.appbantra.adapter.Oder_adapter_ad;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Ad_Fragment extends Fragment {
    private RecyclerView recyclerView;
    private Oder_adapter_ad orderAdapter;
    private List<Order> orderList = new ArrayList<>();

    public Order_Ad_Fragment() {
        // Required empty public constructor
    }

    public static Order_Ad_Fragment newInstance(String param1, String param2) {
        Order_Ad_Fragment fragment = new Order_Ad_Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order__ad_, container, false);

        recyclerView = view.findViewById(R.id.rcvProAd);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderAdapter = new Oder_adapter_ad(orderList);
        recyclerView.setAdapter(orderAdapter);

        // Gọi hàm lấy danh sách tất cả đơn hàng
        fetchOrders();

        return view;
    }

    private void fetchOrders() {
        ApiService apiService = RetrofitClient.getApiService();

        apiService.getAllUsersOrders().enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Order_Ad_Fragment", "Response JSON: " + response.body().toString());
                    orderList.clear();
                    orderList.addAll(response.body().getOrders());

                    // Kiểm tra từng đơn hàng để đảm bảo mã đơn được nhận
                    for (Order order : orderList) {
                        Log.d("Order_Ad_Fragment", "Order ID: " + order.getId()); // Kiểm tra ID
                    }

                    orderAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không thể lấy danh sách đơn hàng", Toast.LENGTH_SHORT).show();
                    Log.d("Order_Ad_Fragment", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Order_Ad_Fragment", "API call failed: " + t.getMessage(), t);
            }
        });
    }


}
