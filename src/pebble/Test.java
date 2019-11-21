package pebble;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws FileNotFoundException {
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
    }



}
