package com.example.slidercustomcaroussel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.slidercustomcaroussel.ui.theme.SliderCustomCarousselTheme
import kotlin.math.abs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SliderCustomCarousselTheme {
                val items = listOf(
                    R.drawable.kitchen,
                    R.drawable.interior,
                    R.drawable.largehome,
                    R.drawable.realstate
                )

                ImageCarousel(items = items, modifier = Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun ImageCarousel(
    items: List<Int>,  // List of image resource IDs
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val itemWidth = 250.dp
    val selectedItemWidth = 350.dp
    val scaleFactor = 0.7f // Scale factor for non-selected items

    val itemWidthPx = with(LocalDensity.current) { itemWidth.toPx() }
    val selectedItemWidthPx = with(LocalDensity.current) { selectedItemWidth.toPx() }
    val viewportWidthPx = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }

    LazyRow(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = with(LocalDensity.current) { ((viewportWidthPx - selectedItemWidthPx) / 2).toDp() })
    ) {
        itemsIndexed(items) { index, imageResId ->
            val layoutInfo = listState.layoutInfo
            val centerOffset = (viewportWidthPx / 2) - (itemWidthPx / 2)
            val itemOffset = layoutInfo.visibleItemsInfo.find { it.index == index }?.offset ?: 0
            val distance = abs(centerOffset - itemOffset)

            // Determine if the item is close to the center
            val isCentered = distance < itemWidthPx / 2

            // Animate the scale and width values
            val scale by animateFloatAsState(
                targetValue = if (isCentered) 1f else (1 - (distance / (viewportWidthPx / 2 + itemWidthPx)) * scaleFactor).coerceIn(0.8f, 1f)
            )

            val scaledWidth by animateDpAsState(
                targetValue = if (isCentered) selectedItemWidth else with(LocalDensity.current) {
                    (itemWidthPx + ((selectedItemWidthPx - itemWidthPx) * (scale - 0.8f) / 0.2f)).toDp()
                }
            )

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .width(scaledWidth)
                    .height(200.dp)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}




@Composable
fun CarouselScreen() {
    val items = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4
    )

    ImageCarousel(items = items, modifier = Modifier.fillMaxSize())
}

@Preview(showBackground = true)
@Composable
fun CarouselScreenPreview() {
    CarouselScreen()
}
