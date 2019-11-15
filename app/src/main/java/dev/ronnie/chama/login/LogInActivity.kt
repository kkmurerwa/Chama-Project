package dev.ronnie.chama.login

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.LandingPageActivity
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityLogInBinding
import dev.ronnie.chama.signin.SignUpActivity

class LogInActivity : AppCompatActivity(), LogInListener {

    lateinit var viewModel: LogInViewModel
    lateinit var binding: ActivityLogInBinding

    companion object {
        lateinit var contextLogInActivity: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)
        viewModel = ViewModelProviders.of(this).get(LogInViewModel::class.java)
        binding.logModel = viewModel
        viewModel.listener = this

        contextLogInActivity = this

        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.notification2)
        }
    }

    override fun goToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)

    }

    override fun goToLandingPage() {
        finish()
        val intent = Intent(this, LandingPageActivity::class.java)
        startActivity(intent)

    }

    override fun viewsEmpty(message: String, code: Int) {
        if (code == 1) {
            binding.tvEmailLog.error = message
            binding.tvEmailLog.requestFocus()
        }
        if (code == 2) {
            binding.passwordLog.error = message
            binding.passwordLog.requestFocus()
        }

    }

    override fun progressBarVisible() {

        binding.progressLog.visibility = View.VISIBLE

    }

    override fun progressBarGone() {
        binding.progressLog.visibility = View.GONE

    }

    override fun toast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    }

    override fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }

}
