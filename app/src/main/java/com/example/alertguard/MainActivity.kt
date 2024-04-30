package com.example.alertguard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.alertguard.databinding.ActivityMainBinding
import com.example.alertguard.db.AppDatabase
import com.example.alertguard.fragments.HistoryFragment
import com.example.alertguard.fragments.HomeFragment
import com.example.alertguard.fragments.SettingsFragment
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Singleton.userModel == null) {
            val userID = SharedPreferences.getInt(Singleton.userId)
            val thread = Thread {
                Singleton.userModel = AppDatabase.getDatabase().userModelDao().getUserById(userID)
            }
            thread.start()
            thread.join()
        }

        showFragment(HomeFragment())

        binding.bottomNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    showFragment(HomeFragment())
                }

                R.id.settingsFragment -> {
                    showFragment(SettingsFragment())
                }

                R.id.historyFragment -> {
                    showFragment(HistoryFragment())
                }
            }
            true
        })
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.fragmentContainer.id, fragment)
            commit()
        }
    }
}