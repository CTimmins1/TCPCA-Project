package com.mycompany.tcpechoserverthreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Conor
 */
//I wanted to implement runnable rather then extending thread
//so i can extend the Multithread class if needed and not be tied down to a extends thread.
public class ClientConnectionThread implements Runnable {

    //https://www.youtube.com/watch?v=r_MbozD32eo&ab_channel=CodingwithJohn
    //to complete the CA i had to flesh out my client thread class substantially
    //hashMap to store TODO tasks with unique IDs
    private static Map<Integer, Task> tasks = new HashMap<>();
    private static int taskIdCounter = 1; //unique ID counter for tasks

    private Socket link;
    private String clientId;

    public ClientConnectionThread(Socket socket, int clientId) {
        this.link = socket;
        this.clientId = String.valueOf(clientId);
    }
//good prActice to annotate with an override method

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);

            String message;
            while ((message = in.readLine()) != null) {
                System.out.println("Message received from client " + clientId + ": " + message);

                if (message.equalsIgnoreCase("STOP")) {
                    System.out.println("Client " + clientId + " requested to close the connection.");
                    break;
                }
                 // Check for 'DELETE_ALL' command
                 https://www.geeksforgeeks.org/removing-all-mapping-from-hashmap-in-java/
            if (message.equalsIgnoreCase("DELETE_ALL")) {
                System.out.println("Client " + clientId + " requested to delete all tasks.");
                tasks.clear();  // Clear all tasks
                out.println("All tasks have been deleted.");  // Send confirmation to client
                //ensure that user gets message strraight away
                out.flush();
            } 
                //reads input for 'add;'
                if (message.startsWith("add;")) {
                    //handling everything after add on the fourth index
                    //i was getting 'null' response everytime from server.
                    //adding the .trim() method solved this
                    //removes leading spaces
                    handleAddTask(message.substring(4).trim(), out);
                } else if (message.startsWith("list;")) {
                    //List from index 0 to after semi-colon is 5, so we attatch substring to 6
                    handleListTasks(message.substring(6).trim(), out);
                } else {
                    //throw custom exception for incorrect actions
                    throw new IncorrectActionException("Invalid command. Use 'add;' or 'list;'.");

                }
                           }
        } catch (IOException e) {
            // e.printStackTrace();
            //added catch clause through netbeans
        } catch (IncorrectActionException ex) {
            Logger.getLogger(ClientConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                System.out.println("Closing connection with client " + clientId);
                link.close();
            } catch (IOException e) {
                System.out.println("Unable to close connection with client " + clientId);
            }
        }
    }

//logic to handle the tasks input by the client
    //used a split() function to break up the string enabling me to differ from date and task
    //https://stackoverflow.com/questions/9886751/how-to-split-date-and-time-from-a-datetime-string
    //replaced "time" with task to adapt to needs of CA
    private void handleAddTask(String input, PrintWriter out) {
        String[] parts = input.split(";", 2); //split into date and task description
        if (parts.length < 2) {
            out.println("Invalid format. Use 'add;YYYY-MM-DD;*TASK*:");
            return;
        }
        //https://www.geeksforgeeks.org/split-string-java-examples/
        //USING .TRIM() to catch any leading whitespace
        String date = parts[0].trim();
        String taskDescription = parts[1].trim();

        //create a new task and store it
        Task newTask = new Task(date, taskDescription);
        //store with a unique ID
        tasks.put(taskIdCounter++, newTask);

        out.println("Task added: " + newTask);
    }

    //exception as per the CA requirement
    //https://www.geeksforgeeks.org/user-defined-custom-exception-in-java/
    public class IncorrectActionException extends Exception {

        public IncorrectActionException(String message) {
            super(message);
        }
    }
//filters tasks based on specified date and builds a string containing tasks that match date.

    private void handleListTasks(String input, PrintWriter out) {
        //get the date entered by the user
        String date = input.trim();
        //This creates a stringbuilder that will hold list of tasks for specific date
        StringBuilder taskList = new StringBuilder();
        //iterate over map of tasks
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            //gets the task
            Task task = entry.getValue();
            if (task.getDate().equals(date)) {
                //if tasklist matches,it appends the task's key(ID) and its description
                taskList.append(entry.getKey()).append(": ").append(task.getDescription()).append("; ");
            }
        } 

        if (taskList.length() > 0) {
            out.println("Tasks for " + date + ": " + taskList.toString());
        } else {
            out.println("No TODO tasks for " + date);
        }
    }
}

//task class to hold task details
class Task {

    private String date;
    private String description;

    public Task(String date, String description) {
        this.date = date;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return date + ": " + description;
    }
}
