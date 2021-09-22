package com.sign.deftpdf.ui.help

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sign.deftpdf.R
import com.sign.deftpdf.model.faq.FaqData

class HelpAdapter(private var mList: MutableList<FaqData>) : RecyclerView.Adapter<HelpAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_help, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = mList[position]

        val expanded: Boolean = data.expanded
        if (expanded) holder.subItem.animate()
                .alpha(1.0f)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        holder.subItem.visibility = View.VISIBLE
                    }
                }) else holder.subItem.animate()
                .alpha(0.0f)
                .setDuration(200)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        holder.subItem.visibility = View.GONE
                    }
                })
        holder.iconDraw.setImageDrawable(
                if (expanded) holder.itemView.resources.getDrawable(R.drawable.ic_arrow_close)
                else holder.itemView.resources.getDrawable(R.drawable.ic_arrow_open))
        holder.text.text = data.answer
        holder.title.text = data.question
        holder.container.setOnClickListener(View.OnClickListener {
            val expanded1: Boolean = data.expanded
            data.expanded = !expanded1
            notifyItemChanged(position)
        })
    }

    fun setData(list: MutableList<FaqData>) {
        mList = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val subItem: LinearLayout = itemView.findViewById(R.id.sub_item)
        val container: LinearLayout = itemView.findViewById(R.id.question_container)
        val title: TextView = itemView.findViewById(R.id.item_title)
        val text: TextView = itemView.findViewById(R.id.sub_item_text)
        val iconDraw: ImageView = itemView.findViewById(R.id.icon_draw)
    }
}