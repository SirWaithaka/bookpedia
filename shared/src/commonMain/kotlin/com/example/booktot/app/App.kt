package com.example.booktot.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.booktot.app.sharedKoinViewModel
import com.example.booktot.book.presentation.SelectedBookViewModel
import com.example.booktot.book.presentation.book_detail.BookDetailScreenRoot
import com.example.booktot.book.presentation.book_detail.BookDetailViewModel

import com.example.booktot.book.presentation.book_list.BookListScreenRoot
import com.example.booktot.book.presentation.book_list.BookListViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(navController, startDestination = Route.BookGraph) {
            navigation<Route.BookGraph>(startDestination = Route.BookList) {
                composable<Route.BookList> {
                    val viewModel = koinViewModel<BookListViewModel>()
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)

                    LaunchedEffect(true) {
                        selectedBookViewModel.onSelectBook(null)
                    }

                    BookListScreenRoot(
                        viewModel = viewModel,
                        onBookClick = { book ->
                            selectedBookViewModel.onSelectBook(book)
                            navController.navigate(Route.BookDetails(book.id))
                        },
                    )
                }
                composable<Route.BookDetails> {
                    val selectedBookViewModel =
                        it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    val selectedBook by selectedBookViewModel.selectedBook.collectAsStateWithLifecycle()
                    val viewModel = koinViewModel<BookDetailViewModel>()

                    BookDetailScreenRoot(
                        viewModel = viewModel,
                        onBackClick = { navController.navigateUp() },
                    )
                }
            }
        }

    }

}

@Composable
private inline fun <reified T : ViewModel> NavBackStackEntry.sharedKoinViewModel(navController: NavController): T {
    val parentEntry = remember(this) {
        navController.getBackStackEntry<Route.BookGraph>()
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}