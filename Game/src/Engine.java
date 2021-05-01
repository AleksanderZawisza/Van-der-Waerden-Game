import java.util.*;
import java.util.stream.Collectors;

public class Engine {

    private List<Integer> tokens;
    private int n;
    private int r;
    private List<Integer> ks;
    private boolean ended;
    private int map_color;

    public static void play() {
        // makes new game
        Engine engine = new Engine();
        Engine.initialize(engine);

        Engine.makeFirstTurn(engine);
        int turn = 2;

        while (turn <= engine.getN()) {
            System.out.println("\n---- TURN " + turn + " ----");
            Engine.makeTurn(engine);
            List<Integer> pot_map = Detector.checkStatus(engine);
            if (engine.isEnded()) {
                Engine.showState(engine);
                System.out.println("\n--------------------------");
                System.out.println("There is a MAP of color: " + engine.getMap_color());
                System.out.println("Indexes: " + pot_map);
                System.out.println("Player 1  is a WINNER!");
                System.out.println("--------------------------");
                break;
            }
            turn = turn + 1;
        }
        if (!engine.isEnded()) {
            System.out.println("---------------------");
            System.out.println("No turns are left.");
            System.out.println("Player 2 is a WINNER!");
            System.out.println("---------------------");
        }
    }

    public static void makeFirstTurn(Engine engine) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> tokens = engine.getTokens();

        System.out.println("\n---- TURN " + 1 + " ----");

        System.out.println("Player 1:");
        System.out.println("Only one position to choose.");

        // color chosen randomly for now TODO
        Random random = new Random();
        int col_choice = random.nextInt(engine.getR());

        System.out.println("\nPlayer 2:");
        System.out.println("Chooses color " + col_choice + ".");

        tokens.add(0, col_choice);
    }


    public static void makeTurn(Engine engine) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> tokens = engine.getTokens();

        Engine.showState(engine);

        System.out.println("\nPlayer 1:");
        System.out.println("Choose position for next token:");
        System.out.println(">> ... << ");

        String pos_str = scanner.nextLine();
        int pos = Integer.parseInt(pos_str);

        // color chosen randomly for now TODO
        Random random = new Random();
        int col_choice = random.nextInt(engine.getR());

        System.out.println("\nPlayer 2:");
        System.out.println("Chooses color " + col_choice + ".");

        tokens.add(pos, col_choice);
    }

    public static void showState(Engine engine) {
        List<Integer> tokens = engine.getTokens();

        System.out.println("Current token vector and positions:");
        System.out.println(" " + Arrays.toString(tokens.toArray()));
        String str = "|  ";
        String repeated = str.repeat(tokens.size() + 1);
        System.out.println(" " + repeated);
        int[] indexes = new int[tokens.size() + 1];
        for (int i = 0; i < tokens.size() + 1; i++) {
            indexes[i] = i;
        }
        System.out.println(Arrays.toString(indexes));
    }

    public static void initialize(Engine engine) {
        // initializes all properties of game engine
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDefine n:");
        System.out.println(">> ... << ");
        String n_str = scanner.nextLine();
        int n = Integer.parseInt(n_str);

        List<Integer> tokens = new ArrayList<>();

        engine.setTokens(tokens);
        engine.setN(n);
        engine.setEnded(false);
        Engine.start(engine);
    }

    public static void start(Engine engine) {
        // for Detector demo
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDefine r:");
        System.out.println(">> ... << ");
        String r_str = scanner.nextLine();
        int r = Integer.parseInt(r_str);
        //System.out.println(r);

        System.out.println("\nDefine vector of k_i: \n(separated by spaces)");
        System.out.println(">> ... << ");
        String ks_str = scanner.nextLine();

        List<Integer> ks = Arrays.stream(ks_str.trim().split("\\s+"))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        if (ks.size() != r) {
            throw new IllegalStateException("Length of vector k does not match r!");
        }
        //System.out.println(Arrays.toString(ks));

        engine.setR(r);
        engine.setKs(ks);
    }

    public void setN(int n) {
        this.n = n;
    }

    public void setR(int r) {
        this.r = r;
    }

    public void setKs(List<Integer> ks) {
        this.ks = ks;
    }

    public void setTokens(List<Integer> tokens) {
        this.tokens = tokens;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public void setMap_color(int map_color) {
        this.map_color = map_color;
    }

    public int getN() {
        return n;
    }

    public int getR() {
        return r;
    }

    public List<Integer> getKs() {
        return ks;
    }

    public List<Integer> getTokens() {
        return tokens;
    }

    public boolean isEnded() {
        return ended;
    }

    public int getMap_color() {
        return map_color;
    }
}
