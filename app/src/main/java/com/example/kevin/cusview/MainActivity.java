package com.example.kevin.cusview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private XProgress xp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xp = (XProgress) findViewById(R.id.xp);
        xp.setRoundRectNum(new int[]{20,35,5000},new String[]{"nihao","nifd","fds","fdsfds"},-1,"箱");
    }
}
