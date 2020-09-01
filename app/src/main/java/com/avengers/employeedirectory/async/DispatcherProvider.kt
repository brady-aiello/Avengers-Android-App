package com.avengers.employeedirectory.async

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


// "You should ALWAYS inject dispatchers"
// https://craigrussell.io/2019/11/unit-testing-coroutine-suspend-functions-using-testcoroutinedispatcher/
// This lets us swap dispatchers for testing
interface DispatcherProvider {
    fun main(): CoroutineDispatcher = Dispatchers.Main
    fun default(): CoroutineDispatcher = Dispatchers.Default
    fun io(): CoroutineDispatcher = Dispatchers.IO
    fun unconfined(): CoroutineDispatcher = Dispatchers.Unconfined
}

class DefaultDispatcherProvider : DispatcherProvider