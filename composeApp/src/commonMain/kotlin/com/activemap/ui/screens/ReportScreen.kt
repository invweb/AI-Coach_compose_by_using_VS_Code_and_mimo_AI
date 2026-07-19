package com.activemap.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.activemap.model.Activity
import com.activemap.ui.animation.AnimationConfig
import com.activemap.ui.animation.fadeInSlideUp
import com.activemap.viewmodel.MainViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

@Composable
fun ReportScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val activities by viewModel.activities.collectAsState()
    val animationConfig by viewModel.animationConfig.collectAsState()
    val exportResult by viewModel.exportResult.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.selectScreen(com.activemap.viewmodel.Screen.Map) }) {
                Text("←")
            }
            Text(
                text = "Weekly Report",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary card
            item {
                fadeInSlideUp(visible = visible, config = animationConfig) { mod ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(mod),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "This Week",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatItem(
                                    value = "${activities.size}",
                                    label = "Activities"
                                )
                                StatItem(
                                    value = "${activities.sumOf { it.durationMinutes }}",
                                    label = "Minutes"
                                )
                                StatItem(
                                    value = "${activities.count { it.completedAt != null }}",
                                    label = "Completed"
                                )
                            }
                        }
                    }
                }
            }

            // Activities list
            if (activities.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Activities",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(activities.take(10).size) { index ->
                    val activity = activities[index]
                    fadeInSlideUp(visible = visible, config = animationConfig) { mod ->
                        ActivityItem(
                            activity = activity,
                            modifier = mod
                        )
                    }
                }
            } else {
                item {
                    fadeInSlideUp(visible = visible, config = animationConfig) { mod ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(mod),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No activities yet",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Start your first workout to see it here!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }
            }

            // Export button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                fadeInSlideUp(visible = visible, config = animationConfig) { mod ->
                    Button(
                        onClick = { viewModel.exportWeeklyReport() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .then(mod),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Export Markdown Report",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

            // Export result
            exportResult?.let { path ->
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Report Exported!",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = path,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(onClick = { viewModel.clearExportResult() }) {
                                Text("Dismiss")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ActivityItem(
    activity: Activity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activity.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = activity.startedAt.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${activity.durationMinutes}min",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (activity.completedAt != null) "✓" else "In progress",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (activity.completedAt != null) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }
    }
}
