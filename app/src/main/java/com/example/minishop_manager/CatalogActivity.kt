package com.example.minishop_manager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class CatalogActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)

        dbHelper = DBHelper(this)

        val tvHeader = findViewById<TextView>(R.id.tvWelcomeHeader)
        val listView = findViewById<ListView>(R.id.listViewProducts)
        val btnGoToCart = findViewById<Button>(R.id.btnGoToCart)

        val username = intent.getStringExtra("USER_NAME") ?: "Utilisateur"
        tvHeader.text = "Bienvenue, $username !"

        val productsList = chargerProduitsDepuisJSON()
        val adapter = ProductAdapter(this, productsList) { product ->
            sauvegarderEtNotifier(product)
        }
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val product = productsList[position]
            sauvegarderEtNotifier(product)
        }

        btnGoToCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun sauvegarderEtNotifier(product: Product) {
        val inserer = dbHelper.ajouterAuPanier(product.nom, product.prix)
        if (inserer) {
            Toast.makeText(this, "Produit sélectionné : ${product.nom}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show()
        }
    }

    private fun chargerProduitsDepuisJSON(): List<Product> {
        val products = ArrayList<Product>()
        try {
            val jsonString = assets.open("produits.json").bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("produits")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                products.add(
                    Product(
                        id = obj.getInt("id"),
                        nom = obj.getString("nom"),
                        description = obj.getString("description"),
                        prix = obj.getDouble("prix"),
                        imageName = obj.getString("image")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return products
    }
}