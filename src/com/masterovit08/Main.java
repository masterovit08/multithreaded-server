package com.masterovit08;

import java.util.Scanner;
import com.beust.jcommander.*;

public class Main {
    @Parameter(names = {"--mode", "-m"})
    private String mode;

    @Parameter(names = {"--server-ip", "-ip"})
    private String serverIp;

    @Parameter(names = {"--port", "-p"})
    private int port;

    @Parameter(names = {"--help", "-h"}, help = true)
    private boolean help;

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        main.run();
    }

    public void run(){
        if (help) showHelp();
        if (mode == null) interactiveInput("mode");

        switch (mode){
            case "server":
                if (port == 0)  interactiveInput("port");

                Server server = new Server(port);
                break;

            case "client":
                if (serverIp == null) interactiveInput("server-ip");
                if (port == 0) interactiveInput("port");

                Client client = new Client(serverIp, port);
                break;

            default:
                System.out.println("Invalid mode: " + mode);
                System.out.println("Use 'server' or 'client'");
                System.exit(-1);
        }
    }

    private void interactiveInput(String parameter){
        Scanner input = new Scanner(System.in);

        switch (parameter){
            case "mode":
                System.out.println("Select startup mode");
                System.out.println("1 - Server");
                System.out.println("2 - Client");
                System.out.print("Number: ");

                int number = input.nextInt();

                if (number == 1) mode = "server";
                else mode = "client";
                break;

            case "server-ip":
                System.out.print("Server IP: ");
                serverIp = input.next();
                break;

            case "port":
                System.out.print("Port: ");
                port = input.nextInt();
                break;
        }
    }

    private void showHelp(){
        System.out.println("Help on using a multithreaded messaging server");
        System.out.println("==============================================");
        System.out.println("Usage: java -jar <path_to_the_compiled_jar>/multithreaded-server.jar [CMD LINE OPTIONS]\n");
        System.out.println("COMMAND LINE OPTIONS:");
        System.out.println("\t--mode OR -m [server, client] - Selecting the startup mode");
        System.out.println("\t--server-ip OR -ip [localhost OR other ip] - Server IP address, ONLY NEEDED FOR CLIENT");
        System.out.println("\t--port OR -p [Any integer from 0 to 65535] - Server port to connect to");
        System.out.println("\t--help OR -h - Show this help\n");
        System.out.println("EXAMPLE:");
        System.out.println("\tStarting the server: java -jar target/multithreaded-server.jar --mode server --port 8081\n");
        System.out.println("\tStarting the client: java -jar target/multithreaded-client.jar --mode client --server-ip localhost --port 8081\n");
        System.out.println("NOTE:");
        System.out.println("\tAll required unpassed parameters will be requested interactively.");
        System.exit(0);
    }
}
