package com.plh.trackme.base;

import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

public class BaseActivity extends AppCompatActivity {

    public void addFragment(int id, BaseFragment baseFragment, String tag) {
        getSupportFragmentManager().beginTransaction()
                .replace(id, baseFragment, tag)
                .commit();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
