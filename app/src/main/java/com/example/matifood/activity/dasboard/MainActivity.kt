package com.example.matifood.activity.dasboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.matifood.R
import com.example.matifood.models.Category
import com.example.matifood.ui.theme.Typography
import com.example.matifood.viewmodel.FoodViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

// 🥗 Danh mục hiển thị ở trên cùng
val categoryList = listOf(
    Category(R.drawable.menu_1, "Salad"),
    Category(R.drawable.menu_2, "Rolls"),
    Category(R.drawable.menu_3, "Deserts"),
    Category(R.drawable.menu_4, "Sandwich"),
    Category(R.drawable.menu_5, "Cake"),
    Category(R.drawable.menu_6, "Pure Veg"),
    Category(R.drawable.menu_7, "Pasta"),
    Category(R.drawable.menu_8, "Noodles")
)

@Composable
fun MainScreen(viewModel: FoodViewModel = viewModel()) {
    val scaffoldState = rememberScaffoldState()

    // Lấy state từ ViewModel
    val foodList = viewModel.foodList
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    // Gọi API 1 lần khi vào màn hình
    LaunchedEffect(Unit) {
        viewModel.fetchFoods()
    }

    Scaffold(
        bottomBar = { MyBottomBar() },
        scaffoldState = scaffoldState
    ) { paddingValues ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(paddingValues),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 🔹 Top bar
            item(span = { GridItemSpan(2) }) {
                TopBar()
            }

            // 🔹 Category section
            item(span = { GridItemSpan(2) }) {
                CategorySection(categories = categoryList, showCategoryLoading = false)
            }

            // 🔹 Tiêu đề danh sách món ăn
            item(span = { GridItemSpan(2) }) {
                Text(
                    text = "Món ăn có thể bạn sẽ thích",
                    style = Typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )
            }

            // 🔹 Hiển thị khi đang tải
            if (isLoading) {
                item(span = { GridItemSpan(2) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colorResource(R.color.orange))
                    }
                }
            }

            // 🔹 Hiển thị lỗi
            if (error != null) {
                item(span = { GridItemSpan(2) }) {
                    Text(
                        text = error,
                        color = colorResource(R.color.purple_200),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // 🔹 Hiển thị danh sách món ăn
            items(foodList) { food ->
                FoodItem(food)
            }
        }
    }
}

@Composable
fun FoodItem(food: com.example.matifood.models.Food) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(R.color.white))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "http://10.0.2.2:4000/image/${food.image}", // ⚠️ Địa chỉ backend
            contentDescription = food.name,
            modifier = Modifier
                .size(130.dp)
                .padding(bottom = 8.dp)
        )
        Text(text = food.name, style = Typography.bodySmall)
        Text(text = "${food.price} VNĐ", style = Typography.labelSmall)
    }
}
