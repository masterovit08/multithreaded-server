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
	private boolean running =  true;

	public String getName(){
		return this.name;
	}

	public Client(){
		try{
			clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new Scanner(clientSocket.getInputStream());
			input = new Scanner(System.in);

			System.out.println("Connected");
			System.out.println("All streams are established");
		}

		catch (IOException ex){
			ex.printStackTrace();
			closeStreams();
		}

		System.out.print("Enter your name: ");
		name = input.nextLine();
		System.out.println();

		new Thread(new Runnable() {
			@Override
			public void run(){
				try{
					while (true){
						if (in.hasNext()){
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

							else if (flag = true){
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

				catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}).start();

		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					while (true){
						String message = input.nextLine();

						if (message.equalsIgnoreCase("exit")){
							try{
								if (!name.isEmpty()){
									out.println(name + " left the server");
								}
								else{
									out.println("<unknown user> left the server");
								}

								out.println("exit");
								out.flush();
								out.close();
								in.close();
								clientSocket.close();

								System.out.println("All streams closed");
								break;
							}

							catch (IOException ex){
								ex.printStackTrace();
							}
						}

						out.println(name + ": " + message);
						out.flush();
					}
				}

				catch (Exception ex){
					ex.printStackTrace();
				}
			}
		}).start();
	}

	private class MessageReader implements Runnable{
		@Override
		public void run(){
			while (running){
				String message_from_server = in.nextLine();
				System.out.println(message_from_server);
			}
		}
	}

	private class MessageWriter implements Runnable{
		@Override
		public void run(){
			Scanner input = new Scanner(System.in);

			while (running){
				String message = input.nextLine();

				if ("exit".equals(message)){
					running = false;
					out.println("DISCONNECTED");
					closeStreams();
					break;
				}

				out.println(message);
			}
		}
	}

	private void closeStreams(){
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
