package pebble;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertThat;

public class PebbleGameTest {

    List<Integer> hand = (new ArrayList<>());
    List<Integer> blackBag = (new ArrayList<>());
    List<Integer> whiteBag = (new ArrayList<>());


    class NotEnoughPebblesException extends Exception {
        private NotEnoughPebblesException(){}
    }

    class negativePebblesException extends Exception {
        private negativePebblesException(){}
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


    @Test
    public void testDiscard() {

        hand.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        List<Integer> whiteBag = (new ArrayList<>());

        int pebbleIndex = ThreadLocalRandom.current().nextInt(0, hand.size());
        int pebble = hand.get(pebbleIndex);
        hand.remove(pebbleIndex);
        whiteBag.add(pebble);

        int handSize = hand.size();
        int bagSize = whiteBag.size();

        assertThat(handSize, is(9));
        assertThat(bagSize, is(1));
    }

    @Test
    public void testDraw() {
        blackBag.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
        List<Integer> hand = (new ArrayList<>());

        int randomIndex = ThreadLocalRandom.current().nextInt(0, blackBag.size());
        int pebble = blackBag.get(randomIndex);
        blackBag.remove(randomIndex);
        hand.add(pebble);

        int handSize = hand.size();
        int bagSize = blackBag.size();

        assertThat(handSize, is(1));
        assertThat(bagSize, is(10));
    }

    @Test
    public void testDrawEmpty() {
        List<Integer> hand = (new ArrayList<>());
        List<Integer> blackBag = (new ArrayList<>());
        whiteBag.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11));

        int initialBlackBagSize = blackBag.size();
        int initialWhiteBagSize = whiteBag.size();

        if (blackBag.size() == 0) {
            blackBag.addAll(whiteBag);
            whiteBag.clear();
        }

        int blackBagSize = blackBag.size();
        int whiteBagSize = whiteBag.size();

        assertEquals(blackBagSize, initialWhiteBagSize);
        assertEquals(whiteBagSize, initialBlackBagSize);
    }

    @Test
    public void testCheckHand() {
        hand.addAll(Arrays.asList(50, 10, 5, 3, 2, 8, 1, 1, 16, 4));
        boolean winnerStatus = false;
        int sumHand = 0;
        for (int i=0; i<hand.size(); i++) {
            sumHand += hand.get(i);
        }
        if (!winnerStatus && hand.size() == 10) {
            if (sumHand == 100) {
                winnerStatus = true;
            }
        }

        assertTrue(winnerStatus);
    }

    /**
     * initialise starting hand
     * @throws InterruptedException
     */
    public void testStartHand() throws InterruptedException {

        int bagNum = ThreadLocalRandom.current().nextInt(0, 3);

        int currentIndex;
        int currentPebble;
        int sumHand = 0;
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