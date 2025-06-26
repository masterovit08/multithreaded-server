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

    public static void main(String[] args) {
        Main main = new Main();
        JCommander.newBuilder().addObject(main).build().parse(args);
        main.run();
    }

    public void run(){
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
}
