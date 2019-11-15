package dev.ronnie.chama.chat

import androidx.lifecycle.ViewModel
import dev.ronnie.chama.models.ChatMessage
import dev.ronnie.chama.models.Groups

class ChatRoomViewModel : ViewModel() {

    lateinit var listener: ChatRoomListener

    fun getMessages(group: Groups): MutableList<ChatMessage>? {
        return ChatRepository(group, this).getMessages()
    }

    fun createNewMessage(group: Groups, message: String) {
        NewChatMessage().createNewMessage(group, message, this)
    }
}