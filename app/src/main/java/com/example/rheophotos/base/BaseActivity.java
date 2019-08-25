package com.example.rheophotos.base;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rheophotos.views.search.SearchActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public void openSearchActivity() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }
}
