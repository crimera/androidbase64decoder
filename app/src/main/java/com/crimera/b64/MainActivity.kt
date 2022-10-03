package com.crimera.b64

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Process
import android.util.Base64
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.crimera.b64.ui.theme.B64Theme
import kotlin.system.exitProcess

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
                    BottomSheet(intent = intent, applicationContext)
                }
            }
        }
    }
}

fun isBase64(text: String): Boolean {
//    Log.d("hello", text)
//    Log.d("hello", encode(decode(text)))
    val canDecode = try {
        decode(text)
        true
    } catch (e: IllegalArgumentException) {
        false
    }
    return canDecode && encode(decode(text))==text && text!=""
}

fun isHttp(text: String): Boolean {
    val http = """^(http|https)://.*""".toRegex()
    return http.matches(text)
}

fun decode(text: String): String {
    return Base64.decode(text, Base64.DEFAULT).decodeToString()
}

fun encode(text: String): String {
    return Base64.encode(text.encodeToByteArray(), Base64.NO_WRAP).decodeToString()
}

fun toast(message: String, context: Context) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun setClip(text: String, clipboardManager: ClipboardManager) {
    val clip = ClipData.newPlainText("text", text)
    clipboardManager.setPrimaryClip(clip)
}

fun openLink(url: String, context: Context) {
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    )
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(browserIntent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(intent: Intent, context: Context) {

    var data: String = when (intent.action) {
        Intent.ACTION_PROCESS_TEXT -> {
            intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
        }
        Intent.ACTION_SEND -> {
            intent.getStringExtra(Intent.EXTRA_TEXT).toString().replace("\"", "")
        }
        else -> {
            ""
        }
    }

    while (isBase64(data)) {
        data = decode(data)
    }

    if (isHttp(data)) {
        openLink(data, context)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    var text by remember { mutableStateOf(data) }

    Column(
        Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(25.dp, 15.dp)
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
                .clip(CircleShape)
                .background(colorScheme.outline))
        
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = colorScheme.surface,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(15.dp, 10.dp)
        )

        Row(
            Modifier.padding(0.dp, 0.dp, 0.dp, 40.dp)
        ) {
            MyButton("Decode") {
                if (isBase64(text)) {
                    text = decode(text)
                } else {
                    toast("Not a valid Base64", context)
                }
            }

            Spacer(modifier = Modifier.padding(10.dp))

            MyButton("Encode") {
                text = encode(text)
            }

            Spacer(modifier = Modifier.padding(10.dp))

            MyButton(text = "Copy") {
                setClip(
                    text,
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheet(intent: Intent, context: Context) {
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)

    ModalBottomSheetLayout(
        sheetState = sheetState,
        scrimColor = Color.Transparent,
        sheetShape = ShapeDefaults.Large,
        sheetContent = {
            if (!sheetState.isVisible) {
                Process.killProcess(Process.myPid())
                exitProcess(0)
            }

            Home(intent = intent, context)
        },
        sheetBackgroundColor = colorScheme.surface,
        content = {},
    )
}

@Composable
fun MyButton(text: String, onclick: () -> Unit) {
    FilledTonalButton(
        onClick = onclick,
    ) {
        Text(
            text = text
        )
    }
}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DefaultPreview() {
//    B64Theme {
//        MyButton(text = "Hello", icon = R.drawable.paste) {
//
//        }
//    }
//}