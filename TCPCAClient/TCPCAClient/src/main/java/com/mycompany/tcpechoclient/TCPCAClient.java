/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.tcpechoclient;
import java.io.*;
import java.net.*;

/**
 *
 * @author Conor
 */
public class TCPCAClient {
    
 private static InetAddress host;
    private static final int PORT = 1243;

    public static void main(String[] args) {
     try 
     {
        host = InetAddress.getLocalHost();
     } 
     catch(UnknownHostException e) 
     {
	System.out.println("Host ID not found!");
	System.exit(1);
     }
     run();
   }
    
   private static void run() {
    Socket link = null;				
    try 
    {
        //https://www.geeksforgeeks.org/multithreaded-servers-in-java/
        //establishing the connection
	link = new Socket(host,PORT);		
        //setting up input anf output streams;
	BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
	PrintWriter out = new PrintWriter(link.getOutputStream(),true);

	//Set up stream for keyboard entry...
	BufferedReader userEntry =new BufferedReader(new InputStreamReader(System.in));
	String message ;
        String response;
	
        System.out.println("Enter a Task to be added to your list:\n Please use following format: add; YYYY-MM-DD; <taskToBeDone>:\n To pull up list of tasks for given day, use: list; YYYY-MM--DD\n To end the program type: STOP \nIf you wish to delete all tasks enter: DELETE_ALL\n");
       //do loop allows the client to send multiple messages until "STOP" is entered
            do {
                message = userEntry.readLine(); //reads user input
                // Send message to the server
                out.println(message);              // Break the loop if client types "STOP"
                if (message.equalsIgnoreCase("STOP")) {  // <--- Added this check to break the loop
                    System.out.println("Connection closing...");  // <--- Added message to indicate connection closing
                    break;
                }

                //Receive and print the server's response
                response = in.readLine(); 
                System.out.println("SERVER RESPONSE> " + response);

            } while (!message.equalsIgnoreCase("STOP")); //added a loop for continuous input and stop condition

        } catch (IOException e) {
            //not needed to throw, can be removed to optimize
            //e.printStackTrace();
        } finally {
            try {
                if (link != null) {
                    System.out.println("\n* Closing connection... *");
                    link.close(); // Step 4
                }
            } catch (IOException e) {
                System.out.println("Unable to disconnect!");
                System.exit(1);
            }
        }
    }
}