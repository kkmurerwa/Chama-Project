package  dev.ronnie.chama.groups

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dev.ronnie.chama.R
import dev.ronnie.chama.chat.ChatRoomActivity
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_groups.*


class GroupsActivity : AppCompatActivity() {

    lateinit var group: Groups

    companion object {
        lateinit var bundle: Bundle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_groups)

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Groups"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }

        }
        val fragmentAdapter = GroupPageAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)

        create.setOnClickListener {
            val ft = supportFragmentManager.beginTransaction()
            val prev = supportFragmentManager.findFragmentByTag("Dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)

            val dialog = CreateNewGroupFragment()
            dialog.show(ft, "Dialog")
        }
    }

    override fun onResume() {
        super.onResume()
        ChatRoomActivity.mMessagesList?.clear()
        ChatRoomActivity.mMessageIdSet?.clear()
    }
}

