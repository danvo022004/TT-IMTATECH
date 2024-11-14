package jccom.example.appbantra.FragmentAd;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import java.util.ArrayList;
import java.util.List;
import jccom.example.appbantra.API.ApiService;
import jccom.example.appbantra.API.RetrofitClient;
import jccom.example.appbantra.Model.Revenue;
import jccom.example.appbantra.Model.RevenueResponse;
import jccom.example.appbantra.Model.TotalRevenueResponse;
import jccom.example.appbantra.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevennueFragment extends Fragment {
    private PieChart pieChartTotalRevenue;
    private PieChart pieChartProductRevenue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_revennue, container, false);
        pieChartTotalRevenue = view.findViewById(R.id.chart);
        pieChartProductRevenue = view.findViewById(R.id.chartProductRevenue);

        fetchTotalRevenue();
        fetchProductRevenue();

        return view;
    }

    private void fetchTotalRevenue() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<TotalRevenueResponse> call = apiService.getTotalRevenue();

        call.enqueue(new Callback<TotalRevenueResponse>() {
            @Override
            public void onResponse(Call<TotalRevenueResponse> call, Response<TotalRevenueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupTotalRevenueChart(response.body().getTotalRevenue());
                }
            }

            @Override
            public void onFailure(Call<TotalRevenueResponse> call, Throwable t) {
                Log.e("Retrofit", "Lỗi khi lấy dữ liệu tổng doanh thu", t);
            }
        });
    }

    private void setupTotalRevenueChart(int totalRevenue) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalRevenue, "Tổng Doanh Thu"));

        PieDataSet dataSet = new PieDataSet(entries, "Biểu đồ Tổng Doanh Thu");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        pieChartTotalRevenue.setData(data);
        pieChartTotalRevenue.getDescription().setEnabled(false);
        pieChartTotalRevenue.setCenterText("Tổng: " + totalRevenue + " VNĐ");
        pieChartTotalRevenue.setCenterTextSize(18f);
        pieChartTotalRevenue.animateY(1000);
        pieChartTotalRevenue.invalidate();
    }

    private void fetchProductRevenue() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<RevenueResponse> call = apiService.getRevenueByProduct();

        call.enqueue(new Callback<RevenueResponse>() {
            @Override
            public void onResponse(Call<RevenueResponse> call, Response<RevenueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupProductRevenueChart(response.body().getRevenue());
                }
            }

            @Override
            public void onFailure(Call<RevenueResponse> call, Throwable t) {
                Log.e("Retrofit", "Lỗi khi lấy dữ liệu doanh thu theo sản phẩm", t);
            }
        });
    }

    private void setupProductRevenueChart(List<Revenue> revenueList) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Revenue revenue : revenueList) {
            entries.add(new PieEntry(revenue.getTotalRevenue(), revenue.getProduct_name()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Doanh Thu Theo Sản Phẩm");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        PieData data = new PieData(dataSet);
        pieChartProductRevenue.setData(data);
        pieChartProductRevenue.getDescription().setEnabled(false);
        pieChartProductRevenue.animateY(1000);
        pieChartProductRevenue.invalidate();
    }
}
