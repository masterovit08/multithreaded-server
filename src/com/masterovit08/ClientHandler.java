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

	private boolean active = false;
	private String clientName;

	public ClientHandler(Socket socket, Server server){
		this.client_socket = socket;
		this.server = server;

		try{
			this.out = new PrintWriter(socket.getOutputStream(), true);
			this.in = new Scanner(socket.getInputStream());
		}

		catch (IOException ex){
			ServerLogger.error("ERROR IN SETTING UP I/O STREAMS WITH CLIENT");
			ex.printStackTrace();
		}
	}

	@Override
	public void run(){
		while (true){
			String newUserJsonMessage = in.nextLine();
			Message newUserMessage = new Gson().fromJson(newUserJsonMessage, Message.class);

			if (!server.names.contains(newUserMessage.sender)){
				Message goodNameMessage = new Message();
				goodNameMessage.command = "unique_name";
				goodNameMessage.sender = "server";
				goodNameMessage.message = "";

				out.println(new  Gson().toJson(goodNameMessage));

				server.names.add(newUserMessage.sender);
				clientName = newUserMessage.sender;
				server.broadcastMessage(newUserJsonMessage);
				ServerLogger.info(newUserMessage.sender + " CONNECTED TO THE SERVER");

				break;
			}

			else{
				Message usedNameMessage = new Message();
				usedNameMessage.sender = "server";
				usedNameMessage.command = "used_name";
				usedNameMessage.message = "";

				out.println(new Gson().toJson(usedNameMessage));
			}
		}

		this.active = true;

		while (true){
			if (in.hasNextLine()){
				String jsonMessage = in.nextLine();
				Message message = new Gson().fromJson(jsonMessage, Message.class);

				if (message.command.equals("user_disconnection")){
					server.broadcastMessage(jsonMessage);

					ServerLogger.info(message.sender + " DISCONNECTED FROM SERVER");
					this.close();

					break;
				}

				server.broadcastMessage(jsonMessage);
			}
		}
	}

	public boolean active(){
		return this.active;
	}

	public void sendMessage(String jsonMessage){
		out.println(jsonMessage);
	}

	public void close(){
		try{
			out.close();
			in.close();
			client_socket.close();
			server.removeClient(this);
			server.names.remove(clientName);

			ServerLogger.info("ALL STREAMS WERE CLOSED");
		} catch (IOException ex){
			ServerLogger.error("UNABLE TO CLOSE I/O STREAMS WITH CLIENT");
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