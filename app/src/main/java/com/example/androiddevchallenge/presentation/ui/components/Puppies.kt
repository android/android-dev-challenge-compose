package com.example.androiddevchallenge.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.domain.Puppy
import com.example.androiddevchallenge.R
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun Puppies(
    poppies: List<Puppy>,
    navigateToDetails: (Puppy) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState, reverseScrolling = false)
        ) {
            poppies.forEach { puppy ->
                Poppy(puppy = puppy, navigateToDetails)
                Spacer(Modifier.height(8.dp))
            }
        }
    }

}

@Composable
fun Poppy(puppy: Puppy, navigateToDetails: (Puppy) -> Unit) {
    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable { navigateToDetails(puppy) } then Modifier
    ) {
        val (
            name, image
        ) = createRefs()


        CoilImage(
            data = puppy.imageUrl,
            contentDescription = null,
            fadeIn = false,
            contentScale = ContentScale.Crop,
            loading = {
                Box(Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = null
                    )
                }
            },
            error = {
                Box(Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .align(Alignment.Center),
                        painter = painterResource(id = R.drawable.placeholder),
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .size(56.dp)
                .clip(MaterialTheme.shapes.medium)
                .constrainAs(image) {
                    end.linkTo(parent.end, 16.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )

        Text(text = puppy.name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .constrainAs(name) {
                    start.linkTo(parent.start, 16.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}