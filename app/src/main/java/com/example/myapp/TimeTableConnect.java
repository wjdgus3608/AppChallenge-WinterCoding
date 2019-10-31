package com.example.myapp;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TimeTableConnect {
    public String run(int mode, String myurl, String data){
        String api_key="QJuHAX8evMY24jvpHfHQ4pHGetlk5vn8FJbk70O6";
        try {
            URL url = new URL(myurl);
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if(mode==0)
                    conn.setRequestMethod("GET");
                else if(mode==1) {
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);

                }

                conn.setRequestProperty("x-api-key", api_key);
                conn.setRequestProperty("Content-Type", "application/json");
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

                        try {
                            OutputStream os = conn.getOutputStream();
                            JSONObject job = new JSONObject();
                            job.put("user_key", "8882ca818a4ee457998b5c010ae9f1a1");
                            job.put("code", data);
                            Log.e("my",job.toString());
                            os.write(job.toString().getBytes("UTF-8"));
                            os.flush();

                            BufferedReader br = new BufferedReader(new InputStreamReader(
                                    (conn.getInputStream())));

                            String output;
                            System.out.println("Output from Server .... \n");
                            while ((output = br.readLine()) != null) {
                                System.out.println(output);
                            }

                        }
                        catch (JSONException e){
                            Log.e("my",e.toString());
                        }

                        return null;
                    }
                    else if(mode==2){

                    }
                    return null;
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
