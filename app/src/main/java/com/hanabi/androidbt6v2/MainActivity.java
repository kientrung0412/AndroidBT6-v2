package com.hanabi.androidbt6v2;

import androidx.appcompat.app.AppCompatActivity;


import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TypedArray arrIdImg;
    private String nameImg, result = "";
    private ImageView imgvPlay;
    private Random random = new Random();
    private int idImg = -1, score, heart, count;
    private TextView tvScore, tvHeart;
    private GridLayout glSelect, glShows;
    private int[] arrIdBtnShow;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        //mapping đến view
        arrIdImg = getResources().obtainTypedArray(R.array.images); // xem file values/arrays để hiểu
        tvHeart = findViewById(R.id.tv_heart);
        tvScore = findViewById(R.id.tv_score);
        glShows = findViewById(R.id.gr_btn_show);
        glSelect = findViewById(R.id.gr_btn_select);
        imgvPlay = findViewById(R.id.imgv_play);
        btnContinue = findViewById(R.id.btn_continue);
        btnContinue.setOnClickListener(this);

        reStartGame();

    }

    private void continueGame() {
        count = 0;
        result = "";
        glSelect.removeAllViews();
        glShows.removeAllViews();
        btnContinue.setVisibility(View.INVISIBLE);
        createPlay();
    }

    private void reStartGame() {
        heart = 5;
        score = 0;
        tvScore.setText(score + "");
        tvHeart.setText(heart + "");
        continueGame();
    }

    private void setStyleTvSelect(int i, ArrayList<String> chars) {
        TextView textView = (TextView) getLayoutInflater().inflate(R.layout.text_view_item, null);// sử dụng text_view_item.xml làm style
        if (i < nameImg.length()) {
            textView.setText(chars.get(i));
        } else {
            char character = (char) (random.nextInt(25) + 97);
            textView.setText(character + "");
        }
        textView.setOnClickListener(this);
        //Thêm nút
        glSelect.addView(textView);
    }

    private void setStyleTvShow(int i) {
        //tạo nút mới và xác định vị trí thuộc vào
        TextView textView = (TextView) getLayoutInflater().inflate(R.layout.text_view_item, null);
        textView.setId(9999900 + i);
        arrIdBtnShow[i] = textView.getId();
        //Thêm nút
        glShows.addView(textView);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_continue:
                continueGame();
                break;
            default:
                if (count < nameImg.length()) {
                    TextView textView = (TextView) view;
                    textView.setVisibility(View.INVISIBLE);
                    String character = textView.getText().toString();
                    result += character;
                    ((TextView) findViewById(arrIdBtnShow[count])).setText(character);
                    count++;
                    //kiem tra thang thua
                    if (count == nameImg.length()) {
                        isWin();
                    }
                }
                break;
        }
    }

    private void isWin() {
        int idBg;
        if (nameImg.equalsIgnoreCase(result)) {
            idBg = R.drawable.ic_tile_true;
            score += 100;
            tvScore.setText(score + "");
        } else {
            idBg = R.drawable.ic_tile_false;
            heart--;
            if (heart < 0) {
                Toast.makeText(this, "Thua", Toast.LENGTH_SHORT).show();
                return;
            }
            tvHeart.setText(heart + "");
        }
        for (int id : arrIdBtnShow) {
            findViewById(id).setBackgroundResource(idBg);
        }
        btnContinue.setVisibility(View.VISIBLE);
    }

    private void createPlay() {
        int id = random.nextInt(arrIdImg.length());
        while (idImg == id) {
            id = random.nextInt(arrIdImg.length());
        }
        idImg = id;
        idImg = arrIdImg.getResourceId(idImg, -1);

        //set anh
        Glide.with(this).load(idImg).into(imgvPlay);
        //lấy tên ảnh
        nameImg = getResources().getResourceEntryName(idImg);
        //String to ArrayList
        String[] charArray = nameImg.split("");
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(charArray));
        chars.remove(0);
        // sort ramdom
        Collections.shuffle(chars);
        //thiết lập số ô điền
        arrIdBtnShow = new int[nameImg.length()];


        //Hiển thị số ô chữ
        for (int i = 0; i < nameImg.length(); i++) {
            setStyleTvShow(i);
        }
        //Hiển thị ô trọn chữ
        for (int i = 0; i < 12; i++) {
            setStyleTvSelect(i, chars);
        }
    }

    @Override
    public void onBackPressed() {
        reStartGame();
    }
}