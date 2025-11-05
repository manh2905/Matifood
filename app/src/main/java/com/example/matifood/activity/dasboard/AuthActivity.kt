package com.example.matifood.activity.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.matifood.R
import com.example.matifood.activity.dasboard.profile.LoginScreenEmbedded
import com.example.matifood.ui.theme.Typography
import com.example.matifood.viewmodel.AuthViewModel

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthScreen(
                viewModel = viewModel(),
                onBack = { finish() },
                onAuthSuccess = { finish() }
            )
        }
    }
}

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onAuthSuccess: () -> Unit
) {
    var showLogin by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = Color.White,
                elevation = 0.dp,
                title = {
                    Text(
                        text = if (showLogin) "Đăng nhập" else "Đăng ký",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFB8C00)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color(0xFFFB8C00)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.White
                )
                .padding(innerPadding)

        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .align(Alignment.TopCenter)  //  Căn giữa ngang, nằm trên cùng
                    .padding(top = 50.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .border(0.dp, Color.Transparent, CircleShape)
            )
            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
                    .fillMaxWidth(),
                elevation = 8.dp,
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                backgroundColor = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Crossfade(targetState = showLogin, animationSpec = tween(300)) { it ->
                        if (it) LoginScreenEmbedded(viewModel, onAuthSuccess)
                        else SignupScreenEmbedded(viewModel, onAuthSuccess)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    ToggleText(showLogin = showLogin, onToggle = { showLogin = !showLogin })
                }
            }
        }
    }
}


@Composable
fun ToggleText(
    showLogin: Boolean,
    onToggle: () -> Unit)
{
    val (text, toggleText) = if (showLogin){
        "Bạn chưa đăng ký" to "Đăng ký"
    } else {
        "Bạn đã có tài khoản ?" to "Đăng nhập"
    }

    Row (
        modifier = Modifier
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically

    ){
        Text(
            text = text,
            color = Color.Gray,
            style = Typography.bodySmall
        )
        TextButton(
            onClick = onToggle

        ) {
            Text(
                text = toggleText,
                fontSize = 15.sp,
                color = Color(0xFFFB8C00), // Màu cam
                style = Typography.bodySmall
            )
        }

    }
}
