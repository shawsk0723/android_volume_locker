package com.lge.volumelocker;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.d(TAG, "onCreate()");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Switch volumeLockSwitch = findViewById(R.id.volumeLockSwitch);
        syncVolumeLockSwitchWithServiceState(volumeLockSwitch);

        volumeLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked) {
                    executeServiceAction(Constants.ACTION_START_SERVICE);
                } else {
                    executeServiceAction(Constants.ACTION_STOP_SERVICE);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        LOG.d(TAG, "onStart()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LOG.d(TAG, "onStop()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(intent());
        //executeServiceAction(Constants.ACTION_STOP_SERVICE);
        LOG.d(TAG, "onDestroy()");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        Toast toast = Toast.makeText(getApplicationContext(),"", Toast.LENGTH_LONG);

        switch(item.getItemId())
        {
            case R.id.menu1:
                ServiceStateChecker serviceStateChecker = new ServiceStateChecker(getApplicationContext());
                boolean isRunning = serviceStateChecker.isServiceRunning(VolumeLockService.class);
                String toastText;
                if(isRunning) {
                    toastText = "service is running ~";
                } else {
                    toastText = "service is not running ~";
                }
                toast.setText(toastText);
                break;
            case R.id.menu2:
                toast.setText("Select Menu2");
                break;
            case R.id.menu3:
                toast.setText("Select Menu3");
                break;
        }

        toast.show();

        return super.onOptionsItemSelected(item);
    }


    private void syncVolumeLockSwitchWithServiceState(Switch volumeLockSwitch) {
        ServiceStateChecker serviceStateChecker = new ServiceStateChecker(getApplicationContext());
        boolean isRunning = serviceStateChecker.isServiceRunning(VolumeLockService.class);
        LOG.d(TAG, "volume locker service state = " + isRunning);
        if(isRunning) {
            volumeLockSwitch.setChecked(true);
        }
    }

    private void executeServiceAction(String action) {
        Intent intent = intent().setAction(action);
        if (startService(intent) == null)
            throw new RuntimeException();
    }

    private Intent intent() {
        return new Intent(this, VolumeLockService.class);
    }

}