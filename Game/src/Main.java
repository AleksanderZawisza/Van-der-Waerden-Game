import java.util.Scanner;

public class Main{

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String options = "1. Detect MAP demo" + "\n" +
                "2. Play game demo" + "\n" +
                "3. Close app";

        System.out.println("\nWelcome!");
        while (true){
            System.out.println("\nWhat do you want to do?");
            System.out.println(options);
            System.out.println(">> ... << ");
            String choice = scanner.next();


            switch (choice) {
                case "1": Detector.demo(); break;
                case "2": Engine.play(); break;
                case "3": System.exit(0);
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}
