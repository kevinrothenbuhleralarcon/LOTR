package ch.kra.lotr.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.kra.lotr.R
import ch.kra.lotr.core.Routes
import ch.kra.lotr.presentation.book.composable.BookListScreen
import ch.kra.lotr.presentation.book.composable.ChapterListScreen
import ch.kra.lotr.presentation.character.composable.CharacterDetailScreen
import ch.kra.lotr.presentation.character.composable.CharacterListScreen
import ch.kra.lotr.presentation.character.viewModel.CharacterViewModel
import ch.kra.lotr.presentation.movie.composable.MovieDetailScreen
import ch.kra.lotr.presentation.movie.composable.MovieListScreen
import ch.kra.lotr.presentation.movie.viewmodel.MovieViewModel
import kotlin.time.TimeSource

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.BOOK_LIST
    ) {
        composable(route = Routes.BOOK_LIST) {
            BookListScreen(
                navigate = { event ->
                    navController.navigate(event.route)
                },
            )
        }

        composable(
            route = Routes.CHAPTER_LIST + "/{bookId}/{bookName}",
            arguments = listOf(
                navArgument("bookId") {
                    type = NavType.StringType
                },
                navArgument("bookName") {
                    type = NavType.StringType
                }
            )
        ) {
            val bookId = remember {
                it.arguments?.getString("bookId")
            }

            val bookName = remember {
                it.arguments?.getString("bookName")
            }

            ChapterListScreen() {
                navController.popBackStack()
            }
        }
        movieGraph(navController = navController)

        characterGraph(navController = navController)
    }
}


private fun NavGraphBuilder.movieGraph(navController: NavHostController) {
    navigation(startDestination = Routes.MOVIE_LIST, route = "movie") {

        composable(route = Routes.MOVIE_LIST) {
            val parentEntry = remember {
                navController.getBackStackEntry("movie")
            }

            val viewModel: MovieViewModel = hiltViewModel(parentEntry)
            MovieListScreen(
                viewModel = viewModel,
                navigate = { event ->
                    navController.navigate(event.route)
                }
            )
        }

        composable(route = Routes.MOVIE_DETAIL) {
            val parentEntry = remember {
                navController.getBackStackEntry("movie")
            }

            val viewModel: MovieViewModel = hiltViewModel(parentEntry)
            MovieDetailScreen(
                viewModel = viewModel
            ) {
                navController.popBackStack()
            }
        }
    }
}

private fun NavGraphBuilder.characterGraph(navController: NavHostController) {
    navigation(startDestination = Routes.CHARACTER_LIST, route = "character") {

        composable(route = Routes.CHARACTER_LIST) {
            val parentEntry = remember {
                navController.getBackStackEntry("character")
            }

            val viewModel: CharacterViewModel = hiltViewModel(parentEntry)
            CharacterListScreen(
                viewModel = viewModel,
                navigate = { event ->
                    navController.navigate(event.route)
                }
            )
        }

        composable(route = Routes.CHARACTER_DETAIL) {
            val parentEntry = remember {
                navController.getBackStackEntry("character")
            }
            val viewModel: CharacterViewModel = hiltViewModel(parentEntry)
            CharacterDetailScreen(viewModel = viewModel) {
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun NavDrawer(
    items: List<NavigationBarItem>,
    backStackEntryState: State<NavBackStackEntry?>,
    modifier: Modifier = Modifier,
    navigate: (String) -> Unit
) {
    BottomNavigation (
        modifier = modifier,
        backgroundColor = Color.DarkGray,
        elevation = 5.dp
            ) {
        items.forEach { item ->
            val selected = item.route == backStackEntryState.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { navigate(item.route) },
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Color.Gray,
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        Icon(
                            painter = item.painter,
                            contentDescription = item.name
                        )
                        if (selected) {
                            Text(text = item.name)
                        }
                    }
                }
            )
        }
    }
}