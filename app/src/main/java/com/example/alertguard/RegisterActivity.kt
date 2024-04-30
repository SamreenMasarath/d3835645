package com.example.alertguard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.alertguard.databinding.ActivityRegisterBinding
import com.example.alertguard.db.AppDatabase
import com.example.alertguard.db.models.UserModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initClicks()
    }

    private fun initClicks() {
        binding.signUpBtn.setOnClickListener {
            if (binding.emailEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter Email ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (binding.confirmPasswordEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter confirm password ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (binding.emergencyEdt.text.isNullOrEmpty()) {
                Toast.makeText(this, "Please Enter emergency contact ", Toast.LENGTH_LONG).show()
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
            if (binding.emergencyEdt.text.toString().length < 10) {
                Toast.makeText(
                    this, "Enter correct emergency number ",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val userModel = UserModel(
                email = binding.emailEdt.text.toString(),
                password = binding.passwordEdt.text.toString(),
                contact = binding.emergencyEdt.text.toString()
            )
            val thread = Thread {
                AppDatabase.getDatabase().userModelDao().insert(userModel)
            }
            thread.start()
            thread.join()
            Toast.makeText(
                this, "User added successfully", Toast.LENGTH_LONG
            ).show()
            onBackPressed()
        }

        binding.goBack.setOnClickListener {
            onBackPressed()
        }
    }
}