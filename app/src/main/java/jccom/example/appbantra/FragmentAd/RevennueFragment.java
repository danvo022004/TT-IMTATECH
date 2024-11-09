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
import jccom.example.appbantra.Model.Revennue;
import jccom.example.appbantra.Model.RevennueResponse;
import jccom.example.appbantra.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RevennueFragment extends Fragment {
    private PieChart pieChart;

    public static RevennueFragment newInstance(String param1, String param2) {
        RevennueFragment fragment = new RevennueFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_revennue, container, false);
        pieChart = view.findViewById(R.id.chart);
        fetchRevennueData();
        return view;
    }

    private void fetchRevennueData() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<RevennueResponse> call = apiService.getRevenueByProduct();

        call.enqueue(new Callback<RevennueResponse>() {
            @Override
            public void onResponse(retrofit2.Call<RevennueResponse> call, Response<RevennueResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setupPieChart(response.body().getRevenue());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<RevennueResponse> call, Throwable t) {
                Log.e("Retrofit", "Error fetching revenue data", t);
            }
        });
    }

    private void setupPieChart(List<Revennue> revennueList) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Revennue revennue : revennueList) {
            entries.add(new PieEntry(revennue.getTotalRevennue(), revennue.getProduct_name()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Product Revennue");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.getDescription().setEnabled(false);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}