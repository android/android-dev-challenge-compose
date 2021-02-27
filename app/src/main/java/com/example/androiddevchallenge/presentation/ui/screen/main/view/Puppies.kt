package com.example.androiddevchallenge.presentation.ui.screen.main.view

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.domain.Puppy
import com.example.androiddevchallenge.presentation.ui.theme.PlaceHolder
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.insets.*
import androidx.compose.foundation.layout.Arrangement


@OptIn(ExperimentalAnimatedInsets::class)
@Composable
fun Puppies(
    puppies: List<Puppy>,
    searchAction: (String) -> Unit,
    navigateToDetails: (Puppy) -> Unit
) {
    ProvideWindowInsets(windowInsetsAnimationsEnabled = true) {
        Column(Modifier.fillMaxSize()) {
            val isSearched = remember { mutableStateOf(false) }

            if (puppies.isEmpty() && isSearched.value)
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Text(text = "Not found :/")
                }
            else
                LazyColumn(
                    reverseLayout = false,
                    modifier = Modifier
                        .weight(1f)
                        .nestedScroll(connection = rememberImeNestedScrollConnection())
                ) {
                    items(items = puppies) { puppy ->
                        PuppyItem(puppy = puppy, navigateToDetails)
                    }
                }

            Surface(elevation = 4.dp) {
                val text = remember { mutableStateOf(TextFieldValue()) }
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                        searchAction(it.text)
                        isSearched.value = it.text.isNotBlank()
                    },
                    placeholder = { Text(text = "Find your pet") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

        }
    }
}

@Composable
fun PuppyItem(puppy: Puppy, navigateToDetails: (Puppy) -> Unit) {
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
            contentDescription = "Photo of ${puppy.name}",
            contentScale = ContentScale.Crop,
            loading = { PlaceHolder() },
            error = { PlaceHolder() },
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