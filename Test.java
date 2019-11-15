package SoftDev;
import java.util.*;

public class Test {

    static int numOfPlayer = 0;
    static List<Player> PlayerObjArr = new ArrayList<>();

    public static class Player implements Runnable{

        String playerName;

        public Player (String name) {
            playerName = name;
        }

        public void run() {

            // select random bag
            // draw 10 pebbles
            // while (no winner):
            // check if 100
            // discard random
            // draw random
        }
        public static List<Integer> playerHand() {
            List<Integer> hand = new ArrayList<>();
            List<Integer> bag = new ArrayList<>();
            List<Integer> whiteBag = new ArrayList<>();

            // randomise a number
            Random rand = new Random();
            int randomNumber = rand.nextInt(3);

            // selecting random bag
            bag = Pebble.randomBag(randomNumber);
            whiteBag = Pebble.currentWhiteBag(randomNumber);

            // selecting first 10 random pebbles
            for (int i = 0; i < 10; i++) {
                int currentPebble = rand.nextInt(bag.size());

                hand.add(bag.get(currentPebble-1));
                bag.remove(currentPebble-1);
            }

            // check for winner
            double sum = 0;
            for(int i = 0; i < hand.size(); i++)
                sum += hand.get(i);


            /*
            // check, discard, draw, repeat
            while (sum != 100) {
                // discard to white bag

                // draw from bag
            }
             */

            System.out.println("hand: " + hand);
            System.out.println(bag);
            //System.out.println(whiteBag);
            return hand;
        }


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

        /*
        public static List<Player> generatePlayers(int numPlayer) {
            List<Player> PlayerObj = new LinkedList<Player>();
            for(int i = 0; i < numPlayer; i++) {
                PlayerObj.add(new Player());
            }
            System.out.println(PlayerObj);
            PlayerObjArr = PlayerObj;
            return PlayerObjArr;
        }
         */

    }

    public static void main(String[] args) {

        // Ask user for the number of players
        Player.numOfPlayers();

        // Initialise black bags X, Y and Z
        Pebble.getBlackBagX();
        Pebble.getBlackBagY();
        Pebble.getBlackBagZ();

        //System.out.println(Arrays.toString(Pebble.blackBagX.toArray()));
        //System.out.println(Arrays.toString(Pebble.blackBagY.toArray()));
        //System.out.println(Arrays.toString(Pebble.blackBagZ.toArray()));

        // Generate Player objects
        //Player.generatePlayers(numOfPlayer);

        /*
        // Generate player hand
        for (int i = 1; i < (numOfPlayer + 1); i++) {
            PlayerObjArr.get(i-1).playerHand();
        }
        */

        for (int i = 1; i < (numOfPlayer + 1); i++) {
            Thread player = new Thread(new Player("Player " + i));
            player.start();
        }



        /*
        // Assign map to player objects
        HashMap<String, List<Player>> hash_map = new HashMap<String, List<Player>>();
        for (int i = 1; i < (numOfPlayer + 1); i++) {
            hash_map.put("player ", (List<Player>) PlayerObjArr.get(i-1));
            //System.out.println(PlayerObjArr.get(i-1));
        }
         */
        //System.out.println(hash_map);

        // start threading

    }
}
