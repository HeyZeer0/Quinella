package net.heyzeer0.quinella.core.utilities

import java.lang.ref.WeakReference

class WeakList<T> {

    private val list = ArrayList<WeakReference<T>>()

    fun get(index: Int): T? {
        removeReleased()
        return list[index].get()
    }

    fun size(): Int {
        removeReleased()
        return list.size
    }

    private fun removeReleased() {
        val iterator = list.iterator();
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (next.get() != null) continue

            iterator.remove()
        }
    }

}