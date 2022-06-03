package com.example.compose_remember.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.compose_remember.ui.theme.ComposerememberTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposerememberTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val localCount = compositionLocalOf<Int> { error("") }
                    CompositionLocalProvider(localCount provides 0) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val (count, setCount) = remember { mutableStateOf(0) }

                            CounterButton(count = count) {
                                setCount(count.inc())
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CounterButton(count: Int, onClick: () -> Unit) {
    Column {
        Button(
            onClick = onClick
        ) {
            Text(
                text = "count: $count"
            )
        }

        Button(
            onClick = onClick
        ) {
            Text(
                text = "count: 0"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposerememberTheme {
        Text(text = "Preview")
    }
}