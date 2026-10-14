package com.example.booktot.book.presentation.book_list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import booktot.shared.generated.resources.Res
import booktot.shared.generated.resources.close_hint
import booktot.shared.generated.resources.ic_close_24px
import booktot.shared.generated.resources.ic_search_24px
import booktot.shared.generated.resources.search_hint
import com.example.booktot.core.presentation.DarkBlue
import com.example.booktot.core.presentation.DesertWhite
import com.example.booktot.core.presentation.SandYellow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookSearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = SandYellow,
            backgroundColor = SandYellow,
        )
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            shape = RoundedCornerShape(100),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = DarkBlue,
                focusedBorderColor = SandYellow
            ),
            placeholder = {
                Text(
                    text = stringResource(Res.string.search_hint)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(Res.drawable.ic_search_24px),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f),
                )
            },
            singleLine = true,
            keyboardActions = KeyboardActions(onSearch = { onImeSearch() }),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search,
            ),
            trailingIcon = {
                AnimatedVisibility(visible = searchQuery.isNotBlank()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_close_24px),
                            contentDescription = stringResource(Res.string.close_hint),
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            },
            modifier = modifier
                .background(shape = RoundedCornerShape(100), color = DesertWhite)
                .minimumInteractiveComponentSize()
        )
    }
}

@Preview
@Composable
private fun BookSearchBarPreview() {
    MaterialTheme {
        BookSearchBar(
            searchQuery = "Kotlin",
            onSearchQueryChange = {},
            onImeSearch = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}