package com.larrykin.notificationhub.core.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.larrykin.notificationhub.core.presentation.components.ComingSoonScreen
import com.larrykin.notificationhub.core.presentation.viewModels.AnalyticsViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun AnalyticsScreen() {
    val viewModel: AnalyticsViewModel = getViewModel()
    ComingSoonScreen()
//        Column {
//            // Time period selector
//            TimeRangeSelector(
//                options = listOf("Today", "Week", "Month", "Year"),
//                selected = viewModel.selectedTimeRange,
//                onSelect = { viewModel.updateTimeRange(it) }
//            )
//
//            // Stats summary cards
//            Row {
//                StatCard("Total", viewModel.totalNotifications)
//                StatCard("Daily Avg", viewModel.averageDaily)
//                StatCard("Most Active", viewModel.mostActiveApp)
//            }
//
//            // Charts
//            NotificationFrequencyChart(viewModel.frequencyData)
//            AppDistributionChart(viewModel.distributionData)
//
//            // Insights
//            InsightsList(viewModel.insights)
//        }
}