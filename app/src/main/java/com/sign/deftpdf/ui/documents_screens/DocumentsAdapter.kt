package com.sign.deftpdf.ui.documents_screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sign.deftpdf.R
import com.sign.deftpdf.model.documents.DocumentData

class DocumentsAdapter (private var mList: List<DocumentData>) : RecyclerView.Adapter<DocumentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_documents, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val documentData = mList[position]

        holder.documentName.text = documentData.originalName
        holder.documentDate.text = documentData.createdAt

    }

    fun setDocuments(list :List<DocumentData>){
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