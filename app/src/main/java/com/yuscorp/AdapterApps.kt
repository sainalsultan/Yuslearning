package com.yuscorp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuscorp.data.AppsInstalled

class AdapterApps : RecyclerView.Adapter<AdapterApps.ViewHolder>(){

    private var list : MutableList<AppsInstalled>? = mutableListOf()

    fun setData(list : MutableList<AppsInstalled>?) {
        this.list?.addAll(list!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterApps.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appss, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterApps.ViewHolder, position: Int) {
        list?.get(position).apply {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

    inner class ViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview){
        private val imageView : ImageView = itemview.findViewById(R.id.imageView)
        private val text1 : TextView = itemview.findViewById(R.id.text1)

        fun bind(appsInstalled: AppsInstalled?) {
            imageView.setImageDrawable(appsInstalled?.icon)
            text1.text = appsInstalled?.name
        }

    }
}