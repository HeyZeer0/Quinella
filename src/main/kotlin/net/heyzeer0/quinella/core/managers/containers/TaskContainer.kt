package net.heyzeer0.quinella.core.managers.containers

class TaskContainer(

    val canceled: Boolean = false,
    val runnable: () -> Unit

)