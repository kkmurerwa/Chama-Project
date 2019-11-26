package dev.ronnie.chama.admin

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.confirm_request_layout.view.*


class ConfirmRequestDialog : DialogFragment(), RequestListener {
    lateinit var viewModel: ConfirmRequestViewModel
    lateinit var group: Groups
    var views: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.confirm_request_layout, container, false)
        viewModel = ViewModelProviders.of(this)[ConfirmRequestViewModel::class.java]
        viewModel.listener = this


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups
            viewModel.getRequestList(group).observe(this, Observer {
                val adapter = RequestJoinRecyclerView(context!!, it, group)
                views!!.requestRecyclerView.layoutManager = LinearLayoutManager(context)
                views!!.requestRecyclerView.adapter = adapter
                views!!.requestProgressbar.visibility = View.GONE
            })
        }


        return views
    }


    override fun noRequests() {
        views!!.textViewNoRequest.visibility = View.VISIBLE
        views!!.requestProgressbar.visibility = View.GONE

    }

}