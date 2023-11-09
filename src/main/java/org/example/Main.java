package org.example;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io. * ;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //ArrayList<Activity> activitiesData = new ArrayList<>();
        Activities activitiesData = new Activities("dataSet");
        Scanner keyboard = new Scanner(System.in);
        int userInput;
        boolean run = true;

        System.out.println("Welcome!");

        // Main menu
        while(run)
        {
            System.out.println("""
                    What would you like to do?
                    1: Upload activity data
                    2: View all stored activity data
                    3: View activities that fit a category
                    4: Search for specific activity
                    5: View your performance statistics
                    6: Exit
                    """);
            userInput = keyboard.nextInt();

            switch (userInput)
            {
                case 1 -> menu1(activitiesData);  // Opens the upload file menu
                case 2 -> menu2(activitiesData);  // Opens the stored activities menu
                case 3 -> menu3(activitiesData);  // Opens the category filter menu
                case 4 -> menu4(activitiesData);  // Opens activity search menu
                case 5 -> menu5(activitiesData);  // Opens performance statistics menu
                case 6 -> {
                    System.out.println("Goodbye!");
                    run = false;
                }  // Ends the program
                default -> System.out.println("Invalid input!");  // Message on incorrect input
            }
        }


        //  System.out.printf("%-10s %5s %5d %7.2f %5d %n", activity, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), duration, distance, heartRate);

    }

    // Menu 1 (Upload file menu)
    private static void menu1(Activities activitiesData) {
        Scanner keyboard = new Scanner(System.in);
        String userInput;
        boolean run = true;

        while(run) {
            System.out.println("""
                    Enter the name of the file to add to the collection (File name must include it's file extension)
                    Enter 1 to cancel.
                    """);
            userInput = keyboard.nextLine();

            if(userInput.equals("1")) {
                run = false;
            }
            else {
                // Format of each row of data is:
                // Activity Type, Date, Duration, Distance, Average heart rate - these heading names are included as the first row in file
                // Running, 04/01/2020, 67, 8.80, 152  for example

                try (Scanner sc = new Scanner(new File(userInput))) {
                    if (sc.hasNextLine())
                        sc.nextLine();   // read the header line containing column titles, but don't use it

                    // read one line at a time into a String, and parse the String into tokens (parts)
                    while (sc.hasNextLine()) {
                        String line = sc.nextLine();             // read full line ( delimited by a "\n" )
                        String[] tokens = line.split(", ");  // split line using a comma as the delimiter (separator)

                        String activity = tokens[0];  // extract first token/field from the tokens array (i.e. the activity)
                        LocalDate date = LocalDate.parse(tokens[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));  // e.g. Convert String "04/01/2020" to LocalDate 2020-01-04
                        int duration = Integer.parseInt(tokens[2]);  // e.g. Convert String "67" to int value 67
                        double distance = Double.parseDouble(tokens[3]);  // e.g. Convert String "8.80" to double 8.80
                        int heartRate = Integer.parseInt(tokens[4]);

                        double kilometersPerHour = (double) distance / duration;
                        double intensityValue = findIntensityValue(kilometersPerHour, activity);
                        double calories = intensityValue * duration;

                        Activity tokenData = new Activity(activity, date, duration, distance, heartRate, calories);
                        activitiesData.addData(tokenData);
                        // Print out the row of field values using format specifiers
                        //System.out.printf("%-10s %5s %5d %7.2f %5d %n", activity, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), duration, distance, heartRate);
                    }

                } catch (FileNotFoundException exception) {
                    System.out.println("FileNotFoundException caught. The file " + userInput + " may not exist." + exception);
                }
            }
        }
    }

    // Menu 2 (Activity sorting)
    private static void menu2(Activities activitiesData) {
        Scanner keyboard = new Scanner(System.in);
        int userInput;
        boolean run = true;

        while(run)
        {
            System.out.println("""
                    How would you like to sort it?
                    1: Calories burned (Descending)
                    2: Date (Ascending)
                    3: Date (Descending)
                    4: Activity Duration (Ascending)
                    5: Activity Duration (Descending)
                    6: Type of Activity
                    7: Distance (Ascending)
                    8: Distance (Descending)
                    9: Go Back
                    """);
            userInput = keyboard.nextInt();

            switch (userInput)
            {
                case 1 -> {
                    CaloriesComparator caloriesComparator = new CaloriesComparator();
                    Collections.sort(activitiesData.getData(), caloriesComparator.reversed());
                    display(activitiesData.getData());
                }
                case 2 -> {
                    activitiesData.getData().sort((Activity a1, Activity a2) -> a1.getDate().compareTo(a2.getDate()));
                    display(activitiesData.getData());
                }
                case 3 -> {
                    Comparator<Activity> dateComparator = (a1, a2) -> a1.getDate().compareTo(a2.getDate());
                    activitiesData.getData().sort(dateComparator.reversed());
                    display(activitiesData.getData());
                }
                case 4 -> {
                    activitiesData.getData().sort((Activity a1, Activity a2) -> Integer.compare(a1.getDuration(), a2.getDuration()));
                    display(activitiesData.getData());
                }
                case 5 -> {
                    Comparator<Activity> durationComparator = (a1, a2) -> Integer.compare(a1.getDuration(), a2.getDuration());
                    activitiesData.getData().sort(durationComparator.reversed());
                    display(activitiesData.getData());
                }
                case 6 -> {
                    activitiesData.getData().sort( new Comparator<Activity>() {
                        @Override
                        public int compare(Activity a1, Activity a2) {
                            return a1.getActivity().compareTo(a2.getActivity());
                        }
                    });
                    display(activitiesData.getData());
                }
                case 7 -> {
                    DistanceComparator distanceComparator = new DistanceComparator();
                    activitiesData.getData().sort(distanceComparator);
                    display(activitiesData.getData());
                }
                case 8 -> {
                    DistanceComparator distanceComparator = new DistanceComparator();
                    activitiesData.getData().sort(distanceComparator.reversed());
                    display(activitiesData.getData());
                }
                case 9 -> run = false;
                default -> System.out.println("Invalid input!");
            }
        }
    }

    // Menu 3 (Search subsets)
    private static void menu3(Activities activitiesData) {
        Scanner keyboard = new Scanner(System.in);
        int userInput;
        boolean run = true;

        while(run) {
            System.out.println("""
                    Choose a search subset.
                    1: Activity type
                    2: Above a minimum distance
                    3: Type of energy expended
                    4: Above a minimum duration
                    5: Go Back
                    """);
            userInput = keyboard.nextInt();

            switch (userInput) {
                case 1 -> {
                    Scanner keyboard2 = new Scanner(System.in);
                    String userActivity;
                    ArrayList<Activity> tempData = new ArrayList<>();
                    System.out.println("What activity?");
                    userActivity = keyboard2.nextLine();

                    int count = 0;
                    for (Activity activity : activitiesData.getData()) {
                        if(activitiesData.getData().get(count).getActivity().equals(userActivity)) {
                            tempData.add(activitiesData.getData().get(count));
                        }
                        count++;
                    }
                    display(tempData);
                }
                case 2 -> {
                    Scanner keyboard2 = new Scanner(System.in);
                    int userDistance;
                    ArrayList<Activity> tempData = new ArrayList<>();
                    System.out.println("What's the minimum distance?");
                    userDistance = keyboard2.nextInt();

                    int count = 0;
                    for (Activity activity : activitiesData.getData()) {
                        if(activitiesData.getData().get(count).getDistance() > (userDistance)) {
                            tempData.add(activitiesData.getData().get(count));
                        }
                        count++;
                    }
                    display(tempData);
                }
                case 3 -> {} // Unsure what I'm supposed to do here, sorry
                case 4 -> {
                    Scanner keyboard2 = new Scanner(System.in);
                    double userDuration;
                    ArrayList<Activity> tempData = new ArrayList<>();
                    System.out.println("What's the minimum duration?");
                    userDuration = keyboard2.nextDouble();

                    int count = 0;
                    for (Activity activity : activitiesData.getData()) {
                        if(activitiesData.getData().get(count).getDuration() > (userDuration)) {
                            tempData.add(activitiesData.getData().get(count));
                        }
                        count++;
                    }
                    display(tempData);
                }
                case 5 -> run = false;
            }
        }
    }

    private static void menu4(Activities activitiesData) {

    }

    private static void menu5(Activities activitiesData) {

    }
    
    private static double findIntensityValue(double kilometersPerHour, String activity) {
        int intensity;
        double intensityValue = 0;

        switch (activity) {
            case "Running" -> {
                if (kilometersPerHour < 4) {
                    intensity = 1;
                } else if (kilometersPerHour >= 4 && kilometersPerHour < 8) {
                    intensity = 2;
                } else if (kilometersPerHour >= 8 && kilometersPerHour < 12) {
                    intensity = 3;
                } else if (kilometersPerHour >= 12 && kilometersPerHour < 16) {
                    intensity = 4;
                } else {
                    intensity = 5;
                }
                switch (intensity) {
                    case 1 -> intensityValue = 4.1;
                    case 2 -> intensityValue = 7.2;
                    case 3 -> intensityValue = 10;
                    case 4 -> intensityValue = 15.4;
                    case 5 -> intensityValue = 20.8;
                }
            }
            case "Swimming" -> {
                if (kilometersPerHour < 1.25) {
                    intensity = 1;
                } else if (kilometersPerHour >= 1.25 && kilometersPerHour < 2) {
                    intensity = 2;
                } else if (kilometersPerHour >= 2 && kilometersPerHour < 2.75) {
                    intensity = 3;
                } else if (kilometersPerHour >= 2.75 && kilometersPerHour < 3.5) {
                    intensity = 4;
                } else {
                    intensity = 5;
                }
                switch (intensity) {
                    case 1 -> intensityValue = 5;
                    case 2 -> intensityValue = 6.3;
                    case 3 -> intensityValue = 7.6;
                    case 4 -> intensityValue = 8.9;
                    case 5 -> intensityValue = 10.2;
                }
            }
            case "Cycling" -> {
                if (kilometersPerHour < 8) {
                    intensity = 1;
                } else if (kilometersPerHour >= 8 && kilometersPerHour <= 16) {
                    intensity = 2;
                } else if (kilometersPerHour > 16 && kilometersPerHour < 25) {
                    intensity = 3;
                } else if (kilometersPerHour >= 25 && kilometersPerHour < 33) {
                    intensity = 4;
                } else {
                    intensity = 5;
                }
                switch (intensity) {
                    case 1 -> intensityValue = 2;
                    case 2 -> intensityValue = 5;
                    case 3 -> intensityValue = 7;
                    case 4 -> intensityValue = 13;
                    case 5 -> intensityValue = 15;
                }
            }
        }

        return intensityValue;
    }

    public static void display(ArrayList<Activity> activities) {
        int count = 0;
        for (Activity activity : activities) {
            System.out.println(activities.get(count));
            count++;
        }
        System.out.println();
    }

}