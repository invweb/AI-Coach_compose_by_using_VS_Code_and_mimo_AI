package com.activemap.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.activemap.map.OsmMap
import com.activemap.map.OsmState
import com.activemap.model.*
import com.activemap.ui.animation.AnimationConfig
import com.activemap.ui.animation.fadeInSlideUp
import com.activemap.ui.animation.pulseGlow
import com.activemap.ui.components.ChallengeCard
import com.activemap.ui.components.PlaceListItem
import com.activemap.ui.components.RecommendationCard
import com.activemap.viewmodel.MainViewModel

@Composable
fun MapScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val places by viewModel.places.collectAsState()
    val recommendations by viewModel.recommendations.collectAsState()
    val challenges by viewModel.challenges.collectAsState()
    val animationConfig by viewModel.animationConfig.collectAsState()
    val currentWeather = viewModel.currentWeather.collectAsState()
    val isDarkTheme = viewModel.isDarkTheme.collectAsState()

    var osmState by remember { mutableStateOf(OsmState()) }
    var selectedPlace by remember { mutableStateOf<Place?>(null) }
    var showWorkoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.generateDailyChallenge()
        viewModel.generateRecommendations()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top bar with weather
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ActiveMap",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    currentWeather.value?.let { weather ->
                        Text(
                            text = "${weather.temperature.toInt()}°C • ${weather.condition}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
                Row {
                    IconButton(onClick = { viewModel.toggleTheme() }) {
                        Text(if (isDarkTheme.value) "☀️" else "🌙")
                    }
                    IconButton(onClick = { viewModel.selectScreen(com.activemap.viewmodel.Screen.Report) }) {
                        Text("📊")
                    }
                }
            }
        }

        // OSM Map
        Box(modifier = Modifier.fillMaxWidth().weight(0.5f)) {
            OsmMap(
                state = osmState,
                onStateChanged = { osmState = it },
                places = places,
                modifier = Modifier.fillMaxSize()
            )

            // Zoom controls
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                FloatingActionButton(
                    onClick = {
                        osmState = osmState.copy(zoom = (osmState.zoom + 1).coerceAtMost(19))
                    },
                    modifier = Modifier.size(40.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Text("+", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
                FloatingActionButton(
                    onClick = {
                        osmState = osmState.copy(zoom = (osmState.zoom - 1).coerceAtLeast(1))
                    },
                    modifier = Modifier.size(40.dp),
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Text("-", fontSize = 20.sp)
                }
            }

            // Map label
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🗺",
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "OpenStreetMap",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Legend
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 2.dp
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    LegendItem(color = Color(0xFF4CAF50), label = "Park")
                    LegendItem(color = Color(0xFFFF5722), label = "Gym")
                    LegendItem(color = Color(0xFF2196F3), label = "Stadium")
                    LegendItem(color = Color(0xFF9C27B0), label = "Street")
                }
            }
        }

        // Bottom section with recommendations and places
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            if (challenges.isNotEmpty()) {
                item {
                    Text(
                        text = "Today's Challenge",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                itemsIndexed(challenges) { _, challenge ->
                    ChallengeCard(
                        challenge = challenge,
                        animationConfig = animationConfig,
                        onComplete = { viewModel.completeChallenge(challenge.id) }
                    )
                }
            }

            if (recommendations.isNotEmpty()) {
                item {
                    Text(
                        text = "Recommended for You",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                itemsIndexed(recommendations) { _, recommendation ->
                    RecommendationCard(
                        recommendation = recommendation,
                        animationConfig = animationConfig,
                        onClick = {
                            val place = places.find { it.id == recommendation.placeId }
                            if (place != null) {
                                selectedPlace = place
                                showWorkoutDialog = true
                            }
                        }
                    )
                }
            }

            item {
                Text(
                    text = "Your Places",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            itemsIndexed(places) { index, place ->
                PlaceListItem(
                    place = place,
                    index = index,
                    animationConfig = animationConfig,
                    onClick = {
                        selectedPlace = place
                        showWorkoutDialog = true
                    }
                )
            }

            item {
                Button(
                    onClick = { viewModel.selectScreen(com.activemap.viewmodel.Screen.AddPlace) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("+ Add New Place")
                }
            }
        }
    }

    if (showWorkoutDialog && selectedPlace != null) {
        AlertDialog(
            onDismissRequest = { showWorkoutDialog = false },
            title = { Text("Start Workout") },
            text = {
                Column {
                    Text("Start a workout at ${selectedPlace?.name}?")
                    Spacer(modifier = Modifier.height(8.dp))
                    pulseGlow(config = animationConfig) { mod ->
                        Text(text = "Ready to go!", modifier = mod)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    selectedPlace?.let { place ->
                        viewModel.startActivity(place.id, "Workout at ${place.name}")
                    }
                    showWorkoutDialog = false
                }) {
                    Text("Start")
                }
            },
            dismissButton = {
                TextButton(onClick = { showWorkoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 1.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = color,
            modifier = Modifier.size(8.dp)
        ) {}
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
