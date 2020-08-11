package com.arpit.newsapp2

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerAdapter(private val titles: List<String> ,private val details: List<String> , private val images: List<String>,
                      private val links: List<String> ):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val itemTitle:TextView = itemView.findViewById(R.id.tvTitle)
        val itemDetails:TextView = itemView.findViewById(R.id.tvDescription)
        val itemPicture: ImageView = itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener {
                val position :Int = adapterPosition
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(links[position])
                startActivity(itemView.context , intent , null)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent ,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
       return titles.size
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
       holder.itemTitle.text = titles[position]
        holder.itemDetails.text = details[position]
        Glide.with(holder.itemPicture).load(images[position]).into(holder.itemPicture)
    }
}