package dev.ronnie.chama.cash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_banks_and_mpesa.*

class BanksAndMpesaActivity : AppCompatActivity() {
    lateinit var group: Groups

    companion object {
        lateinit var bundle: Bundle
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banks_and_mpesa)

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Accounts"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }

        }

        val intent = intent
        group = intent.getParcelableExtra("group")

        bundle = Bundle()
        bundle.putParcelable("group", group)

        val fragmentAdapter = PagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)
    }
}
