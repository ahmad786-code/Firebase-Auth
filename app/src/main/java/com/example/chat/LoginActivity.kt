package com.example.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val bindung by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindung.root)

        navigateSignUp()
    }

    private fun navigateSignUp() {
        val signUpText = bindung.textSignUp

        signUpText.setOnClickListener() {
        startActivity(Intent(this, SignUpActivity::class.java))
        }

    }
}