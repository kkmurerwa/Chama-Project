package dev.ronnie.chama.projects

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ProjectsListBinding
import dev.ronnie.chama.models.Projects

class ProjectsRecyclerViewAdapter(var context: Context, var list: List<Projects>) :
    RecyclerView.Adapter<ProjectsRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ProjectsListBinding = DataBindingUtil.inflate(
            LayoutInflater
                .from(parent.context), R.layout.projects_list, parent, false
        )

        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val project = list[position]
        holder.setData(project, position)

    }

    inner class MyViewHolder(var binding: ProjectsListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var projectPosition: Projects? = null
        var currentPosition: Int = 0

        init {
            binding.root.setOnClickListener {

                val intent = Intent(context, ProjectsExpanded::class.java)
                intent.putExtra("project", projectPosition)
                context.startActivity(intent)
            }
        }

        fun setData(project: Projects, position: Int) {

            project.let {
                when (it.statues) {
                    "Completed" -> {
                        binding.status.setTextColor(Color.parseColor("#32CD32"))
                    }
                    "Not Started" -> {
                        binding.status.setTextColor(Color.parseColor("#ff0800"))
                    }
                    "Work In Progress" -> {
                        binding.status.setTextColor(
                            Color.parseColor("#9B870C")

                        )
                    }

                }
            }

            binding.projects = project
            this.currentPosition = position
            this.projectPosition = project
        }
    }
}