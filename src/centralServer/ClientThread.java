package centralServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {

	private static Socket clientSocket;
	private BufferedReader brForConn;
	private static PrintWriter pw;

	// -----------Constructor with socket------------
	public ClientThread(Socket clientSocket) {
		ClientThread.clientSocket = clientSocket;
	}

	// ----------------------------------------------

	@Override
	public void run() {

		CSApp.taConsole
				.append("Client " + clientSocket.getInetAddress().getHostAddress() + " is connected to server\n");
		CentralServer.listOfClientIpsOnServer.add(clientSocket.getInetAddress().getHostAddress());
		CentralServer.writeIps = "";
		CSApp.onlineClientsIps.setText("");
		for (int i = 0; i < CentralServer.listOfClientIpsOnServer.size(); i++) {
			CentralServer.writeIps += CentralServer.listOfClientIpsOnServer.get(i) + "\n";
		}
		CSApp.onlineClientsIps.setText(CentralServer.writeIps);

		// ----------Setting streams for communication-------------
		SetupStreams();
		CSApp.taConsole.append("Streams are ok...\n");
		// -------------------------------------------------------

		// ----------Reading from client--------------------------
		CommunicateWithClient();
	}

	/** Setting up streams */
	public void SetupStreams() {
		try {
			brForConn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			pw = new PrintWriter(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			CSApp.taConsole.append("Couldn't setup streams\n");
		}
	}

	/**
	 * Receiving list from client with videos that are on that client That list
	 * has meta data about videos Right now, just title This method also checks
	 * if peer is still conected
	 */
	private void CommunicateWithClient() {

		try {
			boolean b = true;
			String s = "";
			while (b) {
				s = brForConn.readLine();
				String s1 = "";
				String ipsWithVideo = "";
				if (s.startsWith("choice")) {
					for (int i = 0; i < CentralServer.getClientInfos().size(); i++) {
						if (CentralServer.getClientInfos().get(i).contains(s.substring(7))) {
							ipsWithVideo += CentralServer.getClientInfos().get(i).substring(0,
									CentralServer.getClientInfos().get(i).indexOf('#')) + "#";
						}
					}
					pw.println(ipsWithVideo);
					pw.flush();
					CSApp.taConsole.append("ips with video send to peer; ips: " + ipsWithVideo + "\n");
				} else if (!s.equals("-1")) {
					s1 = clientSocket.getInetAddress().getHostName() + "#" + s;
					CentralServer.getClientInfos().add(s1);
					CSApp.taConsole.append("Printing list with added peer: " + CentralServer.getClientInfos() + "\n");
				} else {
					int index;
					String ips;
					for (int i = 0; i < CentralServer.getClientInfos().size(); i++) {
						index = CentralServer.getClientInfos().get(i).indexOf('#');
						ips = CentralServer.getClientInfos().get(i).substring(0, index);
						if (ips.equals(clientSocket.getInetAddress().getHostAddress())) {
							CentralServer.getClientInfos().remove(i);
						}
					}
					for (int i1 = 0; i1 < CentralServer.listOfClientIpsOnServer.size(); i1++) {
						if (CentralServer.listOfClientIpsOnServer.get(i1)
								.equals(clientSocket.getInetAddress().getHostAddress())) {
							CentralServer.listOfClientIpsOnServer.remove(i1);
						}
					}

					String updatedTextArea = "";
					for (int i1 = 0; i1 < CentralServer.listOfClientIpsOnServer.size(); i1++) {
						updatedTextArea += CentralServer.listOfClientIpsOnServer.get(i1);
					}

					CSApp.onlineClientsIps.setText("");
					CSApp.onlineClientsIps.setText(updatedTextArea);
					b = false;
					CloseSockets();
					CSApp.taConsole.append("Socket " + clientSocket + " is closed\n");
					CSApp.taConsole
							.append("Printing list after disconection: " + CentralServer.getClientInfos() + "\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			CSApp.taConsole.append("Object input stream couldnt read the object\n");
		}
	}

	/** Closing socket */
	public static void CloseSockets() {
		try {
			clientSocket.close();
		} catch (IOException e) {
			CSApp.taConsole.append("Couldn't close socket\n");
			e.printStackTrace();
		}
	}
}
