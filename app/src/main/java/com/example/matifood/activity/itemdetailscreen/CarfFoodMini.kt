package com.example.matifood.activity.itemdetailscreen

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.matifood.R
import com.example.matifood.models.Food
import com.example.matifood.activity.itemdetailscreen.DetailFoodScreen

@Composable
fun CardFoodMini(item: Food) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .width(140.dp)
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(
                BorderStroke(1.dp, colorResource(R.color.grey_orange)),
                RoundedCornerShape(20.dp)
            )
            .clickable {
                val intent = Intent(context, DetailFoodScreen::class.java).apply {
                    putExtra("food", item)
                }
                context.startActivity(intent)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "http://10.0.2.2:5000/image/${item.image}",
            contentDescription = item.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
            contentScale = ContentScale.Crop
        )

        Text(
            text = item.name,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 15.dp, start = 6.dp, end = 6.dp)
        )

        Text(
            text = "${item.price.toInt()} USD",
            color = Color(0xFF4CAF50),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            modifier = Modifier.padding(bottom = 8.dp, top = 10.dp)
        )
    }
}
