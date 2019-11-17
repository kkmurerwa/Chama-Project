package dev.ronnie.chama.admin

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.R
import dev.ronnie.chama.cloudmessaging.FCM
import dev.ronnie.chama.cloudmessaging.fcm.Data
import dev.ronnie.chama.cloudmessaging.fcm.FirebaseCloudMessage
import dev.ronnie.chama.databinding.ActivityAdminBinding
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_admin.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class AdminActivity : AppCompatActivity() {

    companion object {
        lateinit var group: Groups
    }


    lateinit var mServerKey: String
    lateinit var viewModel: AdminViewModel
    lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin)
        viewModel = ViewModelProviders.of(this)[AdminViewModel::class.java]
        binding.model = viewModel

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Admin"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }
        }
        val intent = intent
        group = intent.getParcelableExtra("group")

        init()
        getServerKey()


    }

    private fun init() {

        viewModel.getRequestList(group).observe(this, Observer {

            val adapter = RequestJoinRecyclerView(this, it)
            binding.requestRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.requestRecyclerView.adapter = adapter
        })

    }

    private fun getServerKey() {
        val reference = FirebaseDatabase.getInstance().reference
        val query =
            reference.child("server")
                .orderByValue()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val singleSnapshot = dataSnapshot.children.iterator().next()
                mServerKey = singleSnapshot.value.toString()
                sendMessageToDepartment("Some Title", "Some Message")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


    private fun sendMessageToDepartment(
        title: String,
        message: String
    ) {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://fcm.googleapis.com/fcm/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        //create the interface
        val fcmAPI: FCM = retrofit.create<FCM>(FCM::class.java)
        //attach the headers
        val headers =
            HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Authorization"] = "key=$mServerKey"
        //send the message to all the tokens
        val data = Data()
        data.message = message
        data.title = title
        data.data_type = "admin_broadcast"
        val firebaseCloudMessage = FirebaseCloudMessage()
        firebaseCloudMessage.data = data
        firebaseCloudMessage.to =
            "cbH4Xu3VIwc:APA91bHpZ7tNEy_Eet_DQlo9YH_0Kk9RjotfjeLSbH-r3n3uJmGCQRP2EHwj" +
                    "-iHWMfJEOn4mPs-2CUTiDV-wOEI6uv5_qBkkRkatoEWPL1DFnsbgjh0n6C1J6dBrOkjCGUX2j7v3odeB"
        val call: Call<ResponseBody?>? =
            fcmAPI.send(headers, firebaseCloudMessage)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {

            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                t: Throwable
            ) {
                Toast.makeText(this@AdminActivity, "error", Toast.LENGTH_LONG).show()
            }
        })

    }

}
