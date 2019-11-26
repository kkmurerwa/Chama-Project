package dev.ronnie.chama.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.*

class AddAccountViewModel : ViewModel() {

    var listener: AddAcountListener? = null

    fun getBank(group: Groups): LiveData<MutableList<Bank>> {
        return FireBaseData().getBank(group)
    }

    fun getMpesa(group: Groups): LiveData<MutableList<Mpesa>> {
        return FireBaseData().getMpesa(group)
    }


    fun addBankAccount(group: Groups, name: String) {
        AddAccounts().addBankAccount(this, group, name)
    }

    fun addMpesaAccount(group: Groups, name: String) {
        AddAccounts().addMpesaAccount(this, group, name)
    }

    fun getProject(group: Groups): LiveData<MutableList<Projects>> {

        return FireBaseData().getProjects(group)


    }

    fun getInvestments(group: Groups): LiveData<MutableList<Investment>> {
        return FireBaseData().getInvestment(group)

    }

    fun getTasks(group: Groups): LiveData<MutableList<Tasks>> {
        return FireBaseData().getTasks(group)

    }

    fun addProject(
        group: Groups,
        projectName: String,
        projectCost: String,
        leader: String,
        projectStatues: String
    ) {
        AddAccounts().addProjects(this, group, projectName, projectCost, leader, projectStatues)

    }

    fun editProject(group: Groups, project: Projects, statues: String) {
        AddAccounts().editProject(this,group, project, statues)

    }

}