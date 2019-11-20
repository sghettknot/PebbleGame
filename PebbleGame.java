package pebble;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

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

    static int globalNum;

    private static boolean winnerStatus = false;
    static int numOfPlayers = 0;
    static String[] output;

    private enum WhiteBag { A, B, C; }
    private enum BlackBag { X, Y, Z; }

    public static class Player implements Runnable {

        String playerName; // Name (number) of player. E.g. Player 1 (playerName = 1), player 2 (playerName = 2) etc.
        int playerIndex;
        private int sumHand;

        List<Integer> hand = (new ArrayList<>());
        List<Integer> blackBag = (new ArrayList<>());
        List<Integer> whiteBag = (new ArrayList<>());

        public Player(int num) { playerName = "Player " + num;  playerIndex = num - 1; }

        static int bagNum; // Number representing the bag. E.g. bag 0 means Black Bag X and White Bag A, 1 is Y/B, 2 is Z/C


        public synchronized void startHand() throws InterruptedException {

            int bagNum = ThreadLocalRandom.current().nextInt(0, 3);

            int currentIndex;
            int currentPebble;

            // selecting random bag
            blackBag = Bags.randomBlackBag(bagNum);
            whiteBag = Bags.currentWhiteBag(bagNum);
            //blackBag = randomBag();

            // selecting first 10 random pebbles
            for (int i = 0; i < 10; i++) {
                // change bags if not enough to choose from
                if (blackBag.size() <= 9) {
                    System.out.println("PIKACHU");
                    boolean isEmpty = true;
                    while (isEmpty = true) {
                        bagNum = ThreadLocalRandom.current().nextInt(0, 3);
                        blackBag = Bags.randomBlackBag(bagNum);
                        whiteBag = Bags.currentWhiteBag(bagNum);
                        if (blackBag.size() >11) {
                            isEmpty = false;
                        }
                    }
                }
                synchronized (blackBag) {
                    currentIndex = ThreadLocalRandom.current().nextInt(0, blackBag.size());
                    currentPebble = blackBag.get(currentIndex);
                    blackBag.remove(currentIndex);
                    //removePebble(bagNum, currentIndex);

                    //System.out.println("black bag " + BlackBag.values()[bagNum] + " is now " + blackBag);
                }
                //

                hand.add(currentPebble);
                sumHand += currentPebble;
            }
            globalNum = bagNum;
            whiteBag = Bags.currentWhiteBag(globalNum);
            //System.out.println("OUTPUT: " + output);
            //System.out.println("INDEX: " + playerIndex);
            //System.out.println("LENGTH: " + output.length);

            output[playerIndex] += playerName + " has drawn 10 pebbles at random from black bag " + BlackBag.values()[bagNum];
            output[playerIndex] += "\r\n" + playerName + " starting hand is " + hand;

            System.out.println(playerName + " has drawn 10 pebbles at random from black bag " + BlackBag.values()[bagNum]);
            System.out.println(playerName + " starting hand is " + hand);

            if (sumHand == 100){
                System.out.println(playerName + " has won!");
                output[playerIndex] += playerName + " has won!";
                saveOutputs();
                winnerStatus = true;
                System.exit(0);
            }

            //System.out.println("black bag " + BlackBag.values()[bagNum] + " is now " + blackBag);

            //Thread.sleep((long)(Math.random() * 50));

            firstDiscard(bagNum);
        }

        public void firstDiscard(int bagNum) {
            if (bagNum == 0) {
                whiteBag = Bags.whiteBagA;
            } else if (bagNum == 1) {
                whiteBag = Bags.whiteBagB;
            } else { whiteBag = Bags.whiteBagC ;}

            int pebbleIndex = ThreadLocalRandom.current().nextInt(0, hand.size());
            int pebble = hand.get(pebbleIndex);
            hand.remove(pebbleIndex);
            sumHand -= pebble;

            whiteBag.add(pebble);

            output[playerIndex] += playerName + " has discarded a " + pebble + " to white bag "
                    + PebbleGame.WhiteBag.values()[bagNum] + "\r\n";

            System.out.println(playerName + " has discarded a " + pebble + " to white bag "
                    + PebbleGame.WhiteBag.values()[bagNum]);

            output[playerIndex] += playerName + " hand is " + hand + " (weight: " + sumHand + ")\r\n";

            System.out.println(playerName + " hand is " + hand + " (weight: " + sumHand + ")");

            if (sumHand == 100){
                System.out.println(playerName + " has won!");
                output[playerIndex] += playerName + " has won!";
                saveOutputs();
                winnerStatus = true;
                System.exit(0);
            }
        }


        public void discard() {

            whiteBag = Bags.currentWhiteBag(globalNum);
            //System.out.println("CURRENT PAIR: " + PebbleGame1.BlackBag.values()[globalNum] + "/" + PebbleGame1.WhiteBag.values()[globalNum]);

            int pebbleIndex = ThreadLocalRandom.current().nextInt(0, hand.size());
            int pebble = hand.get(pebbleIndex);
            hand.remove(pebbleIndex);
            sumHand -= pebble;


            whiteBag.add(pebble);

            output[playerIndex] += playerName + " has discarded a " + pebble + " to white bag "
                    + PebbleGame.WhiteBag.values()[globalNum] + "\r\n";

            System.out.println(playerName + " has discarded a " + pebble + " to white bag "
                    + PebbleGame.WhiteBag.values()[globalNum]);

            output[playerIndex] += playerName + " hand is " + hand + " (weight: " + sumHand + ")\r\n";

            System.out.println(playerName + " hand is " + hand + " (weight: " + sumHand + ")");

            if (sumHand == 100){
                System.out.println(playerName + " has won!");
                output[playerIndex] += playerName + " has won!";
                saveOutputs();
                winnerStatus = true;
                System.exit(0);
            }

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

            //System.out.println("CURRENT PAIR: " + PebbleGame1.BlackBag.values()[bagNum] + "/" + PebbleGame1.WhiteBag.values()[bagNum]);

            int randomIndex = ThreadLocalRandom.current().nextInt( 0, blackBag.size() );
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

                Thread.sleep((long)(Math.random() * 50));

                if (sumHand == 100){
                    System.out.println(playerName + " has won!");
                    output[playerIndex] += playerName + " has won!";
                    saveOutputs();
                    winnerStatus = true;
                    System.exit(0);
                }
            }
            globalNum = bagNum;
        }

        public void run() {
            // initial 10 pebbles
            try {
                startHand();
                // keep playing until there is a winner
                draw();
                while (!winnerStatus) {
                    discard();
                    draw();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
                        "directory \"Player Outputs\"");

            } catch (IOException e) { System.out.println("Failed to write outputs to file."); }
        }
    }

    public static void main(String[] args) {

        System.out.println("<> Welcome to the Pebble Game! <> \n<> Created by wv211 and mtj202 <>");

        // Sets the number of players in the game by asking for user input checking whether it is valid
        numOfPlayers = Player.getNumOfPlayers();

        // Initiates black bags X, Y, Z
        Bags.createBlackBags();

        //Player.bbx = Bags.createBagX();
        //Player.bby = Bags.createBagY();
        //Player.bbz = Bags.createBagZ();

        //Test function. Note, this bypasses any requirements or checks.
        //Bags.force_createBlackBags("file2.csv", "example_file_1.csv", "file3.csv");

        // Creates the array in which the player output log information is stored
        output = new String[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            output[i] = "";
        }
        //System.out.print(output.length);


        // Launches the game with the players acting as concurrent threads
        for (int i = 1; i < (numOfPlayers + 1); i++) {
            Thread player = new Thread(new Player(i));
            player.start();
        }




    }
}
