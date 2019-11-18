package  dev.ronnie.chama.groups

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_members.*

class MembersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)


        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Members"

            (toolbar as Toolbar).setNavigationOnClickListener {
             onBackPressed()
            }

        }

        val intent = intent
        val group = intent.getParcelableExtra("group") as Groups

        FireBaseData().getDisplayUsers(group).observe(this, Observer {
            val adapter = MembersRecyclerViewAdapter(this, it)
            membersRv.layoutManager = LinearLayoutManager(this)
            membersRv.adapter = adapter

        })

    }
}
