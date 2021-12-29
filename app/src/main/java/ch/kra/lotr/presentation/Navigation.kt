package ch.kra.lotr.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ch.kra.lotr.presentation.book.composable.BookListScreen
import ch.kra.lotr.presentation.book.composable.ChapterListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "book_list_screen"
    ) {
        composable(route = "book_list_screen") {
            BookListScreen() { bookId, bookName ->
                navController.navigate("chapter_list_screen/$bookId/$bookName")
            }
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
    }
}