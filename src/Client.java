package src;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson;

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

		Message register_message = new Message();
		register_message.command = "user_joining";
		register_message.sender = name;
		register_message.message = "";

		String jsonMessage = new Gson().toJson(register_message);
		out.println(jsonMessage);
		out.flush();

		new Thread(new MessageReader()).start();
		new Thread(new MessageWriter()).start();
	}

	public class MessageReader implements Runnable{
		@Override
		public void run(){
			while (running){
				if (in.hasNext()) {
					String message_from_server = in.nextLine();
					Message deserialized_message = new Gson().fromJson(message_from_server, Message.class);

					switch (deserialized_message.command){
						case "user_message":
							System.out.println("\n" + deserialized_message.sender + ": " + deserialized_message.message);
							break;
						case "user_joining":
							System.out.println("\n" + deserialized_message.sender + " joined the server\n");
							break;
						case "user_disconnection":
							System.out.println("\n" + deserialized_message.sender + " disconnected from the server\n");
							break;
					}

					System.out.print(name + ": ");
				}
			}
		}
	}

	public class MessageWriter implements Runnable{
		@Override
		public void run(){
			while (running){
				String stringMessage = input.nextLine();
				Message message = new Message();
				message.sender = name;

				if ("exit".equalsIgnoreCase(stringMessage)){
					message.command = "user_disconnection";
					message.message = "";

					String jsonMessage = new Gson().toJson(message);

					out.println(jsonMessage);
					out.flush();

					closeStreams();
					running = false;
					break;
				}

				message.message = stringMessage;
				message.command = "user_message";

				String jsonMessage = new Gson().toJson(message);

				out.println(jsonMessage);
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

	public static class Message{
		private String message;
		private String sender;
		private String command;

		@Override
		public String toString(){
			return "Message{" +
					"message='" + message + '\'' +
					", sender='" + sender + '\'' +
					", command='" + command + '\'' +
					'}';
		}
	}
}