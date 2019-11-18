package dev.ronnie.chama.cloudmessaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dev.ronnie.chama.R
import dev.ronnie.chama.chat.ChatRoomActivity
import dev.ronnie.chama.groups.MainGroupActivity
import dev.ronnie.chama.models.Groups
import java.util.*


class FirebaseMessagingService :
    FirebaseMessagingService() {


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("MessagingService", "onMessageReceived Called")


        val identifyDataType =
            remoteMessage.data["data_type"]

        if (identifyDataType.equals("data_type_chat_message")) {
            val title =
                remoteMessage.data["title"]
            val message =
                remoteMessage.data["message"]
            val groupId =
                remoteMessage.data["group_id"]
            val senderId =
                remoteMessage.data["sender_id"]

            Log.d("MessagingService", "SenderId $senderId")
            Log.d("MessagingService", "CurrentId ${FirebaseAuth.getInstance().currentUser!!.uid}")
            if (senderId != FirebaseAuth.getInstance().currentUser!!.uid) {
                val query =
                    FirebaseDatabase.getInstance().reference
                        .child("groups")
                        .orderByKey()
                        .equalTo(groupId!!)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) 
                        if (dataSnapshot.exists()) {
                            for (snapshot in dataSnapshot.children) {

                                try {
                                    val group = Groups()
                                    val objectMap = snapshot.value as HashMap<*, *>?
                                    group.group_id =
                                        objectMap!!["group_id"]!!.toString()
                                    group.group_name =
                                        objectMap["group_name"]!!.toString()
                                    group.creator_id =
                                        objectMap["creator_id"]!!.toString()

                                    sendChatmessageNotification(title!!, message!!, group)
                                } catch (e: NullPointerException) {
                                    Log.d("Notif", "Null Notification")
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })

            }
        }


    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                val deviceToken = instanceIdResult.token

                Log.d("Token", "New Token $deviceToken")
                sendRegistrationToServer(deviceToken)
            }
    }

    private fun sendRegistrationToServer(token: String?) {

        if (FirebaseAuth.getInstance().currentUser?.uid != null) {
            val reference = FirebaseDatabase.getInstance().reference
            reference.child("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("messaging_token")
                .setValue(token)
        }
    }

    private fun sendChatmessageNotification(
        title: String,
        message: String,
        group: Groups
    ) {

        if (!ChatRoomActivity.isActivityRunning || ChatRoomActivity.GroupUserIn != group.group_id) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationManager =
                    this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notificationId = 1
                val channelId = "channel-01"
                val channelName = "Chat Channel"
                val importance = NotificationManager.IMPORTANCE_DEFAULT

                val mChannel = NotificationChannel(
                    channelId, channelName, importance
                )
                notificationManager.createNotificationChannel(mChannel)

                val mBuilder = NotificationCompat.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText("New messages in " + group.group_name)
                    .setSubText(message)
                    .setColor(Color.BLUE)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("New messages in " + group.group_name).setSummaryText(
                                message
                            )
                    )
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            applicationContext.resources,
                            R.drawable.logo
                        )
                    )
                    .setOnlyAlertOnce(true)
                    .setAutoCancel(true)

                val pendingIntent = Intent(this, MainGroupActivity::class.java)
                pendingIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                pendingIntent.putExtra("pending_intent_group", group)
                val notifyPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    pendingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT  or  PendingIntent.FLAG_ONE_SHOT
                )

                mBuilder.setContentIntent(notifyPendingIntent)
                notificationManager.notify(notificationId, mBuilder.build())

            } else {
                val notificationId: Int = buildNotificationId(group.group_id)
                // Instantiate a Builder object.
                val builder: NotificationCompat.Builder = NotificationCompat.Builder(
                    this, "default_notification_channel_name"
                )
                val pendingIntent = Intent(this, MainGroupActivity::class.java)
                pendingIntent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                pendingIntent.putExtra("pending_intent_group", group)
                val notifyPendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    pendingIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT  or  PendingIntent.FLAG_ONE_SHOT
                )
                //add properties to the builder
                builder.setSmallIcon(R.drawable.logo)
                    .setLargeIcon(
                        BitmapFactory.decodeResource(
                            applicationContext.resources,
                            R.drawable.logo
                        )
                    )
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(title)
                    .setContentText("New messages in " + group.group_name)
                    .setColor(Color.BLUE)
                    .setAutoCancel(true)
                    .setSubText(message)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("New messages in " + group.group_name).setSummaryText(
                                message
                            )
                    )
                    .setOnlyAlertOnce(true)
                builder.setContentIntent(notifyPendingIntent)
                val mNotificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.notify(notificationId, builder.build())

                Log.d(
                    "Notifications",
                    "Service: Is Activity running ${ChatRoomActivity.isActivityRunning}"
                )
                Log.d(
                    "Notifications",
                    "Service: Which Group is this ${ChatRoomActivity.GroupUserIn}"
                )

            }
        }
    }

    private fun buildNotificationId(id: String): Int {

        var notificationId = 0
        for (i in 0..8) {
            notificationId += id[0].toInt()
        }
        return notificationId
    }

}

