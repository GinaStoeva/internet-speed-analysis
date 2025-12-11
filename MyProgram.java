import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class MyProgram {

    public static void main(String[] args) {

        Scanner console = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Count  people");
        System.out.println("2. Some other analysis");
        System.out.println("3. Another analysis");
        System.out.println("4. Print all records");

        System.out.print("Enter a number: ");
        int choice = console.nextInt();

        // Load data from CSV into an array
        Record[] data = loadData();

        if (data == null) {
            System.out.println("Error loading data.");
            return;
        }

        if (choice == 1) {
            method1(data);
        } else if (choice == 2) {
            method2(data);
        } else if (choice == 3) {
            method3(data);
        } else if (choice == 4) {
            printRecords(data);
        } else {
            System.out.println("Invalid choice.");
        }

        console.close();
    }

    // Represents a single row in the CSV
    static class Record {
        String country;
        String majorArea;
        String region;
        double speed2023;
        double speed2024;
        

        Record(String c, String m, String r, double s23, double s24) {
            country = c;
            majorArea = m;
            region = r;
            speed2023 = s23;
            speed2024 = s24;
        }
    }

    // Load CSV rows into an array
    public static Record[] loadData() {
        try {
            File file = new File("data.csv");

            // First pass: count rows
            Scanner counter = new Scanner(file);
            int count = 0;

            if (counter.hasNextLine()) {
                counter.nextLine(); // skip header
            }

            while (counter.hasNextLine()) {
                counter.nextLine();
                count++;
            }
            counter.close();

            // Create array of correct size
            Record[] data = new Record[count];

            // Second pass: load data
            Scanner input = new Scanner(file);

            if (input.hasNextLine()) {
                input.nextLine();
            }

            int index = 0;
            while (input.hasNextLine()) {
                String[] parts = input.nextLine().split(",");
                String country = parts[0];
                String majorArea = parts[1];
                String region = parts [2].trim();
                
                String s23 = parts[9].trim();
                
                String s24 = parts[10].trim();
                
                double speed2023 = 0.0;
                double speed2024 = 0.0;
                if (!s23.equals("")&& !s23.equalsIgnoreCase("null")){
                    speed2023=Double.parseDouble(s23);
                }
                if (!s24.equals("") && !s24.equalsIgnoreCase("null")){
                    speed2024 = Double.parseDouble(s24);
                }
                String valStr = parts[3].trim();
                double value;
                
                if(valStr.equals("") || valStr.equalsIgnoreCase("null")){
                    value=0;
                } else {
                    value = Double.parseDouble(valStr);
                }

                data[index] = new Record(country, majorArea, region, speed2023, speed2024);
                index++;
            }

            input.close();
            return data;

        } catch (FileNotFoundException e) {
            return null;
        }
    }

    // Example analysis: count healthy people
    public static void method1(Record[] data) {
        System.out.println("Method 1: Region With The Highest Average Internet Speed (2024)");
        
        
        HashMap<String, Double> speedTotals = new HashMap<>();
        HashMap<String, Integer> speedCounts = new HashMap<>();
        
        for(Record r : data) {
            if (!speedTotals.containsKey(r.region)){
                speedTotals.put(r.region, 0.0);
                speedCounts.put(r.region, 0);

            }
            speedTotals.put(r.region, speedTotals.get(r.region)+r.speed2024);
            speedCounts.put(r.region, speedCounts.get(r.region)+1);
        }
        
        String topRegion="";
        double topAvg = -1;
        
        for (String region : speedTotals.keySet()){
            double avg = speedTotals.get(region)/speedCounts.get(region);
            
            if (avg> topAvg){
                topAvg=avg;
                topRegion = region;
            }
        }
        System.out.println("Region with the highest average 2024 speed: "+ topRegion);
        System.out.printf("Average speed: %.2f Mbps\n", topAvg);
    }

    // Placeholder methods
    public static void method2(Record[] data) {
        System.out.println("Method 2: Countries With the Biggest Internet Speed Improvement (2023-2024)");
        double biggestIncrease=-999.0;
        String topCountry = "";
        for(Record r: data){
            double increase = r.speed2024-r.speed2023;
            if(increase>biggestIncrease){
                biggestIncrease = increase;
                topCountry = r.country;
            }
        }
        System.out.println("The country with the biggest improvement from 2023 to 2024 is:");
        System.out.println(topCountry + " with a speed increase of " + biggestIncrease +" Mbps.\n");
        
        System.out.println("This shows how different countries are improving internet access over time.");
    }


    public static void method3(Record[] data) {
        Scanner input = new Scanner(System.in);
        System.out.println("Method 3: Digital Inequality Index (DII) for a Region.");
        System.out.println("Choose from the region options: ");
        System.out.println(" • Southern Asia");
        System.out.println(" • South America");
        System.out.println(" • Southern Africa");
        System.out.println(" • Northern Africa");
        System.out.println(" • Northern America");
        System.out.println(" • Central America");
        System.out.println(" • Eastern Africa");
        System.out.println(" • Western Europe");
        System.out.println(" • Southern Europe");
        System.out.println(" • Northern Europe");
        System.out.println(" • Eastern Europe");
        System.out.println(" • Melanasia");
        System.out.println(" • South-Eastern Asia");
        System.out.println(" • Caribbean");
        System.out.print("Enter a region: ");
        String userRegion = input.nextLine().trim();
        
        double highest = -1;
        double lowest = Double.MAX_VALUE;
        String highestCountry = "";
        String lowestCountry="";
        
        boolean found = false;
        
        for (Record r:data){
            if(r.region.equalsIgnoreCase(userRegion)){
                found = true;
                
                if(r.speed2024 > highest){
                    highest = r.speed2024;
                    highestCountry = r.country;
                
                }
                if (r.speed2024<lowest){
                    lowest = r.speed2024;
                    lowestCountry = r.country;
                }
            }
        }
        if (!found){
            System.out.println("No countries found for that region.");
            return;
        }
        double DII = highest-lowest;
        
        System.out.println("\nRegion: " + userRegion);
        System.out.printf("Highest Speed: %s-%.2f Mbps\n", highestCountry, highest);
        System.out.printf("Lowest Speed: %s - %.2f Mbps\n", lowestCountry, lowest);
        System.out.printf("\nDigital Inequality Index (DII): %.2f Mbps\n", DII);
        
        System.out.println("\nInterpretation:");
        System.out.println("The Digital Inequality Index measures how unequal internet access is");
        System.out.println("This helps highlight digital divides and areas needing improvement.");
        
    }

    // Print all records
    public static void printRecords(Record[] data) {
        for (int i = 0; i < data.length; i++) {
            Record r = data[i];
            System.out.println("#" + i + ": " + r.country + ", " + r.majorArea + ", " + r.region + ", " + r.speed2024);
        }
        System.out.println("Total records: " + data.length);
    }
}
