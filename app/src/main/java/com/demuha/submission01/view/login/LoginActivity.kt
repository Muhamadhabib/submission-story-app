package com.demuha.submission01.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.demuha.submission01.databinding.ActivityLoginBinding
import com.demuha.submission01.util.Resource
import com.demuha.submission01.view.ViewModelFactory
import com.demuha.submission01.view.home.MainActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }
    private fun playAnimation() {
        binding.apply {
            ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title = ObjectAnimator.ofFloat(titleTextView, View.ALPHA, 1f).setDuration(100)
            val msgTitle = ObjectAnimator.ofFloat(messageTextView, View.ALPHA, 1f).setDuration(100)
            val emailTitle = ObjectAnimator.ofFloat(emailTextView, View.ALPHA, 1f).setDuration(100)
            val emailInput = ObjectAnimator.ofFloat(emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
            val passTitle = ObjectAnimator.ofFloat(passwordTextView, View.ALPHA, 1f).setDuration(100)
            val passInput = ObjectAnimator.ofFloat(passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
            val submitBtn = ObjectAnimator.ofFloat(loginButton, View.ALPHA, 1f).setDuration(100)

            AnimatorSet().apply {
                playSequentially(title, msgTitle, emailTitle, emailInput, passTitle, passInput, submitBtn)
                start()
            }
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.apply {
            loginButton.setOnClickListener {
                val email = edLoginEmail.text.toString().trim()
                val password = edLoginPassword.text.toString().trim()

                if (edLoginEmail.isValid() && edLoginPassword.isValid()) {
                    viewModel.login(email, password)
                } else {
                    Toast.makeText(this@LoginActivity, "Pengisian kurang sesuai", Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.response.observe(this@LoginActivity) {resource ->
            when(resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@LoginActivity, resource.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}