
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
    private lateinit var productsList: ArrayList<Product>
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalog)
        val btnBack = findViewById<Button>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }
        dbHelper = DBHelper(this)

        val tvHeader = findViewById<TextView>(R.id.tvWelcomeHeader)
        val listView = findViewById<ListView>(R.id.listViewProducts)
        val btnGoToCart = findViewById<Button>(R.id.btnGoToCart)

        val prefs = getSharedPreferences("MiniShopPrefs", MODE_PRIVATE)

        val username = intent.getStringExtra("USER_NAME")
            ?: prefs.getString("CURRENT_USER", "Visiteur")
            ?: "Visiteur"

        tvHeader.text = "Bienvenue, $username !"

        productsList = chargerProduitsDepuisJSON()

        adapter = ProductAdapter(this, productsList) { product ->
            ouvrirDetailProduit(product)
        }

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            ouvrirDetailProduit(productsList[position])
        }

        btnGoToCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
    }

    private fun ouvrirDetailProduit(product: Product) {
        val intent = Intent(this, ProductDetailActivity::class.java)
        intent.putExtra("PRODUCT_EXTRA", product)
        startActivity(intent)
    }

    private fun chargerProduitsDepuisJSON(): ArrayList<Product> {
        val products = ArrayList<Product>()

        try {
            val jsonString = assets.open("produits.json")
                .bufferedReader()
                .use { it.readText() }

            val jsonObject = JSONObject(jsonString)
            val jsonArray = jsonObject.getJSONArray("produits")

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                products.add(
                    Product(
                        id = obj.getInt("id"),
                        nom = obj.getString("nom"),
                        categorie = obj.getString("categorie"),
                        description = obj.getString("description"),
                        prix = obj.getDouble("prix"),
                        imageName = obj.getString("image"),
                        rating = obj.optDouble("rating", 4.5)
                    )
                )
            }

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Erreur de chargement des produits",
                Toast.LENGTH_LONG
            ).show()
        }

        return products
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
