package dev.ronnie.chama.chat

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.chat.ChatRoomActivity.Companion.mMessageIdSet
import dev.ronnie.chama.chat.ChatRoomActivity.Companion.mMessagesList
import dev.ronnie.chama.models.ChatMessage
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.User
import java.util.*

class ChatRepository(var groupChat: Groups, val model: ChatRoomViewModel?) {


    fun getMessages(): MutableList<ChatMessage>? {
        if (mMessagesList == null) {
            mMessagesList = ArrayList()
            mMessageIdSet = HashSet()
            model?.listener?.initMessagesList()
        }
        model?.listener?.initMessagesList()

        val reference = FirebaseDatabase.getInstance().reference
        val query = reference.child("groups")
            .child(groupChat.group_id)
            .child("chatroom")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (snapshot in dataSnapshot.children) {

                        Log.d("Chat", "onDataChange: found chatroom message: " + snapshot.value!!)
                        try {
                            val message = ChatMessage()

                            if (!mMessageIdSet!!.contains(snapshot.key)) {
                                mMessageIdSet!!.add(snapshot.key!!)

                                message.message =
                                    snapshot.getValue(ChatMessage::class.java)!!.message
                                message.user_id =
                                    snapshot.getValue(ChatMessage::class.java)!!.user_id
                                message.timestamp =
                                    snapshot.getValue(ChatMessage::class.java)!!.timestamp
                                message.profile_image = ""
                                message.name = ""
                                mMessagesList!!.add(message)
                                //getUserDetails2(message)
                            }

                        } catch (e: NullPointerException) {
                            Log.d("Chat", "onDataChange: NullPointerException: " + e.message)
                        }

                    }
                }
                getUserDetails()
                model?.listener?.notifyAdapter()
                model?.listener?.setSelection()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return mMessagesList
    }


    fun getUserDetails2(message: ChatMessage) {
        val reference = FirebaseDatabase.getInstance().reference
        if (message.user_id != null && message.profile_image == "") {
            val query = reference.child("Users")
                .orderByKey()
                .equalTo(message.user_id)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val singleSnapshot = dataSnapshot.children.iterator().next()
                    message.profile_image =
                        singleSnapshot.getValue(User::class.java)!!.profile_image
                    message.name = singleSnapshot.getValue(User::class.java)!!.fname
                    mMessagesList!!.add(message)
                    model?.listener?.notifyAdapter()
                    model?.listener?.setSelection()
                }

                override fun onCancelled(databaseError: DatabaseError) {

                }
            })
        }
    }


    fun getUserDetails() {

        val reference = FirebaseDatabase.getInstance().reference
        for (i in 0 until mMessagesList!!.size) {
            if (mMessagesList!![i].user_id != null && mMessagesList!![i].profile_image == "") {
                val query = reference.child("Users")
                    .orderByKey()
                    .equalTo(mMessagesList!![i].user_id)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val singleSnapshot = dataSnapshot.children.iterator().next()
                        mMessagesList!![i].profile_image =
                            singleSnapshot.getValue(User::class.java)!!.profile_image
                        mMessagesList!![i].name = singleSnapshot.getValue(User::class.java)!!.fname
                        model?.listener?.notifyAdapter()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                })
            }
        }

    }


}