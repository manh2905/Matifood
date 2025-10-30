package com.example.matifood.activity.itemlistbycategory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matifood.activity.dasboard.FoodItemCard
import com.example.matifood.ui.theme.Typography
import com.example.matifood.viewmodel.FoodViewModel

class ItemListActivity : ComponentActivity() {
    private var title: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        title = intent.getStringExtra("title") ?: ""

        setContent {
            ItemListScreen(
                title = title,
                onBackClick = { finish() },
            )
        }
    }
}

@Composable
fun ItemListScreen(
    title: String,
    onBackClick: () -> Unit,
    viewModel: FoodViewModel = viewModel(),
) {
    val items = viewModel.foodList
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    // Gọi API
    LaunchedEffect(Unit) {
        viewModel.fetchFoodsByCategory(title)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
        ) {
            val (backBtn, titleTxt) = createRefs()

            IconButton(
                onClick = onBackClick,
                modifier = Modifier.constrainAs(backBtn) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Text(
                text = title,
                style = Typography.titleLarge,
                modifier = Modifier.constrainAs(titleTxt) {
                    centerTo(parent)
                }
            )
        }


        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(com.example.matifood.R.color.orange))
            }
        } else if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) { food ->
                    FoodItemCard(food)
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun prev() {
    ItemListScreen(
        title = "Salad",
        onBackClick = {}
    )
}
