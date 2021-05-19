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

    public static void play(Debugger debugger) {
        // makes new game
        Engine engine = new Engine();
        Engine.initialize(engine);

        Engine.makeFirstTurn(engine);
        int turn = 2;

        while (turn <= engine.getN()) {
            System.out.println("\n---- TURN " + turn + " ----");
            Engine.makeTurn(engine, debugger);
            List<Integer> pot_map;
            pot_map = Detector.checkStatus(engine);
            if (engine.isEnded()) {
                System.out.println();
                Engine.showState(engine);
                System.out.println("\n--------------------------");
                System.out.println("There is a MAP of color: " + engine.getMap_color());
                System.out.println("Indexes: " + pot_map);
                System.out.println("Player 1 is THE WINNER!");
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


    public static void makeTurn(Engine engine, Debugger debugger) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> tokens = engine.getTokens();
        Engine.showState(engine);

        System.out.println("\nPlayer 1:");
        System.out.println("Choose position for next token:");
        System.out.println(">> ... << ");

        int pos = -1;
        while (pos < 0 || pos > tokens.size()) {
            try {
                pos = Integer.parseInt(scanner.nextLine());
                if (pos >= 0 && pos <= tokens.size()) break;
            } catch (Exception ignored) {
            }
            System.out.println("Invalid position! (Range: 0 - " + tokens.size() + ")");
            System.out.println(">> ... << ");
        }
        int col_choice = Strategist.chooseColor(engine, pos, debugger);
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

        int n = -1;
        while (n < 0) {
            try {
                n = Integer.parseInt(scanner.nextLine());
                if (n > 0) break;
            } catch (Exception ignored) {
            }
            System.out.println("Invalid input!");
            System.out.println(">> ... << ");
        }

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
        int r = -1;
        while (r < 0) {
            try {
                r = Integer.parseInt(scanner.nextLine());
                if (r > 0) break;
            } catch (Exception ignored) {
            }
            System.out.println("Invalid input!");
            System.out.println(">> ... << ");
        }

        System.out.println("\nDefine vector of k_i:" +
                " \n(separated by spaces for each k_i or single number if all identical)");
        System.out.println(">> ... << ");
        List<Integer> ks = null;
        boolean flag = true;
        while (flag) {
            try {
                String ks_str = scanner.nextLine();
                if (ks_str.trim().length() == 1) {
                    int k = Integer.parseInt(ks_str.trim());
                    if (k < 0) {
                        System.out.println("Invalid input!");
                        System.out.println(">> ... << ");
                        continue;
                    }
                    ks = Collections.nCopies(r, k);
                } else {
                    int numOfNegatives = ks_str.replaceAll("[^-]+", "").length();
                    ks = Arrays.stream(ks_str.trim().split("\\s+"))
                            .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
                    if (numOfNegatives > 0) {
                        System.out.println("Invalid input!");
                        System.out.println(">> ... << ");
                        continue;
                    }
                }
                flag = false;
                if (ks.size() != r) {
                    System.out.println("Length of vector k does not match r!");
                    System.out.println(">> ... << ");
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid input!");
                System.out.println(">> ... << ");
            }
        }

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
