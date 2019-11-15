package  dev.ronnie.chama.groups

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityNewGroupBinding
import kotlinx.android.synthetic.main.activity_new_group.*

class CreateNewGroupFragment : DialogFragment(), CreateGroupListener {
    override fun showProgressBar() {
        progressBarCreate.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBarCreate.visibility = View.GONE
    }

    override fun toast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun notifyAdapter() {
        dialog?.dismiss()

    }

    lateinit var binding: ActivityNewGroupBinding
    lateinit var viewModel: CreateGroupViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_new_group, container, false)
        viewModel = ViewModelProviders.of(this).get(CreateGroupViewModel::class.java)
        binding.createModel = viewModel
        viewModel.listener = this

        return binding.root

    }
}