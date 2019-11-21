package dev.ronnie.chama.profile


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Base64
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityProfileBinding
import dev.ronnie.chama.imageUtils.ChooseImageFragment
import dev.ronnie.chama.imageUtils.ImageActivity
import dev.ronnie.chama.login.LogInActivity
import dev.ronnie.chama.models.User
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL


class ProfileActivity : AppCompatActivity(), ProfileListener,
    ChooseImageFragment.OnInputListener {

    lateinit var viewModel: ProfileViewModel
    lateinit var binding: ActivityProfileBinding


    companion object {
        val SHARED_PREFS = "sharedPrefs"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        binding.profileModel = viewModel
        viewModel.listener = this
        fab.hide()

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Profile"
            (toolbar as Toolbar).setNavigationOnClickListener {
                onBackPressed()
            }

        }

        setDataFromSharedPrefs()
        viewModel.getProfile()


    }

    private fun setDataFromSharedPrefs() {

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        viewModel.fname = sharedPreferences.getString("fname", "")
        viewModel.sname = sharedPreferences.getString("sname", "")
        viewModel.phone = sharedPreferences.getString("phone", "")
        binding.inputEmail.setText(FirebaseAuth.getInstance().currentUser?.email.toString())
        val imageInput = sharedPreferences.getString("imageByte", "")

        val handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                val data = msg?.data

                val bitmap: Bitmap? = data?.getParcelable("bitmap")

                bitmap?.let {
                    binding.profileImage.setImageBitmap(it)

                }


            }

        }
        val thread = Thread(object : MyRunnable(handler, imageInput!!) {

        })
        thread.start()
    }

    override fun sendInput(bytes: ByteArray) {

        viewModel.sendInput(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        binding.profileImage.setImageBitmap(bitmap)

        val thread = Thread(Runnable {
            val sharedPreferences: SharedPreferences =
                getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("imageByte", encodeTobase64(bitmap))
            editor.apply()

        })
        thread.start()
    }

    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 70, baos)
        val b: ByteArray = baos.toByteArray()
        val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)
        Log.d("Image Log:", imageEncoded)
        return imageEncoded
    }

    override fun displayProfile(user: LiveData<User>) {

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val savedImage = sharedPreferences.getString("imageString", "")
        Log.d("Picture", "Saved Image $savedImage")

        user.observe(this, Observer {
            binding.inputFname.setText(it.fname)
            binding.inputSname.setText(it.sname)
            binding.inputPhone.setText(it.phone)
            binding.inputEmail.setText(FirebaseAuth.getInstance().currentUser?.email.toString())


            if (!it.profile_image.isNullOrEmpty() && savedImage != it.profile_image) {
                Log.d("Picture", "Picture found ${it.profile_image}")
                Picasso.get()
                    .load(it.profile_image)
                    .placeholder(R.drawable.loading)
                    .into(profile_image)
                saveImageFromNetwork(it.profile_image!!)

            }

        })
    }

    override fun showDialog() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("MyDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val dialog = ChooseImageFragment()
        dialog.show(ft, "MyDialog")
    }

    override fun progressBarGone() {
        progressBar.visibility = View.GONE

    }

    override fun progressBarVisible() {
        progressBar.visibility = View.VISIBLE

    }

    override fun toast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

    override fun enableSaveButton() {
        binding.btnSave.isEnabled = true
    }

    override fun disableSaveButton() {
        binding.btnSave.isEnabled = false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.Edit_menu) {
            supportActionBar!!.title = "Edit Profile"
            setViewsEnabled()
        } else if (item.itemId == R.id.log_out) {
            logOut()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun logOut() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LogInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finishAffinity()
        startActivity(intent)
    }

    private fun setViewsEnabled() {

        btnSave.isEnabled = true
        inputFname.isEnabled = true
        inputSname.isEnabled = true
        inputPhone.isEnabled = true
        fab.show()
    }

    override fun openPicture() {

        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val savedImage = sharedPreferences.getString("imageByte", "")
        if (savedImage != "") {
            val intent = Intent(this, ImageActivity::class.java)
            startActivity(intent)
        }

    }

    override fun savePofilePicToPrefs(image: String) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("imageString", image)
        editor.apply()
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("fname", inputFname.text.toString())
        editor.putString("sname", inputSname.text.toString())
        editor.putString("phone", inputPhone.text.toString())
        editor.putString("email", FirebaseAuth.getInstance().currentUser?.email.toString())
        editor.apply()

        Log.d("ProfileActivity", "Shared Prefs Saved")
    }

    private fun saveImageFromNetwork(profile_image: String) {
        val thread = Thread(Runnable {
            try {
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                val url = URL(profile_image)
                val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                //   binding.profileImage.setImageBitmap(image)
                val editor = sharedPreferences.edit()
                editor.putString("imageString", profile_image)
                editor.putString("imageByte", encodeTobase64(image))
                editor.apply()
            } catch (e: IOException) {

            }
        })
        thread.start()
    }

}


