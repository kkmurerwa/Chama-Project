package dev.ronnie.chama.profile


import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
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

class ProfileActivity : AppCompatActivity(), ProfileListener,
    ChooseImageFragment.OnInputListener {

    lateinit var viewModel: ProfileViewModel
    lateinit var binding: ActivityProfileBinding
    private var bigPictureUrl: String? = null

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
                finish()
            }

        }

        viewModel.getProfile()


    }

    override fun sendInput(bytes: ByteArray) {

        viewModel.sendInput(bytes)
        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        binding.profileImage.setImageBitmap(bitmap)

    }

    override fun displayProfile(user: LiveData<User>) {

        user.observe(this, Observer {
            binding.inputFname.setText(it.fname)
            binding.inputSname.setText(it.sname)
            binding.inputPhone.setText(it.phone)

            binding.inputEmail.setText(FirebaseAuth.getInstance().currentUser?.email.toString())

            if (!it.profile_image.isNullOrEmpty()) {

                bigPictureUrl = it.profile_image!!

                Log.d("Picture", "Picture found ${it.profile_image}")
                Picasso.get()
                    .load(it.profile_image)
                    .placeholder(R.drawable.loading)
                    .fit()
                    .centerInside()
                    .into(binding.profileImage)
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
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LogInActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        finish()
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

        if (!bigPictureUrl.isNullOrEmpty()) {

            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("Image", bigPictureUrl)
            startActivity(intent)
        }

    }


}

