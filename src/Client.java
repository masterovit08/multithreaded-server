package src;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 8081;

	private Socket clientSocket;
	private PrintWriter out;
	private Scanner in;
	private Scanner input;
	private String name;
	private boolean flag = false;
	private boolean running = true;

	public String getName(){
		return this.name;
	}

	public Client() {
		try {
			clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new Scanner(clientSocket.getInputStream());
			input = new Scanner(System.in);

			System.out.println("Connected");
			System.out.println("All streams are established");

		} catch (IOException ex) {
			ex.printStackTrace();
			closeStreams();
		}

		System.out.print("Enter your name: ");
		name = input.nextLine();
		System.out.println();

		new Thread(new MessageReader()).start();
		new Thread(new MessageWriter()).start();
	}

	public class MessageReader implements Runnable{
		@Override
		public void run(){
			while (running){
				if (in.hasNext()) {
					String message_from_server = in.nextLine();

					if (message_from_server.trim().equals("New member joined to server")){
						System.out.println();
						System.out.println("==========================");
						System.out.println(message_from_server);
						System.out.println();
					}
					else if (message_from_server.trim().equals("<clients_number>")){
						flag = true;
					}

					else if (flag){
						System.out.println();
						System.out.println(message_from_server);
						System.out.println();
						System.out.print(name + ": ");
						flag = false;
					}

					else{
						System.out.println(message_from_server);
					}
				}
			}
		}
	}

	public class MessageWriter implements Runnable{
		@Override
		public void run(){
			while (running){
				String message = input.nextLine();

				if ("exit".equalsIgnoreCase(message)){
					if (!name.isEmpty()){
						out.println(name + " left the server");
					}
					else{
						out.println("<unknown user> left the server");
					}

					out.flush();
					closeStreams();
					running = false;
					break;
				}

				out.println(name + ": " + message);
				out.flush();
			}
		}
	}

	public void closeStreams(){
		try{
			if (clientSocket != null) clientSocket.close();
			if (in != null) in.close();
			if (out != null) out.close();
			System.out.println("All streams are closed");
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}
}