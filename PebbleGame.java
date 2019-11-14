package SoftDev;
import java.util.*;

public class PebbleGame {

    static int numOfPlayer = 0;

    public static class Player {

        public static int numOfPlayers() {
            Scanner myScanner = new Scanner(System.in);
            System.out.println("Enter the number of players: ");
            int num = myScanner.nextInt();
            if (num > 0) {
                System.out.println("number of players is " + num);
                numOfPlayer = num;
                return numOfPlayer;
            } else {
                System.out.println("Please enter a positive number of players!");
                System.exit(0);
                return numOfPlayer;
            }
        }

        public static List<Player> generatePlayers(int numPlayer) {
            List<Player> PlayerObj = new LinkedList<Player>();
            for(int i = 0; i < numPlayer; i++) {
                PlayerObj.add(new Player());
            }
            System.out.println(PlayerObj);
            return PlayerObj;
        }
    }

    public static void main(String[] args) {

        // Ask user for the number of players
        Player.numOfPlayers();

        // Initialise black bags X, Y and Z
        Pebble.getBlackBagX();
        Pebble.getBlackBagY();
        Pebble.getBlackBagZ();

        System.out.println(Arrays.toString(Pebble.blackBagX.toArray()));
        System.out.println(Arrays.toString(Pebble.blackBagY.toArray()));
        System.out.println(Arrays.toString(Pebble.blackBagZ.toArray()));

        // Generate Player objects
        Player.generatePlayers(numOfPlayer);

        // Thread Start

        // Each player picks 10 pebbles from a random black bag

        // check for winner, discard, draw

    }
}
