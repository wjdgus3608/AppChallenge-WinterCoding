package com.example.myapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnect {
    public String run(int mode, String myurl, String data){
        String api_key="QJuHAX8evMY24jvpHfHQ4pHGetlk5vn8FJbk70O6";
        try {
            URL url = new URL(myurl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(mode==0)
                    conn.setRequestMethod("GET");
                else if(mode==1)
                    conn.setRequestMethod("POST");


                conn.setRequestProperty("x-api-key", api_key);
                conn.setRequestProperty("Content-Type", "application/json");
                if (conn.getResponseCode() == conn.HTTP_OK) {
                    if(mode==0) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String line;
                        String page = "";

                        while ((line = reader.readLine()) != null) {
                            page += line;
                        }
                        Log.e("my", page);
                        return page;
                    }
                    else if(mode==1){
                        String strParams = data; //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
                        OutputStream os = conn.getOutputStream();
                        os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.
                        os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
                        os.close();
                        return null;
                    }
                    else if(mode==2){

                    }
                    return null;
                } else {
                    Log.e("my", conn.getResponseCode() + " error");
                }
            } catch (IOException e) {
                Log.e("my", e.toString());
            }
        } catch (
                MalformedURLException e) {
            Log.e("my", e.toString());
        }
        return "";
    }
}
