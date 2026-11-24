
package com.example.minishop_manager

import java.io.Serializable

data class Product(
    val id: Int,
    val nom: String,
    val categorie: String,
    val description: String,
    val prix: Double,
    val imageName: String,
    val rating: Double = 4.5
) : Serializable
