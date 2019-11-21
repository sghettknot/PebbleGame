package pebble;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Test {

    static List<Integer> blackBagX = new ArrayList<Integer>();
    static List<Integer> hand = new ArrayList<Integer>();
    public static void main(String[] args) throws FileNotFoundException {

        blackBagX.addAll(Arrays.asList(1, 2, 3, 4, 5, 7, 8, 9, 10, 12, 15, 18, 20, 21, 23, 25, 26, 10, 28, 30, 31, 35, 38, 39, 10, 42, 43, 44, 46, 10, 10, 10, 51, 53, 10, 57, 59, 10, 62, 32, 10, 67, 10, 5));
        hand.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        int pebbleIndex = ThreadLocalRandom.current().nextInt(0, hand.size());
        int pebble = hand.get(pebbleIndex);
        hand.remove(pebbleIndex);

        System.out.println(hand.size());
        /*
        List<Integer> bag = new ArrayList<Integer>();
        FileReader csvFile = new FileReader(new File("D:\\Comp Sci\\2\\Java\\tests\\pebble\\negative.csv"));
        Scanner csvScanner = new Scanner(csvFile);
        String nextLine = csvScanner.nextLine();
        String[] text = nextLine.split(",");

        for (String input : text) {
            Bags.negativePebblesCheck(bag,input);
        }

        for (int i = 0; i < bag.size(); i++) {
            System.out.println(bag.get(i));
        }

         */
    }



}
