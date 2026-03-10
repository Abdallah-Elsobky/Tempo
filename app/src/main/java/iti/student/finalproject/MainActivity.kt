package iti.student.finalproject

import android.R
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import iti.student.finalproject.data.remote.api.RetrofitInstance
import iti.student.finalproject.presentation.components.BottomBar
import iti.student.finalproject.presentation.navigation.NavGraph
import iti.student.finalproject.ui.theme.FinalProjectTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            Log.d(
                "testo today", RetrofitInstance.api
                    .getWeather(30.0443879, 31.2357257).toString()
            )

            Log.d(
                "testo forecast", RetrofitInstance.api
                    .getHourlyForecast(30.0443879, 31.2357257).toString()
            )


            Log.d(
                "testo city names", RetrofitInstance.api
                    .getCityNamesLocalized(30.0443879, 31.2357257, 2).toString()
            )

            Log.d(
                "testo city nemo", RetrofitInstance.api
                    .getPossibleCities("cairo", 5).toString()
            )
        }


        setContent {
            FinalProjectTheme {
                val navController = rememberNavController()

                Scaffold { padding ->
                    Box(
                        modifier = Modifier
                            .padding(padding)
                            .fillMaxSize()
                    ) {
                        MainScreen(navController)
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {

    Box(modifier = Modifier.fillMaxSize()) {
        NavGraph(navController)
        BottomBar(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
    }
}