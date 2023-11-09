package org.example;

import java.time.LocalDate;
import java.util.Comparator;

public class DateComparator implements Comparator<Activity> {
    public int compare(Activity activity1, Activity activity2)
    {
        return activity1.getDate().compareTo(activity2.getDate());
    }
}
