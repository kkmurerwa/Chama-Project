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
import dev.ronnie.chama.investment.InvestmentRecyclerViewAdapter
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.bank_account_fragment.view.*

class InvestmentFragment : DialogFragment(), AddAcountListener {

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

        views!!.addBankAccount.text = getString(R.string.Add_investment)


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups
            viewModel.getInvestments(group).observe(this, Observer {
                val adapter = InvestmentRecyclerViewAdapter(context!!, it)
                recyclerview.layoutManager = LinearLayoutManager(context)
                recyclerview.adapter = adapter
            })
        }

        textViewEnable.setOnClickListener {

            showNewInvestment()

        }


        return views
    }

    private fun showNewInvestment() {
        val ft = activity!!.supportFragmentManager.beginTransaction()
        val prev = activity!!.supportFragmentManager.findFragmentByTag("newInvestmentDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val dialog = AddNewInvestmentFragment()
        val bundle = Bundle()
        bundle.putParcelable("group", group)
        dialog.arguments = bundle
        dialog.show(ft, "newInvestmentDialog")
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