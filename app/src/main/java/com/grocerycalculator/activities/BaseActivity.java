package com.grocerycalculator.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import com.grocerycalculator.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void swapToolbar(BaseActivity activity, String title) {
        activity.setSupportActionBar(activity.findViewById(R.id.toolbar));
        super.setTitle("");
        if (title != null && !title.equals("")) {
            setTitle(title);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        ((TextView) findViewById(R.id.title_tv)).setText(title);
    }

    public void enableBackBtn(BaseActivity activity) {
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_arrow);
    }

    public void setContentView(View view, BaseActivity activity, String title) {
        super.setContentView(view);
        swapToolbar(activity, title);
    }

    public void setContentView(View view, BaseActivity activity, String title, boolean enableBackBtn) {
        super.setContentView(view);
        swapToolbar(activity, title);
        if (enableBackBtn) enableBackBtn(activity);
    }
}