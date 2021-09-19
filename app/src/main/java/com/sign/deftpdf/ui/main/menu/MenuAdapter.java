package com.sign.deftpdf.ui.main.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sign.deftpdf.R;

import java.util.ArrayList;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private Context _context;
    private static ClickListener clickListener;

    public ArrayList<Top> list;

    public MenuAdapter(ArrayList<Top> list, Context _context) {
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
        View view = inflater.inflate(R.layout.item_filters_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Top item = list.get(position);
        //  holder.setIsRecyclable(false);
        holder.display(item);

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        MenuAdapter.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private final TextView text_name_cat;
        private final ImageView image_cat;

        public MyViewHolder(final View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            text_name_cat = itemView.findViewById(R.id.text_discont_child_cat);
            image_cat = itemView.findViewById(R.id.image_cat);
        }

        public void display(Top currentItem) {
            if (currentItem != null) {
                text_name_cat.setText((currentItem.text == null) ? "" : currentItem.text);
                image_cat.setImageResource(currentItem.img_r);
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

