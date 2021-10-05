package com.sign.deftpdf.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sign.deftpdf.R
import com.sign.deftpdf.model.notifications.NotificationData
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(private var mList: List<NotificationData>) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    interface OnDocumentClickListener {
        fun onDetailClick(position: Int)
        fun onItemClick(position: Int)
    }

    lateinit var listener: OnDocumentClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notifications, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentData = mList[position]

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        calendar.time = sdf.parse(documentData.updatedAt)

        sdf.timeZone = TimeZone.getDefault()
        val date = sdf.format(calendar.time)

        holder.documentName.text = documentData.notification
        holder.documentDate.text = date
    }

    fun setDocuments(list: List<NotificationData>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val documentName: TextView = itemView.findViewById(R.id.notification_name)
        val documentDate: TextView = itemView.findViewById(R.id.notification_date)
    }
}