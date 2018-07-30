package com.kotlinlib.utils

interface CollectUtils {


    fun <T> HashMap<String, T>.p(key:String,value:T): HashMap<String, T> {
        put(key,value)
        return this
    }

    fun <T> ArrayList<T>.a(value:T):ArrayList<T>{
        add(value)
        return this
    }
}