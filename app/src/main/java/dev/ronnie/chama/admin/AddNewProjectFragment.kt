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
import kotlinx.android.synthetic.main.add_project_layout.*
import kotlinx.android.synthetic.main.add_project_layout.view.*


class AddNewProjectFragment : DialogFragment(), AddAcountListener {

    lateinit var group: Groups
    lateinit var viewModel: AddAccountViewModel
    private var views: View? = null
    lateinit var statues: String
    lateinit var leaderId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.add_project_layout, container, false)
        viewModel = ViewModelProviders.of(this)[AddAccountViewModel::class.java]
        viewModel.listener = this


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups

            init()

            views!!.textViewAddProject.setOnClickListener {
                viewModel.addProject(
                    group,
                    project_name.text.toString(),
                    project_cost.text.toString(),
                    leaderId,
                    statues

                )
            }

        }

        return views
    }

    private fun init() {
        FireBaseData().getUsers(group).observe(this, Observer {
            val arrayListNames = ArrayList<String>()

            for (i in it) {
                if (!i.sname.isNullOrEmpty()) {
                    arrayListNames.add("${i.fname!!} ${i.sname}")
                } else {
                    arrayListNames.add(i.fname!!)
                }
            }

            spinner_leader!!.adapter =
                ArrayAdapter(
                    activity!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    arrayListNames
                )
            spinner_leader!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(parent: AdapterView<*>?) {


                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    leaderId = it[position].user_id!!
                }

            }


        })

        FireBaseData().getStatus().observe(this, Observer {
            spinner_statues!!.adapter =
                ArrayAdapter(
                    activity!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    it
                )
            spinner_statues!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

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
        projectProgress.visibility = View.GONE
        dialog?.dismiss()

        Toast.makeText(activity, "Project Added Successfully", Toast.LENGTH_LONG).show()

    }

    override fun setProgress() {

        projectProgress.visibility = View.VISIBLE

    }

}