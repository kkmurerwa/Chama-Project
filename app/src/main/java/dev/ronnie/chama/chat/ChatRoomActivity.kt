package dev.ronnie.chama.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        var isActivityRunning = false
        var GroupUserIn: String? = null
        var mAdapterChat: ChatMessageRecyclerdapter? = null

    }

    var mListView: RecyclerView? = null
    var mMessage: EditText? = null
    lateinit var viewModel: ChatRoomViewModel
    lateinit var groupChat: Groups


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)


        val intentComing = intent
        groupChat = intentComing.getParcelableExtra("group")
        GroupUserIn = groupChat.group_id


        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = groupChat.group_name
            (toolbar as Toolbar).setNavigationOnClickListener {
                onBackPressed()
            }

        }

        viewModel = ViewModelProviders.of(this)[ChatRoomViewModel::class.java]
        viewModel.listener = this

        mMessage = input_message
        mListView = listView

        enableChatRoomListener()
        viewModel.getMessages(groupChat)

        checkmark.setOnClickListener {

            if (mAdapterChat != null && mAdapterChat!!.itemCount > 0) {
                listView!!.smoothScrollToPosition(mAdapterChat!!.itemCount - 1)
            }
            val message = input_message.text.toString()
            viewModel.createNewMessage(groupChat, message)
        }

        mListView!!.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                mListView!!.postDelayed({
                    mListView!!.scrollToPosition(
                        mListView!!.adapter!!.itemCount - 1
                    )
                }, 10)
            }
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
            ChatMessageRecyclerdapter(this, it)

        }
        mListView!!.layoutManager = LinearLayoutManager(this)
        mListView!!.adapter = mAdapterChat


    }

    override fun notifyAdapter() {
        mAdapterChat!!.notifyDataSetChanged()
    }

    override fun setSelection() {
        mListView!!.scrollToPosition(mAdapterChat!!.itemCount - 1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMessagesReference!!.removeEventListener(mValueEventListener)
        Log.d("Notifications", "ChatRoom Methods: OnDestroy Called")
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

    override fun onStart() {
        super.onStart()
        isActivityRunning = true
        GroupUserIn = groupChat.group_id

        Log.d("Notifications", "ChatRoom Methods: OnStart Called")

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        isActivityRunning = true
        GroupUserIn = groupChat.group_id
        Log.d("Notifications", "ChatRoom Methods: OnNewIntent Called")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        isActivityRunning = false
        mAdapterChat!!.notifyDataSetChanged()
    }


}
