package util.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GeneratorScreen(){
    Column (modifier = Modifier.fillMaxSize()) {
        Box {
            Text("Generator Screen")
        }
    }
}