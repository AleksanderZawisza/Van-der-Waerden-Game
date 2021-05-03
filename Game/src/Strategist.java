import java.util.*;

public class Strategist {

    private static class DFStats implements Comparable<DFStats> {
        int i;
        int n_absolute_death;
        int n_death;
        int n_left;

        DFStats(List<Integer> tokens, Map<Integer, List<Integer>> df, List<Integer> ks, int i) {
            this.i = i;
            int r = ks.size();
            List<Integer> common = new ArrayList<>(df.get(0));
            List<Integer> all_fields = new ArrayList<>(df.get(0));
            for (int j = 1; j < r; j++) {
                common.retainAll(df.get(j));
                all_fields.addAll(df.get(j));
                if (common.size() == 0)
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
            if (o.n_absolute_death > this.n_absolute_death)
                return -1;
            else if (o.n_absolute_death < this.n_absolute_death)
                return 1;
            else {
                if (o.n_death > this.n_death)
                    return -1;
                else if (o.n_death < this.n_death)
                    return 1;
                else {
                    return Integer.compare(this.n_death, o.n_left);
                }
            }
        }

    }

    public static int chooseColor(Engine engine, int pos) {
        Random random = new Random();
        int col_choice = random.nextInt(engine.getR());
        List<Integer> forbidden_colors = new ArrayList<>();
        List<Integer> shifting_colors = new ArrayList<>(); // colors causing shifting fields
        List<Integer> tokens = engine.getTokens();

        for (int i = 0; i < engine.getR(); i++) {
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
        for (int i = 0; i < engine.getR(); i++)
            if (!forbidden_colors.contains(i) && !shifting_colors.contains(i))
                ok_colors.add(i);

        if (ok_colors.size() == 0)
            if (shifting_colors.size() > 0)
                ok_colors = new ArrayList<>(shifting_colors);
            else
                col_choice = 0;

        List<Strategist.DFStats> dfStats = new ArrayList<>();
        System.out.println("\nPossible death fields by color:");
        for (Integer ok_color : ok_colors) {
            List<Integer> tmpTokens = new ArrayList<>(tokens);
            tmpTokens.add(pos, ok_color);
            Map<Integer, List<Integer>> df = new HashMap<>(Strategist.calculateDeathFields(engine, tmpTokens));
            System.out.println("---- Color " + ok_color + " ----");
            System.out.println(df.toString());
            dfStats.add(new DFStats(tmpTokens, df, engine.getKs(), ok_color));
        }
        if (dfStats.size() > 0) col_choice = Collections.min(dfStats).i;

        System.out.println("\nStatistics for death fields by color:");
        System.out.println(dfStats.toString());

        return col_choice;
    }


    public static Map<Integer, List<Integer>> calculateDeathFields(Engine engine, List<Integer> tokens) {
        Map<Integer, List<Integer>> df = new HashMap<>();
        for (int i = 0; i < engine.getR(); i++) df.put(i, new ArrayList<>());
        for (int i = 0; i <= tokens.size(); i++) {
            for (int j = 0; j < engine.getR(); j++) {
                List<Integer> tmpTokens = new ArrayList<>(tokens);
                tmpTokens.add(i, j);
                List<List<Integer>> positions = Detector.makePositions(tmpTokens, engine.getR());
                List<List<List<Integer>>> map_list = Detector.findMAP(positions, engine.getKs());
                if (map_list.get(j).size() > 0)
                    df.get(j).add(i);
            }
        }
        return df;
    }

}
