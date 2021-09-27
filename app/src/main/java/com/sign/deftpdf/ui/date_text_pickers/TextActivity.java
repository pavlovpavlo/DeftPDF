package com.sign.deftpdf.ui.date_text_pickers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.sign.deftpdf.DeftApp;
import com.sign.deftpdf.R;
import com.sign.deftpdf.ui.draw.OnDrawSaveListener;

import java.util.ArrayList;

public class TextActivity extends AppCompatActivity implements View.OnClickListener {
    private ScrollView item_list_format;
    private TextView textFormat1;
    private TextView textFormat2;
    private TextView textFormat3;
    private TextView textFormat4;
    private TextView textTitle;
    private EditText text_edit;
    private RecyclerView history_list;
    private TabLayout tableLayout;
    private ImageButton btn_close;
    private ImageButton btn_save;
    private ImageButton btn_open_k;
    private History_Adapter adapter;
    private TinyDB tinydb;
    SharedPreferences shref;
    ArrayList<String> ModelArrayList = new ArrayList();
    public static OnDrawSaveListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        tinydb = new TinyDB(this);
        initView();
        shref = getSharedPreferences("BD_PDF", MODE_PRIVATE);
        getBD();

    }

    private void initView() {
        history_list = findViewById(R.id.history_list);
        history_list.setVisibility(View.GONE);
        textTitle = findViewById(R.id.textTitle);
        textTitle.setText("Text");
        btn_close = findViewById(R.id.imageButton);
        btn_save = findViewById(R.id.imageButton2);
        btn_open_k = findViewById(R.id.imageButton3);
        btn_open_k.setVisibility(View.VISIBLE);
        tableLayout = findViewById(R.id.tabLayout);
        item_list_format = findViewById(R.id.item_list_format);
        item_list_format.setVisibility(View.VISIBLE);
        textFormat1 = findViewById(R.id.textFormat1);
        textFormat2 = findViewById(R.id.textFormat2);
        textFormat3 = findViewById(R.id.textFormat3);
        textFormat4 = findViewById(R.id.textFormat4);
        text_edit = findViewById(R.id.editText);

        initListner();

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        history_list.setLayoutManager(mLayoutManager);
        adapter = new History_Adapter((ArrayList<String>) ModelArrayList, this);
        history_list.setAdapter(adapter);
        adapter.setOnItemClickListener(OnResultItemClick);
    }

    private History_Adapter.ClickListener OnResultItemClick = new History_Adapter.ClickListener() {
        @Override
        public void onItemClick(int position, View v) {
            if (!ModelArrayList.get(position).equals("No Text History")) {
                text_edit.setText(ModelArrayList.get(position));
            }
        }

        @Override
        public void onItemLongClick(int position, View v) {
        }
    };

    public Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    private void initListner() {
        btn_close.setOnClickListener(view -> {
            finish();
        });
        btn_save.setOnClickListener(view -> {
            ModelArrayList.add(text_edit.getText().toString());
            saveTextBd();
            Bitmap bitmap = textAsBitmap(text_edit.getText().toString(),
                    text_edit.getTextSize(), text_edit.getCurrentTextColor());
            listener.saveImageBitmap(bitmap);
            finish();
        });

        btn_open_k.setOnClickListener(v -> {
            hideKeyboard(this);
        });


        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: {
                        item_list_format.setVisibility(View.VISIBLE);
                        history_list.setVisibility(View.GONE);
                        break;
                    }
                    case 1: {
                        item_list_format.setVisibility(View.GONE);
                        history_list.setVisibility(View.VISIBLE);
                        getBD();
                        break;
                    }

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setColor(String color) {
        text_edit.setTextColor(Color.parseColor(color));
    }

    private void resetColorItem() {
        textFormat1.setTextColor(Color.parseColor("#757575"));
        textFormat2.setTextColor(Color.parseColor("#757575"));
        textFormat3.setTextColor(Color.parseColor("#757575"));
        textFormat4.setTextColor(Color.parseColor("#757575"));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textFormat1: {
                resetColorItem();
                textFormat1.setTextColor(Color.parseColor("#7C4DFF"));
                //SingletonClassApp.getInstance().set_format = 1;
                text_edit.setText(DeftApp.user.getName());



                break;
            }
            case R.id.textFormat2: {
                resetColorItem();
                textFormat2.setTextColor(Color.parseColor("#7C4DFF"));
                //      SingletonClassApp.getInstance().set_format=2;
                text_edit.setText(DeftApp.user.getName());
                break;
            }
            case R.id.textFormat3: {
                resetColorItem();
                textFormat3.setTextColor(Color.parseColor("#7C4DFF"));
                //     SingletonClassApp.getInstance().set_format=3;
                text_edit.setText(DeftApp.user.getEmail());
                break;
            }
            case R.id.textFormat4: {
                resetColorItem();
                textFormat4.setTextColor(Color.parseColor("#7C4DFF"));
                //     SingletonClassApp.getInstance().set_format=4;
                text_edit.setText(DeftApp.user.getRole());
                break;
            }

            case R.id.imageButtonColor1: {
                setColor("#323232");
                break;
            }
            case R.id.imageButtonColor2: {
                setColor("#7C4DFF");
                break;
            }
            case R.id.imageButtonColor3: {
                setColor("#EE4242");
                break;
            }
        }
    }

    private void getBD() {
        ModelArrayList = new ArrayList<>();
        tinydb.getListString("history");
        Log.d("bd", "" + tinydb.getListString("history").size());
        ModelArrayList.addAll(tinydb.getListString("history"));
        Log.d("bd", "" + ModelArrayList.size());
        if (ModelArrayList.size() == 0) {
            ModelArrayList.add("No Text History");
        }
        adapter = new History_Adapter((ArrayList<String>) ModelArrayList, this);
        history_list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void saveTextBd() {
        tinydb.putListString("history", ModelArrayList);
    }


}