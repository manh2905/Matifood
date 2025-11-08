package com.example.matifood.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matifood.auth.RetrofitClient
import com.example.matifood.models.Food
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val api = RetrofitClient.instance

    private val _results = MutableStateFlow<List<Food>>(emptyList())
    val results: StateFlow<List<Food>> = _results

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun searchFoods(query: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = api.searchFoods(query)
                if (response.isSuccessful && response.body()?.success == true) {
                    _results.value = response.body()?.data ?: emptyList()
                } else {
                    _results.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("SearchVM", "❌ Error: ${e.message}")
                _results.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }
}
