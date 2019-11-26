package  dev.ronnie.chama.groups

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.FragmentMyGroupsBinding
import dev.ronnie.chama.models.Groups
import java.lang.ref.WeakReference

class MyGroupsFragment : Fragment(), GroupListener {
    override fun toast(message: String) {

    }


    lateinit var binding: FragmentMyGroupsBinding
    lateinit var mAdapter: MyGroupRecyclerViewAdapter

    companion object {
        lateinit var viewModel: MyGroupsViewModel
        lateinit var adapter: WeakReference<MyGroupRecyclerViewAdapter>
        lateinit var textViewEmpty: WeakReference<View>
        lateinit var progressbar: WeakReference<View>
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_groups, container, false)
        viewModel = ViewModelProviders.of(this)[MyGroupsViewModel::class.java]
        binding.myGroupModel = viewModel
        viewModel.listener = this

        textViewEmpty = WeakReference(binding.textNotJoined)
        progressbar = WeakReference(binding.myProgress)
        binding.myProgress.visibility = View.VISIBLE

        adapter =
            WeakReference(MyGroupRecyclerViewAdapter(context!!, null))

        init()

        return binding.root
    }

    private fun init() {

        viewModel.init().observe(this, Observer {
            mAdapter = MyGroupRecyclerViewAdapter(context!!, it)
            binding.myGroupRecyclerview.layoutManager = LinearLayoutManager(context)
            binding.myGroupRecyclerview.adapter = mAdapter

            binding.myProgress.visibility = View.GONE
            binding.textNotJoined.visibility = View.GONE
        })

    }
    override fun navigate(groups: Groups) {
        val intent = Intent(context, MainGroupActivity::class.java)
        intent.putExtra("group", groups)
        startActivity(intent)
    }

    override fun notifyDatasetChanged() {

    }


}
