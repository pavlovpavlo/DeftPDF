package com.sign.deftpdf;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class adapter_menu extends RecyclerView.Adapter<adapter_menu.MyViewHolder> {

    private Context _context;
    private static ClickListener clickListener;

    public ArrayList<Top> list;

    public adapter_menu(ArrayList<Top> list, Context _context) {
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
        adapter_menu.clickListener = clickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {


        private final TextView text_name_cat;
        private final ImageView image_cat;
        //private final TextView text_discription;
        //  private final TextView text_discont;
//        private final TextView time_work_shops;
//        private final TextView phone_shops;
        // private final ImageView img_cat;

        public MyViewHolder(final View itemView) {
            super(itemView);

               itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);

            //     text_discont= itemView.findViewById(R.id.text_discont);
            text_name_cat = itemView.findViewById(R.id.text_discont_child_cat);
            image_cat= itemView.findViewById(R.id.image_cat);
            //     text_discription= itemView.findViewById(R.id.text_discription);;
            //img_cat=itemView.findViewById(R.id.img_cat);;
     //       text_name_cat.setOnClickListener(this);
            //            time_work_shops= itemView.findViewById(R.id.time_work_shops);;
//            phone_shops= itemView.findViewById(R.id.phone_shops);;
            //           brend_logo= itemView.findViewById(R.id.image_brand);

        }

        public void display(Top currentItem) {
            if (currentItem != null) {
                String url_brend_img = "https://static.bbs-t32se7fxe6vsup5z.com/content-image/200xAUTO/";
                Log.d("TEG-ADAPTER",currentItem.text);
                text_name_cat.setText((currentItem.text == null) ? "" : currentItem.text);
                image_cat.setImageResource(currentItem.img_r);
//                if (SingletonClassApp.getInstance().set_brand.containsKey(currentItem.slug)){
//                    text_name_cat.setChecked(true);
//                }else {
//                    text_name_cat.setChecked(false);
//                }


                //               try {
//                   text_discription.setText((currentItem.getAdditionalConfig().getText() == null) ? "" : currentItem.getAdditionalConfig().getText());
//               }catch (NullPointerException e){
//                   text_discription.setText("");
//               }
//                if (currentItem.getShowDiscountLabel()) {
//                    text_discont.setVisibility(View.VISIBLE);
//                    try {
//                        text_discont.setText((currentItem.getAdditionalConfig().getDiscount() == null) ? "" : currentItem.getAdditionalConfig().getDiscount());
//                    }catch (NullPointerException e){
//                        text_discont.setVisibility(View.GONE);
//                    }
//                }
//                else{text_discont.setVisibility(View.GONE);}

//             Picasso.get()
//            .load(url_brend_img+currentItem.getMenuImage())
//            .resize(0, (int)80)
//            .into(img_cat);


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

