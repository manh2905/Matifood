package com.example.matifood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // Thẻ (Tag) để lọc log trong Logcat
    private val TAG = "ApiServiceTest"

    /**
     * Hàm này được gọi để bắt đầu kiểm tra kết nối API
     */
    fun testApiConnection() {
        Log.d(TAG, "Đang chuẩn bị gọi API...")

        // Chạy một coroutine an toàn trong ViewModel
        viewModelScope.launch {
            try {
                // Thực hiện lệnh gọi API
                val response = RetrofitClient.instance.listFood()

                // Kiểm tra xem máy chủ có trả về mã 200-299 không
                if (response.isSuccessful) {
                    // Lấy dữ liệu
                    val foodList = response.body()?.data

                    // Log ra Logcat
                    Log.d(TAG, "✅ KẾT NỐI THÀNH CÔNG!")
                    Log.d(TAG, "Mã phản hồi: ${response.code()}")
                    if (foodList != null) {
                        for (food in foodList) {
                            Log.d(TAG, food.name)
                            Log.d(TAG, food.category)
                            Log.d(TAG, food.price.toString())

                        }
                    }

                } else {
                    // Xử lý lỗi từ máy chủ (ví dụ: 404, 500)
                    Log.e(TAG, "❌ KẾT NỐI THẤT BẠI (Lỗi Server)")
                    Log.e(TAG, "Mã lỗi: ${response.code()}")
                    Log.e(TAG, "Nội dung lỗi: ${response.errorBody()?.string()}")
                }

            } catch (e: Exception) {
                // Xử lý lỗi (ví dụ: không có mạng, sai BASE_URL, v.v.)
                Log.e(TAG, "❌ KẾT NỐI THẤT BẠI (Lỗi Ứng dụng/Mạng)")
                Log.e(TAG, "Ngoại lệ: ${e.message}", e)
            }
        }
    }
}