package com.example.booktot.book.presentation.book_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import booktot.shared.generated.resources.Res
import booktot.shared.generated.resources.ic_star_rate_24px
import booktot.shared.generated.resources.languages
import booktot.shared.generated.resources.no_description
import booktot.shared.generated.resources.pages
import booktot.shared.generated.resources.rating
import booktot.shared.generated.resources.synopsis
import com.example.booktot.book.presentation.book_detail.components.BlurredBackground
import com.example.booktot.book.presentation.book_detail.components.BookChip
import com.example.booktot.book.presentation.book_detail.components.ChipSize
import com.example.booktot.book.presentation.book_detail.components.TitledContent
import com.example.booktot.core.presentation.SandYellow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.round

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    onBackClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    BookDetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                BookDetailAction.OnBackClick -> onBackClick()
                BookDetailAction.OnFavoriteClick -> Unit
                is BookDetailAction.OnSelectedBookChange -> Unit
            }
            viewModel.onAction(action)
        }
    )
}


@Composable
private fun BookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit,
) {

    BlurredBackground(
        imageUrl = state.book?.imageUrl,
        isFavorite = state.isFavorite,
        onFavoriteClick = { onAction(BookDetailAction.OnFavoriteClick) },
        onBackClick = { onAction(BookDetailAction.OnBackClick) },
        modifier = Modifier.fillMaxSize(),
    ) {
        if (state.book != null) {
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = state.book.title,
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = state.book.authors.joinToString(),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )

                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    state.book.averageRating?.let { rating ->
                        TitledContent(title = stringResource(Res.string.rating)) {
                            BookChip {
                                Text(text = "${round(rating * 10) / 10.0}")
                                Icon(
                                    painter = painterResource(Res.drawable.ic_star_rate_24px),
                                    contentDescription = null,
                                    tint = SandYellow,
                                )
                            }
                        }
                    }
                    state.book.numPages?.let { count ->
                        TitledContent(title = stringResource(Res.string.pages)) {
                            BookChip {
                                Text(text = "$count")
                            }
                        }
                    }
                }

                if (state.book.languages.isNotEmpty()) {
                    TitledContent(
                        title = stringResource(Res.string.languages),
                        modifier = Modifier.padding(vertical = 8.dp),
                    ) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                        ) {
                            state.book.languages.forEach { language ->
                                BookChip(size = ChipSize.SMALL, modifier = Modifier.padding(2.dp)) {
                                    Text(
                                        text = language.uppercase(),
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            }
                        }
                    }
                }

                Text(
                    text = stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp)
                )
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(
                        text = if (state.book.description.isNullOrBlank()) {
                            stringResource(Res.string.no_description)
                        } else {
                            state.book.description
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = if (state.book.description.isNullOrBlank()) Color.Black.copy(alpha = 0.4f) else
                            Color.Black
                    )
                }
            }
        }
    }
}