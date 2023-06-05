
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun AppBottomNavigation(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = Color(0xFF030711)

    BottomNavigation(modifier = modifier,
        backgroundColor = color,
        ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Sparkasse"
                )
            },
            selected = currentScreen == Screen.Sparkasse,
            onClick = {
                onScreenSelected(Screen.Sparkasse)
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.LightGray
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
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.LightGray
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
            },
            selectedContentColor = Color.White,
            unselectedContentColor = Color.LightGray,
        )
    }
}