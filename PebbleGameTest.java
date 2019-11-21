package pebble;

import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class PebbleGameTest {

    String playerName;
    int playerIndex;
    private int sumHand;

    List<Integer> hand = (new ArrayList<>());
    List<Integer> blackBag = (new ArrayList<>());
    List<Integer> whiteBag = (new ArrayList<>());


    class NotEnoughPebblesException extends Exception {
        private NotEnoughPebblesException(){}
    }

    class negativePebblesException extends Exception {
        private negativePebblesException(){}
    }

    @Test
    public void testPositivePlayers() {
        //assertEquals("Error: Please enter a positive integer for the number of players!",PebbleGame.Player.getNumOfPlayers());
    }

    @Test (expected = FileNotFoundException.class)
    public void testCorrectCSV() throws Exception {
        Bags.checkFileType("D:\\Comp Sci\\2\\Java\\tests\\pebble\\file5.txt");
    }

    @Test (expected = NumberFormatException.class)
    public void testNegativePebbles() throws FileNotFoundException {
        List<Integer> bag = new ArrayList<Integer>();
        FileReader csvFile = new FileReader(new File("D:\\Comp Sci\\2\\Java\\tests\\pebble\\negative.csv"));
        Scanner csvScanner = new Scanner(csvFile);
        String nextLine = csvScanner.nextLine();
        String[] text = nextLine.split(",");

        for (String input : text) {
            Bags.negativePebblesCheck(bag,input);
        }
    }

    @Test (expected = NotEnoughPebblesException.class)
    public void testBagPlayerRatio() throws FileNotFoundException, NotEnoughPebblesException {
        List<Integer> bag = new ArrayList<Integer>();
        FileReader csvFile = new FileReader(new File("D:\\Comp Sci\\2\\Java\\tests\\pebble\\tooSmall.csv"));
        Scanner csvScanner = new Scanner(csvFile);
        String nextLine = csvScanner.nextLine();
        String[] text = nextLine.split(",");

        if (bag.size() < (11)) {
            throw new NotEnoughPebblesException();
        }
    }

    @Test
    public void testInitialHand() throws InterruptedException {
        testStartHand();
    }


    /**
     * initialise starting hand
     * @throws InterruptedException
     */
    public void testStartHand() throws InterruptedException {

        int bagNum = ThreadLocalRandom.current().nextInt(0, 3);

        int currentIndex;
        int currentPebble;

        // selecting random bag
        for (int i = 1; i < 23; i++) {
            blackBag.add(i);
        }

        // selecting first 10 random pebbles
        for (int i = 0; i < 10; i++) {
            // change bags if not enough to choose from
            if (blackBag.size() < 10) {
                while (true) {
                    bagNum = ThreadLocalRandom.current().nextInt(0, 3);
                    blackBag = Bags.randomBlackBag(bagNum);
                    whiteBag = Bags.currentWhiteBag(bagNum);
                    if (blackBag.size() > 11) {
                        break;
                    }
                }
            }
            synchronized (blackBag) {
                currentIndex = ThreadLocalRandom.current().nextInt(0, blackBag.size());
                currentPebble = blackBag.get(currentIndex);
                blackBag.remove(currentIndex);
            }
            hand.add(currentPebble);
            sumHand += currentPebble;
        }

        whiteBag = Bags.currentWhiteBag(bagNum);
        System.out.println(" starting hand is " + hand);
    }
}