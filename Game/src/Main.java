import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Debugger debugger = new Debugger();

        String options = "1. Game" + "\n" +
                "2. Options" + "\n" +
                "3. Exit";

        String options_debug = "1. Game (with logic shown)" + "\n" +
                "2. Options" + "\n" +
                "3. Exit" + "\n" +
                "4. MAP detection demo";

        System.out.println("\n           ___  __  __           _ _                               _           \n" +
                "          /___\\/ _|/ _|       __| (_) __ _  __ _  ___  _ __   __ _| |          \n" +
                "         //  // |_| |_ _____ / _` | |/ _` |/ _` |/ _ \\| '_ \\ / _` | |          \n" +
                "        / \\_//|  _|  _|_____| (_| | | (_| | (_| | (_) | | | | (_| | |          \n" +
                "        \\___/ |_| |_|        \\__,_|_|\\__,_|\\__, |\\___/|_| |_|\\__,_|_|          \n" +
                "                        _             __   |___/                 _             \n" +
                "__   ____ _ _ __     __| | ___ _ __  / / /\\ \\ \\__ _  ___ _ __ __| | ___ _ __   \n" +
                "\\ \\ / / _` | '_ \\   / _` |/ _ \\ '__| \\ \\/  \\/ / _` |/ _ \\ '__/ _` |/ _ \\ '_ \\  \n" +
                " \\ V / (_| | | | | | (_| |  __/ |     \\  /\\  / (_| |  __/ | | (_| |  __/ | | | \n" +
                "  \\_/ \\__,_|_| |_|  \\__,_|\\___|_|      \\/  \\/ \\__,_|\\___|_|  \\__,_|\\___|_| |_| \n" +
                "                        / _ \\__ _ _ __ ___   ___                               \n" +
                "                       / /_\\/ _` | '_ ` _ \\ / _ \\                              \n" +
                "                      / /_\\\\ (_| | | | | | |  __/                              \n" +
                "                      \\____/\\__,_|_| |_| |_|\\___|         ");

        while (true) {
            System.out.println("\nChoose:");
            System.out.println(debugger.getDebugMode() ? options_debug : options);
            System.out.println(">> ... << ");
            String choice = scanner.next();

            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
                System.out.println("Invalid input!");
                System.out.println(">> ... << ");
                choice = scanner.next();
            }

            switch (choice) {
                case "1":
                    Engine.play(debugger);
                    break;
                case "2":
                    debugger.options();
                    break;
                case "3":
                    System.exit(0);
                case "4":
                    Detector.demo();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + choice);
            }
        }
    }
}
