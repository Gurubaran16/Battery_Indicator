package com.example.batteryindicator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SwitchCompat SwitchCompat;
    private EditText percent;
    private BatteryManager batteryManager;
    private Ringtone ringtone;
    private int batteryPercentage ;
    private String percentage1 ;
    private Boolean bool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchCompat = findViewById(R.id.switchbutton);
        percent = findViewById(R.id.percent);
        bool = true;
    }



    @Override
    protected void onResume() {
        super.onResume();
        SwitchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SwitchCompat.isChecked()&&bool) {
                    batteryManager = (BatteryManager)getSystemService(BATTERY_SERVICE);
                    int percentage = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    ringtone = RingtoneManager.getRingtone(getApplicationContext(),RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                    BroadcastReceiver broadcastReceiverBattery = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {
                            Integer integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                            percentage1 = percent.getText().toString();
                            batteryPercentage = Integer.parseInt(percent.getText().toString());
                            if(integerBatteryLevel.equals(batteryPercentage)&&bool){
                                ringtone.play();
                            }
                            batteryPercentage = 0;
                        }
                    };
                    registerReceiver(broadcastReceiverBattery,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                    Toast.makeText(getApplicationContext(), "started" + percentage, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Don't kill the app from recent apps", Toast.LENGTH_LONG).show();
                    ringtone.stop();
                    bool = false;
                    batteryPercentage = 0;
                    finish();
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bool = false;
        ringtone.stop();
    }
}