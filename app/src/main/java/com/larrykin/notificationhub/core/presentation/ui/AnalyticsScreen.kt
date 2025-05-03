package com.larrykin.notificationhub.core.presentation.ui

    import androidx.compose.runtime.Composable
    import androidx.navigation.NavController
    import com.larrykin.notificationhub.core.presentation.components.ComingSoonScreen

    @Composable
    fun AnalyticsScreen(navController: NavController) {
        ComingSoonScreen(
            featureName = "Analytics",
            message = "We're building powerful notification analytics to help you gain insights. Stay tuned!"
        )
    }