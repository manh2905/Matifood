package com.example.matifood.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import com.example.matifood.auth.TokenManager
import com.example.matifood.models.LoginRequest
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.instance.loginUser(request)

                if (response.isSuccessful && response.body()?.success == true) {
                    // Đăng nhập thành công!
                    val token = response.body()?.token

                    // LƯU TOKEN LẠI ĐỂ SỬ DỤNG CHO CÁC API KHÁC
                    TokenManager.authToken = token

                    // TODO: Thông báo cho UI biết đã đăng nhập thành công
                } else {
                    // TODO: Thông báo lỗi
                }
            } catch (e: Exception) {
                // TODO: Xử lý lỗi mạng
            }
        }
    }
}