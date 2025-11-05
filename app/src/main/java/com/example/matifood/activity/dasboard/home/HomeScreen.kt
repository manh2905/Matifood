package com.example.matifood.activity.dasboard.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.matifood.R
import com.example.matifood.activity.dasboard.FoodItemCard
import com.example.matifood.activity.dasboard.categoryList
import com.example.matifood.ui.theme.Typography
import com.example.matifood.viewmodel.FoodViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(viewModel: FoodViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val foodList = viewModel.foodList
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    LaunchedEffect(Unit) {
        viewModel.fetchFoods()
    }

    Scaffold(
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
                ImageSlider()
            }

            item {
                Text(
                    text = "Hôm nay MATIFOOD có gì ?",
                    style = Typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold ,
                    fontSize = 20.sp,
                    color = colorResource(R.color.orange),
                    modifier = Modifier.padding(start = 16.dp, top = 1.dp)
                )
            }

            item {
                CategorySection(categories = categoryList, showCategoryLoading = false)
            }

            item {
                Text(
                    text = "Món ăn có thể bạn sẽ thích",
                    style = Typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
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

            itemsIndexed(foodList.shuffled().take(20)) { index, food ->

                var startAnim by remember { mutableStateOf(false) }
                //  Kích hoạt animation lần lượt
                LaunchedEffect(Unit) {
                    delay(index * 5L) // trễ dần từng item cho đẹp
                    startAnim = true
                }

                //  Animate vị trí ngang (slide-in)
                val offsetX by animateDpAsState(
                    targetValue = if (startAnim) 0.dp else (-100).dp,
                    animationSpec = tween(durationMillis = 200),
                    label = "slide"
                )

                //  Animate độ mờ (fade-in)
                val alpha by animateFloatAsState(
                    targetValue = if (startAnim) 1f else 0f,
                    animationSpec = tween(durationMillis = 200),
                    label = "fade"
                )

                Box(
                    modifier = Modifier
                        .offset(x = offsetX)
                        .alpha(alpha)
                ) {
                    FoodItemCard(food)
                }
            }

        }
    }
}
