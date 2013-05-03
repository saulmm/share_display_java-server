package main.core;

import net.core.ServerConnection;

public class ServerMain {
	public static void main(String [] args) {
		ServerConnection server = new ServerConnection();
		
		Thread serverThread = new Thread(server);
		server.setRunning(true);
		serverThread.start();
		
	}
}
