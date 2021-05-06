import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class App {
    static LinkedList<String>[] tableA = new LinkedList[500]; // hash table for dataset A with 50 buckets
    static String[] arrayA = new String[9900]; // array for dataset A
    static final int buckets = 500; // the number of buckets in the table

    public static void main(String[] args) throws Exception {
        getInput();
    }

    private static void getInput () {
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (command.equals("SELECT A.Col1, A.Col2, B.Col1, B.Col2 FROM A, B WHERE A.RandomV = B.RandomV")) {
            long startTime = System.currentTimeMillis();
            buildHashtable();
            joinTables();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("The command took " + elapsedTime + " milliseconds to execute.");
        }
        if (command.equals("SELECT count(*) FROM A, B WHERE A.RandomV > B.RandomV")) {
            long startTime = System.currentTimeMillis();
            buildArray();
            selectCount();
            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("The command took " + elapsedTime + " milliseconds to execute.");
        }
        getInput();
    }

    private static void buildHashtable () { // builds the hashtable on dataset A
        try {
            for (int F = 1; F <= 99; F++) { // iterate through each file in the dataset directory
                String fileName = System.getProperty("user.dir")+"/Project3Dataset-A/A"+F+".txt";
                Scanner scanner = new Scanner(new File(fileName));
                String data = scanner.nextLine();
                for (int R = 0; R < 100; R++) { // iterate through each record, each record is 40 characters long
                    String record = getRecord(data, R);
                    int randomV = getRandomV(record);
                    put(randomV, record); // add the value to the table
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid filename");
        }
    }

    // hashes the randomV value to yield a bucket value
    private static int hashFunction (int randomV) {
        return randomV % buckets;
    }

    // puts a value into the hashtable
    private static void put (int key, String value) {
        int bucket = hashFunction(key);
        if (tableA[bucket] == null) {
            tableA[bucket] = new LinkedList<>();
        }
        tableA[bucket].add(value);
    }

    // gets the value of RandomV from a specified record
    private static int getRandomV (String record) {
        return Integer.parseInt(record.substring(33, 37));
    }

    // extracts the record data from an entire file in a string
    private static String getRecord (String data, int R) {
        return data.substring(40*R, 40*R + 40);
    }

    private static void joinTables () { // join the two tables on RandomV
        try {
            System.out.println("A.Col1\t\tA.Col2\t\tB.Col1\t\tB.Col2");
            for (int F = 1; F <= 99; F++) {
                String fileName = System.getProperty("user.dir")+"/Project3Dataset-B/B"+F+".txt";
                Scanner scanner = new Scanner(new File(fileName));
                String data = scanner.nextLine();
                scanner.close();
                for (int R = 0; R < 100; R++) {
                    String recordB = getRecord(data, R);
                    int randomVB = getRandomV(recordB);
                    int bucket = hashFunction(randomVB);
                    for (String recordA : tableA[bucket]) {
                        int randomVA = getRandomV(recordA);
                        if (randomVA == randomVB) { // the two randomV values match
                            String[] colsA = recordA.split(", ");
                            String[] colsB = recordB.split(", ");
                            System.out.println(colsA[0] + "\t" + colsA[1] + "\t\t" + colsB[0] + "\t" + colsB[1]);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    // builds the array of records
    private static void buildArray () {
        try {
            for (int F = 1; F <= 99; F++) {
                String fileName = System.getProperty("user.dir")+"/Project3Dataset-A/A"+F+".txt";
                Scanner scanner = new Scanner(new File(fileName));
                String data = scanner.nextLine();
                scanner.close();
                for (int R = 0; R < 100; R++) {
                    String record = getRecord(data, R);
                    arrayA[(F-1)*100 + R] = record;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

    // selects the count of matching records where A.RandomV > B.RandomV
    private static void selectCount () {
        try { 
            int count = 0; // the count of qualifying records
            for (int F = 1; F <= 99; F++) {
                String fileName = System.getProperty("user.dir")+"/Project3Dataset-A/A"+F+".txt";
                Scanner scanner = new Scanner(new File(fileName));
                String data = scanner.nextLine();
                scanner.close();
                for (int R = 0; R < 100; R++) {
                    String recordB = getRecord(data, R);
                    int randomVB = getRandomV(recordB);
                    for (int i = 0; i < arrayA.length; i++) {
                        String recordA = arrayA[i];
                        int randomVA = getRandomV(recordA);
                        if (randomVA > randomVB) {
                            count++;
                        }
                    }
                }
            }
            System.out.println(count);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        
    }
}
