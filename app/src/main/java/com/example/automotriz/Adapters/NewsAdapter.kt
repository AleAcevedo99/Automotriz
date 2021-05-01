package com.example.automotriz.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.automotriz.Constants.Constants
import com.example.automotriz.DetailNewsActivity
import com.example.automotriz.Entity.EntityNews
import com.example.automotriz.R
import com.example.automotriz.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso

class NewsAdapter(var newsList:ArrayList<EntityNews>, val context: Context): RecyclerView.Adapter<NewsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val inflater = LayoutInflater.from(parent.context)
        return NewsHolder(inflater.inflate(R.layout.item_news, parent, false))
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {

        holder.txtTitle.text = "${newsList[position].title}"
        holder.txtDate.text = "${newsList[position].date}"
        Picasso.get().load(newsList[position].imageUrl)
                .placeholder(R.drawable.imgplaceholder)
                .error(R.drawable.imgplaceholder)
                .into(holder.imgNews)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailNewsActivity::class.java).apply{
                putExtra(Constants.ID_NEW, newsList[position].id)
            }
            context.startActivity(intent)
        }


    }



}

class NewsHolder(view: View): RecyclerView.ViewHolder(view){
    val binding = ItemNewsBinding.bind(view)

    val imgNews = binding.imgNews
    val txtTitle = binding.txtTitle
    val txtDate = binding.txtDate
}