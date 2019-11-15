package dev.ronnie.chama.projects

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityProjectBinding
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_project.*

class ProjectsActivity : AppCompatActivity(), ProjecsListener {

    lateinit var binding: ActivityProjectBinding
    lateinit var viewModel: ProjectsViewModel
    lateinit var group: Groups

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_project)
        viewModel = ViewModelProviders.of(this)[ProjectsViewModel::class.java]
        binding.projectsModel = viewModel
        viewModel.listener = this

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Projects"
            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }

        }

        init()
    }

    private fun init() {
        val intent = intent
        group = intent.getParcelableExtra("group")

        viewModel.getProjects(group).observe(this, Observer {
            val adapter = ProjectsRecyclerViewAdapter(this, it)
            binding.projectRecyclerview.layoutManager = LinearLayoutManager(this)
            binding.projectRecyclerview.adapter = adapter
        })

    }
}