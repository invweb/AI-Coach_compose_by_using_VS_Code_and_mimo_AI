package com.activemap.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.activemap.ui.animation.AnimationConfig
import com.activemap.ui.animation.fadeInSlideUp
import com.activemap.ui.animation.pulseGlow
import com.activemap.viewmodel.MainViewModel

@Composable
fun ChallengeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val challenges by viewModel.challenges.collectAsState()
    val animationConfig by viewModel.animationConfig.collectAsState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.generateDailyChallenge()
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
                text = "Challenges",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(48.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Daily challenge
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
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "🎯 Daily Challenge",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Complete today's challenge to keep your streak going!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Challenges list
            if (challenges.isNotEmpty()) {
                items(challenges.size) { index ->
                    val challenge = challenges[index]
                    fadeInSlideUp(visible = visible, config = animationConfig) { mod ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(mod),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (challenge.completed)
                                    MaterialTheme.colorScheme.tertiaryContainer
                                else
                                    MaterialTheme.colorScheme.surface
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
                                        text = challenge.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = challenge.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }

                                if (!challenge.completed) {
                                    pulseGlow(config = animationConfig) { mod ->
                                        Button(
                                            onClick = { viewModel.completeChallenge(challenge.id) },
                                            modifier = Modifier.then(mod),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text("Complete")
                                        }
                                    }
                                } else {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.tertiary
                                    ) {
                                        Text(
                                            text = "✓ Done",
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            color = MaterialTheme.colorScheme.onTertiary
                                        )
                                    }
                                }
                            }
                        }
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
                                    text = "No challenges today",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Check back tomorrow for a new challenge!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                )
                            }
                        }
                    }
                }
            }

            // Generate new challenge button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                fadeInSlideUp(visible = visible, config = animationConfig) { mod ->
                    OutlinedButton(
                        onClick = { viewModel.generateDailyChallenge() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(mod),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Generate New Challenge")
                    }
                }
            }
        }
    }
}
