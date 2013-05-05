package net.core;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import main.core.Protocol;
import main.entities.Player;
import net.listeners.ClientListener;
import net.listeners.GameListener;


public class ServerClient implements Runnable, GameListener {
	private ArrayList<ClientListener> clientListeners;
	private final Logger log = LoggerFactory.getLogger(ServerClient.class);
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private Player player;
	private Socket clientSocket;

	
	public ServerClient(Socket clientSocket) {
		this.clientSocket = clientSocket;
		Thread clientThread = new Thread(this);
		clientThread.start();
		
		clientListeners = new ArrayList<ClientListener>();
	}
	
	
	@Override
	public void run() {
		handleRequest();
	}

	
	private void handleRequest() {
		int request = getRequest();
		
		switch(request) {
			case 102: // Protocol.PLAYER_REQUEST
				sendResponse(Protocol.ACCEPT);
				registerPlayer(getClientPlayer());
				
			default :
				sendResponse(Protocol.DECLINE);
		}
	}
	
	
	private void registerPlayer(Player clientPlayer) {
		for(ClientListener client : clientListeners)
			client.onClientConnected(clientPlayer);
	}


	private Player getClientPlayer() {
		ObjectInputStream ois = null;
		
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			Object presuntObject = ois.readObject();
			
			if(presuntObject instanceof Player) {
				player = (Player) presuntObject;
			}
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return player;
	}
	
	private void createStreams() {
		try {
			oos = new ObjectOutputStream(clientSocket.getOutputStream());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	private void sendResponse(int protocolResponse) {
		try {
			createStreams();
			oos.writeObject(protocolResponse);
			log.info("Response sent");
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

	
	private int getRequest() {
		int request = -1;
		
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());
			request = (Integer) ois.readObject();
			log.debug("Waitting for the request");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return request;
	}

	
	public void addClientListener(ClientListener client) {
		clientListeners.add(client);
	}
	
	
	@Override
	public void startGame() {};


	@Override
	public void move(int x, int y) {
		try {
			oos.writeObject(x);
			oos.writeObject(y);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
