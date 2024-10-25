/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tcpechoserverthreads;
import java.io.*;
import java.net.*;
/* Did not need this in server class, just in multithredded class
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;*/

/**
 *
 * @author Conor
 */
public class TCPCAServer {
   
    private static ServerSocket servSock;
    //must be incremented each time the connection is severed or else it wont run.
    private static final int PORT = 1243;
    private static int clientConnections = 0;
    //main method of main class
    public static void main(String[] args) {
        System.out.println("Opening port...\n");
        try {
            servSock = new ServerSocket(PORT);  
        } catch (IOException e) {
            System.out.println("Unable to attach to port!");
            System.exit(1);
        }

        while (true) {
            try {
                //accepting socket we have declared
                Socket link = servSock.accept();  
                clientConnections++;
                //will display the client followed by a number to identify them.
                System.out.println("Client " + clientConnections + " connected.");
                ClientConnectionThread handler = new ClientConnectionThread(link, clientConnections);
                //each time a new client connects, a new thread is made
                //enabling multiple clients
                Thread clientThread = new Thread(handler);
                clientThread.start();

            } catch (IOException e) {
                System.out.println("Error accepting client connection!");
            }
        }
    }
}