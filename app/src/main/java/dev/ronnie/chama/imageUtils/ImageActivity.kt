package dev.ronnie.chama.imageUtils

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import dev.ronnie.chama.R
import kotlinx.android.synthetic.main.activity_image.*


class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.statusbar2)
        }
        if (toolbar2 != null) {
            setSupportActionBar(toolbar2 as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Profile"
            (toolbar2 as Toolbar).setNavigationOnClickListener {
                finish()
            }
        }

        val intent = intent
        val profile = intent.getStringExtra("Image")

        Picasso.get()
            .load(profile)
            .placeholder(R.drawable.loading)
            .fit()
            .centerInside()
            .into(imageViewBig)
    }

}

