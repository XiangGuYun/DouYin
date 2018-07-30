package com.kotlinlib.utils

import android.os.Message

interface MessageUtils {
    fun Message.setWhat(what:Int): Message {
        this.what=what
        return this
    }
}