package dev.ronnie.chama

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import dev.ronnie.chama.groups.GroupsActivity
import dev.ronnie.chama.login.LogInActivity
import dev.ronnie.chama.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_landing_page.*

class LandingPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing_page)

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            title = getString(R.string.app_name)

        }

        if (FirebaseAuth.getInstance().currentUser == null || !FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
            val intent = Intent(this, LogInActivity::class.java)
            startActivity(intent)
        }
        cardGroups.setOnClickListener {
            startActivity(Intent(this, GroupsActivity::class.java))
        }
        cardProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))

        }
    }

}
