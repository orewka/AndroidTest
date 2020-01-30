package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class LocalService extends Service {
    private Socket socket;
    private DataOutputStream dataOutputStream;

    @Override
    public void onCreate() {
        Thread getSocket = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("10.0.2.2", 56);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeChar('a');
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        getSocket.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            dataOutputStream.writeChar('b');
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
