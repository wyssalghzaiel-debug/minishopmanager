
package com.example.minishop_manager

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("MiniShopPrefs", MODE_PRIVATE)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val cbRememberMe = findViewById<CheckBox>(R.id.cbRememberMe)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        etUsername.setText(prefs.getString("USERNAME", ""))
        cbRememberMe.isChecked = prefs.getBoolean("REMEMBER", false)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()

            if (username.isEmpty()) {
                etUsername.error = "Veuillez saisir votre nom"
                return@setOnClickListener
            }

            prefs.edit().apply {
                if (cbRememberMe.isChecked) {
                    putString("USERNAME", username)
                    putBoolean("REMEMBER", true)
                } else {
                    remove("USERNAME")
                    putBoolean("REMEMBER", false)
                }

                putString("CURRENT_USER", username)
                putBoolean("IS_LOGGED_IN", true)
                apply()
            }

            val intent = Intent(this, CatalogActivity::class.java)
            intent.putExtra("USER_NAME", username)
            startActivity(intent)
            finish()
        }
    }
}
