package dev.ronnie.chama.chat

import android.os.Bundle
import android.view.WindowManager
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.*
import dev.ronnie.chama.R
import dev.ronnie.chama.models.ChatMessage
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity(), ChatRoomListener {

    companion object {

        var mMessagesList: MutableList<ChatMessage>? = null
        var mMessageIdSet: MutableSet<String>? = null
        var mMessagesReference: DatabaseReference? = null


    }

    var mAdapterChat: ChatMessageListAdapter? = null
    var mListView: ListView? = null
    var mMessage: EditText? = null
    lateinit var viewModel: ChatRoomViewModel
    lateinit var groupChat: Groups


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Chatroom"
            (toolbar as Toolbar).setNavigationOnClickListener {
                onBackPressed()
            }

        }

        viewModel = ViewModelProviders.of(this)[ChatRoomViewModel::class.java]
        viewModel.listener = this

        val intentComing = intent
        groupChat = intentComing.getParcelableExtra("group")

        mMessage = input_message
        mListView = listView

        enableChatRoomListener()
        viewModel.getMessages(groupChat)

        checkmark.setOnClickListener {
            listView!!.smoothScrollToPosition(mAdapterChat!!.count - 1)
            val message = input_message.text.toString()
            viewModel.createNewMessage(groupChat, message)
        }

        hideSoftKeyboard()
    }

    private fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun emptyText() {
        mMessage!!.setText("")
    }

    override fun initMessagesList() {
        mAdapterChat = mMessagesList?.let {
            ChatMessageListAdapter(this, R.layout.message_list, it)
        }
        mListView!!.adapter = mAdapterChat
        listView!!.smoothScrollToPosition(mAdapterChat!!.count - 1)
    }

    override fun notifyAdapter() {
        mAdapterChat!!.notifyDataSetChanged()
    }

    override fun setSelection() {
        mListView!!.setSelection(mAdapterChat!!.count - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMessagesReference!!.removeEventListener(mValueEventListener)
    }

    private var mValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            viewModel.getMessages(groupChat)
        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }

    private fun enableChatRoomListener() {
        mMessagesReference =
            FirebaseDatabase.getInstance().reference.child("groups")
                .child(groupChat.group_id)
                .child("chatroom")
        mMessagesReference!!.addValueEventListener(mValueEventListener)
    }


}
