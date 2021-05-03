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
            List<Integer> pot_map = Detector.checkStatus(engine);
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
            System.out.println("\n---------------------");
            System.out.println("No turns are left.");
            System.out.println("Player 2 is THE WINNER!");
            System.out.println("---------------------");
        }
    }

    private void updateDeathFields() {
        death_fields = new HashMap<>(calculateDeathFields(tokens));
    }

    private Map<Integer, List<Integer>> calculateDeathFields(List<Integer> tokens) {
        Map<Integer, List<Integer>> df = new HashMap<>();
        for(int i=0; i< r; i++) df.put(i, new ArrayList<>());
        for(int i = 0; i<=tokens.size(); i++) {
            for(int j = 0; j < r; j++) {
                List<Integer> tmpTokens = new ArrayList<>(tokens);
                tmpTokens.add(i, j);
                List<List<Integer>> positions = Detector.makePositions(tmpTokens, r);
                List<List<List<Integer>>> map_list = Detector.findMAP(positions, ks);
                if(map_list.get(j).size() > 0)
                    df.get(j).add(i);
            }
        }
        return df;
    }

    public static void makeFirstTurn(Engine engine) {
        Scanner scanner = new Scanner(System.in);
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

    private void initDeath_fields() {
        death_fields = new HashMap<>();
        for(int i=0; i< r; i++) death_fields.put(i, new ArrayList<>());
    }

    private static class DFStats implements Comparable<DFStats> {
        int i;
        int n_absolute_death;
        int n_death;
        int n_left;
        DFStats(List<Integer> tokens, Map<Integer, List<Integer>> df, List<Integer> ks, int i) {
            this.i = i;
            int r = ks.size();
            List<Integer> common = new ArrayList<Integer>(df.get(0));
            List<Integer> all_fields = new ArrayList<Integer>(df.get(0));
            for(int j = 1; j < r; j++) {
                common.retainAll(df.get(j));
                all_fields.addAll(df.get(j));
                if(common.size() == 0)
                    break;
            }
            Set<Integer> fields_set = new HashSet<>(all_fields);
            this.n_absolute_death = common.size();
            this.n_death = fields_set.size();
            this.n_left = ks.get(i) - Collections.frequency(tokens, i);
        }

        @Override
        public String toString() {
            return "DFStats{" +
                    "i=" + i +
                    ", n_absolute_death=" + n_absolute_death +
                    ", n_death=" + n_death +
                    ", n_left=" + n_left +
                    '}';
        }

        @Override
        public int compareTo(DFStats o) {
            if(o.n_absolute_death > this.n_absolute_death)
                return -1;
            else if(o.n_absolute_death < this.n_absolute_death)
                return 1;
            else {
                if(o.n_death > this.n_death)
                    return -1;
                else if(o.n_death < this.n_death)
                    return 1;
                else {
                    if(o.n_left > this.n_death)
                        return -1;
                    else if(o.n_left < this.n_death)
                        return 1;
                    else
                        return 0;
                }
            }
        }

    }
    public static void makeTurn(Engine engine) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> tokens = engine.getTokens();
        Random random = new Random();
        int col_choice = random.nextInt(engine.getR());
        Engine.showState(engine);

        System.out.println("\nPlayer 1:");
        System.out.println("Choose position for next token:");
        System.out.println(">> ... << ");

        String pos_str = scanner.nextLine();
        int pos = Integer.parseInt(pos_str);

        // color chosen randomly for now TODO
        List<Integer> forbidden_colors = new ArrayList<>();
        List<Integer> shifting_colors = new ArrayList<>(); // colors causing shifting fields
        for(int i = 0; i < engine.getR(); i++) {
            if (engine.getDeath_fields().get(i).contains(pos))
                forbidden_colors.add(i);
            else {
                List<Integer> tmpTokens = new ArrayList<>(tokens);
                tmpTokens.add(pos, i);
                if (Detector.checkShifting(tmpTokens, engine.getR(), engine.getKs()))
                    shifting_colors.add(i);
            }
        }
        List<Integer> ok_colors = new ArrayList<>();
        for(int i = 0; i < engine.getR(); i++)
            if(!forbidden_colors.contains(i) && !shifting_colors.contains(i))
                ok_colors.add(i);

        if(ok_colors.size()==0)
            if(shifting_colors.size()>0)
                ok_colors = new ArrayList<>(shifting_colors);
            else
                col_choice = 0;


        List<DFStats> dfStats = new ArrayList<>();
        for(int i =0; i<ok_colors.size(); i++) {
            List<Integer> tmpTokens = new ArrayList<>(tokens);
            tmpTokens.add(pos, ok_colors.get(i));
            Map<Integer, List<Integer>> df = new HashMap<>(engine.calculateDeathFields(tmpTokens));
            System.out.println(ok_colors.get(i));
            System.out.println(df.toString());
            dfStats.add(new DFStats(tmpTokens, df, engine.getKs(), ok_colors.get(i)));
        }
        if(dfStats.size() > 0) {
            Collections.sort(dfStats);
            col_choice = dfStats.get(0).i;
        }
        System.out.println(dfStats.toString());
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

    public Map<Integer, List<Integer>> getDeath_fields() {
        return death_fields;
    }

    public void setDeath_fields(Map<Integer, List<Integer>> death_fields) {
        this.death_fields = death_fields;
    }
}
