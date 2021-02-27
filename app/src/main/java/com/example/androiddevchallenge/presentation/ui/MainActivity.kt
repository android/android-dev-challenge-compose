package com.example.androiddevchallenge.presentation.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.presentation.PuppyViewModel
import com.example.androiddevchallenge.presentation.ui.components.Puppies
import com.example.androiddevchallenge.presentation.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    private val viewModel: PuppyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getPuppies()
        setContent {
            MyTheme {
                MyApp(viewModel)
            }
        }
    }

    @Composable
    fun MyApp(viewModel: PuppyViewModel) {
        val scrollState = rememberScrollState()
        val puppies = viewModel.puppiesLiveData.observeAsState(emptyList())
        Surface(color = MaterialTheme.colors.background) {
            Puppies(
                poppies = puppies.value,
                navigateToDetails = { },
                scrollState = scrollState
            )
        }
    }

    @Preview("Light Theme", widthDp = 360, heightDp = 640)
    @Composable
    fun LightPreview() {
        MyTheme {
            MyApp(viewModel)
        }
    }

    @Preview("Dark Theme", widthDp = 360, heightDp = 640)
    @Composable
    fun DarkPreview() {
        MyTheme(darkTheme = true) {
            MyApp(viewModel)
        }
    }
}
