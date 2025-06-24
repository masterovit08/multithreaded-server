package com.masterovit08;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson;

public class ClientHandler implements Runnable{
	private Server server;
	private Socket client_socket;

	private PrintWriter out;
	private Scanner in;

	private static int clients_number = 0;

	public ClientHandler(Socket socket, Server server){
		clients_number++;
		this.client_socket = socket;
		this.server = server;

		try{
			this.out = new PrintWriter(socket.getOutputStream());
			this.in = new Scanner(socket.getInputStream());
		}

		catch (IOException ex){
			ex.printStackTrace();
		}
	}


	@Override
	public void run(){
		try{
			String newUserJsonMessage = in.nextLine();
			server.broadcastMessage(newUserJsonMessage);

			while (true){
				if (in.hasNextLine()){
					String jsonMessage = in.nextLine();
					Message message = new Gson().fromJson(jsonMessage, Message.class);

					if (message.command.equals("user_disconnection")){
						server.broadcastMessage(jsonMessage);

						System.out.println(message.sender + ": disconnected from the server");
						break;
					}

					System.out.println(message.sender + ": " + message.message);
					server.broadcastMessage(jsonMessage);
				}

				Thread.sleep(1000);
			}
		} catch (InterruptedException ex){
			ex.printStackTrace();
		} finally{
			this.close();
		}
	}

	public void sendMessage(String jsonMessage){
		out.println(jsonMessage);
		out.flush();
	}

	public void close(){
		try{
			in.close();
			out.close();
			client_socket.close();
			server.removeClient(this);
			clients_number--;
		} catch (IOException ex){
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