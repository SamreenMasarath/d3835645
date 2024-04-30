package com.example.alertguard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.alertguard.databinding.ActivityForgetPasswordBinding
import com.example.alertguard.db.AppDatabase

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClicks()
    }

    private fun initClicks() {
        binding.forgotBtn.setOnClickListener {
            if (binding.emailEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter Email ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (binding.confirmPasswordEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter confirm password ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (binding.passwordEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter password ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (binding.passwordEdt.text.toString().length < 6) {
                Toast.makeText(this, "Please Enter at least 6 digit password ", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            if (binding.passwordEdt.text.toString() != binding.confirmPasswordEdt.text.toString()) {
                Toast.makeText(
                    this, "Entered password and confirm password field not match ",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val thread = Thread {
                val user = AppDatabase.getDatabase().userModelDao().getUser(binding.emailEdt.text.toString())
                if (user != null) {
                    user.password = binding.passwordEdt.text.toString()
                    AppDatabase.getDatabase().userModelDao().insert(user)
                    runOnUiThread {
                        Toast.makeText(this, "User Password reset successfully", Toast.LENGTH_LONG).show()
                        onBackPressed()
                    }
                } else runOnUiThread {
                    Toast.makeText(this, "User does not Exist", Toast.LENGTH_LONG).show()
                }
            }
            thread.start()
            thread.join()
        }
        binding.goBack.setOnClickListener {
            onBackPressed()
        }
    }
}