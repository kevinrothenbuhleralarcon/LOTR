package ch.kra.lotr.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ch.kra.lotr.R
import ch.kra.lotr.core.Routes
import ch.kra.lotr.presentation.ui.theme.LOTRTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LOTRTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavDrawer(
                            items = listOf(
                                NavigationBarItem(
                                    name = "Books",
                                    route = Routes.BOOK_LIST,
                                    painter = painterResource(id = R.drawable.ic_book_ic)
                                ),
                                NavigationBarItem(
                                    name = "Movies",
                                    route = Routes.MOVIE_LIST,
                                    painter = painterResource(id = R.drawable.ic_movie)
                                ),
                                NavigationBarItem(
                                    name = "Characters",
                                    route = Routes.CHARACTER_LIST,
                                    painter = rememberVectorPainter(image = Icons.Default.Person)
                                )
                            ),
                            backStackEntryState = navController.currentBackStackEntryAsState(),
                            navigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(Routes.BOOK_LIST) {
                                        if (route == Routes.BOOK_LIST) {
                                            inclusive = true
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                        )
                    }
                ) { padding ->
                    Box(
                        modifier = Modifier.padding(padding)
                    ) {
                        Navigation(navController)
                    }
                }
            }
        }
    }
}
