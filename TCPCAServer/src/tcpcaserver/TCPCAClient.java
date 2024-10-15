/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tcpcaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 *
 * @author CamaraAdmin
 */
public class TCPCAClient {

    public static void main(String[] args) throws IOException {
        //create a socket to connect to the server on localhost at port number 1234
        Socket socket = new Socket("localhost", 5004);

        //setup output stream to send data to server
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        //setup input stream to recieve data from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //communication loop, to allow for continous comms until client says STOP
        String message;
        BufferedReader userInput = new BufferedReader( new InputStreamReader(System.in));
        
        System.out.println("Enter your message for the server(say 'STOP' to end): ");
        
        while(true){
            //read users input
            message = userInput.readLine();
            //send message to the server
            out.println(message);
            //if user types stop, ends comms
            if(message.equalsIgnoreCase("STOP")){
                break;
            }
            //recieve response from the server
            String response = in.readLine();
            System.out.println("Server says: "+ response);
            
        }
        //close in & out resources
        in.close();
        out.close();
        //closing socket when finished
        socket.close();
        System.out.println("Connection Closed");
    }
}
