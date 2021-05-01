import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String options = "1. Detect MAP" + "\n" +
                "2. Something else" + "\n" +
                "3. Close app";

        System.out.println("\nWelcome!");

        while (true){
            System.out.println("\nWhat do you want to do?");
            System.out.println(options);
            String choice = scanner.next();

            switch (choice) {
                case "1": MAPDetector.start(); break;
                case "2": System.out.println("\nNope!"); break;
                case "3": System.exit(0);
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}
