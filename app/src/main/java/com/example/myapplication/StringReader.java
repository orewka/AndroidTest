package com.example.myapplication;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class StringReader {
    byte[] bytes = new byte[1024];
    int data;
    String string;

    public String readCommand(DataInputStream dataInputStream) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            dataInputStream.read(bytes);
            byteArrayOutputStream.write(bytes);
            string = byteArrayOutputStream.toString().trim();
            System.out.println(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }
}
