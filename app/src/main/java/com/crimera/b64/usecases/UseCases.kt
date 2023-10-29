package com.crimera.b64.usecases

import android.util.Base64

object UseCases {

    fun isHttp(text: String): Boolean {
        val http = """^(http|https)://.*""".toRegex()
        return http.matches(text)
    }

    private fun encodeNoPad(text: String): String {
        return Base64.encode(text.encodeToByteArray(), Base64.NO_PADDING).decodeToString()
    }

    fun isBase64(text: String): Boolean {
        return try {
            val value = decode(text)
            (encode(value)==text || encodeNoPad(value).contains(text.toRegex())) && text!=""
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun decode(text: String) = Base64.decode(text, Base64.DEFAULT).decodeToString()

    fun encode(text: String) = Base64.encode(text.encodeToByteArray(), Base64.NO_WRAP).decodeToString()

}