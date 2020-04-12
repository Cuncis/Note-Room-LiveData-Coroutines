package com.cuncis.noteapp.util

import java.text.SimpleDateFormat
import java.util.*

class Utility {
    companion object {
        fun getCurrentTime(): String {
            return SimpleDateFormat("dd MMM yyyy", Locale.US)
                .format(Calendar.getInstance().time)
        }
    }
}