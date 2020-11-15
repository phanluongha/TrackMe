package com.plh.trackme.activity;

import android.content.Intent;
import android.os.Bundle;

import com.plh.trackme.R;
import com.plh.trackme.base.BaseActivity;
import com.plh.trackme.eventBus.BeginRecordEvent;
import com.plh.trackme.eventBus.StopRecordEvent;
import com.plh.trackme.fragment.HistoryFragment;
import com.plh.trackme.fragment.RecordFragment;

import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends BaseActivity {

    private RecordFragment recordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment(R.id.content, HistoryFragment.newInstance(), HistoryFragment.class.getSimpleName());
    }

    @Subscribe
    public void onEvent(BeginRecordEvent event) {
        recordFragment = RecordFragment.newInstance();
        addFragment(R.id.content, recordFragment, RecordFragment.class.getSimpleName());
    }

    @Subscribe
    public void onEvent(StopRecordEvent event) {
        Intent newIntent = new Intent(this, MainActivity.class);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newIntent);
        MainActivity.this.finish();
    }
}
