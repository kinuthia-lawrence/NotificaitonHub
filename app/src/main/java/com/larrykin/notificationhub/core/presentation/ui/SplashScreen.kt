package com.larrykin.notificationhub.core.presentation.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel
import com.larrykin.notificationhub.ui.theme.darkBackground
import com.larrykin.notificationhub.ui.theme.lightPurpleColor
import com.larrykin.notificationhub.ui.theme.purpleColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, viewModel: MainViewModel) {
    var isVisible by remember { mutableStateOf(true) }
    var showContent by remember { mutableStateOf(false) }

    val hasAccess by viewModel.hasNotificationAccess.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkNotificationPermission()
    }



    LaunchedEffect(Unit) {
        delay(5000) // Splash screen duration
        isVisible = false
        if (!hasAccess) {
            // No permission - show second splash screen
            showContent = true
            delay(10000)
        }
        navController.navigate("dashboard") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground),
        contentAlignment = Alignment.Center
    ) {
        // first splash screen
        AnimatedVisibility(visible = isVisible) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Animated rings
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    val infiniteTransition = rememberInfiniteTransition(label = "ring animation")
                    val outerScale by infiniteTransition.animateFloat(
                        initialValue = 0.5f,
                        targetValue = 1.5f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale animation"
                    )

//                     Outer ring with scaling
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .graphicsLayer(scaleX = outerScale, scaleY = outerScale)
                            .clip(CircleShape)
                            .border(4.dp, purpleColor.copy(alpha = 0.5f), CircleShape),

                        )

                    // Inner ring with icon
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .border(1.dp, purpleColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notification Icon",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "NotificationHub",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Take control of your notifications",
                    color = lightPurpleColor,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                CircularProgressIndicator(
                    color = purpleColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
        //second splash screen
        AnimatedVisibility(
            visible = showContent && !hasAccess,
            enter = fadeIn(animationSpec = tween(800)) + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title with gradient brush
                val gradient = Brush.linearGradient(
                    colors = listOf(purpleColor, lightPurpleColor)
                )

                Text(
                    text = "Welcome to NotificationHub",
                    style = TextStyle(
                        brush = gradient,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Customize your notification experience with powerful controls for every app. Set custom volumes, create profiles and take full control of your alerts.",
                    color = lightPurpleColor,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Glowing animated button
                val pulseTransition = rememberInfiniteTransition()
                val pulse by pulseTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000),
                        repeatMode = RepeatMode.Reverse
                    )
                )

                Button(
                    onClick = {
                        navController.navigate("dashboard") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = purpleColor),
                    border = BorderStroke(2.dp, Color.White),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .graphicsLayer(scaleX = pulse, scaleY = pulse)
                        .shadow(8.dp, shape = RoundedCornerShape(50))
                ) {
                    Text(
                        text = "Get Started",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

    }
}


@Preview
@Composable
fun PreviewSplashScreen() {
    val navController = rememberNavController()
    val viewModel = MainViewModel(TODO())
    SplashScreen(navController, viewModel)

}
