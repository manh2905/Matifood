package com.example.matifood.viewmodel

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import com.example.matifood.auth.TokenManager
import com.example.matifood.models.LoginRequest
import com.example.matifood.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val token: String) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel() : ViewModel()  {


    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState



    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn


    private val api = RetrofitClient.instance


    fun loginUser(email: String, password: String) {

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val res = api.loginUser(LoginRequest(email, password))
                if (res.isSuccessful && res.body()?.success == true) {
                    val token = res.body()?.token ?: ""
                    TokenManager.saveToken(token)
                    _isLoggedIn.value = true
                    _authState.value = AuthState.Success(token)
                } else {
                    _authState.value = AuthState.Error(res.body()?.message ?: "Đăng nhập thất bại")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Lỗi kết nối: ${e.message}")
            }
        }
    }

    fun registerUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val res = api.registerUser(RegisterRequest(name, email, password))
                if (res.isSuccessful && res.body()?.success == true) {
                    val token = res.body()?.token ?: ""
                    TokenManager.saveToken(token)
                    _authState.value = AuthState.Success(token)
                } else {
                    _authState.value = AuthState.Error(res.body()?.message ?: "Đăng ký thất bại")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Lỗi kết nối: ${e.message}")
            }
        }
    }


    fun logoutUser() {
        TokenManager.clearToken()
        _authState.value = AuthState.Idle
        _isLoggedIn.value = false
    }

    fun checkLoginStatus() {

        _isLoggedIn.value = TokenManager.getToken() != null
    }
}
