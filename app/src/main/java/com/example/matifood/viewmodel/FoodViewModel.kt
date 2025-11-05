package com.example.matifood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import com.example.matifood.models.Food
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.matifood.models.FoodIdsRequest

class FoodViewModel : ViewModel() {

    var foodList = mutableStateListOf<Food>()
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchFoods() {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = RetrofitClient.instance.listFood()
                if (response.isSuccessful && response.body()?.success == true) {
                    foodList.clear()
                    foodList.addAll(response.body()?.data ?: emptyList())
                } else {
                    errorMessage = "Lấy danh sách thất bại: ${response.message()}"
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi mạng: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchFoodsByCategory(category: String) {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = RetrofitClient.instance.getFoodsByCategory(category)
                if (response.isSuccessful && response.body() != null) {
                    val foodResponse = response.body()!!
                    foodList.clear()
                    foodList.addAll(foodResponse.data)
                    errorMessage = null
                } else {
                    errorMessage = "Không thể tải dữ liệu"
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi mạng: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchFoodsByIds(ids: List<String>, onResult: (List<Food>?) -> Unit) {
        viewModelScope.launch {
            try {
                isLoading = true
                Log.i("fetchFoodsByIds", "📦 Gửi request: ids=$ids")
                val response = RetrofitClient.instance.getFoodsByIds(FoodIdsRequest(ids))

                Log.i("fetchFoodsByIds", "📩 Code=${response.code()} | Body=${response.body()} | Error=${response.errorBody()?.string()}")

                if (response.isSuccessful && response.body()?.success == true) {
                    onResult(response.body()!!.data)
                } else {
                    errorMessage =  "Không tải được danh sách món ăn"
                    onResult(null)
                }
            } catch (e: Exception) {
                errorMessage = "Lỗi mạng: ${e.message}"
                Log.e("fetchFoodsByIds", "❌ Exception: ${e.message}")
                onResult(null)
            } finally {
                isLoading = false
            }
        }
    }



}
