package com.crimera.b64.ui

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Process
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.crimera.b64.components.MyButton
import com.crimera.b64.usecases.UseCases
import kotlin.system.exitProcess

@Composable
fun Home(intent: Intent, context: Context, viewModel: MainViewModel = viewModel()) {
    val data by viewModel.data.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.init(intent)
        viewModel.decode()
    }

    if (UseCases.isHttp(data)) {
        viewModel.openLink(data, context)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        OutlinedTextField(
            value = data,
            onValueChange = {
                viewModel.setData(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(150.dp, 300.dp)
        )

        Row(
            Modifier.padding(0.dp, 0.dp, 0.dp, 40.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MyButton("Decode") {
                viewModel.decode()
            }

            MyButton("Encode") {
                viewModel.encode()
            }

            MyButton(text = "Copy") {
                viewModel.setClip(
                    data,
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                )
            }
        }
    }
}