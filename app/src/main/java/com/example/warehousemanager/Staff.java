package com.example.warehousemanager;

import java.io.File;

public class Staff {
    private String name;
    private int id;
    private int password;
    private File staffData;

    public Staff(String name, int id, int password) {
        this.name = name;
        this.id = id;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getPassword() {
        return password;
    }
}
