package pebble;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Bags {

    // Instantiating black bags
    static List<Integer> blackBagX = new ArrayList<Integer>();
    static List<Integer> blackBagY = new ArrayList<Integer>();
    static List<Integer> blackBagZ = new ArrayList<Integer>();

    // Instantiating white bags
    static List<Integer> whiteBagA = new ArrayList<Integer>();
    static List<Integer> whiteBagB = new ArrayList<Integer>();
    static List<Integer> whiteBagC = new ArrayList<Integer>();

    //static List<Integer> tempBag = new ArrayList<Integer>();

    // Reads the .txt or .csv file containing pebble values data and imports it
    public static List<Integer> importPebValuesData(String file) {
        List<Integer> tempBag = new ArrayList<Integer>();
        do {
            class NotEnoughPebblesException extends Exception {
                private NotEnoughPebblesException(){}
            }
            String errorMessage = "Please re-enter the location of the bag: ";
            try {

                FileReader csvFile = new FileReader(new File(file));

                // check if file type is .csv
                checkFileType(file);

                Scanner csvScanner = new Scanner(csvFile);
                String nextLine = csvScanner.nextLine();
                String[] text = nextLine.split(",");

                //List<Integer> pebValues = new ArrayList<Integer>();

                // check if all pebbles are positive
                for (String input : text) {
                    negativePebblesCheck(tempBag,input);
                }

                // check pebble/player ratio condition
                if (tempBag.size() < (PebbleGame.numOfPlayers * 11)) {
                    throw new NotEnoughPebblesException();
                }

                // return the bag if everything is valid
                System.out.println(tempBag);
                return tempBag;

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

    public static void checkFileType(String file) throws FileNotFoundException {
        if( file.length() > 4 && file.substring(file.length() - 4).equals(".txt") &&
                !file.substring(file.length() - 4).equals(".csv")) {
            throw new FileNotFoundException();
        }
    }
    public static List<Integer> negativePebblesCheck(List<Integer> bag,String input) {
        //List<Integer> pebValues = new ArrayList<Integer>();
        int value = Integer.parseInt(input);
        if (value > 0) {
            bag.add(value);
        } else {
            throw new NumberFormatException();
        }
        return bag;
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

    // TEST function. Note, this bypasses any requirements and checks
    public static void force_createBlackBags(String file1, String file2, String file3) {
        blackBagX = importPebValuesData(file1);
        blackBagY = importPebValuesData(file2);
        blackBagZ = importPebValuesData(file3);
    }
}


