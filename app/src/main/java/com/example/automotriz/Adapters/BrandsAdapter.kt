package com.example.automotriz.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.automotriz.Constants.Constants
import com.example.automotriz.DetailNewsActivity
import com.example.automotriz.Entity.EntityBrands
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.R
import com.example.automotriz.databinding.ItemBrandsBinding
import com.example.automotriz.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso

class BrandsAdapter(var brandsList:ArrayList<EntityBrands>, val context: Context): RecyclerView.Adapter<BrandsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandsHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BrandsHolder(inflater.inflate(R.layout.item_brands, parent, false))
    }

    override fun getItemCount(): Int {
        return brandsList.size
    }

    override fun onBindViewHolder(holder: BrandsHolder, position: Int) {

        holder.chkBrand.setText(brandsList[position].brand)

        holder.chkBrand.setOnCheckedChangeListener { _, isChecked ->
            

        }
    }
}

class BrandsHolder(view: View): RecyclerView.ViewHolder(view){
    val binding = ItemBrandsBinding.bind(view)

    val chkBrand = binding.chkBrand

}