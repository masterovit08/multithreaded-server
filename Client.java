import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client{
	private static final String SERVER_HOST = "localhost";
	private static final int SERVER_PORT = 8081;

	private Socket client_socket;
	private PrintWriter out;
	private Scanner in;
	private Scanner input;
	private String name = "";
	private Boolean flag;

	public String getName(){
		return this.name;
	}

	public Client(){
		try{
			client_socket = new Socket(SERVER_HOST, SERVER_PORT);
			out = new PrintWriter(client_socket.getOutputStream());
			in = new Scanner(client_socket.getInputStream());
			input = new Scanner(System.in);
			flag = false;

			System.out.println("Connected");
			System.out.println("All streams established");
		}

		catch (IOException ex){
			ex.printStackTrace();
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

							if (message_from_server.toString().trim().equals("New member joined to server")){
								System.out.println();
								System.out.println("==========================");
								System.out.println(message_from_server);
								System.out.println();
							}
							else if (message_from_server.toString().trim().equals("<clients_number>")){
								flag = true;
							}

							else if (flag = true){
								System.out.println(message_from_server);
								System.out.println();
								System.out.println("==========================");
								System.out.println();
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
								client_socket.close();

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
}
