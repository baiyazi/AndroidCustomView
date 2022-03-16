package com.weizu.mykotlindemospace
import org.junit.Test
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine


class MyClassDemo {

    @Test
    fun test(){
        suspend {
            println("123")
            "Return Result!"
        }.createCoroutine(object : Continuation<String>{
            override fun resumeWith(result: Result<String>) {

            }

            override val context: CoroutineContext
                get() = EmptyCoroutineContext

        })
    }
}