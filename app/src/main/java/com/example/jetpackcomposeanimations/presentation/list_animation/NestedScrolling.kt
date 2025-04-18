package com.example.jetpackcomposeanimations.presentation.list_animation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

@Composable
fun ImageResizeOnScrollExample(
    modifier: Modifier = Modifier,
    maxImageSize: Dp = 300.dp,
    minImageSize: Dp = 100.dp
) {
    var currentImageSize by remember { mutableStateOf(maxImageSize) }
    var imageScale by remember { mutableFloatStateOf(1f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Calculate the change in image size based on scroll delta
                val delta = available.y
                val newImageSize = currentImageSize + delta.dp
                val previousImageSize = currentImageSize

                // Constrain the image size within the allowed bounds
                currentImageSize = newImageSize.coerceIn(minImageSize, maxImageSize)
                val consumed = currentImageSize - previousImageSize

                // Calculate the scale for the image
                imageScale = currentImageSize / maxImageSize

                // Return the consumed scroll amount
                return Offset(0f, consumed.value)
            }
        }
    }

    Box(Modifier.nestedScroll(nestedScrollConnection)) {
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .offset {
                    IntOffset(0, currentImageSize.roundToPx())
                }
        ) {
            // Placeholder list items
            items(100, key = { it }) {
                Text(
                    text = "Item: $it",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Image(
            painter = ColorPainter(Color.Red),
            contentDescription = "Red color image",
            Modifier
                .size(maxImageSize)
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    scaleX = imageScale
                    scaleY = imageScale
                    // Center the image vertically as it scales
                    translationY = -(maxImageSize.toPx() - currentImageSize.toPx()) / 2f
                }
        )
    }
}