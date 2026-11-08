package com.example.minishop_manager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "minishop.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE panier (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT,
                prix REAL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS panier")
        onCreate(db)
    }

    fun ajouterAuPanier(nom: String, prix: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nom", nom)
            put("prix", prix)
        }
        val result = db.insert("panier", null, values)
        db.close()
        return result != -1L
    }

    fun obtenirPanier(): List<String> {
        val liste = ArrayList<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT nom, prix FROM panier ORDER BY id DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val nom = cursor.getString(0)
                val prix = cursor.getDouble(1)
                liste.add("$nom - $prix TND")
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return liste
    }

    fun calculerTotal(): Double {
        var total = 0.0
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM(prix) FROM panier", null)
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        db.close()
        return total
    }

    fun viderPanier() {
        val db = writableDatabase
        db.execSQL("DELETE FROM panier")
        db.close()
    }
}