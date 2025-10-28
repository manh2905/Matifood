package com.example.matifood.activity.dasboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.matifood.R
import com.example.matifood.models.Category
import com.example.matifood.ui.theme.Typography
import com.example.matifood.viewmodel.FoodViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
fun MainScreen(viewModel: FoodViewModel = viewModel()) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState() //  Thêm state cho LazyColumn

    val foodList = viewModel.foodList
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    // Gọi API khi vào màn hình
    LaunchedEffect(Unit) {
        viewModel.fetchFoods()
    }

    Scaffold(
        bottomBar = { MyBottomBar() },
        scaffoldState = scaffoldState,
        floatingActionButton = {
            // Chỉ hiển thị khi cuộn xuống đủ xa
            if (listState.firstVisibleItemIndex > 2) {
                FloatingActionButton(
                    onClick = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(0) //  Cuộn lên đầu
                        }
                    },
                    backgroundColor = colorResource(R.color.orange)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Scroll to top",
                        tint = colorResource(R.color.white)
                    )
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.white))
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(19.dp)
        ) {
            item {
                TopBar()
            }

            item {
                Text(
                    text = "Hôm nay MATIFOOD có gì",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colorResource(R.color.orange),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )
            }

            item {
                CategorySection(categories = categoryList, showCategoryLoading = false)
            }

            item {
                Text(
                    text = "Món ăn có thể bạn sẽ thích",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                )
            }

            if (isLoading) {
                item {
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

            if (error != null) {
                item {
                    Text(
                        text = error,
                        color = colorResource(R.color.purple_200),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            items(foodList.shuffled().take(10)) { food ->
                FoodItemCard(food)
            }
        }
    }
}


//@Composable
//fun FoodItem(food: com.example.matifood.models.Food) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(colorResource(R.color.white))
//            .padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        AsyncImage(
//            model = "http://10.0.2.2:4000/image/${food.image}",
//            contentDescription = food.name,
//            modifier = Modifier
//                .size(130.dp)
//                .padding(bottom = 8.dp)
//        )
//        Text(text = food.name, style = Typography.bodySmall)
//        Text(text = "${food.price} VNĐ", style = Typography.labelSmall)
//    }
//}
