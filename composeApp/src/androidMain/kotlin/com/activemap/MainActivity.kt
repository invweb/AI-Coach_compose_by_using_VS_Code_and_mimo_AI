package com.activemap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.activemap.db.ActiveMapDatabase
import com.activemap.platform.AndroidFileExporter
import com.activemap.platform.AndroidLocationProvider
import com.activemap.platform.AndroidWeatherProvider
import com.activemap.repository.SqlDelightRepository
import com.activemap.viewmodel.MainViewModel
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val driver = AndroidSqliteDriver(
            schema = ActiveMapDatabase.Schema,
            context = this,
            name = "activemap.db"
        )
        val database = ActiveMapDatabase(driver)
        val repository = SqlDelightRepository(database)

        val locationProvider = AndroidLocationProvider(this)
        val weatherProvider = AndroidWeatherProvider()
        val fileExporter = AndroidFileExporter(this)

        val viewModel = MainViewModel(
            repository = repository,
            locationProvider = locationProvider,
            weatherProvider = weatherProvider,
            fileExporter = fileExporter
        )

        setContent {
            Surface(modifier = Modifier.fillMaxSize()) {
                App(viewModel = viewModel)
            }
        }
    }
}
