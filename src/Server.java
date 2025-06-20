package src;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class Server{
	static final int PORT = 8081;

	private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();
	private final ArrayList<LocalTime> times = new ArrayList<LocalTime>();
	private final ArrayList<LocalDate> dates = new ArrayList<LocalDate>();

	public Server(){
		try (ServerSocket server = new ServerSocket(PORT)){
			System.out.println("Server created and ready to use");

			while (true){
				Socket client_socket = server.accept();

				LocalTime time = LocalTime.now();
				LocalDate date = LocalDate.now();

				ClientHandler client = new ClientHandler(client_socket, this);

				clients.add(client);
				times.add(time);
				dates.add(date);

				System.out.println("new client");
				System.out.println("name-code: " + client);
				System.out.println("list of clients:");

				System.out.println("        NAME-CODE        |       TIME       |       DATE      ");
				System.out.println("______________________________________________________________");

				for (int i = 0; i < clients.size(); i++){
					ClientHandler cl = clients.get(i);
					LocalTime ti = times.get(i);
					LocalDate da = dates.get(i);

					System.out.println(cl + "    " + ti + "     " + da);
				}


				new Thread(client).start();

			}
		}

		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	public void broadcastMessage(String string){
		for (ClientHandler cl : clients){
			cl.sendMessage(string);
		}
	}

	public void removeClient(ClientHandler cl){
		clients.remove(cl);
	}
}
