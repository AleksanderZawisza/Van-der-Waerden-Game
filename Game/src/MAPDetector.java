import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MAPDetector {

    public static void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDefine r:");
        String r_str = scanner.nextLine();
        int r = Integer.parseInt(r_str);
        //System.out.println(r);

        System.out.println("\nDefine vector of k_i: \n(separated by spaces)");
        String ks_str = scanner.nextLine();

        List<Integer> ks = Arrays.stream(ks_str.trim().split("\\s+"))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        if (ks.size() != r) {
            throw new IllegalStateException("Length of vector k does not match r!");
        }
        //System.out.println(Arrays.toString(ks));

        System.out.println("\nEnter current vector of tokens: \n(starting from 0; separated by spaces)");
        String tokens_str = scanner.nextLine();

        List<Integer> tokens = Arrays.stream(tokens_str.trim().split("\\s+"))
                .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        if (Collections.min(tokens) < 0 || Collections.max(tokens) > r - 1) {
            throw new IllegalStateException("Elements must be between 0 and r-1!");
        }
        //System.out.println(Arrays.toString(tokens));

        List<List<Integer>> positions = MAPDetector.makePositions(tokens, r);

        //print
        System.out.println("\nToken positions by color:");
        int c = 0;
        for (List<Integer> pos_i : positions) {
            System.out.println("---- " + "Color " + c + " ----");
            System.out.println(Arrays.toString(pos_i.toArray()));
            c = c + 1;
        }

        List<List<List<Integer>>> map_list = MAPDetector.findMAP(positions, ks);

        //print
        System.out.println("\nDetected MAPs by colour:");
        int i = 0;
        for (List<List<Integer>> map_list_i : map_list) {
            System.out.println("---- " + "Color " + i + " ----");
            for (List<Integer> map : map_list_i) {
                System.out.println(Arrays.toString(map.toArray()));
            }
            i = i + 1;
        }
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


}
