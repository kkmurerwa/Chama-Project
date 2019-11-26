package dev.ronnie.chama.projects

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.R
import dev.ronnie.chama.admin.AdminActivity
import dev.ronnie.chama.admin.ProjectFragment
import dev.ronnie.chama.databinding.ProjectsListBinding
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Projects
import dev.ronnie.chama.models.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil


class ProjectsRecyclerViewAdapter(
    var context: Context,
    var list: List<Projects>,
    var groups: Groups
) :
    RecyclerView.Adapter<ProjectsRecyclerViewAdapter.MyViewHolder>() {
    private lateinit var listeners: OnItemClickListener

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

                if (AdminActivity.isAdminActivityRunning) {
                    listeners.onItemClick(groups, projectPosition!!)

                } else {
                    val intent = Intent(context, ProjectsExpanded::class.java)
                    intent.putExtra("project", projectPosition)
                    context.startActivity(intent)
                }
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
            getLeaderName(project)
            this.currentPosition = position
            this.projectPosition = project
        }

        private fun getLeaderName(project: Projects) {

            val reference = FirebaseDatabase.getInstance().reference
            val query =
                reference.child("Users")
                    .orderByKey()
                    .equalTo(project.leader)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {

                    for (snap in p0.children) {
                        val user = snap.getValue(User::class.java)
                        Log.d("User", "Project User $user")
                        val fname = user!!.fname
                        val sname = user.sname

                        val name: String

                        name = if (!sname.isNullOrEmpty()) {
                            "$fname $sname"
                        } else {
                            fname!!
                        }
                        project.leader = name

                        var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        val date: Date
                        val date2: Date

                        date = dateFormat.parse(project.date)
                        date2 = dateFormat.parse(timestamp)
                        dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                        val formatedDate: String = dateFormat.format(date)
                        val formatedDate2: String = dateFormat.format(date2)

                        val startDate = dateFormat.parse(formatedDate)
                        val stopDate = dateFormat.parse(formatedDate2)

                        Log.d("Dates", "Old Date $startDate NewDate $stopDate")

                        val diff = stopDate.time - startDate.time


                        val days = diff / (24 * 60 * 60 * 1000)

                        val day: Int = ceil(days.toDouble()).toInt()

                        val runtime = if (day <= 0) {
                            "Implemented Today"
                        } else {
                            "$days Days"
                        }
                        project.runTime = runtime

                        binding.projects = project

                        try {
                            ProjectsActivity.progressbar!!.visibility = View.GONE
                        } catch (e: NullPointerException) {

                        }

                        try {
                            ProjectFragment.progressBar!!.visibility = View.GONE
                        } catch (e: NullPointerException) {

                        }


                    }
                }

            })
        }

        private val timestamp: String
            get() {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                return sdf.format(Date())
            }
    }

    interface OnItemClickListener {
        fun onItemClick(groups: Groups, project: Projects)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        listeners = listener
    }

}