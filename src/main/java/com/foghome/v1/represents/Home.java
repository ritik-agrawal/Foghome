package com.foghome.v1.represents;

import java.util.ArrayList;
import java.util.HashMap;

public class Home {
    private String homeName;
    private HashMap<String,String> user;
    private ArrayList<HashMap<String,Boolean>> moniter;

    public Home(String homeName, HashMap<String, String> user, ArrayList<HashMap<String, Boolean>> moniter) {
        this.homeName = homeName;
        this.user = user;
        this.moniter = moniter;
    }

    public Home() {
    }

    public String getHomeName() {
        return homeName;
    }

    public void setHomeName(String homeName) {
        this.homeName = homeName;
    }

    public HashMap<String, String> getUser() {
        return user;
    }

    public void setUser(HashMap<String, String> user) {
        this.user = user;
    }

    public ArrayList<HashMap<String, Boolean>> getMoniter() {
        return moniter;
    }

    public void setMoniter(ArrayList<HashMap<String, Boolean>> moniter) {
        this.moniter = moniter;
    }
}
