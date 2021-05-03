import java.util.*;
import java.util.stream.Collectors;

public class Engine {

    private List<Integer> tokens;
    private int n;
    private int r;
    private List<Integer> ks;
    private boolean ended;
    private int map_color;
    private Map<Integer, List<Integer>> death_fields;

    public static void play() {
        // makes new game
        Engine engine = new Engine();
        Engine.initialize(engine);

        Engine.makeFirstTurn(engine);
        int turn = 2;

        while (turn <= engine.getN()) {
            System.out.println("\n---- TURN " + turn + " ----");
            Engine.makeTurn(engine);
            List<Integer> pot_map;
            pot_map = Detector.checkStatus(engine);
            if (engine.isEnded()) {
                System.out.println();
                Engine.showState(engine);
                System.out.println("\n--------------------------");
                System.out.println("There is a MAP of color: " + engine.getMap_color());
                System.out.println("Indexes: " + pot_map);
                System.out.println("Player 1  is THE WINNER!");
                System.out.println("--------------------------");
                break;
            }
            engine.updateDeathFields();
            turn = turn + 1;
        }
        if (!engine.isEnded()) {
            System.out.println();
            Engine.showState(engine);
            System.out.println("\n-----------------------");
            System.out.println("No turns are left.");
            System.out.println("Player 2 is THE WINNER!");
            System.out.println("-----------------------");
        }
    }

    private void initDeath_fields() {
        death_fields = new HashMap<>();
        for (int i = 0; i < r; i++) death_fields.put(i, new ArrayList<>());
    }

    private void updateDeathFields() {
        death_fields = new HashMap<>(Strategist.calculateDeathFields(this, tokens));
    }

    public static void makeFirstTurn(Engine engine) {
        //Scanner scanner = new Scanner(System.in);
        List<Integer> tokens = engine.getTokens();

        System.out.println("\n---- TURN " + 1 + " ----");

        System.out.println("Player 1:");
        System.out.println("Only one position to choose.");
        int col_choice = engine.getKs().indexOf(Collections.max(engine.getKs()));

        System.out.println("\nPlayer 2:");
        System.out.println("Chooses color " + col_choice + ".");
        engine.initDeath_fields();
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
        int col_choice = Strategist.chooseColor(engine, pos);
        System.out.println("\nPlayer 2:");
        System.out.println("Chooses color " + col_choice + ".");

        tokens.add(pos, col_choice);
    }

    public static void showState(Engine engine) {
        List<Integer> tokens = engine.getTokens();
        System.out.println("Current token vector and positions:");
        // pretty position formatting
        int n_len = Integer.toString(engine.getN()).length() + 1;
        int r_len = Integer.toString(engine.getR()).length();
        int field_size = Math.max(n_len, r_len);

        String format_d = "%" + field_size + "d"; //print token
        if (field_size != 2) {
            System.out.print(" ");
        }
        for (int i = 0; i < tokens.toArray().length; i++) {
            System.out.print(String.format(format_d, tokens.toArray()[i]));
        }
        System.out.println();

        String formats = "%" + field_size + "s"; //print lines
        String formats_minus1 = "%" + (field_size - 1) + "s";
        System.out.print(String.format(formats_minus1, "|"));
        for (int i = 1; i < tokens.size() + 1; i++) {
            System.out.print(String.format(formats, "|"));
        }
        System.out.println();

        int[] indexes = new int[tokens.size() + 1];
        for (int i = 0; i < tokens.size() + 1; i++) {
            indexes[i] = i;
        }

        String format_d_minus1 = "%" + (field_size - 1) + "d"; //print indexes
        System.out.print(String.format(format_d_minus1, indexes[0]));
        for (int i = 1; i < indexes.length; i++) {
            System.out.print(String.format(format_d, indexes[i]));
        }
        System.out.println();
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

        System.out.println("\nDefine vector of k_i:" +
                " \n(separated by spaces for each k_i or single number if all identical)");
        System.out.println(">> ... << ");
        String ks_str = scanner.nextLine();

        List<Integer> ks;

        if (ks_str.trim().length() == 1) {
            Integer k = Integer.parseInt(ks_str.trim());
            ks = Collections.nCopies(r, k);
        } else {
            ks = Arrays.stream(ks_str.trim().split("\\s+"))
                    .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        }

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

    public Map<Integer, List<Integer>> getDeath_fields() {
        return death_fields;
    }

}
