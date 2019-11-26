package dev.ronnie.chama.admin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Projects
import dev.ronnie.chama.projects.ProjectsRecyclerViewAdapter
import kotlinx.android.synthetic.main.bank_account_fragment.view.*


class ProjectFragment : DialogFragment(), AddAcountListener,
    ProjectsRecyclerViewAdapter.OnItemClickListener {

    lateinit var group: Groups
    lateinit var recyclerview: RecyclerView
    lateinit var textViewEnable: RelativeLayout
    lateinit var textViewAccountName: EditText
    lateinit var textViewAdd: TextView
    lateinit var viewModel: AddAccountViewModel
    private var mListener: ProjectListener? = null
    var views: View? = null

    companion object {
        var progressBar: ProgressBar? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.bank_account_fragment, container, false)
        viewModel = ViewModelProviders.of(this)[AddAccountViewModel::class.java]
        viewModel.listener = this

        progressBar = views!!.project_fragment_progress

        progressBar!!.visibility = View.VISIBLE

        recyclerview = views!!.bankAccountRecycler
        textViewEnable = views!!.relativeEnable
        textViewAccountName = views!!.input_account_name
        textViewAdd = views!!.add_account

        views!!.addBankAccount.text = "Add A New Project"


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups
            viewModel.getProject(group).observe(this, Observer {
                val adapter = ProjectsRecyclerViewAdapter(context!!, it, group)
                recyclerview.layoutManager = LinearLayoutManager(context)
                recyclerview.adapter = adapter
                adapter.setOnItemClickListener(this)
            })
        }

        textViewEnable.setOnClickListener {
            this.dismiss()
            mListener!!.startNewGroup(group)


        }


        return views
    }

    override fun setViewsAfter() {
        views!!.AddingProgress.visibility = View.GONE
        textViewAccountName.visibility = View.GONE
        views!!.account_name.visibility = View.GONE
        textViewAdd.visibility = View.GONE
        textViewEnable.visibility = View.VISIBLE
        recyclerview.visibility = View.VISIBLE

    }

    override fun setProgress() {
        views!!.AddingProgress.visibility = View.VISIBLE
    }

    override fun onItemClick(groups: Groups, project: Projects) {

        mListener!!.parseData(groups, project)


    }

    interface ProjectListener {
        fun parseData(groups: Groups, project: Projects)
        fun startNewGroup(groups: Groups)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as ProjectListener
        } catch (e: ClassCastException) {
            throw ClassCastException(

            )
        }
    }
}