
package com.example.minishop_manager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "minishop.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE panier (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                prix REAL NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        // Conserver les données existantes.
        // Ajouter ici les migrations nécessaires si le schéma évolue.
    }

    // Ajouter un produit au panier
    fun ajouterAuPanier(nom: String, prix: Double): Boolean {
        if (nom.isBlank() || prix < 0) return false

        val values = ContentValues().apply {
            put("nom", nom)
            put("prix", prix)
        }

        val result = writableDatabase.insert(
            "panier",
            null,
            values
        )

        return result != -1L
    }

    // Récupérer les produits du panier
    fun obtenirPanier(): List<String> {
        val liste = mutableListOf<String>()

        val cursor = readableDatabase.rawQuery(
            "SELECT nom, prix FROM panier ORDER BY id DESC",
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                val nom = it.getString(0)
                val prix = it.getDouble(1)

                liste.add(
                    "$nom - ${"%.2f".format(java.util.Locale.US, prix)} TND"
                )
            }
        }

        return liste
    }

    // Calculer le montant total
    fun calculerTotal(): Double {
        val cursor = readableDatabase.rawQuery(
            "SELECT COALESCE(SUM(prix), 0) FROM panier",
            null
        )

        cursor.use {
            return if (it.moveToFirst()) {
                it.getDouble(0)
            } else {
                0.0
            }
        }
    }

    // Vider le panier
    fun viderPanier() {
        writableDatabase.delete("panier", null, null)
    }

    // Supprimer un produit selon son identifiant
    fun supprimerProduit(id: Int): Boolean {
        val result = writableDatabase.delete(
            "panier",
            "id = ?",
            arrayOf(id.toString())
        )

        return result > 0
    }
}
