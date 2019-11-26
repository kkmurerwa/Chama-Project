package dev.ronnie.chama.admin

interface TransactionListener {
    fun toast(message: String)
    fun transactionComplete()
    fun startTransaction()
    fun stopProgress()
}