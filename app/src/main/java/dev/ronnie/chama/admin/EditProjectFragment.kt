package dev.ronnie.chama.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Projects
import kotlinx.android.synthetic.main.edit_project_layout.view.*

class EditProjectFragment : DialogFragment(), AddAcountListener {
    lateinit var statues: String
    var views: View? = null
    lateinit var viewModel: AddAccountViewModel
    lateinit var group: Groups
    lateinit var project: Projects


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.edit_project_layout, container, false)
        viewModel = ViewModelProviders.of(this)[AddAccountViewModel::class.java]
        viewModel.listener = this


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups
            project = bundle.getSerializable("project") as Projects


        }

        init()

        views!!.textViewEditProject.setOnClickListener {
            viewModel.editProject(group, project, statues)
        }

        return views
    }

    private fun init() {

        FireBaseData().getStatus().observe(this, Observer {
            views!!.spinner_edit!!.adapter =
                ArrayAdapter(
                    activity!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    it
                )
            views!!.spinner_edit!!.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onNothingSelected(parent: AdapterView<*>?) {


                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        statues = it[position]

                    }

                }

        })
    }

    override fun setViewsAfter() {
        views!!.editProjectProgress.visibility = View.GONE
        dialog?.dismiss()

        Toast.makeText(activity, "Project Updated", Toast.LENGTH_LONG).show()

    }

    override fun setProgress() {
        views!!.editProjectProgress.visibility = View.VISIBLE

    }
}