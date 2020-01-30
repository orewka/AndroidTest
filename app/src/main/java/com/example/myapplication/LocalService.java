package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LocalService extends Service {
    private final IBinder mBinder = new LocalBinder();
    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    @Override
    public void onCreate() {
        super.onCreate();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("10.0.2.2", 56);
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream.writeChar('a');
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void sendType(final Character character) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.writeChar(character);
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public List<String> getListDevices() {
        StringReader stringReader = new StringReader();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> listDevices = new ArrayList<>();
        String listDevicesJson = stringReader.readCommand(dataInputStream);
        listDevices = new Gson().fromJson(listDevicesJson, type);
        return listDevices;
    }
}