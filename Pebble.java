package SoftDev;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Pebble {

    // Instantiating black bags
    static List<Integer> blackBagX = new ArrayList<Integer>();
    static List<Integer> blackBagY = new ArrayList<Integer>();
    static List<Integer> blackBagZ = new ArrayList<Integer>();

    // Instantiating white bags
    static List<Integer> whiteBagA = new ArrayList<Integer>();
    static List<Integer> whiteBagB = new ArrayList<Integer>();
    static List<Integer> whiteBagC = new ArrayList<Integer>();

    // Read CSV file
    public static String[] readCSV (String file) {
        try {
            File csvFile = new File (file);
            Scanner csvScanner = new Scanner(csvFile);
            String nextLine = csvScanner.nextLine();
            String[] pebValues = nextLine.split(",");

            return pebValues;
        } catch (Exception e) {
            return null;
        }
    }

    // Convert String array to Int array
    public static int[] strToInt (String[] str) {
        int size = str.length;
        int[] intArr = new int [size];
        for (int i = 0; i < size; i++)
            intArr[i] = Integer.parseInt(str[i]);

        return intArr;
    }

    // convert array to arrayList
    public static List<Integer> arrToArrList (int[] arr) {
        List<Integer> arrList = new ArrayList<Integer>(arr.length);
        for (int i : arr) {
            arrList.add(i);
        }
        return arrList;
    }

    // check if all integers in arrayList is positive
    public static void checkArrayList(List<Integer> arrList) {
        for (int i = 0; i < arrList.size(); i++)
            if (arrList.get(i) < 0) {
                System.out.println("Please make sure that all integer values are positive");
                System.exit(0);
            } else {
                continue;
            }
    }

    // Generate BlackBagX
    public static List<Integer> getBlackBagX() {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter the location of bag X: ");

        String bagX = myScanner.nextLine();
        String[] pebbleValues = readCSV(bagX);
        int[] intArr = strToInt(pebbleValues);

        // convert array to arrayList
        List<Integer> tempBlackBagX = arrToArrList(intArr);
        // check if all values are positive
        checkArrayList(tempBlackBagX);
        // check size
        checkSize(tempBlackBagX);
        // return
        blackBagX = tempBlackBagX;
        return blackBagX;
    }

    // Generate BlackBagY
    public static List<Integer> getBlackBagY() {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter the location of bag Y: ");

        String bagY = myScanner.nextLine();
        String[] pebbleValues = readCSV(bagY);
        int[] intArr = strToInt(pebbleValues);

        // convert array to arrayList
        List<Integer> tempBlackBagY = arrToArrList(intArr);
        // check if all values are positive
        checkArrayList(tempBlackBagY);
        // check size
        checkSize(tempBlackBagY);
        // return
        blackBagY = tempBlackBagY;
        return blackBagY;
    }

    // Generate BlackBagZ
    public static List<Integer> getBlackBagZ() {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter the location of bag Z: ");

        String bagZ = myScanner.nextLine();
        String[] pebbleValues = readCSV(bagZ);
        int[] intArr = strToInt(pebbleValues);

        // convert array to arrayList
        List<Integer> tempBlackBagZ = arrToArrList(intArr);
        // check if all values are positive
        checkArrayList(tempBlackBagZ);
        // check size
        checkSize(tempBlackBagZ);
        // return
        blackBagZ = tempBlackBagZ;
        return blackBagZ;
    }

    // Check size of bag
    public static void checkSize (List<Integer> bag) {

        if (bag.size() < (PebbleGame.numOfPlayer * 11)) {
            System.out.println("Bag must contain at least 11 times as many pebbles as players!");
            System.exit(0);
        }
    }

    // Select random black bag
    public static List<Integer> randomBag() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(3);

        if (randomNumber == 0) {
            return blackBagX;
        } else if (randomNumber == 1) {
            return blackBagY;
        } else {
            return blackBagZ;
        }
    }
}
