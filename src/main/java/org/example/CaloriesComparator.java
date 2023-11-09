package org.example;

import java.util.Comparator;

public class CaloriesComparator implements Comparator<Activity> {
    public int compare(Activity activity1, Activity activity2)
    {
        return Double.compare(activity1.getCaloriesBurned(), activity2.getCaloriesBurned());
    }
}
