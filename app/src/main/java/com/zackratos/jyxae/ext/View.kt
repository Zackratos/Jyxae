package com.zackratos.jyxae.ext

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * @Author   : Zackratos
 * @Date     : 2021/7/14 2:52
 * @email    : 869649338@qq.com
 * @Describe :
 */
@SuppressLint("ClickableViewAccessibility")
fun View.onPressHolder(scope: CoroutineScope, block: () -> Unit) {
    var job: Job? = null
    setOnTouchListener { _, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                job?.cancel()
                job = pressHolderJob(scope, block)
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.x < 0 || event.x > width || event.y < 0 || event.y > height) {
                    job?.cancel()
                }
            }
            MotionEvent.ACTION_UP -> {
                job?.cancel()
                job = null
            }
            else -> {
                job?.cancel()
                job = null
            }
        }
        return@setOnTouchListener false
    }
}

private fun pressHolderJob(scope: CoroutineScope, block: () -> Unit): Job {
    return scope.launch {
        flow {
            delay(1000)
            for (i in 1..9999) {
                emit(i)
                delay(50)
            }
        }.flowOn(Dispatchers.Default)
            .collect { block() }
    }
}