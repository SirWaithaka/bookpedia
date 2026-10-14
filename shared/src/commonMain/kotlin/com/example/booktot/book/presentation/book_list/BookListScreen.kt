package com.example.booktot.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import booktot.shared.generated.resources.Res
import booktot.shared.generated.resources.favorites
import booktot.shared.generated.resources.no_favorites
import booktot.shared.generated.resources.no_search_results
import booktot.shared.generated.resources.search_results
import com.example.booktot.book.domain.Book
import com.example.booktot.book.presentation.book_list.components.BookList
import com.example.booktot.book.presentation.book_list.components.BookSearchBar
import com.example.booktot.core.presentation.DarkBlue
import com.example.booktot.core.presentation.DesertWhite
import com.example.booktot.core.presentation.SandYellow
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookListScreenRoot(
    viewModel: BookListViewModel = koinViewModel(),
    onBookClick: (Book) -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    BookListScreen(
        state,
        onAction = { action ->
            when (action) {
                is BookListAction.OnBookClick -> onBookClick(action.book)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun BookListScreen(
    state: BookListState,
    onAction: (BookListAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val pagerState = rememberPagerState { 2 }
    val searchResultsListState = rememberLazyListState()
    val favoritesListState = rememberLazyListState()

    LaunchedEffect(state.searchResults) {
        searchResultsListState.animateScrollToItem(0)
    }

    LaunchedEffect(state.selectedTabIndex) {
        pagerState.animateScrollToPage(state.selectedTabIndex)
    }

    LaunchedEffect(pagerState.currentPage) {
        onAction(BookListAction.OnTabSelected(pagerState.currentPage))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        BookSearchBar(
            searchQuery = state.searchQuery,
            onSearchQueryChange = {
                onAction(BookListAction.OnSearchQueryChange(it))
            },
            onImeSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite,
            shape = RoundedCornerShape(
                topStart = 32.dp,
                topEnd = 32.dp,
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PrimaryTabRow(
                    selectedTabIndex = state.selectedTabIndex,
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .widthIn(max = 700.dp)
                        .fillMaxWidth(),
                    containerColor = DesertWhite,
                    indicator = {
                        TabRowDefaults.SecondaryIndicator(
                            color = SandYellow,
                            modifier = Modifier.tabIndicatorOffset(state.selectedTabIndex),
                        )
                    },
                ) {
                    // tab 1
                    Tab(
                        selected = state.selectedTabIndex == 0,
                        onClick = { onAction(BookListAction.OnTabSelected(0)) },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                    ) {
                        Text(
                            text = stringResource(Res.string.search_results),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                    // tab 2
                    Tab(
                        selected = state.selectedTabIndex == 1,
                        onClick = { onAction(BookListAction.OnTabSelected(1)) },
                        modifier = Modifier.weight(1f),
                        selectedContentColor = SandYellow,
                        unselectedContentColor = Color.Black.copy(alpha = 0.5f),
                    ) {
                        Text(
                            text = stringResource(Res.string.favorites),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    }
                }
                // a little space beneath the tab row
                Spacer(modifier = Modifier.height(4.dp))

                // horizontal pager
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) { pageIndex ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        when (pageIndex) {
                            0 -> {
                                if (state.isLoading) CircularProgressIndicator()
                                else {
                                    when {
                                        state.errorMessage != null -> {
                                            Text(
                                                text = state.errorMessage.asString(),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error,
                                            )
                                        }
                                        state.searchResults.isEmpty() -> {
                                            Text(
                                                text = stringResource(Res.string.no_search_results),
                                                textAlign = TextAlign.Center,
                                                style = MaterialTheme.typography.headlineSmall,
                                                color = MaterialTheme.colorScheme.error,
                                            )
                                        }
                                        else -> {
                                            BookList(
                                                books = state.searchResults,
                                                onBookClick = { onAction(BookListAction.OnBookClick(it))},
                                                modifier = Modifier.fillMaxSize(),
                                                scrollState = searchResultsListState,
                                            )
                                        }
                                    }
                                }
                            }

                            1 -> {
                                if(state.favoriteBooks.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.no_favorites),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                    )
                                } else {
                                    BookList(
                                        books = state.favoriteBooks,
                                        onBookClick = { onAction(BookListAction.OnBookClick(it))},
                                        modifier = Modifier.fillMaxSize(),
                                        scrollState = favoritesListState,
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BookListScreenPreview() {
    val books = (1..100).map {
        Book(
            id = it.toString(),
            title = "Book ${it}",
            imageUrl = "http://test.com",
            authors = listOf("John Doe"),
            description = "Description ${it}",
            languages = listOf("EN", "ES"),
            firstPublishYear = "2009",
            averageRating = 4.67839,
            ratingCount = 5,
            numPages = 100,
            numEditions = 3,
        )
    }

    BookListScreen(
        state = BookListState(searchResults = books),
        onAction = {},
    )
}