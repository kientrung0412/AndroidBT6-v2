package com.hanabi.androidbt6v2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int[] arrIdImg = new int[]{
            R.drawable.aomua, R.drawable.baocao, R.drawable.xedapdien,
            R.drawable.xakep, R.drawable.vuonbachthu, R.drawable.xaphong,
            R.drawable.vuaphaluoi, R.drawable.tranhthu, R.drawable.totien,
            R.drawable.tohoai, R.drawable.tichphan, R.drawable.thothe
    };

    private String nameImg, result = "";
    private ImageView imgvPlay;
    private Random random = new Random();
    private int idImg, score, heart = 5, count;
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
        //Lấy ảnh ngẫu nhiên
        createPlay();

        //mapping đến view
        tvHeart = findViewById(R.id.tv_heart);
        tvScore = findViewById(R.id.tv_score);
        tvScore.setText(score + "");
        tvHeart.setText(heart + "");
        glShows = findViewById(R.id.gr_btn_show);
        glSelect = findViewById(R.id.gr_btn_select);
        imgvPlay = findViewById(R.id.imgv_play);
        btnContinue = findViewById(R.id.btn_continue);

        idImg = arrIdImg[idImg];

        //set ảnh
        imgvPlay.setImageResource(idImg);
        //lấy tên ảnh
        nameImg = this.getResources().getResourceEntryName(idImg);
        arrIdBtnShow = new int[nameImg.length()];
        //Hiển thị số ô chữ
        for (int i = 0; i < nameImg.length(); i++) {
            setStyleTwShow(i);
        }

        //String to ArrayList
        String[] charArray = nameImg.split("");
        ArrayList<String> chars = new ArrayList<>(Arrays.asList(charArray));
        chars.remove(0);
        Collections.shuffle(chars);
        chars = chars;
        //Hiển thị ô trọn chữa
        for (int i = 0; i < 12; i++) {
            setStyleTwSelect(i, chars);
        }
        btnContinue.setVisibility(View.INVISIBLE);
        btnContinue.setOnClickListener(this);

    }

    private void continueGame() {
        count = 0;
        result = "";
        glSelect.removeAllViews();
        glShows.removeAllViews();
        initViews();
    }

    private void reStartGame() {
        heart = 5;
        score = 0;
        continueGame();
    }

    private void setStyleTwSelect(int i, ArrayList<String> chars) {
        TextView textView = new TextView(glSelect.getContext());
        textView.setBackgroundResource(R.drawable.ic_tile_hover);
        if (i < nameImg.length()) {
            textView.setText(chars.get(i));
        } else {
            char character = (char) (random.nextInt(25) + 97);
            textView.setText(character + "");
        }
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(24);
        textView.setTextColor(Color.WHITE);
        textView.setOnClickListener(this);
        //Thêm nút
        glSelect.addView(textView);
    }

    private void setStyleTwShow(int i) {
        //tạo nút mới và xác định vị trí thuộc vào
        TextView textView = new TextView(glShows.getContext());
        textView.setBackgroundResource(R.drawable.ic_tile_hover);
        textView.setTextSize(24);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
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
                    Toast.makeText(this, view.getId() + "", Toast.LENGTH_SHORT).show();
                    TextView textView = (TextView) view;
                    textView.setVisibility(View.INVISIBLE);
                    String character = textView.getText().toString();
                    result += character;
                    TextView textView1 = findViewById(arrIdBtnShow[count]);
                    textView1.setText(character);
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
        int idBg = 0;
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
            TextView textView = findViewById(id);
            textView.setBackgroundResource(idBg);
        }

        btnContinue.setVisibility(View.VISIBLE);
    }

    private void createPlay() {
        int id = random.nextInt(arrIdImg.length);
        while (idImg == id) {
            id = random.nextInt(arrIdImg.length);
        }

        idImg = id;
    }

    @Override
    public void onBackPressed() {
        reStartGame();
    }
}