package com.qifan.emojibattle.internal

internal class Queue<T> {
    private val data: MutableList<T> = mutableListOf()
    private val lock = Any()

    fun push(item: T) {
        synchronized(lock) {
            data.add(item)
        }
    }

    fun pop(): T {
        synchronized(lock) {
            val result = data.firstOrNull() ?: error("Queue is empty")
            data.removeAt(0)
            return result
        }
    }


    fun isNotEmpty(): Boolean = data.isNotEmpty()

    operator fun plusAssign(item: T) {
        push(item)
    }
}