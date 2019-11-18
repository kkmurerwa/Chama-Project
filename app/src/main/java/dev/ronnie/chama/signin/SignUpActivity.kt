package dev.ronnie.chama.signin

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.iid.FirebaseInstanceId
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivitySignUpBinding
import dev.ronnie.chama.login.LogInActivity
import dev.ronnie.chama.profile.ProfileActivity


class SignUpActivity : AppCompatActivity(), SignUpListener {


    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignUpBinding

    companion object {
        lateinit var contextSignUpActivity: Context
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)

        viewModel = ViewModelProviders.of(this).get(SignUpViewModel::class.java)
        binding.signModel = viewModel
        viewModel.listener = this

        contextSignUpActivity = this

        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.notification2)
        }

    }

    override fun viewsEmpty(message: String, code: Int) {
        if (code == 1) {
            binding.tvEmailSign.error = message
            binding.tvEmailSign.requestFocus()

        }

        if (code == 2) {
            binding.passwordSign.error = message
            binding.passwordSign.requestFocus()

        }

        if (code == 3) {
            binding.passwordConfirm.error = message
            binding.passwordConfirm.requestFocus()


        }

    }

    override fun error(message: String, code: Int) {

        if (code == 1) {
            binding.passwordConfirm.error = message
            binding.passwordConfirm.requestFocus()

        }
        if (code == 2) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()

        }

    }

    override fun progressBarVisible() {

        binding.progressSign.visibility = View.VISIBLE

    }

    override fun progressBarGone() {

        binding.progressSign.visibility = View.GONE

    }

    override fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

    override fun goToLogIn() {
        finishAndRemoveTask()
        val intent = Intent(this, LogInActivity::class.java)
        startActivity(intent)

    }

    override fun goToProfile() {
        finishAndRemoveTask()
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)

    }

    override fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }



}
