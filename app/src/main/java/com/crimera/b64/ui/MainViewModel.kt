package com.crimera.b64.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.crimera.b64.usecases.UseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _data = MutableStateFlow("")
    val data = _data.asStateFlow()

    fun init(intent: Intent) {
        _data.value = when (intent.action) {
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
    }

    fun setData(text: String) {
        _data.value = text
    }

    fun decode() {
        val dataArray = _data.value.split("\n")
        val decodedArray = dataArray.map { if (UseCases.isBase64(it)) UseCases.decode(it) else it }
        _data.value = decodedArray.joinToString("\n")
    }

    fun encode() {
        _data.update { UseCases.encode(_data.value) }
    }

    fun openLink(url: String, context: Context) {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url)
        )
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(browserIntent)
    }

    fun setClip(text: String, clipboardManager: ClipboardManager) {
        val clip = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clip)
    }
}