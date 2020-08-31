package com.avengers.employeedirectory.util

data class Event<T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content

    companion object{

        // we don't want an event if there's no data
        fun <T> dataEvent(data: T): Event<T>{
            return Event(data)
        }

        // we don't want an event if there is no message
        fun messageEvent(message: String?): Event<String>?{
            message?.let{
                return@messageEvent Event(message)
            }
            return null
        }
    }
}