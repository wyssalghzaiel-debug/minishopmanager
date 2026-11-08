package com.example.minishop_manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ProductAdapter(
    private val context: Context,
    private val products: List<Product>,
    private val onItemBuyClick: (Product) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = products.size
    override fun getItem(position: Int): Any = products[position]
    override fun getItemId(position: Int): Long = products[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)
        val product = products[position]

        val imgProduct = view.findViewById<ImageView>(R.id.imgProduct)
        val tvName = view.findViewById<TextView>(R.id.tvProductName)
        val tvDesc = view.findViewById<TextView>(R.id.tvProductDesc)
        val tvPrice = view.findViewById<TextView>(R.id.tvProductPrice)
        val btnAdd = view.findViewById<Button>(R.id.btnAddToCart)

        tvName.text = product.nom
        tvDesc.text = product.description
        tvPrice.text = String.format("%.2f TND", product.prix)

        val resId = context.resources.getIdentifier(product.imageName, "drawable", context.packageName)
        if (resId != 0) {
            imgProduct.setImageResource(resId)
        } else {
            imgProduct.setImageResource(R.drawable.logo_shop)
        }

        btnAdd.setOnClickListener {
            onItemBuyClick(product)
        }

        return view
    }
}