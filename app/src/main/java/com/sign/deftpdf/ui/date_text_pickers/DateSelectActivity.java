package com.sign.deftpdf.ui.date_text_pickers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.lany192.picker.DatePicker;
import com.google.android.material.tabs.TabLayout;
import com.sign.deftpdf.R;
import com.sign.deftpdf.ui.draw.OnDrawSaveListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateSelectActivity extends AppCompatActivity implements View.OnClickListener {
    private ScrollView item_list_format;
    private TextView textFormat1;
    private TextView textFormat2;
    private TextView textFormat3;
    private TextView textFormat4;
    private TextView textFormat5;
    private TextView text_date_main;
    private TabLayout tableLayout;
    private ImageButton btn_close;
    private ImageButton btn_save;
    private ImageButton btn_open_k;
    public static OnDrawSaveListener listener;
    DatePicker datePicker1;
    SimpleDateFormat format;
    String stringDate;
    String curDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_date);
        initView();
    }

    private void initView() {
        btn_close=findViewById(R.id.imageButton);
        btn_save=findViewById(R.id.imageButton2);
        btn_open_k=findViewById(R.id.imageButton3);
        btn_open_k.setVisibility(View.GONE);
        datePicker1=findViewById(R.id.dateTimePicker);
        tableLayout= findViewById(R.id.tabLayout);
        item_list_format=findViewById(R.id.item_list_format);
        textFormat1 = findViewById(R.id.textFormat1);
        textFormat2 = findViewById(R.id.textFormat2);
        textFormat3 = findViewById(R.id.textFormat3);
        textFormat4 = findViewById(R.id.textFormat4);
        textFormat5 = findViewById(R.id.textFormat5);
        text_date_main = findViewById(R.id.text_date_main);
        text_date_main.setText(setDataTimeText(1,""));
        item_list_format.setVisibility(View.GONE);
        initListner();
    }
    private void initListner(){
        datePicker1.setDividerThickness(1);
        datePicker1.setDividerColor(Color.parseColor("#7C4DFF"));
        datePicker1.setTextColor(Color.parseColor("#BEBEBE"));
        datePicker1.setSelectedTextColor(Color.BLACK);
        datePicker1.setMinDate(0);
        datePicker1.setDayViewShown(true);
        datePicker1.setSelectedTextColor(Color.BLACK);

        datePicker1.setOnChangedListener((view, year, monthOfYear, dayOfMonth) ->
                {
                   int monthOfYearNew=monthOfYear+1;
                   curDate = dayOfMonth+"-"+monthOfYearNew+"-"+year;
                    text_date_main.setText(setDataTimeText(SingletonClassApp.getInstance().set_format
                        ,curDate));
                }) ;

        btn_close.setOnClickListener(view -> {
            finish();
        });


        btn_save.setOnClickListener(view -> {

            Bitmap bitmap = textAsBitmap(text_date_main.getText().toString(),
                    text_date_main.getTextSize(), text_date_main.getCurrentTextColor());
            listener.saveImageBitmap(bitmap);
            finish();
        });

        tableLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch(tab.getPosition()) {
                    case 0:{item_list_format.setVisibility(View.GONE);
                        datePicker1.setVisibility(View.VISIBLE);
                        break;}
                    case 1:{item_list_format.setVisibility(View.VISIBLE);
                     datePicker1.setVisibility(View.GONE);
                    break;}

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

    private String setDataTimeText(int setFormat, String format_picker) {
        Date currentTime = null;
        if (TextUtils.isEmpty(format_picker)){
         currentTime = Calendar.getInstance().getTime();}else{

            String dtStart = format_picker;
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                currentTime = format.parse(dtStart);

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        String setText = "";
        format = new SimpleDateFormat("dd/MM/yyyy");
        stringDate = format.format(currentTime);
        textFormat1.setText(stringDate);
        if (setFormat == 1) {
            setText = stringDate;
        }
        ;
        format = new SimpleDateFormat("MM/dd/yyyy");
        stringDate = format.format(currentTime);
        textFormat2.setText(stringDate);
        if (setFormat == 2) {
            setText = stringDate;
        }
        ;
        format = new SimpleDateFormat("d MMM yyyy");
        stringDate = format.format(currentTime);
        textFormat3.setText(stringDate);
        if (setFormat == 3) {
            setText = stringDate;
        }
        ;
        format = new SimpleDateFormat("EEE, d MMM yyyy");
        stringDate = format.format(currentTime);
        textFormat4.setText(stringDate);
        if (setFormat == 4) {
            setText = stringDate;
        }
        ;
        format = new SimpleDateFormat("dd.MM.yyyy");
        stringDate = format.format(currentTime);
        textFormat5.setText(stringDate);
        if (setFormat == 5) {
            setText = stringDate;
        }
        ;
        return setText;
    }

     private void setColor(String color){
        text_date_main.setTextColor(Color.parseColor(color));
     }

    private void resetColorItem() {
        textFormat1.setTextColor(Color.parseColor("#757575"));
        textFormat2.setTextColor(Color.parseColor("#757575"));
        textFormat3.setTextColor(Color.parseColor("#757575"));
        textFormat4.setTextColor(Color.parseColor("#757575"));
        textFormat5.setTextColor(Color.parseColor("#757575"));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textFormat1: {
                resetColorItem();
                textFormat1.setTextColor(Color.parseColor("#7C4DFF"));
                text_date_main.setText(setDataTimeText(1,curDate));
                SingletonClassApp.getInstance().set_format=1;
                break;
            }
            case R.id.textFormat2: {
                resetColorItem();
                textFormat2.setTextColor(Color.parseColor("#7C4DFF"));
                text_date_main.setText(setDataTimeText(2,curDate));
                SingletonClassApp.getInstance().set_format=2;
                break;
            }
            case R.id.textFormat3: {
                resetColorItem();
                textFormat3.setTextColor(Color.parseColor("#7C4DFF"));
                text_date_main.setText(setDataTimeText(3,curDate));
                SingletonClassApp.getInstance().set_format=3;
                break;
            }
            case R.id.textFormat4: {
                resetColorItem();
                textFormat4.setTextColor(Color.parseColor("#7C4DFF"));
                text_date_main.setText(setDataTimeText(4,curDate));
                SingletonClassApp.getInstance().set_format=4;
                break;
            }
            case R.id.textFormat5: {
                resetColorItem();
                textFormat5.setTextColor(Color.parseColor("#7C4DFF"));
                text_date_main.setText(setDataTimeText(5,curDate));
                SingletonClassApp.getInstance().set_format=5;
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
}