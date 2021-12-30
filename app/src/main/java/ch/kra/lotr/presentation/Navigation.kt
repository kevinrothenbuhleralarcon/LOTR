package ch.kra.lotr.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ch.kra.lotr.presentation.book.composable.BookListScreen
import ch.kra.lotr.presentation.book.composable.ChapterListScreen
import ch.kra.lotr.presentation.movie.composable.MovieListScreen
import ch.kra.lotr.presentation.movie.viewmodel.MovieViewModel
import kotlin.time.TimeSource

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "book_list_screen"
    ) {
        composable(route = "book_list_screen") {
            BookListScreen(
                navigate =  { bookId, bookName ->
                navController.navigate("chapter_list_screen/$bookId/$bookName")
                },
                navigateToMovies = {
                    navController.navigate("movie_list_screen")
                }
            )
        }

        composable(
            route = "chapter_list_screen/{bookId}/{bookName}",
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

            ChapterListScreen(bookId = bookId ?: "", bookName = bookName ?: "") {
                navController.popBackStack()
            }
        }
        composable(route = "movie_list_screen") {
            val viewModel: MovieViewModel = hiltViewModel()
            MovieListScreen(viewModel = viewModel, navigate = {navController.navigate("book_list_screen")})
        }
    }
}

@Composable
fun NavDrawer(
    navController: NavHostController
) {

}