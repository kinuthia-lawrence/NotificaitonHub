package com.larrykin.notificationhub.core.presentation.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.larrykin.notificationhub.ui.theme.darkBackground
import com.larrykin.notificationhub.ui.theme.lightPurpleColor
import com.larrykin.notificationhub.ui.theme.purpleColor
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(true) }
    var showContent by remember { mutableStateOf(false) }



    LaunchedEffect(Unit) {
        delay(5000) // Splash screen duration
        isVisible = false
        showContent = true
        delay(10000) // Delay before navigating to the next screen
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground),
        contentAlignment = Alignment.Center
    ) {
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


        AnimatedVisibility(visible = showContent) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to NotificationHub",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Customize your notification experience with powerful controls for every app. Set custom volumes, create profiles\n and \ntake full control of your alerts.",
                    color = lightPurpleColor,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = purpleColor),
                    border = BorderStroke(2.dp, Color.White),
                    modifier = Modifier,
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text(
                        text = "Get Started",
                        color = Color.White,
                        fontSize = 20.sp
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
    SplashScreen(navController)

}
