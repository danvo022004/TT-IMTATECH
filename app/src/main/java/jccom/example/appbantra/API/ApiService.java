package jccom.example.appbantra.API;

import java.util.List;

import jccom.example.appbantra.Model.CartRequest;
import jccom.example.appbantra.Model.CartResponse;
import jccom.example.appbantra.Model.Category;
import jccom.example.appbantra.Model.Product;
import java.util.List;

import jccom.example.appbantra.Model.Category;
import jccom.example.appbantra.Model.Product;
import jccom.example.appbantra.Model.Revennue;
import jccom.example.appbantra.Model.RevennueResponse;
import jccom.example.appbantra.Model.User;
import jccom.example.appbantra.Model.LoginRequest;
import jccom.example.appbantra.Model.AuthResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET("revenue/total")
    Call<TotalRevenueResponse> getTotalRevenue();
    @GET("revenue/products")
    Call<RevenueResponse> getRevenueByProduct();

    // **Danh mục sản phẩm**

    // Lấy danh sách danh mục
    @GET("categories")
    Call<List<Category>> getListCategory();

    // Thêm danh mục mới
    @POST("categories")
    @Multipart
    Call<Category> addCategory(@Part("name") String name,
                               @Part("description") String description,
                               @Part("imageUrl") String imageUrl);

    // Cập nhật danh mục
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
    Call<Product> addProduct(@Part("categoryId") String categoryId,
                             @Part("name") String name,
                             @Part("price") double price,
                             @Part("description") String description,
                             @Part MultipartBody.Part image);

    // Cập nhật sản phẩm
    @PUT("products/{id}")
    @Multipart
    Call<Product> updateProduct(@Path("id") String productId,
                                @Part("categoryId") String categoryId,
                                @Part("name") String name,
                                @Part("price") double price,
                                @Part("description") String description,
                                @Part("status") boolean status,
                                @Part MultipartBody.Part image);

    // Xóa sản phẩm
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
    @GET("revenue/by-product") // Adjust endpoint accordingly
    Call<RevennueResponse> getRevenueByProduct();

    // **Danh mục sản phẩm**

    // Lấy danh sách danh mục
    @GET("categories")
    Call<List<Category>> getListCategory();

    // Thêm danh mục mới
    @POST("categories")
    @Multipart
    Call<Category> addCategory(@Part("name") String name,
                               @Part("description") String description,
                               @Part("imageUrl") String imageUrl);

    // Cập nhật danh mục
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
    Call<Product> addProduct(@Part("categoryId") String categoryId,
                             @Part("name") String name,
                             @Part("price") double price,
                             @Part("description") String description,
                             @Part MultipartBody.Part image);

    // Cập nhật sản phẩm
    @PUT("products/{id}")
    @Multipart
    Call<Product> updateProduct(@Path("id") String productId,
                                @Part("categoryId") String categoryId,
                                @Part("name") String name,
                                @Part("price") double price,
                                @Part("description") String description,
                                @Part("status") boolean status,
                                @Part MultipartBody.Part image);

    // Xóa sản phẩm
    @DELETE("products/{id}")
    Call<Void> deleteProduct(@Path("id") String productId);
}
