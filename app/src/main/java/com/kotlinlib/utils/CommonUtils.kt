package com.kotlinlib.utils

import android.content.Context
import android.os.Environment
import java.io.File

interface CommonUtils {
    val Context.SDPATH: String
        get() = Environment.getExternalStorageDirectory().toString()

}