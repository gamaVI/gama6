import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import util.ListingsResponse
import util.objects.Listing
import util.objects.Posel
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
    // ------------------ Nepremicnine states ------------------
    val apiResponse = remember { mutableStateOf(ListingsResponse(emptyList(), 1)) }
    val listings = remember { mutableStateOf(mutableListOf<Listing>()) }
    val pageNumber = remember { mutableStateOf(1) }

    // ------------------ Generator states ------------------
    var posli = remember { mutableStateOf(mutableListOf<Posel>()) }
    var generate = remember { mutableStateOf(false) }
    var steviloPoslov = remember { mutableStateOf("") }
    val pattern = remember { Regex("^\\d+\$") }
    var showInputSection = remember { mutableStateOf(true) }
    val state = rememberLazyListState()
    MaterialTheme {
        var currentScreen by remember { mutableStateOf(Screen.Generator) }
        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(Modifier.weight(1f)) {
                when (currentScreen) {
                    Screen.Generator -> GeneratorScreen(posli, generate, steviloPoslov, pattern, showInputSection, state)
                    Screen.MojiKvadrati -> MojiKvadratiScreen()
                    Screen.Nepremicnine -> NepremicnineScreen(apiResponse, listings, pageNumber)
                }
            }

            AppBottomNavigation(
                currentScreen,
                onScreenSelected = { screen -> currentScreen = screen },
                modifier = Modifier.align(Alignment.End)
            )
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
