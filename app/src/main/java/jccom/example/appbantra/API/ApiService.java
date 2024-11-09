package jccom.example.appbantra.API;

import jccom.example.appbantra.Model.Revennue;
import jccom.example.appbantra.Model.RevennueResponse;
import jccom.example.appbantra.Model.User;
import jccom.example.appbantra.Model.LoginRequest;
import jccom.example.appbantra.Model.AuthResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Endpoint cho đăng ký người dùng
    @POST("register")
    Call<Void> register(@Body User user);

    // Endpoint cho đăng nhập người dùng
    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    // Endpoint lấy thông tin người dùng
    @GET("user/{id}")
    Call<User> getUser(@Path("id") String userId);

    // Endpoint cập nhật thông tin người dùng
    @PUT("user/{id}")
    Call<User> updateUser(@Path("id") String userId, @Body User user);

    @GET("revenue/by-product") // Adjust endpoint accordingly
    Call<RevennueResponse> getRevenueByProduct();
}
