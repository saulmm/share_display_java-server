Share displays project - server
=============================

Proof of concept, to test how to make a game that shares various interconnected screens, project done with the purpose of porting to mobile platforms

The server is connected to clients, when there are a number of then, server calculates the canvas dimension based on the size of each display, in this case frames.

The server is who does the magic and shares to the clients, depending on the position of the server x element shared the coordinates to a client or other

Comments 
========
The application needs at leats 2 clients to be runned

Client side 
=========================
https://github.com/saulmm/share_display_java-client.git
