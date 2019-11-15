package dev.ronnie.chama.projects

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Projects

class ProjectsViewModel : ViewModel() {

    var listener: ProjecsListener? = null

    fun getProjects(group: Groups): LiveData<MutableList<Projects>> {
        return FireBaseData().getProjects(group)
    }
}