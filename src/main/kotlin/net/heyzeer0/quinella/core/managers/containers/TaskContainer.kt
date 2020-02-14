package net.heyzeer0.quinella.core.managers.containers

class TaskContainer(

    var canceled: Boolean = false,
    val runnable: () -> Unit,
    var name: String = "Not Assigned"

)