package net.listeners;

import main.entities.Player;


public interface ClientListener {
	public void onClientConnected(Player player);
}
