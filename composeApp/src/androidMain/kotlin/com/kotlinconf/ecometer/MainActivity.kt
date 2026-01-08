package com.kotlinconf.ecometer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import com.kotlinconf.ecometer.data.AndroidLocalStorageProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val storageProvider = remember { AndroidLocalStorageProvider(applicationContext) }
            App(storageProvider)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    // Preview without actual persistence
}