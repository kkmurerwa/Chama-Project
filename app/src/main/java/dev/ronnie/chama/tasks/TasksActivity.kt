package dev.ronnie.chama.tasks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityTasksBinding
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_tasks.*

class TasksActivity : AppCompatActivity(), TaskListener {

    lateinit var group: Groups
    lateinit var binding: ActivityTasksBinding
    lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tasks)
        viewModel = ViewModelProviders.of(this)[TaskViewModel::class.java]
        binding.tasksModel = viewModel
        viewModel.listener = this

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Tasks"
            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }

        }
        init()
    }

    private fun init() {
        val intent = intent
        group = intent.getParcelableExtra("group")
        viewModel.getTasks(group).observe(this, Observer {

            val adapter = TasksRecyclerViewAdapter(this, it)
            binding.tasksRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.tasksRecyclerView.adapter = adapter

        })

    }
}