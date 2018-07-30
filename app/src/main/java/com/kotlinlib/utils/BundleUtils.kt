package com.kotlinlib.utils

import android.os.Bundle
import java.io.Serializable

interface BundleUtils {

    /**
     * Bundle类扩展
     */
    fun Bundle.str(key:String, value:String): Bundle {
        putString(key,value)
        return this
    }

    fun Bundle.int(key:String, value:Int): Bundle {
        putInt(key,value)
        return this
    }

    fun Bundle.bool(key:String, value:Boolean): Bundle {
        putBoolean(key,value)
        return this
    }

    fun Bundle.double(key:String, value:Double): Bundle {
        putDouble(key,value)
        return this
    }

    fun Bundle.serial(key:String, value: Serializable): Bundle {
        putSerializable(key,value)
        return this
    }

}