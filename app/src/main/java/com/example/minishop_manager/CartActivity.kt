package com.example.minishop_manager

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val listViewCart = findViewById<ListView>(R.id.listViewCart)
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)
        val dbHelper = DBHelper(this)

        val items = dbHelper.obtenirPanier()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listViewCart.adapter = adapter

        btnCheckout.setOnClickListener {
            if (items.isEmpty()) {
                Toast.makeText(this, "Votre panier est vide !", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            }
        }
    }
}