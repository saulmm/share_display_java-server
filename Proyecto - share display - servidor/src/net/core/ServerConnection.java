package net.core;

import gui.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.core.Items;


public class ServerConnection implements Runnable {
	private boolean running;
	private ServerSocket serverSocket;
	private final Logger log = LoggerFactory.getLogger(ServerSocket.class);
	public ServerConnection() {
	}
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(Items.PORT);
			log.info("Server created");
			Game mainGame = new Game();
			
			while(running) {
				Socket clientSocket = serverSocket.accept();
				ServerClient client = new ServerClient(clientSocket);
				log.info("Client connected : "+clientSocket.getInetAddress().toString());
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
