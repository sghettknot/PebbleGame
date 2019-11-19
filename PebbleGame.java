package SoftDev;
import java.util.*;

/**
 * Main class for the pebble game.
 * Players are implemented here as nested classes and act as concurrent threads.
 *
 * Created using Java version:     <><><><><><><><><><><><><> ADD JAVA VERSION   AND   SDK VERSION HERE <><><><><><><><><><><><><>
 *
 * ECM2414 - Software Development - CA 2019 - Pebble Game
 *
 * @author wv211
 * @author mtj202
 * @version 18 November 2019
 **/

public class PebbleGame {

    static int numOfPlayers = 0;

    private enum WhiteBag { A, B, C; }
    private enum BlackBag { X, Y, Z; }

    public static class Player implements Runnable {

        String playerName; // Name (number) of player. E.g. Player 1 (playerName = 1), player 2 (playerName = 2) etc.
        int bagNum; // Number representing the bag. E.g. bag 0 means Black Bag X and White Bag A, 1 is Y/B, 2 is Z/C

        List<Integer> hand;
        List<Integer> blackBag = new ArrayList<>();
        List<Integer> whiteBag = new ArrayList<>();
        List<String> output;

        public Player(int num) {
            playerName = "Player " + Integer.toString(num);
            hand = new ArrayList<>();
            output = new ArrayList<>();
        }

        public void startHand() throws InterruptedException {
            Thread.sleep((long)(Math.random() * 50));
            Random rand = new Random();
            bagNum = rand.nextInt(3);  // from 0-2 (3 bags)

            // selecting random bag
            blackBag = Pebble.randomBlackBag(bagNum);
            whiteBag = Pebble.currentWhiteBag(bagNum);

            System.out.println("STARTING BLACKBAG: " + BlackBag.values()[bagNum]);
            System.out.println("STARTING WHITEBAG: " + WhiteBag.values()[bagNum]);

            // selecting first 10 random pebbles
            for (int i = 0; i < 10; i++) {
                Thread.sleep((long)(Math.random() * 50));
                int currentPebbleIndex = rand.nextInt(blackBag.size());
                int currentPebble = blackBag.get(currentPebbleIndex);

                hand.add(blackBag.get(currentPebble));
                blackBag.remove(currentPebble);
            }

            //output.add(playerName + "draws");

            // sum to check for winner
            int sum = 0;
            for(int i = 0; i < hand.size(); i++)
                sum += hand.get(i);

            // (1) DISCARD, (2) SELECT, (3) DRAW, REPEAT until sum = 100
            while (sum != 100) {

                // (1) DISCARD to white bag
                int randPebble = rand.nextInt(hand.size());
                whiteBag.add(hand.get(randPebble));
                hand.remove(randPebble);

                Thread.sleep((long)(Math.random() * 50));

                // (2) SELECT a new random bag
                while (true) {
                    bagNum = rand.nextInt(3);
                    blackBag = Pebble.randomBlackBag(bagNum);
                    whiteBag = Pebble.currentWhiteBag(bagNum);

                    // fills bag if empty
                    if (blackBag.size() == 0) {
                        blackBag.addAll(whiteBag);
                        whiteBag.clear();

                    } else { break; }
                }

                // (3) DRAW a random pebble from the new random bag
                int randomIndex = rand.nextInt(blackBag.size());
                hand.add(blackBag.get(rand.nextInt(blackBag.size())));
                blackBag.remove(randomIndex);

                Thread.sleep((long)(Math.random() * 100));

                System.out.println("hand of " + playerName + ": " + hand);
                System.out.println("black bag " + BlackBag.values()[bagNum] + ": " + blackBag);
                System.out.println("white bag: " + WhiteBag.values()[bagNum] + ": " + whiteBag);

                int newSum = 0;
                for(int i = 0; i < hand.size(); i++)
                    newSum += hand.get(i);
                sum = newSum;
                System.out.println("Sum = " + sum);
            }
            // Sum is 100 !!!
            System.out.println("WE HAVE A WINNER! " + playerName);
            // <><><><><><><><> Write output to file for each player here. Maybe use method with for loop.
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
    }

    public static void main(String[] args) {

        System.out.println("<> Welcome to the Pebble Game! <> \n<> Created by wv211 and mtj202 <>");

        // Sets the number of players in the game by asking for user input checking whether it is valid
        numOfPlayers = Player.getNumOfPlayers();

        // Initiates black bags X, Y, Z
        Pebble.createBlackBags();

        // Launches the game with the players acting as concurrent threads
        for (int i = 1; i < (numOfPlayers + 1); i++) {
            Thread player = new Thread(new Player(i));
            player.start();
        }
    }
}
