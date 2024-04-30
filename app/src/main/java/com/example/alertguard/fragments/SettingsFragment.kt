package com.example.alertguard.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.alertguard.LoginActivity
import com.example.alertguard.SharedPreferences
import com.example.alertguard.Singleton
import com.example.alertguard.databinding.FragmentSettingsBinding
import com.example.alertguard.db.AppDatabase

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.updateBtn.setOnClickListener {
            if (binding.passwordOldEdt.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(), "Enter old password", Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.passwordNewEdt.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(), "Enter new password", Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.confirmPasswordEdt.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(), "Enter confirm password", Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.emergencyEdt.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(), "Enter emergency number", Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if(binding.passwordNewEdt.text.toString().length < 6) {
                Toast.makeText(
                    requireContext(), "Please Enter at least 6 digit password", Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.passwordNewEdt.text.toString() != binding.confirmPasswordEdt.text.toString()) {
                Toast.makeText(
                    requireContext(), "Entered password and confirm password field not match ",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (binding.emergencyEdt.text.toString().length < 10) {
                Toast.makeText(
                    requireContext(), "Enter correct emergency number ", Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            Singleton.userModel?.let {
                if (it.password == binding.passwordOldEdt.text.toString()) {
                    it.password = binding.passwordNewEdt.text.toString()
                    it.contact = binding.emergencyEdt.text.toString()
                    val thread = Thread {
                        AppDatabase.getDatabase().userModelDao().insert(it)
                    }
                    thread.start()
                    thread.join()
                    Toast.makeText(
                        requireContext(), "Information Updated", Toast.LENGTH_LONG
                    ).show()
                    binding.passwordNewEdt.setText("")
                    binding.passwordOldEdt.setText("")
                    binding.confirmPasswordEdt.setText("")
                } else Toast.makeText(
                    requireContext(), "Old password is incorrect", Toast.LENGTH_LONG
                ).show()
            }
        }
        binding.logoutBtn.setOnClickListener {
            SharedPreferences.saveBoolean(Singleton.login, false)
            SharedPreferences.saveInt(Singleton.userId, 0)
            requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
        binding.emailEdt.setText(Singleton.userModel?.email)
        binding.emergencyEdt.setText(Singleton.userModel?.contact)
    }

}