import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            handleCommandLineArgs(args);
        } else {
            interactiveMode();
        }
    }

    private static void handleCommandLineArgs(String[] args) {
        String mode = args[0].toLowerCase();
        switch (mode) {
            case "server":
                Server server = new Server();
                break;
            case "client":
                Client client = new Client();
                break;
            default:
                System.out.println("Unknown parameter. Use 'server' or 'client'");
                System.exit(1);
        }
    }

    private static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select startup mode");
        System.out.println("1 - Server");
        System.out.println("2 - Client");
        System.out.print("Number:");

        try {
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    Server server = new Server();
                    break;
                case 2:
                    Client client = new Client();
                    break;
                default:
                    System.out.println("Incorrect choice. Enter 1 or 2");
                    System.exit(1);
            }
        } catch (Exception e) {
            System.out.println("Input error. Please enter a number");
            System.exit(1);
        }
    }
}
