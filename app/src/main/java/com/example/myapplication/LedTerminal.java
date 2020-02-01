package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.concurrent.LinkedBlockingQueue;

public class LedTerminal extends AppCompatActivity {
    LocalService mService;
    boolean mBound = false;
    final LinkedBlockingQueue<Integer> linkedBlockingQeque = new LinkedBlockingQueue<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        final TextView textViewR, textViewG, textViewB, textViewRed, textViewGreen, textViewBlue;
        SeekBar seekBarR, seekBarG, seekBarB;

        textViewRed = findViewById(R.id.textViewRed);
        textViewR = findViewById(R.id.textViewR);
        seekBarR = findViewById(R.id.seekBarR);
        textViewGreen = findViewById(R.id.textViewGreen);
        textViewG = findViewById(R.id.textViewG);
        seekBarG = findViewById(R.id.seekBarG);
        textViewBlue = findViewById(R.id.textViewBlue);
        textViewB = findViewById(R.id.textViewB);
        seekBarB = findViewById(R.id.seekBarB);

            textViewRed.setText("RED : ");
            textViewR.setText(String.valueOf(seekBarR.getProgress()));
            textViewGreen.setText("GREEN : ");
            textViewG.setText(String.valueOf(seekBarG.getProgress()));
            textViewBlue.setText("BLUE : ");
            textViewB.setText(String.valueOf(seekBarB.getProgress()));

            mService.sendData(linkedBlockingQeque);


            seekBarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    try {
                        linkedBlockingQeque.put(i + 1000);
                        textViewR.setText(String.valueOf(i));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            seekBarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    try {
                        linkedBlockingQeque.put(i + 2000);
                        textViewG.setText(String.valueOf(i));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
            seekBarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    try {
                        linkedBlockingQeque.put(i + 3000);
                        textViewB.setText(String.valueOf(i));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
