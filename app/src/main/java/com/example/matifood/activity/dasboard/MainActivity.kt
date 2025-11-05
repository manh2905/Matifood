package com.example.matifood.activity.dasboard

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matifood.R

import com.example.matifood.activity.dasboard.cart.CartScreen
import com.example.matifood.activity.dasboard.home.HomeScreen
import com.example.matifood.activity.dasboard.order.OrderScreen

import com.example.matifood.activity.dasboard.profile.ProfileScreen
import com.example.matifood.auth.TokenManager
import com.example.matifood.models.Category
import com.example.matifood.ui.theme.Typography
import com.example.matifood.viewmodel.AuthViewModel
import com.example.matifood.viewmodel.CartViewModel
import com.example.matifood.viewmodel.FoodViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        TokenManager.init(this)
        val authViewModel: AuthViewModel by viewModels()
        authViewModel.checkLoginStatus()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

val categoryList = listOf(
    Category(R.drawable.menu_1, "Salad"),
    Category(R.drawable.menu_2, "Rolls"),
    Category(R.drawable.menu_3, "Deserts"),
    Category(R.drawable.menu_4, "Sandwich"),
    Category(R.drawable.menu_5, "Cake"),
    Category(R.drawable.menu_6, "Pure Veg"),
    Category(R.drawable.menu_7, "Pasta"),
    Category(R.drawable.menu_8, "Noodles"),
    Category(R.drawable.menu_9, "Gà rán")
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val viewModel: FoodViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()

    Scaffold(
        bottomBar = { MyBottomBar(navController) },
        backgroundColor = colorResource(R.color.white)
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { HomeScreen(viewModel) }
            composable("cart") { CartScreen(authViewModel, cartViewModel, viewModel) }
            composable("orders") { OrderScreen() }
            composable("profile") { ProfileScreen(viewModel = authViewModel) }


        }
    }
}
