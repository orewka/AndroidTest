package com.example.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

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

    public void sendString(final String string) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dataOutputStream.write(string.getBytes());
                    dataOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void sendData(final LinkedBlockingQueue<Integer> linkedBlockingQueue) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        dataOutputStream.writeInt(linkedBlockingQueue.take());
                        dataOutputStream.flush();
                    } catch (IOException | InterruptedException e) {
                        Toast.makeText(getApplicationContext(), "Device disconnected", Toast.LENGTH_SHORT).show();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });
        thread.start();
    }

    public ArrayList<String> getListDevices() throws InterruptedException {
        final LinkedBlockingQueue<ArrayList> listLinkedBlockingQueue = new LinkedBlockingQueue<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> arrayList = new ArrayList<>();
                StringReader stringReader = new StringReader();
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                String listDevicesJson = stringReader.readCommand(dataInputStream);
                arrayList = new Gson().fromJson(listDevicesJson, type);
                try {
                    listLinkedBlockingQueue.put(arrayList);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return listLinkedBlockingQueue.take();
    }
}