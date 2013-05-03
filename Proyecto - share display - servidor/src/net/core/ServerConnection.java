package net.core;

import gui.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.core.Items;


public class ServerConnection implements Runnable {
	private boolean running;
	private ServerSocket serverSocket;
	public ServerConnection() {
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(Items.PORT);
			System.out.println("Server.run() server created...");
			Game mainGame = new Game();
			
			while(running) {
				System.out.println("Server.run() waiting for client");
				Socket clientSocket = serverSocket.accept();
				ServerClient client = new ServerClient(clientSocket);
				mainGame.addGameListener(client);
				client.addClientListener(mainGame);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public boolean isRunning() {
		return running;
	}

	
	public void setRunning(boolean running) {
		this.running = running;
	}
}
