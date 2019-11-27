package dev.ronnie.chama.admin

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.add_new_investment_layout.*
import kotlinx.android.synthetic.main.add_new_investment_layout.view.*


class AddNewInvestmentFragment : DialogFragment(), AddAcountListener {

    companion object {
        lateinit var viewModel: AddAccountViewModel
    }

    var views: View? = null
    lateinit var group: Groups
    var type: String? = null
    var day: String? = null
    var month: String? = null
    var year: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.add_new_investment_layout, container, false)
        viewModel = ViewModelProviders.of(this)[AddAccountViewModel::class.java]
        viewModel.listener = this


        val bundle = arguments
        if (bundle != null) {
            group = bundle.getParcelable("group") as Groups

            views!!.textViewAddInvestment.setOnClickListener {
                val date = "$day $month $year"

                Log.d("Date", "New new $date")
                viewModel.addInvestment(
                    group,
                    investment_name.text.toString(),
                    investment_amount.text.toString(),
                    date,
                    type
                )
            }

        }

        init()


        return views
    }


    private fun init() {
        FireBaseData().getType().observe(this, Observer {
            views!!.spinner_type.adapter =
                ArrayAdapter(
                    activity!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    it
                )
            views!!.spinner_type.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onNothingSelected(parent: AdapterView<*>?) {


                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        type = it[position]

                    }

                }

        })

        FireBaseData().getDay().observe(this, Observer {
            views!!.day.adapter =
                ArrayAdapter(
                    activity!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    it
                )
            views!!.day.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onNothingSelected(parent: AdapterView<*>?) {


                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        day = it[position]


                    }

                }

        })
        FireBaseData().getMonth().observe(this, Observer {
            views!!.month.adapter =
                ArrayAdapter(
                    activity!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    it
                )
            views!!.month.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onNothingSelected(parent: AdapterView<*>?) {


                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        month = it[position]


                    }

                }

        })
        FireBaseData().getYear().observe(this, Observer {
            views!!.year.adapter =
                ArrayAdapter(
                    activity!!,
                    R.layout.support_simple_spinner_dropdown_item,
                    it
                )
            views!!.year.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onNothingSelected(parent: AdapterView<*>?) {


                    }

                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {

                        year = it[position]


                    }

                }

        })


    }


    override fun setViewsAfter() {

        views!!.investmentProgress.visibility = View.VISIBLE
        Toast.makeText(activity, "Investment Added Successfully", Toast.LENGTH_LONG).show()
        this.dialog?.dismiss()


    }

    override fun setProgress() {

        views!!.investmentProgress.visibility = View.VISIBLE

    }


}