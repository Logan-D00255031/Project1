package org.example;

import java.util.ArrayList;

public class Activities {

    private ArrayList<Activity> data = new ArrayList<Activity>();
    private String name;

    Activities(String name)
    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Activity> getData() {
        return data;
    }

    public void addData(Activity activity) {
        this.data.add(activity);
    }

    public void setData(ArrayList<Activity> data) {
        this.data = data;
    }
}
