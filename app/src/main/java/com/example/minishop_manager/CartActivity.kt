package com.example.minishop_manager

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var listViewCart: ListView
    private lateinit var tvCartTotal: TextView
    private lateinit var adapter: ArrayAdapter<String>

    private var items = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        // Initialisation de la base de données
        dbHelper = DBHelper(this)

        // Récupération des composants XML
        listViewCart = findViewById(R.id.listViewCart)
        tvCartTotal = findViewById(R.id.tvCartTotal)

        val btnCheckout = findViewById<Button>(R.id.btnCheckout)
        val btnBack = findViewById<Button>(R.id.btnBack)

        // Initialisation de la liste
        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            items
        )

        listViewCart.adapter = adapter

        // Bouton retour
        btnBack.setOnClickListener {
            finish()
        }

        // Bouton passer la commande
        btnCheckout.setOnClickListener {
            if (items.isEmpty()) {
                Toast.makeText(
                    this,
                    "Votre panier est vide",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                startActivity(
                    Intent(this, CheckoutActivity::class.java)
                )
            }
        }

        // Charger les données du panier
        chargerPanier()
    }

    // Actualiser le panier lorsque l'écran devient visible
    override fun onResume() {
        super.onResume()

        if (::dbHelper.isInitialized && ::adapter.isInitialized) {
            chargerPanier()
        }
    }

    // Récupérer les produits et calculer le total
    private fun chargerPanier() {

        items.clear()
        items.addAll(dbHelper.obtenirPanier())

        adapter.notifyDataSetChanged()

        val total = dbHelper.calculerTotal()

        tvCartTotal.text = String.format(
            Locale.FRANCE,
            "Total : %.2f TND",
            total
        )

        // Désactiver la commande si le panier est vide
        val btnCheckout = findViewById<Button>(R.id.btnCheckout)
        btnCheckout.isEnabled = items.isNotEmpty()
        btnCheckout.alpha = if (items.isEmpty()) 0.5f else 1f
    }

    // Fermer la connexion lorsque l'activité est détruite
    override fun onDestroy() {
        if (::dbHelper.isInitialized) {
            dbHelper.close()
        }

        super.onDestroy()
    }
}
