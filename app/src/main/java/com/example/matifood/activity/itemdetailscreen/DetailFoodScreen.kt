package com.example.matifood.activity.itemdetailscreen

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matifood.R
import com.example.matifood.models.Food
import com.example.matifood.viewmodel.FoodViewModel


class DetailFoodScreen : AppCompatActivity() {
//    private lateinit var item: Food
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_food_screen)


        var item : Food = intent.getSerializableExtra("food") as Food
        setContent {
            DetailScreen(
                item = item,
                onClickBack = {finish()}
            )
        }

    }
}

@Composable
fun DetailScreen(
    item: Food,
    onClickBack: () -> Unit = {},
    onAddToCart: () -> Unit = {},
) {
    var numberInCart by remember { mutableStateOf(0) }

    val viewModel: FoodViewModel = viewModel()
    val items = viewModel.foodList
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    // Gọi API để lấy danh sách món cùng loại
    LaunchedEffect(Unit) {
        viewModel.fetchFoodsByCategory(item.category)
    }

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (footer, column) = createRefs()

        // 📦 Phần nội dung chính
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .constrainAs(column) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(footer.top)
                }
        ) {
            HeaderDetailScreen(item, onClickBack = onClickBack)
            Spacer(modifier = Modifier.height(1.dp))

            TitleNumberRow(
                numberInCart = numberInCart,
                item = item,
                onIncrement = { numberInCart++ },
                onDecrement = {
                    if (numberInCart >= 1) numberInCart--
                }
            )

            // Mô tả
            Text(
                text = "Mô tả",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = item.description,
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 16.dp).padding(top = 10.dp)
            )

            // Giá
            Text(
                text = "Giá: ${item.price.toInt()} USD",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF4CAF50),
                modifier = Modifier.padding(16.dp)
            )

            // Các món cùng loại
            if (isLoading) {
                Text(
                    text = "Đang tải...",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (error != null) {
                Text(
                    text = "Lỗi: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (items.isNotEmpty()) {
                Text(
                    text = "Các món cùng loại (${item.category})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp, top = 18.dp)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .padding(top = 10.dp)
                ) {
                    items(items) { relatedItem ->
                        CardFoodMini(item = relatedItem)
                    }
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }

        //  Footer cố định
        Box(
            modifier = Modifier
                .height(90.dp)
                .fillMaxWidth()
                .graphicsLayer {
                    shadowElevation = 16.dp.toPx()
                    shape = RectangleShape
                    clip = false
                }
//                .clip(RoundedCornerShape(topStart = 60.dp, topEnd = 60.dp))
                .background(colorResource(R.color.white))
                .constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp, vertical = 16.dp)
                    .padding(top = 6.dp), // padding bên trong, không bị cắt mép ngoài
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Tổng giá
//                Text(
//                    text = "Tổng: ${(item.price * numberInCart).toInt()} USD",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )

                // Nút thêm vào giỏ
                Box(
                    modifier = Modifier
                        .height(300.dp)
                        .width(450.dp)
                        .clip(RoundedCornerShape(30.dp))
//                        .background(Color(0xFF4CAF50))
                        .background(colorResource(R.color.orange))
                        .clickable { onAddToCart() }
                        .wrapContentSize(Alignment.Center)
                ) {
                    Text(
                        text = "Thêm vào giỏ hàng - ${(item.price * numberInCart).toInt()} USD",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

    }
}

