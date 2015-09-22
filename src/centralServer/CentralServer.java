package centralServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CentralServer implements Runnable {

	public static volatile ArrayList<String> listOfClientIpsOnServer = new ArrayList<String>();
	public static volatile String writeIps = "";
	private static volatile ArrayList<String> clientInfos = new ArrayList<String>();

	// *****Creating sockets for communication between server and client*****
	private static ServerSocket serverSocket;
	private Socket clientSocket;

	@Override
	public void run() {

		try {
			serverSocket = new ServerSocket(7812);
			CSApp.taConsole.append("Socket on port 7812 is open\n");
		} catch (IOException e) {
			e.printStackTrace();
			CSApp.taConsole.append("Couldn't open socket on port 7812");
		}

		while (true) {
			try {
				clientSocket = serverSocket.accept();
				// *****Starting client thread*****
				Thread client = new Thread(new ClientThread(clientSocket));
				client.start();

			} catch (IOException e) {
				e.printStackTrace();
				CSApp.taConsole.append("Exception in Peer connections; socket is closed\n");
			}
		}
	}

	/**Deleting server thread. Actually, it sets it to null, and closes sockets*/
	public static void ExitServer(Thread x) {
		if (x != null) {
			x = null;
		}
		try {
			if (serverSocket != null) {
				serverSocket.close();
				serverSocket = null;
				CSApp.taConsole.append("Server is closed!\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			CSApp.taConsole.append("Couldn't close the server!\n");
		}
	}

	// *****Gettters and setters*****
	public static ArrayList<String> getClientInfos() {
		return clientInfos;
	}

	public static void setClientInfos(ArrayList<String> clientInfos) {
		CentralServer.clientInfos = clientInfos;
	}
}
