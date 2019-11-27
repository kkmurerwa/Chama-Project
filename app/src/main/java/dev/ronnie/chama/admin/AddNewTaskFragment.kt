package dev.ronnie.chama.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.add_new_task_layout.*
import kotlinx.android.synthetic.main.add_new_task_layout.view.*

class AddNewTaskFragment : DialogFragment(), AddAcountListener {

    lateinit var viewModel: AddAccountViewModel
    var views: View? = null
    lateinit var group: Groups

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.add_new_task_layout, container, false)
        viewModel = ViewModelProviders.of(this)[AddAccountViewModel::class.java]
        viewModel.listener = this

        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups

            views!!.textViewAddTask.setOnClickListener {
                viewModel.addTasks(group, task_action.text.toString(), task_name.text.toString())

            }
        }


        return views
    }

    override fun setViewsAfter() {

        views!!.taskProgress.visibility = View.VISIBLE
        Toast.makeText(activity, "Task Added Successfully", Toast.LENGTH_LONG).show()
        this.dialog?.dismiss()


    }

    override fun setProgress() {

        views!!.taskProgress.visibility = View.VISIBLE
    }

}