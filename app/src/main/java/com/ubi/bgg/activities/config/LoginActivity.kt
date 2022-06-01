package com.ubi.bgg.activities.config

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ubi.bgg.Common
import com.ubi.bgg.activities.MainActivity
import com.ubi.bgg.databinding.ActivityLoginBinding
import com.ubi.bgg.services.bgg.user.BGGUserService


class LoginActivity : AppCompatActivity() {
  private lateinit var binding: ActivityLoginBinding

  override fun onCreate(icicle: Bundle?) {
    super.onCreate(icicle)
    binding = ActivityLoginBinding.inflate(layoutInflater)
    Common.initialize(applicationContext)
    setSupportActionBar(binding.toolbar.root)
    setContentView(binding.root)

    binding.bLogin.setOnClickListener { attemptLogin() }
    if (Common.Preferences.getString("username", null) != null) {
      login()
    }
  }

  private fun attemptLogin() {
    val username = binding.tfLogin.text.toString()
    if (username.isBlank()) return showToast("Specify valid username")
    if (BGGUserService.exists(username).not()) return showToast("User does not exist")

    Common.Preferences.edit().putString("username", username).apply()
    showToast("Login successful for $username")
    login()
  }

  private fun login() = startActivity(Intent(this, MainActivity::class.java))
}

fun showToast(text: String) {
  val toast = Toast.makeText(Common.Context, text, Toast.LENGTH_SHORT)
  toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
  toast.show()
}