package com.masterovit08;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server{
    private final CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

	public Server(int port){

        try (ServerSocket server = new ServerSocket(port)){
			ServerLogger.info("The server is created and ready for use");
			ServerLogger.info("LISTENING ON PORT " + port);

			while (true){
				Socket client_socket = server.accept();
				ClientHandler client = new ClientHandler(client_socket, this);
				clients.add(client);

				ServerLogger.info("NEW CONNECTION");

				new Thread(client).start();
			}
		}

		catch (IOException ex){
			ServerLogger.error("SERVER CREATION ERROR");
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public void broadcastMessage(String jsonMessage){
		for (ClientHandler cl : clients){
			if (cl.active()) cl.sendMessage(jsonMessage);
		}
	}

	public void removeClient(ClientHandler cl){
		clients.remove(cl);
	}
}
