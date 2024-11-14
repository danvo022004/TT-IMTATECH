package jccom.example.appbantra.API;

import java.util.List;

import jccom.example.appbantra.Model.CartRequest;
import jccom.example.appbantra.Model.CartResponse;
import jccom.example.appbantra.Model.Category;
import jccom.example.appbantra.Model.Order;
import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.Model.RevenueResponse;
import jccom.example.appbantra.Model.StatusUpdate;
import jccom.example.appbantra.Model.TotalRevenueResponse;
import jccom.example.appbantra.Model.User;
import jccom.example.appbantra.Model.LoginRequest;
import jccom.example.appbantra.Model.AuthResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @POST("register")
    Call<Void> register(@Body User user);

    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String userId);

    @PUT("user/{id}")
    Call<User> updateUser(@Path("id") String userId, @Body User user);

    @GET("revenue/total")
    Call<TotalRevenueResponse> getTotalRevenue();
    @GET("revenue/products")
    Call<RevenueResponse> getRevenueByProduct();



    @GET("categories")
    Call<List<Category>> getListCategory();

    @POST("categories")
    @Multipart
    Call<Category> addCategory(@Part("name") String name,
                               @Part("description") String description,
                               @Part("imageUrl") String imageUrl);

    @PUT("categories/{id}")
    @Multipart
    Call<Category> updateCategory(@Path("id") String id,
                                  @Part("name") String name,
                                  @Part("description") String description,
                                  @Part("status") boolean status,
                                  @Part("imageUrl") String imageUrl);
    // Xóa danh mục
    @DELETE("categories/{id}")
    Call<Void> deleteCategory(@Path("id") String id);

    // **Sản phẩm**
    // Lấy danh sách tất cả sản phẩm
    @GET("products")
    Call<List<Product>> getListProduct();

    // Lấy sản phẩm theo ID
    @GET("products/{id}")
    Call<Product> getProductById(@Path("id") String productId);

    // Lấy danh sách sản phẩm theo categoryId
    @GET("products/category/{categoryId}")
    Call<List<Product>> getProductsByCategoryId(@Path("categoryId") String categoryId);

    // Thêm sản phẩm mới
    @POST("products")
    @Multipart
    Call<Product> addProduct(
            @Part("categoryId") RequestBody categoryId,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part imageUrl
    );

    // Cập nhật sản phẩm
    @PUT("products/{id}")
    @Multipart
    Call<Product> updateProduct(
            @Path("id") String productId,
            @Part("categoryId") RequestBody categoryId,
            @Part("name") RequestBody name,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("status") RequestBody status,
            @Part MultipartBody.Part image
    );

    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") String productId);

    // View Cart

    // View Cart
//    @GET("cart")
//    Call<CartResponse> viewCart();
//
//    // Add to Cart
//    // Endpoint thêm sản phẩm vào giỏ hàng
//    @POST("cart")
//    Call<CartResponse> addToCart(@Header("Authorization") String token, @Body int cartRequest);

    @GET("cart")
    Call<CartResponse> viewCart(@Header("Authorization") String token);

    @POST("cart")
    Call<CartResponse> addToCart(@Header("Authorization") String token, @Body CartRequest cartRequest);

    @DELETE("cart/{productId}")
    Call<CartResponse> removeProductFromCart(@Header("Authorization") String token, @Path("productId") String productId);

    @POST("cart/calculate-selected-total")
    Call<CartResponse> calculateSelectedTotal(@Header("Authorization") String token, @Body List<String> selectedProductIds);

    @PUT("orders/updateStatus/{id}")
    Call<Order> updateOrderStatus(@Path("id") String orderId, @Body StatusUpdate statusUpdate);

    @GET("orders/all")
    Call<OrderResponse> getAllUsersOrders();
}
