package dev.ronnie.chama.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Tasks

class TaskViewModel : ViewModel() {

    var listener: TaskListener? = null

    fun getTasks(group: Groups): LiveData<MutableList<Tasks>> {

        return FireBaseData().getTasks(group)
    }
}