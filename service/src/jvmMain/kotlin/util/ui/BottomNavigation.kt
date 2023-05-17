import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppBottomNavigation(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomNavigation(modifier = modifier) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Moji kvadrati"
                )
            },
            selected = currentScreen == Screen.MojiKvadrati,
            onClick = {
                onScreenSelected(Screen.MojiKvadrati)
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = "Generator"
                )
            },
            selected = currentScreen == Screen.Generator,
            onClick = {
                onScreenSelected(Screen.Generator)
            }
        )
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "Nepremicnine"
                )
            },
            selected = currentScreen == Screen.Nepremicnine,
            onClick = {
                onScreenSelected(Screen.Nepremicnine)
            }
        )
    }
}