package com.sign.deftpdf.ui.date_text_pickers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sign.deftpdf.R;

import java.util.ArrayList;

public class History_Adapter extends RecyclerView.Adapter<History_Adapter.MyViewHolder> {

    private Context _context;
    private static ClickListener clickListener;

    public ArrayList<String> list;

    public History_Adapter(ArrayList<String> list, Context _context) {
        this.list = list;
        this._context = _context;

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_h_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String item = list.get(position);
        //  holder.setIsRecyclable(false);
        holder.display(item);

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        History_Adapter.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private final TextView text_name;

        public MyViewHolder(final View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.textView);
            text_name.setOnClickListener(this);
        }

        public void display(String currentItem) {
            if (currentItem != null) {
                text_name.setText((currentItem == null) ? "" : currentItem);
            }
        }

        @Override
        public void onClick(View v) {

            clickListener.onItemClick(getAdapterPosition(), v);

        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View v);
    }

}