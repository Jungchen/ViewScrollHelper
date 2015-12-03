package com.hrules.viewscrollhelper.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = "MainActivity";

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    findViewById(R.id.buttonRecycler).setOnClickListener(this);
    findViewById(R.id.buttonRecyclerTabs).setOnClickListener(this);
    findViewById(R.id.buttonScrollView).setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.buttonRecycler:
        startActivity(new Intent(this, RecyclerActivity.class));
        break;

      case R.id.buttonRecyclerTabs:
        startActivity(new Intent(this, RecyclerTabsActivity.class));
        break;

      case R.id.buttonScrollView:
        startActivity(new Intent(this, ScrollViewActivity.class));
        break;
    }
  }
}
