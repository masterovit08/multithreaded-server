import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class Server{
	static final int PORT = 3443;

	private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	private ArrayList<LocalTime> times = new ArrayList<LocalTime>();
	private ArrayList<LocalDate> dates = new ArrayList<LocalDate>();

	public Server(){
		Socket client_socket = null;

		ServerSocket server = null;

		try{
			server = new ServerSocket(PORT);

			System.out.println("Server created and ready to use");

			while (true){
				client_socket = server.accept();

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

		finally{
			try{
				client_socket.close();
				server.close();

				System.out.println("Server interrupted");
			}

			catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}

	public void sendData(String string){
		for (ClientHandler cl : clients){
			cl.sendString(string);
		}
	}

	public void removeClient(ClientHandler cl){
		clients.remove(cl);
	}
}