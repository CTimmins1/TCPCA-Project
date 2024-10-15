/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tcpcaserver;

//TCPServer.java
import java.io.BufferedReader;
import java.net.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author Conor
 */
public class TCPCAServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        //creating the socket instance named socket
        ServerSocket serverSocket = new ServerSocket(5004);
        System.out.println("Listening for clients. . .");
        //accept incoming client connection
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected: ");
        
        //setup input an doutput streams for communication with the client
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
        //read message from client
        String message = in.readLine();
        System.out.println("Client says: " + message);
        
        //send the response to the client
        String response = in.readLine();
        out.println("Message recieved by the server..." + response);
        
        //close the client socket
        clientSocket.close();
        //close server socket
        serverSocket.close();
        
        
        
    }
    
}
