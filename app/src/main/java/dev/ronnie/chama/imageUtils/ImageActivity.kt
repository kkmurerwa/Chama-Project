package dev.ronnie.chama.imageUtils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import dev.ronnie.chama.R
import dev.ronnie.chama.profile.MyRunnable
import dev.ronnie.chama.profile.ProfileActivity
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
                onBackPressed()
            }
        }

        val intent = intent
        if (intent.hasExtra("Image")) {
            val profile = intent.getStringExtra("Image")
            Picasso.get()
                .load(profile)
                .placeholder(R.drawable.loading)
                .fit()
                .centerInside()
                .into(imageViewBig)
        } else {
            setBitmap()
        }
    }

    private fun setBitmap() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(ProfileActivity.SHARED_PREFS, Context.MODE_PRIVATE)
        val imageInput = sharedPreferences.getString("imageByte", "")

        val handler = object : Handler(Looper.getMainLooper()) {

            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                val data = msg!!.data
                val bitmap = data.getParcelable("bitmap") as Bitmap
                imageViewBig.setImageBitmap(bitmap)
                imageViewBig.tag = bitmap
            }

        }

        val thread = Thread(object : MyRunnable(handler, imageInput!!) {

        })
        thread.start()
    }

}

