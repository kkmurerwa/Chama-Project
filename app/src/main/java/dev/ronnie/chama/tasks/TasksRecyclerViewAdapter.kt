package dev.ronnie.chama.tasks

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.TasksListBinding
import dev.ronnie.chama.models.Tasks

class TasksRecyclerViewAdapter(var context: Context, var list: List<Tasks>) :
    RecyclerView.Adapter<TasksRecyclerViewAdapter.MyViewModel>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {

        val activityAdapterBinding = DataBindingUtil.inflate<TasksListBinding>(
            LayoutInflater
                .from(parent.context), R.layout.tasks_list, parent, false
        )
        return MyViewModel(activityAdapterBinding)

    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: MyViewModel, position: Int) {

        val tasks = list[position]
        holder.binding.tasks = tasks


    }

    inner class MyViewModel(var binding: TasksListBinding) :
        RecyclerView.ViewHolder(binding.root)

}