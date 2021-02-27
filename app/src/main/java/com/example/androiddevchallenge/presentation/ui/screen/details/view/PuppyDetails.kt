package com.example.androiddevchallenge.presentation.ui.screen.details.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.domain.Puppy
import com.example.androiddevchallenge.presentation.ui.theme.PlaceHolder
import dev.chrisbanes.accompanist.coil.CoilImage

@Preview
@Composable
fun PuppyDetails(
    puppy: Puppy = Puppy(
        "name",
        "3",
        "aaa",
        "https://s3.india.com/wp-content/uploads/2020/08/5170590074_714d36db83_b.jpg"
    ), onAdoptClick: () -> Unit = {}
) {
    Column {
        ConstraintLayout(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            val (image, name, age, breed, button) = createRefs()

            CoilImage(
                data = puppy.imageUrl,
                contentDescription = "Photo of ${puppy.name}",
                contentScale = ContentScale.Crop,
                loading = { PlaceHolder() },
                error = { PlaceHolder() },
                modifier = Modifier
                    .aspectRatio(1F)
                    .constrainAs(image) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    }
            )

            Text(
                fontSize = 28.sp,
                text = puppy.name,
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(name) {
                        start.linkTo(parent.start)
                        top.linkTo(image.bottom)
                    })

            Text(
                fontSize = 16.sp,
                text = "Age: ${puppy.age}",
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                    .constrainAs(age) {
                        start.linkTo(parent.start)
                        top.linkTo(name.bottom)
                    })

            Text(
                fontSize = 16.sp,
                text = "Breed: ${puppy.breed}",
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .constrainAs(breed) {
                        start.linkTo(parent.start)
                        top.linkTo(age.bottom)
                    })

            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.Favorite, "") },
                text = { Text(stringResource(id = R.string.adopt)) },
                onClick = { onAdoptClick() },
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(button) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }

    }

}
