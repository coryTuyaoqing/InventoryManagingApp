package com.example.warehousemanager.Model;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//This is a singleton class!
public class Staff {
    private String staffID;
    private String name;
    private String permission;
    private String email;
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

    public void writeInfo(String staffID, String name, String permission, String email){
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(staffInfo);

            try {
                FileWriter writer = new FileWriter(fos.getFD());
                writer.write(staffID + '\n');
                writer.write(name  + '\n');
                writer.write(permission  + '\n');
                writer.write(email + '\n');
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void readFile(){
        FileInputStream fis;
        try {
            fis = new FileInputStream(staffInfo);

            Scanner scanner = new Scanner(fis);
            if(scanner.hasNext()){
                staffID = scanner.next();
                name = scanner.next();
                permission = scanner.next();
                email = scanner.next();
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
}
