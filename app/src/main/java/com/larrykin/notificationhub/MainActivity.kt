package com.larrykin.notificationhub

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.rememberNavController
import com.larrykin.notificationhub.core.navigation.NavGraph
import com.larrykin.notificationhub.core.presentation.viewModels.MainViewModel
import com.larrykin.notificationhub.ui.theme.NotificationHubTheme
import com.larrykin.notificationhub.ui.theme.darkPurpleColor
import com.larrykin.notificationhub.ui.theme.lightPurpleColor
import com.larrykin.notificationhub.ui.theme.purpleColor
import kotlinx.coroutines.delay
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope

class MainActivity : ComponentActivity(), AndroidScopeComponent {

    override val scope: Scope by activityScope()
    private val hello by inject<String>(named("hello"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = getViewModel<MainViewModel>()
        println(hello)
        enableEdgeToEdge()
        setContent {
            NotificationHubTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    NavGraph(navController = navController, viewModel = viewModel)
                    // Use the viewModel to check notification access
                    CheckNotificationAccess(
                        viewModel = viewModel,
                        startActivity = { intent -> startActivity(intent) }
                    )
                }
            }
        }
    }

    @Composable
    fun CheckNotificationAccess(
        viewModel: MainViewModel,
        startActivity: (Intent) -> Unit
    ) {
        // Collect state from ViewModel
        val showDialog by viewModel.showPermissionDialog.collectAsState()
        val hasAccess by viewModel.hasNotificationAccess.collectAsState()
        val checkingPermission by viewModel.checkingForPermission.collectAsState()

        // Check notification permission on launch
        LaunchedEffect(Unit) {
            viewModel.checkNotificationPermission(this@MainActivity)
        }

        // Monitor permission state when checking
        LaunchedEffect(checkingPermission) {
            if (checkingPermission) {
                while (!hasAccess) {
                    delay(1000)
                    viewModel.checkNotificationPermission(this@MainActivity)
                }
                viewModel.stopCheckingPermission()
            }
        }

        // Show permission dialog if needed
        AnimatedVisibility(
            visible = !hasAccess && showDialog,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Dialog(
                onDismissRequest = { },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    darkPurpleColor,
                                    darkPurpleColor.copy(alpha = 0.95f)
                                )
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Animation and text content remains unchanged
                        val infiniteTransition = rememberInfiniteTransition()
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = 500,
                                    easing = FastOutSlowInEasing
                                ),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        // Animated Icon Box
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .graphicsLayer(scaleX = scale, scaleY = scale),
                            contentAlignment = Alignment.Center
                        ) {
                            // Icon content unchanged
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(purpleColor.copy(alpha = 0.2f))
                            )

                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(purpleColor.copy(alpha = 0.5f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Permission Icon",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Permission Required",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "NotificationHub needs access to your notifications to provide custom controls and enhanced features.",
                            color = lightPurpleColor,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
                                viewModel.startCheckingPermission()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = purpleColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "Grant Access",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }

                        // Add button to dismiss dialog without granting permission
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                viewModel.dismissPermissionDialog()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Not Now",
                                fontSize = 14.sp,
                                color = lightPurpleColor,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }

    //? preview is not able to construct view model references that have a constructor.
    //? Create a root composable and have the actual screen composable   inside it( the actual screen composable takes in the actual values as parameters instead of the viewmodel
    @Preview
    @Composable
    fun PreviewCheckNotificationAccess() {
        NotificationHubTheme {
            // Preview the CheckNotificationAccess composable
            CheckNotificationAccess(
                viewModel = MainViewModel(),
                startActivity = {}
            )
        }
    }
}


@Preview
@Composable
fun PreviewMain() {
    NotificationHubTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            NavGraph(navController = navController, TODO())
        }
    }
}