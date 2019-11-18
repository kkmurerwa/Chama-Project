package dev.ronnie.chama.projects

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Projects
import kotlinx.android.synthetic.main.projectsexpanded.*

class ProjectsExpanded : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.projectsexpanded)


        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Details"
            (toolbar as Toolbar).setNavigationOnClickListener {
              onBackPressed()
            }

        }

        val intent = intent
        val project = intent.getSerializableExtra("project") as Projects

        project.let {
            when (it.statues) {
                "Completed" -> {
                    textStatues.setTextColor(Color.parseColor("#32CD32"))
                }
                "Not Started" -> {
                    textStatues.setTextColor(Color.parseColor("#ff0800"))
                }
                "Work In Progress" -> {
                    textStatues.setTextColor(
                        Color.parseColor("#9B870C")
                    )
                }

            }
            textCost.text = "Shs ${project.cost}"
            textLeader.text = project.leader
            textRunTime.text = project.runTime
            textStatues.text = project.statues
            textName.text = project.name
        }
    }
}