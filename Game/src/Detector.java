import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Detector {

    public static void demo() {
        // demo of detecting MAPs of lengths k_i in token vector
        Scanner scanner = new Scanner(System.in);
        Engine engine = new Engine();
        Engine.start(engine);

        System.out.println("\nEnter chosen vector of tokens: \n(starting from 0; separated by spaces)");
        System.out.println(">> ... << ");
        String tokens_str = scanner.nextLine();

        List<Integer> tokens = Arrays.stream(tokens_str.trim().split("\\s+"))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        if (Collections.min(tokens) < 0 || Collections.max(tokens) > engine.getR() - 1) {
            throw new IllegalStateException("Elements must be between 0 and r-1!");
        }
        //System.out.println(Arrays.toString(tokens));

        List<List<Integer>> positions = Detector.makePositions(tokens, engine.getR());

        //print
        System.out.println("\nToken positions by color:");
        int c = 0;
        for (List<Integer> pos_i : positions) {
            System.out.println("---- " + "Color " + c + " ----");
            System.out.println(Arrays.toString(pos_i.toArray()));
            c = c + 1;
        }

        List<List<List<Integer>>> map_list = Detector.findMAP(positions, engine.getKs());

        //print
        System.out.println("\nDetected MAPs by color:");
        int i = 0;
        for (List<List<Integer>> map_list_i : map_list) {
            System.out.println("---- " + "Color " + i + " ----");
            for (List<Integer> map : map_list_i) {
                System.out.println(Arrays.toString(map.toArray()));
            }
            i = i + 1;
        }
    }

    public static List<Integer> checkStatus(Engine engine) {
        // checks if game is already won
        List<List<Integer>> positions = Detector.makePositions(engine.getTokens(), engine.getR());
        List<List<List<Integer>>> map_list = Detector.findMAP(positions, engine.getKs());

        for (int i = 0; i < map_list.size(); i++) {
            List<List<Integer>> map_list_i = map_list.get(i);
            if (map_list_i.size() > 0) {
                engine.setEnded(true);
                engine.setMap_color(i);
                return map_list_i.get(0);
            }
        }
        return new ArrayList<>();
    }

    public static List<List<Integer>> makePositions(List<Integer> tokens, int r) {
        List<List<Integer>> positions = new ArrayList<>();

        for (int i = 0; i < r; i++) {
            int finalI = i;
            List<Integer> pos_i = IntStream.range(0, tokens.size())
                    .filter(j -> tokens.get(j) == finalI).boxed().collect(Collectors.toList());
            positions.add(i, pos_i);
        }
        return positions;
    }

    public static List<List<List<Integer>>> findMAP(List<List<Integer>> positions, List<Integer> ks) {
        List<List<List<Integer>>> map_list = new ArrayList<>();

        for (int i = 0; i < positions.size(); i++) {
            List<List<Integer>> map_list_i = new ArrayList<>();
            List<Integer> pos_i = positions.get(i);
            int n = pos_i.size();
            if (n == 0) {
                map_list.add(map_list_i);
                continue;
            }
            int min_pos = pos_i.get(0);
            int max_pos = pos_i.get(n - 1);
            int width = max_pos - min_pos + 1;
            //System.out.println("col: " + i + " min_pos: " + min_pos);
            //System.out.println("col: " + i + " max_pos: " + max_pos);
            double m_d = (double) (width - ks.get(i)) / (double) (ks.get(i) - 1);
            int max_d = (int) Math.floor(m_d) + 1;
            //System.out.println("col: " + i + " max_d: " + max_d);

            for (int k = min_pos; k < max_pos - 1; k++) {
                if (!pos_i.contains(k)) {
                    continue;
                }
                //System.out.println("k: " + k + " i: " + i);

                for (int d = 1; d < max_d + 1; d++) {
                    List<Integer> temp_map = new ArrayList<>();
                    temp_map.add(k);

                    while (true) {
                        // temp_map.size() < ks.get(i)  (MAP of length k_i only)
                        int curr = temp_map.get(temp_map.size() - 1);
                        if (pos_i.contains(curr + d)) {
                            temp_map.add(curr + d);
                        } else {
                            break;
                        }
                        if (temp_map.size() >= ks.get(i)) {
                            List<Integer> copy = List.copyOf(temp_map);
                            map_list_i.add(copy);
                        }
                    }
                }
            }
            map_list.add(map_list_i);
        }
        return map_list;
    }


    public static boolean checkShifted(List<Integer> tokens, int r, List<Integer> ks) {
        List<List<Integer>> positions = Detector.makePositions(tokens, r);
        for(int i =0; i<r; i++) {
            int llap = lenghtOfLongestAP(positions.get(i));
            if(ks.get(i) <= llap)
                return true;
        }
        return false;
    }

    public static boolean checkShifting(List<Integer> tokens, int r, List<Integer> ks) {
        for(int i =0; i<=tokens.size(); i++) {
            List<Integer> tmpTokens = new ArrayList<>(tokens);
            tmpTokens.add(i, r);
            if(checkShifted(tmpTokens, r, ks))
                return true;
        }
        return false;
    }

    static int lenghtOfLongestAP(List<Integer> positions)
    {
        int n = positions.size();
        if (n <= 2) return n;

        // Create a table and initialize all
        // values as 2. The value ofL[i][j] stores
        // LLAP with set[i] and set[j] as first two
        // elements of AP. Only valid entries are
        // the entries where j>i
        int L[][] = new int[n][n];

        // Initialize the result
        int llap = 2;

        // Fill entries in last column as 2.
        // There will always be two elements in
        // AP with last number of set as second
        // element in AP
        for (int i = 0; i < n; i++)
            L[i][n - 1] = 2;

        // Consider every element as second element of AP
        for (int j = n - 2; j >= 1; j--)
        {
            // Search for i and k for j
            int i = j -1 , k = j + 1;
            while (i >= 0 && k <= n - 1)
            {
                if (positions.get(i) + positions.get(k) < 2 * positions.get(j))
                    k++;

                    // Before changing i, set L[i][j] as 2
                else if (positions.get(i) + positions.get(k) > 2 * positions.get(j))
                {
                    L[i][j] = 2; i--;

                }

                else
                {
                    // Found i and k for j, LLAP with i and j as first two
                    // elements is equal to LLAP with j and k as first two
                    // elements plus 1. L[j][k] must have been filled
                    // before as we run the loop from right side
                    L[i][j] = L[j][k] + 1;

                    // Update overall LLAP, if needed
                    llap = Math.max(llap, L[i][j]);

                    // Change i and k to fill
                    // more L[i][j] values for current j
                    i--; k++;
                }
            }

            // If the loop was stopped due
            // to k becoming more than
            // n-1, set the remaining
            // entties in column j as 2
            while (i >= 0)
            {
                L[i][j] = 2;
                i--;
            }
        }
        return llap;
    }


}
