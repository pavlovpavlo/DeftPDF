package com.sign.deftpdf.ui.documents_screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sign.deftpdf.R
import com.sign.deftpdf.model.documents.DocumentsData
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DocumentsAdapter(private var mList: List<DocumentsData>) :
    RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {

    interface OnDocumentClickListener {
        fun onDetailClick(position: Int)
        fun onItemClick(position: Int)
    }

    lateinit var listener: OnDocumentClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_documents, parent, false)

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

        holder.documentName.text = documentData.originalName
        holder.documentDate.text = date
        holder.itemView.setOnClickListener { listener.onItemClick(position) }
        holder.documentDetail.setOnClickListener { listener.onDetailClick(position) }

        holder.documentImage.setImageResource(
            when (documentData.status) {
                "original" -> {
                    R.drawable.ic_document_original
                }
                "signed" -> {
                    R.drawable.ic_document_signed
                }
                "pending" -> {
                    R.drawable.ic_document_pending
                }
                "draft" -> {
                    R.drawable.ic_document_draft
                }
                else ->
                    R.drawable.ic_document_original
            }
        )
    }

    fun setDocuments(list: List<DocumentsData>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val documentImage: ImageView = itemView.findViewById(R.id.document_image)
        val documentDetail: ImageView = itemView.findViewById(R.id.document_detail)
        val documentName: TextView = itemView.findViewById(R.id.document_name)
        val documentDate: TextView = itemView.findViewById(R.id.document_date)
    }
}