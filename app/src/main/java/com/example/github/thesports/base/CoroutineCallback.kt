package com.example.github.thesports.base

import kotlinx.coroutines.*

/**
 *   Created by Lee Zhang on 10/20/20 14:24
 **/

data class CoroutineCallback(
    var block : suspend() -> Unit ={},
    var onError :(Throwable) ->Unit={}
)
fun CoroutineScope.safeLaunch(init: CoroutineCallback.() -> Unit): Job {
    val callback = CoroutineCallback().apply { this.init() }
    return launch(CoroutineExceptionHandler { _, throwable ->
        callback.onError(throwable)
    } + GlobalScope.coroutineContext) {
        callback.block()
    }
}


fun CoroutineScope.safeAsync(init: CoroutineCallback.() -> Unit): Job {
    val callback = CoroutineCallback().apply { this.init() }
    return async(CoroutineExceptionHandler { _, throwable ->
        callback.onError(throwable)
    } + GlobalScope.coroutineContext) {
        callback.block()
    }
}