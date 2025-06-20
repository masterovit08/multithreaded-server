package src;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

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
			server.broadcastMessage("New member joined to server");
			server.broadcastMessage("<clients_number>");
			server.broadcastMessage("Members online: " + clients_number);

			while (true){
				if (in.hasNextLine()){
					String message = in.nextLine();

					if (message.equalsIgnoreCase("exit")){
						break;
					}

					System.out.println(message);
					server.broadcastMessage(message);
				}

				Thread.sleep(1000);
			}
		} catch (InterruptedException ex){
			ex.printStackTrace();
		} finally{
			this.close();
		}
	}

	public void sendMessage(String data){
		out.println(data);
		out.flush();
	}

	public void close(){
		try{
			in.close();
			out.close();
			client_socket.close();
			server.removeClient(this);
			clients_number--;
			server.broadcastMessage("Members online: " + clients_number);
		} catch (IOException ex){
			ex.printStackTrace();
		}
	}
}