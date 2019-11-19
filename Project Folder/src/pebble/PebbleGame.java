package pebble;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

    static int numOfPlayers = 0;
    static String[] output;

    private enum WhiteBag { A, B, C; }
    private enum BlackBag { X, Y, Z; }

    public static class Player implements Runnable {

        String playerName; // Name (number) of player. E.g. Player 1 (playerName = 1), player 2 (playerName = 2) etc.
        int playerIndex;

        List<Integer> hand = new ArrayList<>();
        List<Integer> blackBag = new ArrayList<>();
        List<Integer> whiteBag = new ArrayList<>();

        public Player(int num) { playerName = "Player " + num;  playerIndex = num - 1; }

        public synchronized void startHand() throws InterruptedException {

            int bagNum; // Number representing the bag. E.g. bag 0 means Black Bag X and White Bag A, 1 is Y/B, 2 is Z/C

            Thread.sleep((long)(Math.random() * 50));
            Random rand = new Random();
            bagNum = rand.nextInt(3);  // from 0-2 (3 bags)

            // selecting random bag
            blackBag = Bags.randomBlackBag(bagNum);
            whiteBag = Bags.currentWhiteBag(bagNum);

            // selecting first 10 random pebbles
            for (int i = 0; i < 10; i++) {
                Thread.sleep((long)(Math.random() * 50));
                int currentPebbleIndex = rand.nextInt(blackBag.size());
                int currentPebble = blackBag.get(currentPebbleIndex);

                hand.add(blackBag.get(currentPebble));
                blackBag.remove(currentPebble);
            }

            System.out.println("OUTPUT: " + output);
            System.out.println("INDEX: " + playerIndex);
            System.out.println("LENGTH: " + output.length);

            output[playerIndex] += playerName + " has drawn 10 pebbles at random from black bag " + BlackBag.values()[bagNum];
            output[playerIndex] += "\r\n" + playerName + " hand is " + hand;



            System.out.println(playerName + " has drawn 10 pebbles at random from black bag " + BlackBag.values()[bagNum]);
            System.out.println(playerName + " hand is " + hand);

            // sum to check for winner
            int sum = 0;
            for(int i = 0; i < hand.size(); i++)
                sum += hand.get(i);

            // (1) DISCARD, (2) SELECT, (3) DRAW, REPEAT until sum = 100
            while (sum != 100) {

                // (1) DISCARD to white bag
                int randPebble = rand.nextInt(hand.size());
                whiteBag.add(hand.get(randPebble));
                output[playerIndex] += playerName + " has discarded a " + hand.get(randPebble) + " to white bag "
                        + WhiteBag.values()[bagNum] + "\r\n";

                System.out.println(playerName + " has discarded a " + hand.get(randPebble) + " to white bag "
                        + WhiteBag.values()[bagNum]);

                hand.remove(randPebble);
                output[playerIndex] += playerName + " hand is " + hand + " (weight: " + sum + ")\r\n";

                System.out.println(playerName + " hand is " + hand + " (weight: " + sum + ")");

                Thread.sleep((long)(Math.random() * 100));

                // (2) SELECT a new random bag
                while (true) {
                    bagNum = rand.nextInt(3);
                    blackBag = Bags.randomBlackBag(bagNum);
                    whiteBag = Bags.currentWhiteBag(bagNum);

                    // fills bag if empty
                    if (blackBag.size() == 0) {
                        blackBag.addAll(whiteBag);
                        whiteBag.clear();

                    } else { break; }
                }

                // (3) DRAW a random pebble from the new random bag
                int randomIndex = rand.nextInt(blackBag.size());
                output[playerIndex] += playerName + " has drawn a " + blackBag.get(randomIndex) + " from black bag "
                        + BlackBag.values()[bagNum] + "\r\n";

                System.out.println(playerName + " has drawn a " + blackBag.get(randomIndex) + " from black bag "
                        + BlackBag.values()[bagNum]);

                hand.add(blackBag.get(randomIndex));
                blackBag.remove(randomIndex);
                output[playerIndex] += playerName + " hand is " + hand + " (weight: " + sum + ")\r\n";

                System.out.println(playerName + " hand is " + hand + " (weight: " + sum + ")");

                Thread.sleep((long)(Math.random() * 100));

                System.out.println("<BAG INFO> black bag " + BlackBag.values()[bagNum] + ": " + blackBag);
                System.out.println("<BAG INFO> white bag " + WhiteBag.values()[bagNum] + ": " + whiteBag);

                int newSum = 0;
                for(int i = 0; i < hand.size(); i++)
                    newSum += hand.get(i);
                sum = newSum;
            }
            // Sum is 100 !!!
            System.out.println(playerName + " has won!");
            output[playerIndex] += playerName + " has won!";
            saveOutputs();
            System.exit(0);
        }

        public void run() {
            synchronized (this) {
                try {
                    startHand();
                } catch (InterruptedException e) {}
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
        //Bags.createBlackBags();

        //Test function. Note, this bypasses any requirements or checks.
        Bags.force_createBlackBags("file2.csv", "example_file_1.csv", "file3.csv");

        // Creates the array in which the player output log information is stored
        output = new String[numOfPlayers];
        for (int i = 0; i < numOfPlayers; i++) {
            output[i] = "";
        }
        System.out.print(output.length);

        // Launches the game with the players acting as concurrent threads
        for (int i = 1; i < (numOfPlayers + 1); i++) {
            Thread player = new Thread(new Player(i));
            player.start();
        }

    }
}
