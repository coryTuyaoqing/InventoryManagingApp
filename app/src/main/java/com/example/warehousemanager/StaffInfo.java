package com.example.warehousemanager;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class StaffInfo {
    private String staffID;
    private String name;
    private String permission;

    private File staffData;
    private final String TAG = "StaffInfo";
    public StaffInfo(Context context) {
        staffData = new File(context.getFilesDir(), "staff_data");
    }

    public boolean fileExist(){
        return staffData.exists();
    }


    public void initInfo(){
        try{
            if(staffData.createNewFile()){
                Log.d(TAG, "initInfo: create staffInfo successfully");
            }
            else{
                Log.d(TAG, "initInfo: file already exist");
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean writeInfo(String staffID, String name, String permission){
        if(!staffData.exists()){
            return false;
        }
        else{
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(staffData);

                try {
                    FileWriter writer = new FileWriter(fos.getFD());
                    writer.write(staffID + '\n');
                    writer.write(name  + '\n');
                    writer.write(permission  + '\n');
                    writer.close();
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    boolean readFile(){
        if(!fileExist()){
            return false;
        }
        else{
            FileInputStream fis;
            try {
                fis = new FileInputStream(staffData);

                Scanner scanner = new Scanner(fis);
                if(scanner.hasNext()){
                    staffID = scanner.next();
                    name = scanner.next();
                    permission = scanner.next();
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }

    public boolean deleteFile(){
        return staffData.delete();
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
}
