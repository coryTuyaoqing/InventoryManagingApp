package com.example.warehousemanager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//This is a singleton class!
public class Staff {
    private String staffID;
    private String name;
    private String permission;
    private String email;
    private String avatar;
    private final File staffInfo;
    private static final String TAG = "Staff";
    private static Staff staff;

    private Staff(Context context) {
        staffInfo = new File(context.getFilesDir(), "staff_info");
    }

    public static synchronized Staff getStaff(Context context){
        if(staff == null){
            staff = new Staff(context);
        }
        return staff;
    }

    public boolean fileExist(){
        return staffInfo.exists();
    }


    public void initInfo(){
        try{
            if(staffInfo.createNewFile()){
                Log.d(TAG, "initInfo: create staffInfo successfully");
            }
            else{
                Log.d(TAG, "initInfo: file already exist");
            }
        }
        catch(IOException e){
            Log.e(TAG, "initInfo: ", e);
        }
    }

    public void writeInfo(String staffID, String name, String permission, String email, String avatar){
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(staffInfo);

            try {
                FileWriter writer = new FileWriter(fos.getFD());
                writer.write(staffID + '|');
                writer.write(name  + '|');
                writer.write(permission  + '|');
                writer.write(email + '|');
                writer.write(avatar + '|');
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        readFile();
    }

    public void readFile(){
        FileInputStream fis;
        try {
            fis = new FileInputStream(staffInfo);

            Scanner scanner = new Scanner(fis);
            scanner.useDelimiter("\\|"); // Set pipe as delimiter
            if(scanner.hasNext()){
                staffID = scanner.next();
                name = scanner.next();
                permission = scanner.next();
                email = scanner.next();
                avatar = scanner.next();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteFile(){
        return staffInfo.delete();
    }

    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getEmail() {
        return email;
    }
    public Bitmap getAvatarBitmap(){
        byte[] decodedBytes = Base64.decode(avatar, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void refreshInfoFromDB(){
        OkHttpClient client = new OkHttpClient();
        String url = DB.DB_URL + "get_one_staff_id/" + staffID;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onFailure: ", e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try {
                    assert response.body() != null;
                    JSONArray responseArray = new JSONArray(response.body().string());
                    JSONObject responseObject = responseArray.getJSONObject(0);
                    String name = responseObject.getString("name");
                    String permission = responseObject.getString("permission");
                    String email = responseObject.getString("email");
                    String id = responseObject.getString("idStaff");
                    String avatar = responseObject.getString("avatar");
                    staff.deleteFile();
                    staff.initInfo();
                    staff.writeInfo(id, name, permission, email, avatar);
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: JSONArray", e);
                }
            }
        });
    }
}
