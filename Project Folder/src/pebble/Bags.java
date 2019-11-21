package pebble;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Bags {

    // Instantiating black bags
    static List<Integer> blackBagX = new ArrayList<Integer>();
    static List<Integer> blackBagY = new ArrayList<Integer>();
    static List<Integer> blackBagZ = new ArrayList<Integer>();

    // Instantiating white bags
    static List<Integer> whiteBagA = new ArrayList<Integer>();
    static List<Integer> whiteBagB = new ArrayList<Integer>();
    static List<Integer> whiteBagC = new ArrayList<Integer>();

    // Reads the .txt or .csv file containing pebble values data and imports it
    public static List<Integer> importPebValuesData(String file) {
        do {
            class NotEnoughPebblesException extends Exception {
                private NotEnoughPebblesException(){}
            }
            String errorMessage = "Please re-enter the location of the bag: ";
            try {

                FileReader csvFile = new FileReader(new File(file));

                if( file.length() > 4 && !file.substring(file.length() - 4).equals(".txt") &&
                        !file.substring(file.length() - 4).equals(".csv")) {
                    throw new FileNotFoundException();
                }
                Scanner csvScanner = new Scanner(csvFile);
                String nextLine = csvScanner.nextLine();
                String[] text = nextLine.split(",");

                List<Integer> pebValues = new ArrayList<Integer>();

                for (String input : text){
                    int value = Integer.parseInt(input);
                    if (value > 0) {
                        pebValues.add(value);
                    } else {
                        throw new NumberFormatException();
                    }
                }
                if (pebValues.size() < (PebbleGame.numOfPlayers * 11)) {
                    throw new NotEnoughPebblesException();
                }
                System.out.println(pebValues);
                return pebValues;

            } catch (FileNotFoundException e) {
                System.out.println("Error: Cannot locate the *.txt or *.csv file.");
                Scanner myScanner = new Scanner(System.in);
                System.out.println(errorMessage);
                file = myScanner.nextLine();

            } catch (NumberFormatException | NoSuchElementException e) {
                System.out.println("Error: The file input must only contain positive integers separated by commas!");
                Scanner myScanner = new Scanner(System.in);
                System.out.println(errorMessage);
                file = myScanner.nextLine();

            } catch (NotEnoughPebblesException e) {
                System.out.println("Error: Bag must contain at least 11 times as many pebbles as players!");
                Scanner myScanner = new Scanner(System.in);
                System.out.println(errorMessage);
                file = myScanner.nextLine();
            }
        } while (true);
    }

    // Generates the 3 black bags X, Y, Z
    public static void createBlackBags() {
        Scanner myScanner = new Scanner(System.in);

        System.out.println("Enter the location of bag X: ");
        String file1 = myScanner.nextLine();
        blackBagX = importPebValuesData(file1);

        System.out.println("Enter the location of bag Y: ");
        String file2 = myScanner.nextLine();
        blackBagY = importPebValuesData(file2);

        System.out.println("Enter the location of bag Z: ");
        String file3 = myScanner.nextLine();
        blackBagZ = importPebValuesData(file3);
    }

    // Selects a random black bag
    public static List<Integer> randomBlackBag(int randomNumber) {
        if (randomNumber == 0) {
            return blackBagX;
        } else if (randomNumber == 1) {
            return blackBagY;
        } else {
            return blackBagZ;
        }
    }

    // Determines which white bag to use based on the whiteBag(A,B,C)-blackBag(X,Y,Z) pairing:
    // 0 = A <> X,    1 = B <> Y,    2 = C <> Z
    public static List<Integer> currentWhiteBag(int randNum) {
        if (randNum == 0) {
            return whiteBagA;
        } else if (randNum == 1) {
            return whiteBagB;
        } else {
            return whiteBagC;
        }
    }

    // TEST function. Creates black bags from given files
    public static void force_createBlackBags(String file1, String file2, String file3) {
        blackBagX = importPebValuesData(file1);
        blackBagY = importPebValuesData(file2);
        blackBagZ = importPebValuesData(file3);
    }
}


