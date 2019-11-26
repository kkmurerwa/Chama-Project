package dev.ronnie.chama.admin

import com.google.firebase.database.FirebaseDatabase
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa
import dev.ronnie.chama.models.Projects
import java.text.SimpleDateFormat
import java.util.*

class AddAccounts {

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
                "0 days",
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
}