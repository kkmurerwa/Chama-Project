package dev.ronnie.chama.chat


import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dev.ronnie.chama.models.ChatMessage
import dev.ronnie.chama.models.Groups
import java.text.SimpleDateFormat
import java.util.*

class NewChatMessage {

    fun createNewMessage(groupNewChat: Groups, message: String, model: ChatRoomViewModel) {

        if (message.isEmpty()) {
            Log.d("New", "message null")
            return
        }

        if(message[0].isWhitespace()){
            return
        }
        val newMessage = ChatMessage()
        newMessage.message = message
        newMessage.timestamp = timestamp
        newMessage.user_id = FirebaseAuth.getInstance().currentUser!!.uid

        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(groupNewChat.group_id)
            .child("chatroom")

        val newMessageId = reference.push().key

        reference
            .child(newMessageId!!)
            .setValue(newMessage)
        model.listener.emptyText()
    }

    private val timestamp: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            sdf.timeZone = TimeZone.getTimeZone("Africa/Nairobi")
            return sdf.format(Date())
        }

}