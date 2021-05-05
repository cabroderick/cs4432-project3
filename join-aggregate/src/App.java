import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class App {
    static Hashtable<Integer, String> datasetATable = new Hashtable<>(); // hashtable for dataset A
    static Hashtable<Integer, String> datasetBTable = new Hashtable<>(); // hashtable for dataset B

    public static void main(String[] args) throws Exception {
        getInput();
    }

    private static void getInput () {
        System.out.print("> ");
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        if (command.equals("SELECT A.Col1, A.Col2, B.Col1, B.Col2 FROM A, B WHERE A.RandomV = B.RandomV")) {
            buildHashtable();
            joinTables();
        }
        getInput();
    }

    private static void buildHashtable () { // builds the hashtable on dataset A
        try {
            for (int F = 1; F <= 99; F++) { // iterate through each file in the dataset directory
                String fileNameA = System.getProperty("user.dir")+"/Project3Dataset-A/A"+F+".txt";
                String fileNameB = System.getProperty("user.dir")+"/Project3Dataset-B/B"+F+".txt";
                System.out.println(fileNameA);
                File fileA = new File(fileNameA);
                File fileB = new File(fileNameB);
                Scanner scannerA = new Scanner(fileA);
                Scanner scannerB = new Scanner(fileB);
                String dataA = scannerA.nextLine();
                String dataB = scannerB.nextLine();
                for (int R = 0; R < 100; R++) { // iterate through each record, each record is 40 characters long
                    // the desired byte range is (40*r + 32) to (40*r + 36)
                    int beginIndex = 40*R + 33;
                    int endIndex = 40*R + 37;

                    Integer k = Integer.parseInt(dataA.substring(beginIndex, endIndex));
                    int O = R*40; // the offset #, record # * 40 since each record is of size 40 bytes
                    String v = Integer.toString(F)+"-"+Integer.toString(O);
                    // now add values to hashtable
                    datasetATable.put(k, v);

                    // do the same for table B
                    k = Integer.parseInt(dataB.substring(beginIndex, endIndex));
                    O = R*40; // the offset #, record # * 40 since each record is of size 40 bytes
                    v = Integer.toString(F)+"-"+Integer.toString(O);
                    // now add values to hashtable
                    datasetBTable.put(k, v);
                }
                scannerA.close();
                scannerB.close();
            }
        } catch (IOException e) {
            System.out.println("Invalid filename");
        }
    }

    private static void joinTables () { // join the two tables on RandomV
        System.out.println("")
        for (int i = 1; i < 500; i++) { // iterate through every bucket in each table

        }
    }
}
