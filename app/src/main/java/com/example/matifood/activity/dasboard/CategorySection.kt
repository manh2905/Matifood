package com.example.matifood.activity.dasboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.matifood.R
import com.example.matifood.models.Category
import com.example.matifood.ui.theme.Typography

@Composable
fun CategorySection (
    categories : List<Category>, showCategoryLoading : Boolean
) {
    if (showCategoryLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val rows = categories.chunked(3)
        val context = LocalContext.current

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp), // khoảng cách giữa các item
            contentPadding = PaddingValues(start = 16.dp, end = 8.dp)   // padding hai bên
        ) {
            items(categories) { category ->
                CategoryItem(
                    category = category,
                    modifier = Modifier.width(100.dp),
                    onItemClick = {
                        // 👉 xử lý khi click vào category
                    }
                )

            }

        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit
) {
    Column(
        modifier = modifier
            .width(140.dp)
            .height(160.dp)
            .shadow(
                elevation = 9.dp,
                shape = RoundedCornerShape(20.dp),
                clip = false
            )
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(
                BorderStroke(1.dp, colorResource(R.color.grey_orange)),
                RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onItemClick)

            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = category.imagePath,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp) // 🔹 ảnh lớn hơn
                .padding(bottom = 8.dp)

        )
        Text(
            text = category.name,
            fontSize = 14.sp, // 🔹 chữ to hơn
            color = Color.Black,
            style = Typography.labelSmall
        )
    }
}

