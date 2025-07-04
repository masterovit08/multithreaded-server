package com.masterovit08;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import com.google.gson.Gson;
import org.jline.reader.*;
import org.jline.terminal.*;

public class Client{
    private Socket clientSocket;
	private PrintWriter out;
	private Scanner in;
	private String name;
	private boolean running = true;

	private Terminal terminal;
	private LineReader reader;

	private Thread readerThread;
	private Thread writerThread;

	public String getName(){
		return this.name;
	}

	public Client(String host, int port){

        try {
			clientSocket = new Socket(host, port);
			out = new PrintWriter(clientSocket.getOutputStream());
			in = new Scanner(clientSocket.getInputStream());

			terminal = TerminalBuilder.builder().system(true).build();
			reader = LineReaderBuilder.builder().terminal(terminal).build();

			System.out.println("Connected");
			System.out.println("All streams are established");

		} catch (IOException ex) {
			ex.printStackTrace();
			closeStreams();
		}

		name = reader.readLine("Enter your name: ");
		System.out.println();

		Message register_message = new Message();
		register_message.command = "user_joining";
		register_message.sender = name;
		register_message.message = "";

		String jsonMessage = new Gson().toJson(register_message);
		out.println(jsonMessage);
		out.flush();

		this.readerThread = new Thread(new MessageReader());
		this.writerThread = new Thread(new MessageWriter());

		readerThread.start();
		writerThread.start();
	}

	public class MessageReader implements Runnable{
		@Override
		public void run(){
			while (running){
				if (Thread.currentThread().isInterrupted()){
					break;
				}

				if (in.hasNext()) {
					String currentBuffer = reader.getBuffer().toString();
					String message_from_server = in.nextLine();
					Message deserialized_message = new Gson().fromJson(message_from_server, Message.class);

					if (!deserialized_message.sender.equals(name) || deserialized_message.command.equals("user_joining")){
						terminal.writer().print("\033[2K");

						switch (deserialized_message.command){
							case "user_message":
								terminal.writer().println("\r" + deserialized_message.sender + ": " + deserialized_message.message);
								break;
							case "user_joining":
								terminal.writer().println("\r" + deserialized_message.sender + " joined the server");
								break;
							case "user_disconnection":
								terminal.writer().println("\r" + deserialized_message.sender + " disconnected from the server");
								break;
						}

						terminal.writer().print(name + ": " + currentBuffer);
						terminal.flush();
					}
				}
			}
		}
	}

	public class MessageWriter implements Runnable{
		@Override
		public void run(){
			while (running){
				String stringMessage = reader.readLine(name + ": ");
				Message message = new Message();
				message.sender = name;

				if ("exit".equalsIgnoreCase(stringMessage)){
					message.command = "user_disconnection";
					message.message = "";

					String jsonMessage = new Gson().toJson(message);

					out.println(jsonMessage);
					out.flush();

					readerThread.interrupt();

					running = false;
					closeStreams();
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