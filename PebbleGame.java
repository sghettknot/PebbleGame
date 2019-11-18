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
    static List<Player> PlayerObjArr = new ArrayList<>();

    public static class Player implements Runnable{

        int playerName; // Name (number) of player. E.g. Player 1 (playerName = 1), player 2 (playerName = 2) etc.

        List<Integer> hand;
        List<Integer> blackBag = new ArrayList<>();
        List<Integer> whiteBag = new ArrayList<>();
        List<String> output;

        public Player(int num) {
            playerName = num;
            hand = new ArrayList<>();
            output = new ArrayList<>();
        }

        List<Integer> tempBlackBag = new ArrayList<>();
        List<Integer> tempWhiteBag = new ArrayList<>();

        public void run() {

            // randomise a number
            Random rand = new Random();
            int randomNumber = rand.nextInt(3);  // from 0-2 (3 bags)

            // selecting random bag
            blackBag = Pebble.randomBlackBag(randomNumber);
            whiteBag = Pebble.currentWhiteBag(randomNumber);

            // selecting first 10 random pebbles
            for (int i = 0; i < 10; i++) {
                int currentPebble = rand.nextInt(blackBag.size());

                hand.add(blackBag.get(currentPebble));
                blackBag.remove(currentPebble);
            }

            System.out.println("STARTING hand of player " + playerName + " : " + hand);

            // check for winner
            double sum = 0;
            for(int i = 0; i < hand.size(); i++)
                sum += hand.get(i);

            // check, discard, draw, repeat
            while (sum != 100) {

                Random rand1 = new Random();
                // discard to white bag
                int randPebble = rand1.nextInt(hand.size());
                whiteBag.add(hand.get(randPebble));
                hand.remove(randPebble);

                // select a new random bag
                Random rand2 = new Random();
                int randomNumber2 = rand2.nextInt(3);
                blackBag = Pebble.randomBlackBag(randomNumber2);
                whiteBag = Pebble.currentWhiteBag(randomNumber2);

                if (blackBag.size() == 0) {

                    System.out.println("============ <> IF STATEMENT <> ============");
                    // fill
                    blackBag.addAll(whiteBag);
                    whiteBag.clear();

                    System.out.println("<> Black bag: " + Arrays.toString(blackBag.toArray()));
                    System.out.println("<> White bag: " + Arrays.toString(whiteBag.toArray()));

                    // select new bag
                    while (tempBlackBag.isEmpty()){
                        int randomNumber4 = rand.nextInt(3);
                        tempBlackBag = Pebble.randomBlackBag(randomNumber4);
                        tempWhiteBag = Pebble.randomBlackBag(randomNumber4);
                    }
                    System.out.println("<> NEW black bag: " + tempBlackBag);
                    System.out.println("<> NEW size: " + tempBlackBag.size());

                    // draw from new bag
                    Random rand5 = new Random();
                    int randomIndex = rand5.nextInt(tempBlackBag.size());
                    hand.add(tempBlackBag.get(randomIndex));
                    tempBlackBag.remove(randomIndex);

                    System.out.println("============ <> CONTINUE <> ============");
                    continue;

                    /* OLD (BROKEN) VERSION
                    int randomNumber4 = rand.nextInt(3);
                    System.out.println("<> BEFORE blackBag size: " + blackBag.size());
                    blackBag = Pebble.randomBlackBag(randomNumber4);
                    System.out.println("<> AFTER blackBag size: " + blackBag.size());
                    whiteBag = Pebble.currentWhiteBag(randomNumber4);
                    System.out.println("<> whiteBag size: " + whiteBag.size());

                    System.out.println("============ <> END OF IF STATEMENT <> ============");
                    //continue;
                    */
                }

                // draw a random pebble from the new random bag
                Random rand3 = new Random();
                int randomIndex = rand3.nextInt(blackBag.size());
                hand.add(blackBag.get(randomIndex));
                blackBag.remove(randomIndex);

                System.out.println("hand of player " + playerName + ": " + hand);
                System.out.println("black bag: " + blackBag);
                System.out.println("white bag: " + whiteBag);

                int newSum = 0;
                for(int i = 0; i < hand.size(); i++)
                    newSum += hand.get(i);
                sum = newSum;
                System.out.println("Sum = " + sum);
            }

            // Sum is 100 !!!
            System.out.println("WE HAVE A WINNER! Player " + playerName);
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

        // Initiating black bags X, Y, Z
        Pebble.createBlackBags();

        // Launching the game with the players acting as concurrent threads
        for (int i = 1; i < (numOfPlayers + 1); i++) {
            Thread player = new Thread(new Player(i));
            player.start();
        }
    }
}
