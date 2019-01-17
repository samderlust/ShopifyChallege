package com.samderlust.shopifychallege

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ProductAdapter(val pList: ListProduct): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator = LayoutInflater.from(parent?.context)
        val v = inflator.inflate(R.layout.product_row,parent,false)
        val productName = v.findViewById(R.id.productName) as TextView
        val productStock = v.findViewById(R.id.productStock) as TextView

        var num =0
        pList.products.get(position).variants.forEach { e -> num += e.inventory_quantity }


        productName.text=pList.products.get(position).title
        productStock.text = "Stock:  "+ num
        return v
    }

    override fun getItem(position: Int): Any {
        return pList.products.get(position)
    }

    override fun getItemId(position: Int): Long {
       return  position.toLong()
    }

    override fun getCount(): Int {
        return pList.products.size
    }
}