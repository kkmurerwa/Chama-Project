package dev.ronnie.chama.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.tasks.TasksRecyclerViewAdapter
import kotlinx.android.synthetic.main.bank_account_fragment.view.*

class TasksFragment : DialogFragment() {

    lateinit var group: Groups
    lateinit var recyclerview: RecyclerView
    lateinit var textViewEnable: RelativeLayout
    lateinit var textViewAccountName: EditText
    lateinit var textViewAdd: TextView
    lateinit var viewModel: AddAccountViewModel
    var views: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.bank_account_fragment, container, false)
        viewModel = ViewModelProviders.of(this)[AddAccountViewModel::class.java]


        recyclerview = views!!.bankAccountRecycler
        textViewEnable = views!!.relativeEnable
        textViewAccountName = views!!.input_account_name
        textViewAdd = views!!.add_account

        views!!.addBankAccount.text = "Add A New Task"


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups
            viewModel.getTasks(group).observe(this, Observer {
                val adapter = TasksRecyclerViewAdapter(context!!, it)
                recyclerview.layoutManager = LinearLayoutManager(context)
                recyclerview.adapter = adapter
            })
        }

        textViewEnable.setOnClickListener {

            openNewTaskFragment()

        }


        return views
    }

    private fun openNewTaskFragment() {

        val ft = activity!!.supportFragmentManager.beginTransaction()
        val prev = activity!!.supportFragmentManager.findFragmentByTag("newTaskDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val dialog = AddNewTaskFragment()
        val bundle = Bundle()
        bundle.putParcelable("group", group)
        dialog.arguments = bundle
        dialog.show(ft, "newTaskDialog")

    }

}