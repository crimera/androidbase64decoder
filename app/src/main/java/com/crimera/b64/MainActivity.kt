package com.crimera.b64

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import com.crimera.b64.components.BottomSheet
import com.crimera.b64.ui.theme.B64Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
        setContent {
            B64Theme {
                Surface(
                    color = Color.Transparent
                ) {
                    BottomSheet(intent, applicationContext)
                }
            }
        }
    }
}