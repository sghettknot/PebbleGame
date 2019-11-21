package pebble;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class for the pebble game.
 * Players are implemented here as nested classes and act as concurrent threads.
 *
 * Created using Java version: 8  (and SDK version used: 11.0.4)
 *
 * ECM2414 - Software Development - CA 2019 - Pebble Game
 *
 * @author wv211
 * @author mtj202
 * @version 18 November 2019
 **/

public class PebbleGame {

    public static int numOfPlayers = 0;
    private static int winner = 0;
    private static int draws = 0;
    private static int sleepMultiplierValue = 1;
    private static boolean winnerStatus = false;
    private static String[] output;
    private static AtomicInteger playersReady = new AtomicInteger();
    private static volatile boolean playersToldToStart = false;

    private enum WhiteBag { A, B, C; }
    private enum BlackBag { X, Y, Z; }

    public static class Player implements Runnable {

        String playerName; // Name (number) of player. E.g. Player 1 (playerName = 1), player 2 (playerName = 2) etc.
        int playerIndex;
        private int sumHand;

        // Variables for some extra features
        int playerTurn = 1; // Counter which records the number of draws a player makes
        int almostWon = 0; // Counter which records the number of times a player was close to winning (90<weight<110)
        int bestAttempt = 0; // Counter which records a player's closest attempt to having a total weight of 100

        List<Integer> hand = (new ArrayList<>());
        List<Integer> blackBag = (new ArrayList<>());
        List<Integer> whiteBag = (new ArrayList<>());

        private static int playerBagNum;

        public Player(int num) {
            playerName = "Player " + num;
            playerIndex = num - 1;
        }

        public synchronized void startHand() throws InterruptedException {

            int bagNum = ThreadLocalRandom.current().nextInt(0, 3);

            int currentIndex;
            int currentPebble;

            // selecting random bag
            blackBag = Bags.randomBlackBag(bagNum);
            whiteBag = Bags.currentWhiteBag(bagNum);

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
            playerBagNum = bagNum;
            whiteBag = Bags.currentWhiteBag(playerBagNum);

            output[playerIndex] += playerName + " has drawn 10 pebbles at random from black bag " + BlackBag.values()[bagNum];
            output[playerIndex] += "\r\n" + playerName + " starting hand is " + hand + "\r\n";

            System.out.println(playerName + " has drawn 10 pebbles at random from black bag " + BlackBag.values()[bagNum]);
            System.out.println(playerName + " starting hand is " + hand);

            checkHand();
        }

        public void discard() throws InterruptedException {

            whiteBag = Bags.currentWhiteBag(playerBagNum);

            int pebbleIndex = ThreadLocalRandom.current().nextInt(0, hand.size());
            int pebble = hand.get(pebbleIndex);
            hand.remove(pebbleIndex);
            sumHand -= pebble;

            whiteBag.add(pebble);

            output[playerIndex] += playerName + " has discarded a " + pebble + " to white bag "
                    + PebbleGame.WhiteBag.values()[playerBagNum] + "\r\n";

            System.out.println(playerName + " has discarded a " + pebble + " to white bag "
                    + PebbleGame.WhiteBag.values()[playerBagNum]);

            output[playerIndex] += playerName + " hand is " + hand + " (weight: " + sumHand + ")\r\n";

            System.out.println(playerName + " hand is " + hand + " (weight: " + sumHand + ")");

            checkHand();
        }

        public void draw() throws InterruptedException {

            int bagNum = ThreadLocalRandom.current().nextInt(0, 3);

            while (true) {

                blackBag = Bags.randomBlackBag(bagNum);
                whiteBag = Bags.currentWhiteBag(bagNum);

                // fills bag if empty
                if (blackBag.size() == 0) {
                    blackBag.addAll(whiteBag);
                    whiteBag.clear();

                } else { break; }
            }

            int randomIndex = ThreadLocalRandom.current().nextInt(0, blackBag.size());
            int pebble = blackBag.get(randomIndex);
            blackBag.remove(randomIndex);

            synchronized (blackBag) {

                hand.add(pebble);

                sumHand += pebble;

                output[playerIndex] += playerName + " has drawn a " + pebble + " from black bag "
                        + PebbleGame.BlackBag.values()[bagNum] + "\r\n";

                System.out.println(playerName + " has drawn a " + pebble + " from black bag "
                        + PebbleGame.BlackBag.values()[bagNum]);

                output[playerIndex] += playerName + " hand is " + hand + " (weight: " + sumHand + ")\r\n";

                System.out.println(playerName + " hand is " + hand + " (weight: " + sumHand + ")");

                Thread.sleep((long)(Math.random() * 50 * sleepMultiplierValue));

                checkHand();
            }
            playerBagNum = bagNum;
        }

