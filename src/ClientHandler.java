package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable{
	private Server server;

	private PrintWriter out;

	private Scanner in;
	private static final String HOST = "localhost";
	private static final int PORT = 3443;

	private Socket client_socket = null;

	private static int clients_number = 0;

	public ClientHandler(Socket socket, Server server){
		try{
			clients_number++;
			this.client_socket = socket;
			this.server = server;
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
			while (true){
				server.sendData("New member joined to server");
				server.sendData("<clients_number>");
				server.sendData("Members online: " + clients_number);
				break;
			}

			while (true){
				if (in.hasNext()){
					String message = in.nextLine();

					if (message.equalsIgnoreCase("exit")){
						break;
					}

					System.out.println("User: " + message);
					server.sendData(message);
				}

				Thread.sleep(1000);
			}
		}

		catch (InterruptedException ex){
			ex.printStackTrace();
		}

		finally{
			this.close();
		}
	}

	public void sendString(String data){
		try{
			out.println(data);
			out.flush();
		}

		catch (Exception ex){
			ex.printStackTrace();
		}
	}

	public void close(){
		server.removeClient(this);
		clients_number--;
		server.sendData("Members online: " + clients_number);
	}
}