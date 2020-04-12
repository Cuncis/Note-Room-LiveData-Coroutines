package com.cuncis.noteapp.util

import android.content.Context
import android.util.Log
import android.widget.Toast

class Helper {
    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun showLog(message: String) {
            Log.d("_logNotes", message)
        }
    }
}