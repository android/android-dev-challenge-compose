package com.example.androiddevchallenge.presentation.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.androiddevchallenge.R

@Composable
fun PlaceHolder(){
    Box(Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(1F),
            painter = painterResource(id = R.drawable.placeholder),
            contentDescription = null
        )
    }
}