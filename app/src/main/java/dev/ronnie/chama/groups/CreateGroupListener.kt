package dev.ronnie.chama.groups

interface CreateGroupListener {

    fun showProgressBar()
    fun hideProgressBar()
    fun toast(message:String)
    fun notifyAdapter()

}