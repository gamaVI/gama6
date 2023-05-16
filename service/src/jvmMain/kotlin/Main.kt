import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import util.screens.GeneratorScreen
import util.screens.MojiKvadratiScreen
import util.screens.NepremicnineScreen

enum class Screen {
    Generator,
    MojiKvadrati,
    Nepremicnine
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.Generator) }
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.weight(1f)) {
                when (currentScreen) {
                    Screen.Generator -> GeneratorScreen()
                    Screen.MojiKvadrati -> MojiKvadratiScreen()
                    Screen.Nepremicnine -> NepremicnineScreen()
                }
            }

            BottomNavigation(Modifier.align(Alignment.End)) {
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Filled.Home,
                            contentDescription = "Moji kvadrati"
                        )
                    },
                    selected = true,
                    onClick = { currentScreen = Screen.MojiKvadrati }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.Create,
                            contentDescription = "Generator"
                        )
                    },
                    selected = true,
                    onClick = { currentScreen = Screen.Generator }
                )
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = androidx.compose.material.icons.Icons.Default.List,
                            contentDescription = "Nepremicnine"
                        )
                    },
                    selected = true,
                    onClick = { currentScreen = Screen.Nepremicnine }
                )
            }
        }
    }
}


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "gama6 service scraper",
        state = rememberWindowState(
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(1200.dp, 1000.dp)
        ),
        undecorated = false,
        resizable = true
    ) {
        App()
    }
}
