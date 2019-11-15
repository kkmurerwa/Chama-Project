package dev.ronnie.chama.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.FragmentAllGroupsBinding
import dev.ronnie.chama.models.Groups
import java.lang.ref.WeakReference


class AllGroupsFragment : Fragment(), GroupListener {
    override fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    lateinit var binding: FragmentAllGroupsBinding
    lateinit var mAdapterAll: AllGroupRecyclerViewAdapter
    var groups: ArrayList<Groups> = ArrayList()

    companion object {
        lateinit var viewModel: AllGroupsViewModel
        lateinit var textViewEmpty: WeakReference<View>
        lateinit var progressbar: WeakReference<View>

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_groups, container, false)
        viewModel = ViewModelProviders.of(this)[AllGroupsViewModel::class.java]
        binding.groupModel = viewModel
        viewModel.listener = this

        textViewEmpty = WeakReference(binding.textError)
        progressbar = WeakReference(binding.pro)

        binding.pro.visibility = View.VISIBLE
        GetGroups(null).getGroups()

        init()

        return binding.root
    }

    private fun init() {
        viewModel.init().observe(this, Observer {
            mAdapterAll =
                (AllGroupRecyclerViewAdapter(context!!, it))

            binding.groupRecyclerview.layoutManager = LinearLayoutManager(context)
            binding.groupRecyclerview.adapter = mAdapterAll
            mAdapterAll.notifyDataSetChanged()

            binding.pro.visibility = View.GONE
            binding.textError.visibility = View.GONE
        })


    }


    override fun navigate(groups: Groups) {
        val intent = Intent(context, MainGroupActivity::class.java)
        intent.putExtra("group", groups)
        startActivity(intent)
    }


}