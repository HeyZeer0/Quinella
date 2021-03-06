package net.heyzeer0.quinella.core.managers

import net.heyzeer0.quinella.core.managers.containers.TaskContainer
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

object SecondExecutor {

    private val thread = Executors.newSingleThreadScheduledExecutor()
    private val executors = mutableListOf<TaskContainer>()

    private var future : ScheduledFuture<*>? = null

    fun registerTask(runnable: () -> Unit, name: String = "Not Assgined"): TaskContainer {
        val container = TaskContainer(false, runnable, name)
        executors.add(container)

        verifyTask()
        return container
    }

    fun verifyTask() {
        if(future != null && !future!!.isCancelled && !future!!.isDone) return

        println("- Started Runnable Thread")
        future = thread.scheduleAtFixedRate({
            val it = executors.iterator()
            while (it.hasNext()) {
                val next = it.next()
                if (next.canceled) {
                    it.remove()
                    continue
                }

                try {
                    next.runnable.invoke()
                }catch (ex: Exception) {
                    it.remove()
                    println("[!!!] Task " + next.name + " crashed and was removed from the task list.")
                    // TODO alert bot owner
                }
            }
        }, 0, 1, TimeUnit.SECONDS)
    }

}