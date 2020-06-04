package com.elbaz.eliran.rappeltout.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.elbaz.eliran.rappeltout.R
import com.elbaz.eliran.rappeltout.model.Reminder

/**
 * Created by Eliran Elbaz on 01-May-20.
 */
class ReminderListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<ReminderListAdapter.ReminderViewHolder>(){

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var reminders = emptyList<Reminder>() // Cached copy of reminders

    inner class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val titleView: TextView = itemView.findViewById(R.id.item_title_text)
        val contentView: TextView = itemView.findViewById(R.id.item_content_text)
        val startTime: TextView = itemView.findViewById(R.id.start_time)
        val endTime: TextView = itemView.findViewById(R.id.end_time)
        val bellIcon: ImageView = itemView.findViewById(R.id.item_bell_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val itemView = inflater.inflate(R.layout.reminder_recycler_item, parent, false)
        return ReminderViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        val current = reminders[position]
        holder.titleView.text = current.title
        holder.contentView.text = current.content
        holder.startTime.text = current.startTime
        holder.endTime.text = current.endTime
        println("current is active? ${current.isActive!!}")
        when (current.isActive!!) {
        true -> holder.bellIcon.visibility = View.VISIBLE
        else -> holder.bellIcon.visibility = View.INVISIBLE
        }
    }

    internal fun setReminder(reminders : List<Reminder>){
        this.reminders = reminders
        notifyDataSetChanged()
    }


    override fun getItemCount() = reminders.size

}

