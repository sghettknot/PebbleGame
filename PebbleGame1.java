


// UPDATED VERSION OF PebbleGame.java BUT DOES NOT WORK PROPERLY

// I DON'T KNOW WHY AAAAAAAAAAAAAAAAAAAAAAAAAA



package pebble;
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

public class PebbleGame1 {

    static int numOfPlayers = 0;

    private enum WhiteBag { A, B, C; }
    private enum BlackBag { X, Y, Z; }

    public static class Player implements Runnable {

        String playerName; // Name of player. E.g. "player1" or "player2"
        int bagNum; // Number representing the bag. E.g. bag 0 means Black Bag X and White Bag A, 1 is Y/B, 2 is Z/C

        // Variables for some extra features
        int playerTurn = 0; // Counter which records the number of draws a player makes
        int almostWon = 0; // Counter which records the number of times a player was close to winning (90<weight<110)
        int bestAttempt = 0; // Counter which records a player's closest attempt to having a total weight of 100

        List<Integer> hand;
        List<Integer> blackBag = new ArrayList<>();
        List<Integer> whiteBag = new ArrayList<>();
        List<String> output;

        public Player(int num) {
            playerName = "player" + num;
            hand = new ArrayList<>();
            output = new ArrayList<>();
        }
        public void run() {
            playerTurn += 1;
            Random rand = new Random();
            bagNum = rand.nextInt(3);  // from 0-2 (3 bags)

            // Selects a random black bag and its corresponding white bag
            blackBag = BagData.randomBlackBag(bagNum);
            whiteBag = BagData.currentWhiteBag(bagNum);

            // Selects first 10 pebbles at random from a black bag
            for (int i = 0; i < 10; i++) {
                int currentPebble = rand.nextInt(blackBag.size());
                hand.add(blackBag.get(currentPebble));
                blackBag.remove(currentPebble);
            }

            output.add(playerName + " has drawn 10 pebbles at random from black bag " + BlackBag.values()[bagNum]);
            output.add("\r\n" + playerName + " hand is " + hand);


            // Sum of pebbles in hand used to check for winner
            int sum = 0;
            for(int i = 0; i < hand.size(); i++)
                sum += hand.get(i);

            // (1) DISCARD, (2) SELECT, (3) DRAW, then REPEAT until sum = 100 (winning hand)
            while (sum != 100) {

                if (sum >= 90 && sum <= 110) {
                    almostWon += 1;
                    output.add(playerName + " was really close to winning with pebbles in hand weighing " + sum);
                }
                if (sum > bestAttempt && sum < 100) { bestAttempt = sum; }
                else if (sum < bestAttempt && sum > 100) { bestAttempt = sum; }

                // (1) DISCARD to a random white bag
                int randPebble = rand.nextInt(hand.size());
                whiteBag.add(hand.get(randPebble));
                output.add(playerName + " has discarded a " + hand.get(randPebble) + " to white bag "
                        + WhiteBag.values()[bagNum] + "\r\n");
                hand.remove(randPebble);
                output.add(playerName + " hand is " + hand + " (weight: " + sum + ")\r\n");

                // (2) SELECT a new random black bag
                while (true) {
                    bagNum = rand.nextInt(3);
                    blackBag = BagData.randomBlackBag(bagNum);
                    whiteBag = BagData.currentWhiteBag(bagNum);

                    // fills bag if empty
                    if (blackBag.size() == 0) {
                        blackBag.addAll(whiteBag);
                        whiteBag.clear();

                    } else { break; }
                }

                // (3) DRAW a random pebble from the new bag
                int randomIndex = rand.nextInt(blackBag.size());
                output.add(playerName + " has drawn a " + blackBag.get(randomIndex) + " from black bag "
                        + BlackBag.values()[bagNum] + "\r\n");
                hand.add(blackBag.get(randomIndex));
                output.add(playerName + " hand is " + hand + " (weight: " + sum + ")\r\n");
                blackBag.remove(randomIndex);

                int newSum = 0;
                for(int i = 0; i < hand.size(); i++)
                    newSum += hand.get(i);
                sum = newSum;
            }
            // Sum is 100, respective player wins and game ends
            System.out.println("WE HAVE A WINNER!");
            if (playerTurn == 1) {
                System.out.print("UNBELIEVABLE! " + playerName + " has won after only drawing the initial 10 pebbles!");
                output.add(playerName + " has won after only drawing the initial 10 pebbles!");
            } else {
                System.out.println(playerName + " has won after drawing " + playerTurn + " times from the bags!");
                output.add(playerName + " has won after drawing " + playerTurn + " times from the bags!");

            }

            // GAME ENDS HERE. TELL ALL OTHER PLAYERS TO CEASE IMMEDIATELY

            System.exit(0);
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

    // Writes the output for each player to a text file, e.g. to player1_output.txt
    // Boolean to let the user know if this was successful or not (should be successful)
    public static boolean saveOutput() {
        return true;
    }

    public static void main(String[] args) {

        System.out.println("<> Welcome to the Pebble Game! <> \n<> Created by wv211 and mtj202 <>");

        // Sets the number of players in the game by asking for user input checking whether it is valid
        numOfPlayers = Player.getNumOfPlayers();

        // Initiates black bags X, Y, Z
        BagData.createBlackBags();

        // Launches the game with the players acting as concurrent threads, with the first player being "player1"
        for (int i = 1; i < (numOfPlayers + 1); i++) {
            Thread player = new Thread(new Player(i));
            player.start();
        }
    }
}
