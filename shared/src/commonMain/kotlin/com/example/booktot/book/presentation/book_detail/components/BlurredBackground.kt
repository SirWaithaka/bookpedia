package com.example.booktot.book.presentation.book_detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import booktot.shared.generated.resources.Res
import booktot.shared.generated.resources.book_cover
import booktot.shared.generated.resources.book_error
import booktot.shared.generated.resources.go_back
import booktot.shared.generated.resources.ic_arrow_back_24px
import booktot.shared.generated.resources.ic_favorite_24px
import booktot.shared.generated.resources.ic_favorite_filled_24px
import booktot.shared.generated.resources.mark_favorite
import booktot.shared.generated.resources.remove_favorite
import coil3.compose.rememberAsyncImagePainter
import com.example.booktot.core.presentation.DarkBlue
import com.example.booktot.core.presentation.DesertWhite
import com.example.booktot.core.presentation.SandYellow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun BlurredBackground(
    imageUrl: String?,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    var imageLoadResult by remember { mutableStateOf<Result<Painter>?>(null) }
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onSuccess = {
            val size = it.painter.intrinsicSize
            // check image was properly loaded
            imageLoadResult = if (size.width > 1 && size.height > 1) Result.success(it.painter)
            else Result.failure(Exception("Invalid image dimenstions"))
        }
    )

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxWidth()
                    .background(DarkBlue)
            ) {
                imageLoadResult?.getOrNull()?.let { painter ->
                    Image(
                        painter = painter,
                        contentDescription = stringResource(Res.string.book_cover),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().blur(20.dp),
                    )
                }
            }

            Box(modifier = Modifier.weight(0.7f).fillMaxWidth().background(DesertWhite))
        }

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 16.dp, start = 16.dp)
                .statusBarsPadding()
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back_24px),
                contentDescription = stringResource(Res.string.go_back),
                tint = Color.White,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            ElevatedCard(
                modifier = Modifier
                    .height(270.dp)
                    .aspectRatio(2 / 3f),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 15.dp)
            ) {
                AnimatedContent(targetState = imageLoadResult) { result ->
                    when (result) {
                        null -> CircularProgressIndicator()
                        else -> {
                            Box {
                                Image(
                                    painter = if (result.isSuccess) painter else {
                                        painterResource(Res.drawable.book_error)
                                    },
                                    contentDescription = stringResource(Res.string.book_cover),
                                    modifier = Modifier.fillMaxSize().background(Color.Transparent),
                                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit,
                                )

                                IconButton(
                                    onClick = onFavoriteClick,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .background(
                                            brush = Brush.radialGradient(
                                                colors = listOf(SandYellow, Color.Transparent),
                                                radius = 70f,
                                            )
                                        )
                                ) {
                                    Icon(
                                        painter = if (isFavorite) painterResource(Res.drawable.ic_favorite_filled_24px) else
                                            painterResource(Res.drawable.ic_favorite_24px),
                                        contentDescription = if (isFavorite) stringResource(Res.string.remove_favorite) else
                                         stringResource(Res.string.mark_favorite),
                                        tint = Color.Red,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            content()
        }

    }
}