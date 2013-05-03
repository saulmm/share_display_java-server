package net.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import main.core.Protocol;
import main.entities.Player;
import net.listeners.ClientListener;
import net.listeners.GameListener;


public class ServerClient implements Runnable, GameListener {
	private ArrayList<ClientListener> clientListeners;
	private DataInputStream dis;
	private DataOutputStream dos;
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


	private void sendResponse(int protocolResponse) {
		 dos = null;
		
		try {
			dos = new DataOutputStream(clientSocket.getOutputStream());
			dos.writeInt(protocolResponse);
			
		} catch(Exception e) {
			e.printStackTrace();
		} 
	}

	
	private int getRequest() {
		dis = null;
		int request = -1;
		
		try {
			dis = new DataInputStream(clientSocket.getInputStream());
			request = dis.readInt();
			
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
			dos.writeInt(x);
			dos.writeInt(y);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
