package com.samderlust.shopifychallege

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ColAdapter(val  cList: ShopCollection):BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflator = LayoutInflater.from(parent?.context)
        val view = inflator.inflate(R.layout.collection_row,parent,false)
        val collectionName = view.findViewById(R.id.collectionName) as TextView

        collectionName.text = cList.custom_collections.get(position).title

        return view
    }

    override fun getItem(position: Int): Any {
        return cList.custom_collections.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return cList.custom_collections.size
    }
}