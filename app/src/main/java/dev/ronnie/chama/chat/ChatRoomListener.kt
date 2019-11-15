package dev.ronnie.chama.chat

interface ChatRoomListener {

    fun initMessagesList()
    fun notifyAdapter()
    fun setSelection()
    fun emptyText()
}