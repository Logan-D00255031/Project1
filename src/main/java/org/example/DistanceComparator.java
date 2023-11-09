package org.example;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Activity> {
    public int compare(Activity activity1, Activity activity2)
    {
        return Double.compare(activity1.getDistance(), activity2.getDistance());
    }
}
