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

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val cbRememberMe = findViewById<CheckBox>(R.id.cbRememberMe)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        prefs = getSharedPreferences("MiniShopPrefs", MODE_PRIVATE)

        val savedUser = prefs.getString("USERNAME", "")
        val isRemembered = prefs.getBoolean("REMEMBER", false)

        etUsername.setText(savedUser)
        cbRememberMe.isChecked = isRemembered

        btnLogin.setOnClickListener {
            val user = etUsername.text.toString().trim()

            if (user.isEmpty()) {
                Toast.makeText(this, "Veuillez entrer votre nom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editor = prefs.edit()
            if (cbRememberMe.isChecked) {
                editor.putString("USERNAME", user)
                editor.putBoolean("REMEMBER", true)
            } else {
                editor.clear()
            }
            editor.apply()

            val intent = Intent(this, CatalogActivity::class.java)
            intent.putExtra("USER_NAME", user)
            startActivity(intent)
            finish()
        }
    }
}