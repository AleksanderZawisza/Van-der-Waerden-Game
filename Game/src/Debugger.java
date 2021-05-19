import java.util.Scanner;

public class Debugger {

    private boolean debug_mode = false;

    public void options() {
        Scanner scanner = new Scanner(System.in);
        String options = "1. Set debug mode ON" + "\n" +
                "2. Set debug mode OFF" + "\n" +
                "3. Exit to main menu";

        System.out.print("\nDebug mode: ");
        System.out.print(debug_mode ? "ON" : "OFF");
        System.out.println("\nOptions:");
        System.out.println(options);
        System.out.println(">> ... << ");
        String choice = scanner.next();

        while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3")) {
            System.out.println("Invalid input!");
            System.out.println(">> ... << ");
            choice = scanner.next();
        }

        switch (choice) {
            case "1":
                this.debug_mode = true;
                System.out.println("\nDebug mode: ON");
                break;
            case "2":
                this.debug_mode = false;
                System.out.println("\nDebug mode: OFF");
                break;
            case "3":
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + choice);

        }
    }

    public boolean getDebugMode() {
        return debug_mode;
    }

    public void setDebugMode(boolean debug_mode) {
        this.debug_mode = debug_mode;
    }

}