        public void checkHand() throws InterruptedException{
            //Thread.sleep((long)(Math.random() * 2000));
            if (!winnerStatus && hand.size() == 10) {
                if (sumHand == 100) {
                    winnerStatus = true;
                    winner = playerIndex + 1;  // first player is "Player 1" (but index is 0), hence '+1'
                    output[playerIndex] += playerName + " has won after making " + playerTurn + " draws!";
                    draws = playerTurn;
                }
                else {
                    if (sumHand >= 90 && sumHand <= 110) {
                        almostWon += 1;
                    }
                    if ((sumHand > bestAttempt && sumHand < 100) || (sumHand < bestAttempt && sumHand > 100)) {
                        bestAttempt = sumHand;
                    }
                }
            }
        }
        public void gameInterrupted(){
            output[playerIndex] += "Game has been interrupted. Any outputs which have been recorded are shown above.";
        }
        @Override
        public void run() {
            try {
                startHand();
                /*
                START OF PROBLEM
                */
                //  Check and wait for players to fill their hand
                if (playersReady.get() != numOfPlayers) {
                    synchronized (playersReady) {
                        try {
                            playersReady.wait();

                        } catch (InterruptedException e) {
                            gameInterrupted();
                        }
                    }
                }
                else if (playersToldToStart) {
                    synchronized(playersReady) {
                        playersReady.notifyAll();
                    }
                    playersToldToStart = true;
                }
                /*
                END OF PROBLEM
                 */

                while (!winnerStatus && !Thread.currentThread().isInterrupted()) {
                    playerTurn += 1;
                    discard();
                    draw();
                }
                if(Thread.currentThread().isInterrupted()) {
                    gameInterrupted();
                }
                if (playerIndex != winner - 1) {  // first player is "Player 1" (but index is 0), hence '-1'
                    output[playerIndex] += "The game has ended with player" + winner + " as the winner.r\r\n";
                    output[playerIndex] += playerName + " has drawn from a bag a total of " + playerTurn + " times!";
                    output[playerIndex] += playerName + " was really close to winning " + almostWon + " times!";
                    output[playerIndex] += playerName + " hand closest to a weight of 100 was " + bestAttempt;
                }
            } catch (InterruptedException e) {
                gameInterrupted();
            }
        }
    }

    // Gets the user integer input to determine the number of players participating in the game
    public static int getNumOfPlayers() {
        do {
            try {
                Scanner myScanner = new Scanner(System.in);
                System.out.println("Enter the number of players: ");
                int num = myScanner.nextInt();
                if (num > 0) {
                    System.out.println("Number of players has been set to " + num);
                    return num;
                } else {
                    System.out.println("Error: Please enter a positive integer for the number of players!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: You must enter an integer to specify the number of players!");
            }
        } while (true);
    }

    // Writes the output for each player to a file
    public static void saveOutputs(){
        try {
            // Creates new directory for the outputs
            File dirLocation = new File("Player Outputs/");
            // Removes any old output files
            if (!dirLocation.mkdir()) {
                for (File file : dirLocation.listFiles()) {
                    file.delete();
                }
            }
            // Writes the output files to the directory
            for (int i = 1; i < (numOfPlayers + 1); i++) {
                Path path = Paths.get("Player Outputs/player" + i + "_output.txt");
                Files.write(path, Collections.singleton((output[i - 1])), StandardCharsets.UTF_8);
            }
            System.out.println("Player outputs have successfully been written to text files in the " +
                    "directory \"Player Outputs\".");

        } catch (IOException e) { System.out.println("Failed to write outputs to file. "); }
    }

    public static void main(String[] args) {

        System.out.println("<> Welcome to the Pebble Game! <> \n<> Created by wv211 and mtj202 <>");

        // Sets the number of players in the game by asking for user input checking whether it is valid
        numOfPlayers = getNumOfPlayers();

        // Initiates black bags X, Y, Z
        //Bags.createBlackBags();

        //Test function. Creates the black bags from the input files.
        Bags.force_createBlackBags("file10000.csv", "file10000.csv", "file10000.csv");

        // Creates the array in which the player output log information is stored
        output = new String[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            output[i] = "";
        }

        System.out.print("The game will now begin and each player will select their first initial 10 pebbles." +
                "\nEach player will then wait to make sure that every player has drawn their starting hand." +
                "\nPlease be patient for the check to commence.");

        // Launches the game with the players acting as concurrent threads
        ExecutorService es = Executors.newFixedThreadPool(numOfPlayers);
        for (int i = 1; i < (numOfPlayers + 1); i++) {
            Thread player = new Thread(new Player(i));
            es.execute(new Thread(player));
        }
        es.shutdown();
        try {
            es.awaitTermination(1, TimeUnit.MINUTES);

        } catch(InterruptedException e) {
            e.printStackTrace();
        }
        if(!es.isTerminated()){
            es.shutdownNow();
            System.out.println("Game ran for over 1 minute and it may be impossible to simulate it." +
                    "\nGame has been interrupted.");
        }

        System.out.println("Player " + winner + " has won after making " + draws + " draws!");

        saveOutputs();
    }
}