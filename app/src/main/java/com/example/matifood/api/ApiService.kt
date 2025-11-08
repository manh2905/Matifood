package com.example.matifood.api

import com.example.matifood.models.AuthResponse
import com.example.matifood.models.CartItemRequest
import com.example.matifood.models.EmptyBody
import com.example.matifood.models.FoodIdsRequest
import com.example.matifood.models.FoodResponse
import com.example.matifood.models.GenericResponse
import com.example.matifood.models.GetCartResponse
import com.example.matifood.models.ListFoodResponse
import com.example.matifood.models.LoginRequest
import com.example.matifood.models.ManyCartItemRequest
import com.example.matifood.models.MobileOrderRequest
import com.example.matifood.models.PlaceOrderRequest
import com.example.matifood.models.PlaceOrderResponse
import com.example.matifood.models.RegisterRequest
import com.example.matifood.models.RemoveFoodRequest
import com.example.matifood.models.SearchResponse
import com.example.matifood.models.UserOrderResult
import com.example.matifood.models.VerifyOrderRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {


    @POST("api/user/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/user/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>


    @GET("api/food/list")
    suspend fun listFood(): Response<ListFoodResponse>

    @GET("/api/food/category/{category}")
    suspend fun getFoodsByCategory(
        @Path("category") category: String
    ): Response<ListFoodResponse>


    @GET("api/food/{id}")
    suspend fun getFoodById(
        @Path("id") id: String
    ): Response<FoodResponse>

    //  Cart
    @POST("api/cart/add")
    suspend fun addToCart(@Body request: CartItemRequest): Response<GenericResponse>


    @POST("api/cart/addMany")
    suspend fun addManyToCart(@Body request: ManyCartItemRequest): Response<GenericResponse>

    @POST("api/food/multiple")
    suspend fun getFoodsByIds(
        @Body request: FoodIdsRequest
    ): Response<ListFoodResponse>

    @POST("api/cart/remove")
    suspend fun removeFromCart(@Body request: CartItemRequest): Response<GenericResponse>

    // API "getCart" của bạn dùng POST (theo cartRoute.js) và không cần body
    @POST("api/cart/get")
    suspend fun getCart(@Body body: EmptyBody = EmptyBody()): Response<GetCartResponse>


    @POST("api/order/placemobile")
    suspend fun placeOrderMobile(
        @Body request: MobileOrderRequest
    ): Response<PlaceOrderResponse>

    @POST("api/order/verify")
    suspend fun verifyOrder(@Body request: VerifyOrderRequest): Response<GenericResponse>

    //  Order
    @POST("api/order/orderusers")
    suspend fun getUserOrders(@Body body: EmptyBody = EmptyBody()): Response<UserOrderResult>

    //search
    @GET("api/food/search")
    suspend fun searchFoods(@Query("query") query: String): Response<SearchResponse>



}