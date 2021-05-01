import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class MAPDetector {

    public static void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nDefine r:");
        String r_str = scanner.nextLine();
        int r = Integer.parseInt(r_str);
        //System.out.println(r);

        System.out.println("\nDefine array of k_i: \n(separated by spaces)");
        String ks_str = scanner.nextLine();

        int[] ks = Arrays.stream(ks_str.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        //System.out.println(Arrays.toString(ks));

        System.out.println("\nEnter current vector of tokens: \n(starting from 0; separated by spaces)");
        String tokens_str = scanner.nextLine();

        int[] tokens = Arrays.stream(tokens_str.trim().split("\\s+")).mapToInt(Integer::parseInt).toArray();
        //System.out.println(Arrays.toString(tokens));

        List<int[]> positions = MAPDetector.makePositions(tokens, r);
        for (int[] array : positions) {
            System.out.println(Arrays.toString(array));
        }
    }

    public static List<int[]> makePositions(int[] tokens, int r) {
        List<int[]> positions = new ArrayList<>();

        for (int i = 0; i < r; i++) {
            int finalI = i;
            int[] pos_i = IntStream.range(0, tokens.length).filter(j -> tokens[j] == finalI).toArray();
            positions.add(i, pos_i);
        }
        return positions;
    }



}
