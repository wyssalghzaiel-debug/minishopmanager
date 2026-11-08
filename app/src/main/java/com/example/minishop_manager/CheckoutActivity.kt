package com.example.minishop_manager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CheckoutActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        dbHelper = DBHelper(this)

        val tvTotalAmount = findViewById<TextView>(R.id.tvTotalAmount)
        val etAddress = findViewById<EditText>(R.id.etAddress)
        val etCardNumber = findViewById<EditText>(R.id.etCardNumber)
        val btnConfirm = findViewById<Button>(R.id.btnConfirmPayment)

        val total = dbHelper.calculerTotal()
        tvTotalAmount.text = String.format("Total à payer : %.2f TND", total)

        btnConfirm.setOnClickListener {
            val address = etAddress.text.toString().trim()
            val card = etCardNumber.text.toString().trim()

            if (address.isEmpty() || card.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            dbHelper.viderPanier()
            Toast.makeText(this, "Commande confirmée avec succès !", Toast.LENGTH_LONG).show()

            val intent = Intent(this, CatalogActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}