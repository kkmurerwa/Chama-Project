package dev.ronnie.chama.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.FirebaseDatabase
import dev.ronnie.chama.models.*
import java.text.SimpleDateFormat
import java.util.*

class AddAccounts {

    var dateLivedata = MutableLiveData<String>()

    companion object {
        var date: String? = null
    }


    fun getDate(): LiveData<String> {

        dateLivedata.value = date

        return dateLivedata

    }

    fun addBankAccount(model: AddAccountViewModel, group: Groups, bankAccountName: String) {

        if (bankAccountName.isEmpty()) {
            return
        }
        if (bankAccountName[0].isWhitespace()) {
            return
        }

        model.listener!!.setProgress()

        val reference = FirebaseDatabase.getInstance().reference
        val bankId = reference
            .child("groups")
            .child("financials")
            .child("cash")
            .child("bank")
            .push().key

        val bank = Bank(bankAccountName, "0", bankId)

        reference.child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("bank")
            .child(bankId!!)
            .setValue(bank)
            .addOnCompleteListener {
                model.listener!!.setViewsAfter()

            }

    }

    fun addMpesaAccount(model: AddAccountViewModel, group: Groups, mpesaAccountName: String) {

        if (mpesaAccountName.isEmpty()) {
            return
        }
        if (mpesaAccountName[0].isWhitespace()) {
            return
        }
        model.listener!!.setProgress()
        val reference = FirebaseDatabase.getInstance().reference
        val mpesaId = reference
            .child("groups")
            .child("financials")
            .child("cash")
            .child("mpesa")
            .push().key

        val mpesa = Mpesa(mpesaAccountName, "0", mpesaId)

        reference.child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("mpesa")
            .child(mpesaId!!)
            .setValue(mpesa)
            .addOnCompleteListener {
                model.listener!!.setViewsAfter()
            }

    }

    private val timestamp: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return sdf.format(Date())
        }

    fun addProjects(
        model: AddAccountViewModel,
        group: Groups,
        projectName: String,
        projectCost: String,
        leader: String,
        projectStatues: String
    ) {

        if (projectName.isEmpty() || projectCost.isEmpty()) {
            return
        }
        if (projectName[0].isWhitespace()) {
            return
        }

        model.listener!!.setProgress()

        val reference = FirebaseDatabase.getInstance().reference

        val projectId = reference
            .child("groups")
            .child("projects")
            .push().key

        val project =
            Projects(
                projectName,
                projectStatues,
                leader,
                " ",
                projectCost,
                timestamp,
                projectId
            )

        reference.child("groups")
            .child(group.group_id)
            .child("projects")
            .child(projectId!!)
            .setValue(project)
            .addOnSuccessListener {
                model.listener!!.setViewsAfter()
            }
    }

    fun editProject(
        addAccountViewModel: AddAccountViewModel,
        group: Groups,
        project: Projects,
        statues: String
    ) {
        addAccountViewModel.listener!!.setProgress()
        val reference = FirebaseDatabase.getInstance().reference
        reference
            .child("groups")
            .child(group.group_id)
            .child("projects")
            .child(project.projectId!!)
            .child("statues")
            .setValue(statues).addOnSuccessListener {
                addAccountViewModel.listener!!.setViewsAfter()
            }
    }

    fun addTask(
        addAccountViewModel: AddAccountViewModel,
        group: Groups,
        action: String,
        taskName: String
    ) {

        if (taskName.isEmpty() || action.isEmpty()) {
            return
        }
        if (taskName[0].isWhitespace() || action[0].isWhitespace()) {
            return
        }
        addAccountViewModel.listener!!.setProgress()
        val reference = FirebaseDatabase.getInstance().reference
        val taskId = reference
            .child("groups")
            .child("activities")
            .child("tasks")
            .push().key
        val formatter = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
        val dateString = formatter.format(Date())

        val tasks = Tasks(taskName, action, dateString, taskId)
        reference.child("groups")
            .child(group.group_id)
            .child("activities")
            .child("tasks")
            .child(taskId!!)
            .setValue(tasks)
            .addOnSuccessListener {
                addAccountViewModel.listener!!.setViewsAfter()
            }

    }

    fun addInvestment(
        addAccountViewModel: AddAccountViewModel,
        group: Groups,
        name: String,
        type: String,
        maturityDate: String,
        amount: String
    ) {

        if (name.isEmpty() || maturityDate.isEmpty() || amount.isEmpty()) {
            return
        }
        if (name[0].isWhitespace() || amount[0].isWhitespace()) {
            return
        }

        addAccountViewModel.listener!!.setProgress()
        val reference = FirebaseDatabase.getInstance().reference

        val inv1id = reference
            .child("groups")
            .child("financials")
            .child("investments")
            .push().key

        val investment = Investment(name, type, amount, maturityDate, inv1id!!)

        reference.child("groups")
            .child(group.group_id)
            .child("financials")
            .child("investments")
            .child(inv1id)
            .setValue(investment)
            .addOnSuccessListener {
                addAccountViewModel.listener!!.setViewsAfter()
            }
    }
}