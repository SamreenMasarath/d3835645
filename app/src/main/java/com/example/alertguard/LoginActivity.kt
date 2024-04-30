package com.example.alertguard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.alertguard.databinding.ActivityLoginBinding
import com.example.alertguard.db.AppDatabase
import com.example.alertguard.db.models.UserModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        if (SharedPreferences.getBoolean(Singleton.login)) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        setContentView(binding.root)
        initClicks()
    }

    private fun initClicks() {
        binding.createOneTxt.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            if (binding.emailEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter Email ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (binding.passwordEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter Password ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var data: UserModel? = null
            val thread = Thread {
                data = AppDatabase.getDatabase().userModelDao().getUser(binding.emailEdt.text.toString())
            }
            thread.start()
            thread.join()

            if (data != null) {
                if (data?.password == binding.passwordEdt.text.toString()) {
                    Singleton.userModel = data
                    SharedPreferences.saveBoolean(Singleton.login, true)
                    SharedPreferences.saveInt(Singleton.userId, data?.id!!)
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                } else Toast.makeText(this, "Email or password is wrong", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Email or password is wrong", Toast.LENGTH_LONG).show()
            }
        }
        binding.forgotTxt.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
    }
}