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
import dev.ronnie.chama.cash.MpesaRecyclerViewAdapter
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.bank_account_fragment.view.*

class MpesaAccountsFragment : DialogFragment(), AddAcountListener {

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
        viewModel.listener = this

        recyclerview = views!!.bankAccountRecycler
        textViewEnable = views!!.relativeEnable
        textViewAccountName = views!!.input_account_name
        textViewAdd = views!!.add_account

        views!!.addBankAccount.text = getString(R.string.add_mpesa)


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups
            viewModel.getMpesa(group).observe(this, Observer {
                val adapter = MpesaRecyclerViewAdapter(context!!, it)
                recyclerview.layoutManager = LinearLayoutManager(context)
                recyclerview.adapter = adapter
            })
        }

        textViewEnable.setOnClickListener {
            prepareViews()
            textViewAdd.setOnClickListener {
                viewModel.addMpesaAccount(group, textViewAccountName.text.toString())
            }
        }


        return views
    }

    private fun prepareViews() {
        textViewEnable.visibility = View.GONE
        recyclerview.visibility = View.GONE
        textViewAccountName.visibility = View.VISIBLE
        views!!.account_name.visibility = View.VISIBLE
        textViewAdd.visibility = View.VISIBLE
        textViewAccountName.isCursorVisible = true
        textViewAccountName.requestFocus()
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
}