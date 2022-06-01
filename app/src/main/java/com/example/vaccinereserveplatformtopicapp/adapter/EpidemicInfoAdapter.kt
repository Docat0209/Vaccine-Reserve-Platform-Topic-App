package com.example.vaccinereserveplatformtopicapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.vaccinereserveplatformtopicapp.MainActivity
import com.example.vaccinereserveplatformtopicapp.R

import com.example.vaccinereserveplatformtopicapp.databinding.ListItemEpidemicInfoBinding
import com.example.vaccinereserveplatformtopicapp.model.EpidemicInfo
import com.example.vaccinereserveplatformtopicapp.ui.news.NewsActivity


class EpidemicInfoAdapter(val context: Context, private val epidemicInfoList :MutableList<EpidemicInfo>): RecyclerView.Adapter<EpidemicInfoAdapter.ViewHolder>() {
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val binding = ListItemEpidemicInfoBinding.bind(view)
        @SuppressLint("SetTextI18n")
        fun bind(epidemicInfo: EpidemicInfo){
            binding.apply {
                itemDate.text = epidemicInfo.time.split("-")[0]+"/"+epidemicInfo.time.split("-")[1]
                itemDay.text = epidemicInfo.time.split("-")[2]
                itemTitle.text = epidemicInfo.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_epidemic_info , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(epidemicInfoList[position])
        holder.binding.item.setOnClickListener{
            val intent = Intent(context , NewsActivity::class.java)
            intent.putExtra("title",epidemicInfoList[position].title)
            intent.putExtra("text",epidemicInfoList[position].text)
            startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {
        return epidemicInfoList.count()
    }

}