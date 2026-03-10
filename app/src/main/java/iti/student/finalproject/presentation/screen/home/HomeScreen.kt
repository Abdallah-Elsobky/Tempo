package iti.student.finalproject.presentation.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun HomeScreen() {
    LazyColumn() {
        items(100){
            Text("Helloooooooooooooooooooo wooooooooooooorld")
        }
    }
}