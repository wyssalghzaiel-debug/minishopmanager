
package com.example.minishop_manager

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        // Initialisation de la base de données
        dbHelper = DBHelper(this)

        // Récupération du produit sélectionné
        @Suppress("DEPRECATION")
        val receivedProduct =
            intent.getSerializableExtra("PRODUCT_EXTRA") as? Product

        if (receivedProduct == null) {
            Toast.makeText(
                this,
                "Produit introuvable",
                Toast.LENGTH_SHORT
            ).show()
            finish()
            return
        }

        product = receivedProduct

        // Récupération des composants XML
        val imgProduct = findViewById<ImageView>(R.id.imgProductDetail)
        val tvCategory = findViewById<TextView>(R.id.tvDetailCategory)
        val tvName = findViewById<TextView>(R.id.tvDetailTitle)
        val tvPrice = findViewById<TextView>(R.id.tvDetailPrice)
        val tvDescription =
            findViewById<TextView>(R.id.tvDetailDescription)

        val btnAddToCart =
            findViewById<Button>(R.id.btnAddToCartDetail)

        // Affichage des informations du produit
        tvCategory.text = product.categorie
        tvName.text = product.nom
        tvPrice.text = String.format("%.2f TND", product.prix)
        tvDescription.text = product.description

        // Affichage de l'image
        val imageId = resources.getIdentifier(
            product.imageName,
            "drawable",
            packageName
        )

        if (imageId != 0) {
            imgProduct.setImageResource(imageId)
        } else {
            imgProduct.setImageResource(R.drawable.logo_shop)
        }

        // Bouton Retour
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Ajouter le produit au panier SQLite
        btnAddToCart.setOnClickListener {

            val success = dbHelper.ajouterAuPanier(
                product.nom,
                product.prix
            )

            if (success) {
                Toast.makeText(
                    this,
                    "${product.nom} ajouté au panier",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Erreur lors de l'ajout du produit",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }
}
