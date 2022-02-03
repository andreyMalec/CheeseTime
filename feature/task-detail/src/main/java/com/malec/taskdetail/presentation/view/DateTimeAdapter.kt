package com.malec.taskdetail.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malec.taskdetail.R

class DateTimeAdapter(private val data: List<String>) :
    RecyclerView.Adapter<DateTimeAdapter.DateTimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateTimeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.item_text, parent, false)
        return DateTimeViewHolder(v)
    }

    override fun onBindViewHolder(holder: DateTimeViewHolder, position: Int) {
        val task = data[position]

        holder.text.text = task
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class DateTimeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view as TextView
    }
}